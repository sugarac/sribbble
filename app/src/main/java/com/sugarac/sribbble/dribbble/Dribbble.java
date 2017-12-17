package com.sugarac.sribbble.dribbble;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sugarac.sribbble.model.Shot;
import com.sugarac.sribbble.model.User;
import com.sugarac.sribbble.utils.ModelUtils;


import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// this is the Dribbble java client we wrote
public class Dribbble {

    private static final String TAG = "Dribbble API";
    public static final int COUNT_PER_PAGE = 12;
    private static final String API_URL = "https://api.dribbble.com/v1/";

    private static final String SHOTS_END_POINT = API_URL + "shots";
    private static final String USER_END_POINT = API_URL + "user";

    private static final String SP_AUTH = "auth";

    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_USER = "user";

    private static final TypeToken<List<Shot>> SHOT_LIST_TYPE = new TypeToken<List<Shot>>(){};
    private static final TypeToken<User> USER_TYPE = new TypeToken<User>(){};

    private static OkHttpClient client = new OkHttpClient();

    private static String accessToken;
    private static User user;

    private static Request.Builder authRequestBuilder(String url) {
        return new Request.Builder()
                .addHeader("Authorization", "Bearer " + accessToken)
                .url(url);
    }

    private static Response makeRequest(Request request) throws IOException {
        Response response = client.newCall(request).execute();
        Log.d(TAG, response.header("X-RateLimit-Remaining"));
        return response;
    }

    private static Response makeGetRequest(String url) throws IOException {
        Request request = authRequestBuilder(url).build();
        return makeRequest(request);
    }

    private static <T> T parseResponse(Response response,
                                       TypeToken<T> typeToken) throws IOException, JsonSyntaxException {
        String responseString = response.body().string();
        Log.d(TAG, responseString);
        return ModelUtils.toObject(responseString, typeToken);
    }

    public static void init(@NonNull Context context) {
        accessToken = loadAccessToken(context);
        if (accessToken != null) {
            user = loadUser(context);
        }
    }

    public static boolean isLoggedIn() {
        return accessToken != null;
    }

    public static void login(@NonNull Context context,
                             @NonNull String accessToken) throws IOException, JsonSyntaxException {
        Dribbble.accessToken = accessToken;
        storeAccessToken(context, accessToken);

        Dribbble.user = getUser();
        storeUser(context, user);
    }

    public static void logout(@NonNull Context context) {
        storeAccessToken(context, null);
        storeUser(context, null);

        accessToken = null;
        user = null;
    }

    public static User getCurrentUser() {
        return user;
    }

    public static void storeAccessToken(@NonNull Context context, @Nullable String token) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(
                SP_AUTH, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_ACCESS_TOKEN, token).apply();
    }

    public static String loadAccessToken(@NonNull Context context) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(
                SP_AUTH, Context.MODE_PRIVATE);
        return sp.getString(KEY_ACCESS_TOKEN, null);
    }

    public static void storeUser(@NonNull Context context, @Nullable User user) {
        ModelUtils.save(context, KEY_USER, user);
    }

    public static User loadUser(@NonNull Context context) {
        return ModelUtils.read(context, KEY_USER, new TypeToken<User>(){});
    }

    public static User getUser() throws IOException, JsonSyntaxException {
        return parseResponse(makeGetRequest(USER_END_POINT), USER_TYPE);
    }

    public static List<Shot> getShots(int page) throws IOException, JsonSyntaxException {
        String url = SHOTS_END_POINT + "?page=" + page;
        return parseResponse(makeGetRequest(url), SHOT_LIST_TYPE);
    }
}
