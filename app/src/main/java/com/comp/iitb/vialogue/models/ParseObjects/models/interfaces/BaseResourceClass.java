package com.comp.iitb.vialogue.models.ParseObjects.models.interfaces;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseParseClass;
import com.parse.ParseException;
import com.parse.ParseFile;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseParseClass.Fields.CHILDREN_RESOURCES;

/**
 * Created by ironstein on 16/02/17.
 */

public abstract class BaseResourceClass extends BaseParseClass {

    // default constructor required by Parse
    // DO NOT USE THIS CONSTRUCTOR (ONLY FOR USE BY PARSE)
    // USE THE OTHER CONSTRUCTOR THAT REQUIRES PARAMETERS DURING
    // INSTANTIATING THE OBJECT
    public BaseResourceClass() {}

    private static final class Fields implements BaseFieldsClass {
        public static final String

        FILE = "file",
        TEMP_URL = "temp_url";

        public ArrayList<String> getAllFields() {
            return new ArrayList<String>(Arrays.asList(new String[] {
                    FILE,
                    TEMP_URL
            }));
        }
    }

    @Override
    public ArrayList<String> getAllFields() {
        ArrayList<String> fields = new Fields().getAllFields();
        fields.addAll(super.getAllFields());
        return fields;
    }

    public static File makeTempResourceFile(Slide.ResourceType resourceType, Context context) {
        String extension = null;
        File storageDirectory = null;

        if(resourceType == Slide.ResourceType.AUDIO) {
            extension = "wav";
            storageDirectory = new File(context.getFilesDir(), extension);
        } else if(resourceType == Slide.ResourceType.IMAGE) {
            extension = "png";
            storageDirectory = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), extension);
        } else if(resourceType == Slide.ResourceType.VIDEO) {
            extension = "mp4";
            storageDirectory = new File(context.getExternalFilesDir(Environment.DIRECTORY_MOVIES), extension);
        }

        if(!storageDirectory.exists()) {
            storageDirectory.mkdirs();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = extension + "_" + timeStamp + "_";
        File a = null;
        try {
            a = File.createTempFile(
                    imageFileName,      /* prefix */
                    "." + extension,    /* suffix */
                    storageDirectory    /* directory */
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
        return a;
    }

    public BaseResourceClass(Uri uri) {
        setUri(uri);
    }

    public Uri getUri() {
        Uri uri = null;
        try {
            uri = Uri.fromFile(getParseFile(Fields.FILE).getFile());
        } catch (Exception e) {}
        if(uri == null) {
            // not stored as a ParseFile
            uri =  Uri.fromFile(new File(getString(Fields.TEMP_URL)));
        } if(uri == null) {
            // not instantiazed properly
            // TODO handle this
        }
        return uri;
    }

    public boolean isParseFileSaved() {
        try {
            Uri.fromFile(getParseFile((Fields.FILE)).getFile());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void setUri(Uri uri) {
        put(Fields.TEMP_URL, new File(uri.getPath()).getAbsolutePath());
    }

    public File getResourceFile() {
        return new File(getUri().getPath());
    }

    @Override
    public void saveParseObject(Context context) throws ParseException {
        if(doesStoreFile()) {
            File f = new File(getUri().getPath());
            ParseFile file;
            if(!isParseFileSaved()) {
                file = new ParseFile(f);
            } else {
                file = getParseFile((Fields.FILE));
            }
            file.save();
            put(Fields.FILE, file);
        }
        super.saveParseObject(context);
    }

    @Override
    public void saveParseObjectEventually() {
        ParseFile file = new ParseFile(new File(getUri().getPath()));
        // TODO think how to implement this
        saveEventually();
    }

    public abstract boolean doesStoreFile();
}
