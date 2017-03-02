package com.comp.iitb.vialogue.GlobalStuff;

/**
 * Created by jeffrey on 1/2/17.
 */
public class Master {
    public static String personName = "name";
    public static String personPhotoUrl = "image";
    public static String email = "email";
    public static String signedOrNot= "isSigned";
    public static String AppName = "LVCreate";
    public static String AppPath = "/LVCreate" ;
    public static String ProjectsPath = "/Projects";
    public static String MyProjectsPath="/MyProjects";
    public static String SavedProjectsPath="/SavedProjects";
    public static String VideosPath = "/Videos";
    public static String SavedVideosPath="/SavedVideos";
    public static String ImagesPath ="/images";
    public static String AudiosPath ="/audios";

    public static void getVideosPath(){

    }
    public static String getProjectsPath(){

        return Master.AppPath + Master.ProjectsPath;
    }
    public static String getMyProjectsPath(){

       return Master.AppPath + Master.ProjectsPath + Master.MyProjectsPath;

    }
    public static String getSavedProjectsPath(){

        return Master.AppPath + Master.ProjectsPath + Master.SavedProjectsPath;

    }
    public static String getSavedVideosPath(){

        return Master.AppPath + Master.VideosPath+ Master.SavedVideosPath;
    }

    }
