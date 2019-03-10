package com.example.android.vision.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils2
{


    static String requesturl;
    private final static String API_KEY = "5bfa18f6312927c7600993f8e9dc8136";

    private final static String API_ID= "b9c781e9";
    public static String buildUrl(String fooditem) {
        String useless="49000036756";



        if(fooditem.equals("8901764011184"))
        {
           requesturl = "https://api.nutritionix.com/v1_1/item?upc=049000028904&appId=b9c781e9" +
                    "&appKey=5bfa18f6312927c7600993f8e9dc8136";
           return requesturl;
        } else if (fooditem.equals("8901058865479"))
        {
            requesturl = "https://api.nutritionix.com/v1_1/item?upc=034000087525&appId=b9c781e9" +
                    "&appKey=5bfa18f6312927c7600993f8e9dc8136";
            return requesturl;
        }


        requesturl = "https://api.nutritionix.com/v1_1/item?upc="+fooditem+"&appId=b9c781e9" +
                "&appKey=5bfa18f6312927c7600993f8e9dc8136";
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

    private void demoRequests()
    {

    }

}
