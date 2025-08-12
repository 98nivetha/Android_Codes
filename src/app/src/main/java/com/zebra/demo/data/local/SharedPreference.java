package com.zebra.demo.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.zebra.demo.data.remote.model.Login;

public class SharedPreference {

    Context context;

    // Constants
    private String COMMON_PREF_NAME = "pfizer_pref_name";
    private String IS_LOGIN = "is_login";
    private String USER_ID = "user_id";
    private String USER_NAME = "user_name";
    private String TOKEN = "Token";
    private String USER_DEPARTMENT_NAME = "user_department_name";
    private String USER_ACCESS_TOKEN = "user_access_token";
    private String USER_LOGIN_RESPONSE = "user_login_response";
    private String APP_API_DOMAIN = "app_api_domain";
    private static final String CREATED_BY_USERID_KEY = "CREATED_BY_USERID";


    /**
     * Constructor for getting context
     *
     * @param context
     */
    public SharedPreference(Context context) {
        this.context = context;
    }


    /**
     * @return true if user logged in else false
     */
    public boolean isLoggedIn() {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(COMMON_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(IS_LOGIN, false);
    }


    /**
     * Set the user login status
     *
     * @param state
     */
    public void setLoggedIn(boolean state) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(COMMON_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_LOGIN, state);
        editor.apply();
    }

    /**
     * @return userCode
     */
    public String getUserId() {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(COMMON_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(USER_ID, "");
    }

    /**
     * Set the userCode
     *
     * @param userCode
     */
    public void setUserId(String userCode) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(COMMON_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_ID, userCode);
        editor.apply();
    }

    public void setCreatedByUserId(Integer createdByUserid) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(COMMON_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(CREATED_BY_USERID_KEY, createdByUserid);
        editor.apply();
    }

    public int getCreatedByUserId() {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(COMMON_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(CREATED_BY_USERID_KEY, 0);
    }


    /**
     * @return userName
     */
    public String getUserName() {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(COMMON_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(USER_NAME, "");
    }

    /**
     * Set the userName
     *
     * @param userName
     */
    public void setUserName(String userName) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(COMMON_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_NAME, userName);
        editor.apply();
    }


    /**
     * @return Token
     */
    public String getToken() {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(COMMON_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(TOKEN, "");
    }

    /**
     * Set the userName
     *
     * @param token
     */
    public void setToken(String token) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(COMMON_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOKEN, token);
        editor.apply();
    }


    /**
     * @return userDeptName
     */
    public String getUserDeptName() {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(COMMON_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(USER_DEPARTMENT_NAME, "");
    }

    /**
     * Set the userDeptName
     *
     * @param userDeptName
     */
    public void setUserDeptName(String userDeptName) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(COMMON_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_DEPARTMENT_NAME, userDeptName);
        editor.apply();
    }

    /**
     * @return login
     */
    /*public TidelLoginUserInfoResponse getUserLoginResp() {
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences(COMMON_PREF_NAME, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = preferences.getString(USER_LOGIN_RESPONSE, "");
        Type type = new TypeToken<TidelLoginUserInfoResponse>() {
        }.getType();
        return gson.fromJson(json, type);
    }*/

    /**
     * Set the login
     *
     * @param root
     */
    /*public void setUserLoginResp(TidelLoginUserInfoResponse root) {*/
    public void setUserLoginResp(Login root) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(COMMON_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(root);
        editor.putString(USER_LOGIN_RESPONSE, json);
        editor.apply();
    }

    /**
     * @return accessToken
     */
    public String getAccessToken() {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(COMMON_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(USER_ACCESS_TOKEN, "");
    }

    /**
     * Set the userDeptName
     *
     * @param accessToken
     */
    public void setAccessToken(String accessToken) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(COMMON_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_ACCESS_TOKEN, accessToken);
        editor.apply();
    }

    /**
     * @return APP Api Domain
     */
    /*public String getAppApiDomain() {
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences(COMMON_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(APP_API_DOMAIN, "");
    }*/

    /**
     * Set APP Api Domain
     *
     * @param domain
     */
    /*public void setAppApiDomain(String domain) {
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences(COMMON_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(APP_API_DOMAIN, domain);
        editor.apply();
    }*/

    /**
     * Clear shared preference session
     */
    public void clearSession() {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(COMMON_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
