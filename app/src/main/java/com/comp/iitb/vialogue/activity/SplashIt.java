package com.comp.iitb.vialogue.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.comp.iitb.vialogue.GlobalStuff.Master;
import com.comp.iitb.vialogue.MainActivity;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.helpers.SharedPreferenceHelper;
import com.comp.iitb.vialogue.models.ParseObjects.models.Project;
import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseParseClass;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseResourceClass;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.ParseObjectsCollection;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class SplashIt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --------------------------------------------------------------
//        ArrayList<Project> projects = new ArrayList<Project>();
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");
//        query.fromLocalDatastore();
//        try {
//            List<ParseObject> localObjects = query.find();
//            for(ParseObject object: localObjects) {
//                projects.add((Project) object);
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        Project project = projects.get(0);
//        project.fetchChildrenObjects();
//        try {
//            project.saveParseObject();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        project.fetchChildrenObjects();
//        System.out.println(project.toString());
//        System.out.println(project.keySet());
//        System.out.println(project.getSlides().get(0).keySet());
//        ParseObjectsCollection<BaseResourceClass> slides = (ParseObjectsCollection) project.getSlides().get(0).getParseObject(BaseParseClass.Fields.CHILDREN_RESOURCES);
//        System.out.println(slides);
//        System.out.println(slides.get(0));




//        System.out.println(project.getSlides().get(0).getResource());

//        ParseObjectsCollection<Slide> slides = null;
//        try {
//            Project project = projects.get(0);
//            slides = (ParseObjectsCollection) project.getParseObject(Project.Fields.SLIDES);
////            System.out.println(slides);
//            projects.get(0).getParseObject(Project.Fields.SLIDES).fetchFromLocalDatastore();
////            System.out.println(slides);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        Slide slide = slides.get(1);
//        System.out.println(slide);
//        System.out.println(slide.keySet());
//        try {
//            slide.fetchFromLocalDatastore();
////            slide.getParseObject(BaseParseClass.Fields.CHILDREN_RESOURCES).fetchFromLocalDatastore();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        System.out.println(slide);
//        System.out.println(slide.keySet());
//
//        System.out.println(slides);

//        finish();
        // --------------------------------------------------------------

        Intent intent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // permission granted
                intent = new Intent(this, MainActivity.class);
            } else {
                // permission not granted
                // ask for permission
                intent = new Intent(this, PermissionsActivity.class);
            }
        }

        startActivity(intent);
        finish();
    }

}
