package com.comp.iitb.vialogue.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.activity.CameraActivity;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Image;
import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;

/**
 * Created by ironstein on 27/04/17.
 */

public class ChooseImageDialog extends Dialog {

    private Activity mActivity;
    private Fragment mFragment;
    private Context mContext;
    private Button mChooseGalleryImageButton;
    private Button mTakeCameraImageButton;

    public ChooseImageDialog(Context context, Fragment fragment) {
        super(context);
        mFragment = fragment;
        mActivity = fragment.getActivity();
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup UI
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.content_choose_image_dialog);

        // instantiate UI Components
        mChooseGalleryImageButton = (Button) findViewById(R.id.button_choose_image_from_gallery);
        mTakeCameraImageButton = (Button) findViewById(R.id.button_take_camera_image);

        // add listeners
        mChooseGalleryImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mFragment.getActivity(), AlbumSelectActivity.class);
                //set limit on number of images that can be selected, default is 10
                intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 10);
                mFragment.startActivityForResult(intent, Constants.REQUEST_CODE);
                ChooseImageDialog.this.dismiss();
            }
        });

        mTakeCameraImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CameraActivity.class);
                mFragment.startActivityForResult(intent, SharedRuntimeContent.GET_MULTIPLE_CAMERA_IMAGES);
                ChooseImageDialog.this.dismiss();
            }
        });
    }

    /**
     * get uri to any resource type
     * @param context - context
     * @param resId - resource id
     * @throws Resources.NotFoundException if the given ID does not exist.
     * @return - Uri to resource by given id
     */
    public static final Uri getUriFromResource(@NonNull Context context,
                                             @AnyRes int resId)
            throws Resources.NotFoundException {
        /** Return a Resources instance for your application's package. */
        Resources res = context.getResources();
        /**
         * Creates a Uri which parses the given encoded URI string.
         * @param uriString an RFC 2396-compliant, encoded URI
         * @throws NullPointerException if uriString is null
         * @return Uri for this given uri string
         */
        Uri resUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(resId)
                + '/' + res.getResourceTypeName(resId)
                + '/' + res.getResourceEntryName(resId));
        /** return uri */
        return resUri;
    }

}
