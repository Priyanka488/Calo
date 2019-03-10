package com.example.android.vision.utilities;


import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils
{

    private final static String NUTRITIONIX_BASE_URL = "https://api.nutritionix.com/v1_1/search/";

    private final static String API_KEY = "5bfa18f6312927c7600993f8e9dc8136";

    private final static String API_ID= "b9c781e9";
    public static String buildUrl(String fooditem) {

        String requesturl = "https://api.nutritionix.com/v1_1/search/"+fooditem+
                "?fields=item_name%2Cnf_calories%2Cnf_total_fat&appId="+API_ID+"&appKey="+API_KEY+
                "&filters=6";

        return requesturl;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}