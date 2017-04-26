package com.comp.iitb.vialogue.utils;

import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.models.ParseObjects.models.Project;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ironstein on 17/03/17.
 */

public class ProjectNameUtils {

    // constants
    public static final String untitledProjectNameRegex = "(^)Untitled Project ([0-9].*$)";

    // variables
    private List<String> mExistingProjectNamesList;

    public ProjectNameUtils(List<String> existingProjectNamesList) {
        mExistingProjectNamesList = existingProjectNamesList;
    }

    public int compareStrings(String s1, String s2) {
        // returns
        // 1 if s1 > s2
        // 0 if s1 == s2
        // -1 if s1 < s2
        String shorterString;
        String longerString;
        if(s1.length() > s2.length()) {
            shorterString = s2;
            longerString = s1;
        } else {
            shorterString = s1;
            longerString = s2;
        }

        for(int i=0; i<shorterString.length(); i++) {
            if(shorterString.charAt(i) > longerString.charAt(i)) {
                if(shorterString.equals(s1)) {
                    return 1;
                } else {
                    return -1;
                }
            } else if(shorterString.charAt(i) < longerString.charAt(i)) {
                if(shorterString.equals(s1)) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }

        if(s1.length() > s2.length()) {
            return 1;
        } else if(s1.length() < s2.length()) {
            return -1;
        } else {
            return 0;
        }
    }

    public int getNewProjectIndex(Project newProject) {
        String newProjectName = newProject.getName();
        int i = 0;
        while(true) {
            while((i < mExistingProjectNamesList.size()) && (compareStrings(newProjectName, mExistingProjectNamesList.get(i)) == 1)) {
                i += 1;
            }
            if((i < mExistingProjectNamesList.size()) && (compareStrings(newProjectName, mExistingProjectNamesList.get(i)) == 0)) {
                newProjectName += "_new";
            } else {
                break;
            }
            i = 0;
        }
        return i;
    }

    public String getNewProjectName(Project newProject) {
        if(newProject.getName().matches(untitledProjectNameRegex)) {
            return getNewUndefinedProjectName();
        }

        String newProjectName = newProject.getName();
        if(newProject == null) {
            return getNewUndefinedProjectName();
        }
        int i = 0;
        while(true) {
            while((i < mExistingProjectNamesList.size()) && (compareStrings(newProjectName, mExistingProjectNamesList.get(i)) == 1)) {
                i += 1;
            }
            if((i < mExistingProjectNamesList.size()) && (compareStrings(newProjectName, mExistingProjectNamesList.get(i)) == 0)) {
                newProjectName += "_new";
            } else {
                break;
            }
            i = 0;
        }
        return newProjectName;
    }

    public static String getNewUndefinedProjectName() {

        ArrayList<Project> localProjects = SharedRuntimeContent.getLocalProjects();
        int maxNum = 0;
        for (Project project : localProjects) {
            if ((project.getName() != null) && project.getName().matches(untitledProjectNameRegex)) {
                System.out.println("&&&&&&&& -------- " + project.getName());
                System.out.println(project.getObjectId());
                try {
                    int number = Integer.parseInt(project.getName().substring(17));
                    if (number >= maxNum) {
                        maxNum = number + 1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "Untitled Project " + maxNum;
    }
}

