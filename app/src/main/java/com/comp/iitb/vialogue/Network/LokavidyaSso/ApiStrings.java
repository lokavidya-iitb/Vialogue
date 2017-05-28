package com.comp.iitb.vialogue.Network.LokavidyaSso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by ironstein on 24/05/17.
 */

/*
 * documentation for the API's can be found at the url below
 * https://drive.google.com/file/d/0B4JG1o0uAKt0V3c1QjR4b3NmVzA/view?usp=sharing
 */
public class ApiStrings {

    public static final String lokavidyaSsoBaseUrl = "https://sso.lokavidya.com/";
    public static final String projectsApiBaseUrl = "http://employee-dev-env.dkprxv4uh3.us-west-2.elasticbeanstalk.com/";
    public static enum RegistrationType {PHONE_NUMBER, EMAIL_ID}

    // api/signup
    public static final String getSignUpApi() {
        return lokavidyaSsoBaseUrl + "api/signup";
    }

    // api/find?token=:session_token&uuid=:session_uuid
    public static final String getFetchUserDetailsApi(String sessionToken, int sessionUuid) {
        return lokavidyaSsoBaseUrl + "api/find?token=" + sessionToken + "&uuid=" + sessionUuid;
    }

    // api/update?token=:session_token&uuid=:session_uuid
    public static final String getUpdateUserDetailsApi(String sessionToken, int sessionUuid) {
        return lokavidyaSsoBaseUrl + "api/update?token=" + sessionToken + "&uuid=" + sessionUuid;
    }

    // api/delete?token=:session_token&uuid=:session_uuid
    public static final String getDeleteUserApi(String sessionToken, int sessionUuid) {
        return lokavidyaSsoBaseUrl + "api/delete&token=" + sessionToken + "&uuid=" + sessionUuid;
    }

    // api/login
    public static final String getLoginApi() {
        return lokavidyaSsoBaseUrl + "api/login";
    }

    // api/logout?token=:session_token
    public static final String getLogoutApi(String sessionToken) {
        return lokavidyaSsoBaseUrl + "api/logout?token=" + sessionToken;
    }

    // api/forgot?email=xyz@abc.com&token=:session_token
    public static final String getForgotPasswordApi(RegistrationType registrationType, String registrationData) throws UnsupportedEncodingException {
        if(registrationType == RegistrationType.PHONE_NUMBER) {
            return lokavidyaSsoBaseUrl + "api/forgot?phone=" + URLEncoder.encode(registrationData, "UTF-8");
        } else {
            return lokavidyaSsoBaseUrl + "api/forgot?email=" + registrationData;
        }
    }

    // api/reset?email=xyz@abc.com&token=session_token&otp=xxxxxx
    public static final String getResetPasswordApi(RegistrationType registrationType, String registrationData, String otp) throws UnsupportedEncodingException {
        if(registrationType == RegistrationType.PHONE_NUMBER) {
            return lokavidyaSsoBaseUrl + "api/reset?phone=" + URLEncoder.encode(registrationData, "UTF-8") + "&otp=" + otp;
        } else {
            return lokavidyaSsoBaseUrl + "api/reset?email=" + registrationData + "&otp=" + otp;
        }
    }

    // api/v1/categories/FetchCategories
    public static final String getFetchCategoriesApi() {
        return projectsApiBaseUrl + "api/v1/categories/FetchCategories";
    }

}
