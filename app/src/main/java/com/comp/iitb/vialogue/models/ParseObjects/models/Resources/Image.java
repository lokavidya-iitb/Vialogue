package com.comp.iitb.vialogue.models.ParseObjects.models.Resources;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.widget.Toast;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.OnFileCopyCompleted;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.library.CopyFileAsync;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.listeners.FileCopyUpdateListener;
import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseResourceClass;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.CanSaveAudioResource;
import com.parse.ParseClassName;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ironstein on 20/02/17.
 */

@ParseClassName("Image")
public class Image extends CanSaveAudioResource {

    // default constructor required by Parse
    // DO NOT USE THIS CONSTRUCTOR (ONLY FOR USE BY PARSE)
    // USE THE OTHER CONSTRUCTOR THAT REQUIRES PARAMETERS DURING
    // INSTANTIATING THE OBJECT
    public Image() {}

    public Image getNewInstance() {
        return new Image();
    }

    private static final String IMAGE_RESOURCE_NAME = "image";

    public Image(Context context) {
        this(Uri.fromFile(BaseResourceClass.makeTempResourceFile(Slide.ResourceType.IMAGE, context)));
    }

    public Image(Uri uri) {
        super(uri);
    }

    public boolean doesStoreFile() {
        return true;
    }

    private static final int MAXIMUM_FILE_SIZE_IN_KBS = 512;
    private static final int JPEG_COMPRESSION_FACTOR = 80;

    public static Uri getResizedImage(Context context, Uri sourceImageUri, Uri destinationImageUri, boolean replaceOriginalImage) {
        // get image width and height
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(sourceImageUri.getPath()).getAbsolutePath(), options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;

        File newImageFile;
        if(destinationImageUri != null) {
            newImageFile = new File(destinationImageUri.getPath());
        } else {
            newImageFile = BaseResourceClass.makeTempResourceFile(Slide.ResourceType.IMAGE, context);
        }
        Storage mStorage = new Storage(context);

        // get image file size (in KBs)
        File file = new File(sourceImageUri.getPath());
        int fileSizeInKBs = Integer.parseInt(String.valueOf(file.length()/1024));

        // get the scaling factor
        // (square root because when we scale both the width and height by the
        // square root of the scaling factor, then the size of the final image
        // will be scaled by the scaling factor)
        double scalingFactor =  Math.sqrt(fileSizeInKBs / MAXIMUM_FILE_SIZE_IN_KBS);
        if(scalingFactor <= 1) {
            scalingFactor = 1;
        }

        // get scaled dimensions
        int scaledImageWidth = (int) Math.floor(imageWidth / scalingFactor);
        int scaledImageHeight = (int) Math.floor(imageHeight / scalingFactor);

        // make the image width and height divisible by 2 (as is required by
        // FFMPEG on the server side, don't know why!)
        if(scaledImageWidth % 2 != 0) {
            scaledImageWidth -= 1;
        }
        if(scaledImageHeight % 2 != 0) {
            scaledImageHeight -= 1;
        }

        if(scaledImageWidth == imageWidth && scaledImageHeight == imageHeight) {
            // no need to scale if file already within maximum file size limit
            if(!replaceOriginalImage) {
                if(!copyFile(new File(sourceImageUri.getPath()), newImageFile)) {
                    return null;
                }
                return Uri.fromFile(newImageFile);
            } else {
                return sourceImageUri;
            }
        }

        // load original bitmap (load a scaled copy to avoid OutOfMemory errors)
        options = new BitmapFactory.Options();
        options.inSampleSize = Math.max(imageHeight/scaledImageWidth, imageHeight/scaledImageHeight);
        Bitmap imageBitmap = BitmapFactory.decodeFile(new File(sourceImageUri.getPath()).getAbsolutePath(), options);

        // the scaled image above will be scaled by a value equal to the
        // nearest power of 2, hence it wont be scaled to the exact value
        // that we want. So, we need to calculate new scaling factors
        // based on the scaled loaded bitmap
        Matrix m = new Matrix();
        RectF inRect = new RectF(0, 0, imageWidth, imageHeight);
        RectF outRect = new RectF(0, 0, scaledImageWidth, scaledImageHeight);
        m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
        float[] values = new float[9];
        m.getValues(values);

        // create a scaled bitmap with required file size
        int finalWidth = (int) (imageWidth*values[0]);
        int finalHeight = (int) (imageHeight*values[4]);
        if(finalWidth % 2 != 0) {
            finalWidth -= 1;
        }
        if(finalHeight % 2 != 0) {
            finalHeight -= 1;
        }
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, finalWidth, finalHeight, true);


        FileOutputStream fos;
        try {
            if(replaceOriginalImage) {
                new File(sourceImageUri.getPath()).delete();
                fos = new FileOutputStream(new File(sourceImageUri.getPath()));
            } else {
                fos = new FileOutputStream(newImageFile);
            }
        } catch (FileNotFoundException e) {
            return null;
        }

        // write bitmap to file
        try {
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_COMPRESSION_FACTOR, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // recycle bitmaps to reallocate memory
        Storage.recycleBitmap(imageBitmap);
        Storage.recycleBitmap(scaledBitmap);

        // scaling done, return new Image
        return Uri.fromFile(newImageFile);
    }

    public static final Uri getResizedImage(Context context, Uri sourceImageUri) {
        return getResizedImage(context, sourceImageUri, null, false);
    }

    public static boolean resizeImage(Context context, Uri imageUri) {
        if(getResizedImage(context, imageUri, null, true) == null) {
            return false;
        } return true;
    }

    public static boolean copyFile(File sourceFile, File destinationFile) {
        boolean isSuccess = true;
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(sourceFile);
            out = new FileOutputStream(destinationFile);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (IOException exception) {
            isSuccess = false;
            exception.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    isSuccess = false;
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    isSuccess = false;
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }

}
