package com.comp.iitb.vialogue.models.ParseObjects.models.Resources;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.widget.Toast;

import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseResourceClass;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.CanSaveAudioResource;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.ParseObjectsCollection;
import com.parse.ParseClassName;
import com.parse.ParseFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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

    private static final int maximumFileSizeInKBs = 512;

    public static Uri resizeImage(Context context, Uri imageUri) {
        // get image width and height
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap imageBitmap = BitmapFactory.decodeFile(new File(imageUri.getPath()).getAbsolutePath(), options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;

        // get image file size (in KBs)
        File file = new File(imageUri.getPath());
        int fileSizeInKBs = Integer.parseInt(String.valueOf(file.length()/1024));

        // get the scaling factor
        // (square root because when we scale both the width and height by the
        // square root of the scaling factor, then the size of the final image
        // will be scaled by the scaling factor)
        double scalingFactor =  Math.sqrt(fileSizeInKBs / maximumFileSizeInKBs);

        // no need to scale if file already within maximum file size limit
        if(scalingFactor <= 1) {
            return imageUri;
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

        // load original bitmap (load a scaled copy to avoid OutOfMemory errors)
        options = new BitmapFactory.Options();
        options.inSampleSize = Math.max(imageHeight/scaledImageWidth, imageHeight/scaledImageHeight);
        imageBitmap = BitmapFactory.decodeFile(new File(imageUri.getPath()).getAbsolutePath(), options);

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
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, (int) (imageWidth*values[0]), (int) (imageHeight*values[4]), true);

        // delete original image
        new File(imageUri.getPath()).delete();

        // write bitmap to file
        File newImageFile = BaseResourceClass.makeTempResourceFile(Slide.ResourceType.IMAGE, context);
        try {
            newImageFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(newImageFile);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            return null;
        }

//        File newImageFile = BaseResourceClass.makeTempResourceFile(Slide.ResourceType.IMAGE, context);
//        new Storage(context).saveBitmapToFile(newImageFile, scaledBitmap);

        // recycle bitmaps to reallocate memory
        Storage.recycleBitmap(imageBitmap);
        Storage.recycleBitmap(scaledBitmap);

        // scaling done, return new Image
        return Uri.fromFile(newImageFile);
    }

}
