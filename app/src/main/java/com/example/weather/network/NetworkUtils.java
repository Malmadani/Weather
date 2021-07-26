package com.example.weather.network;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.weather.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    private static final String WEATHER_ENDPOINT = "weather";
    private static final String FORECAST_ENDPOINT = "forecast";

    private static final String QUERY_PARAM = "q";
    private static final String FORMAT_PARAM = "mode";
    private static final String UNITS_PARAM = "units";
    private static final String LANG_PARAM = "lang";
    private static final String APP_ID_PARAM = "appid";

    private static final String FORMAT = "json";

    private static final String METRIC = "metric";
    private static final String IMPERIAL = "imperial";

    private static String TAG = NetworkUtils.class.getSimpleName();

    private static Context mContext;
    private static NetworkUtils sInstane;
    private static final Object LOCK = new Object();

    private RequestQueue mRequestQueue;

    public static NetworkUtils getInstance(Context context){
        if (sInstane == null){
            synchronized (LOCK){
                if (sInstane == null) sInstane = new NetworkUtils(context);
            }
        }
        return sInstane;
    }

    public RequestQueue getRequestQueue(){
        if (mRequestQueue == null){
            mRequestQueue = new Volley().newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

    public <T> void  addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }

    private NetworkUtils(Context context){
        mContext = context.getApplicationContext();
        mRequestQueue = getRequestQueue();
    }

    public static URL getWeatherUrl(Context context) {
        return buildUrl(context, WEATHER_ENDPOINT);
    }

    public static URL getForecastUrl(Context context) {
        return buildUrl(context, FORECAST_ENDPOINT);

    }

    private static URL buildUrl(Context context, String endPoint) {
        Uri.Builder uriBuldir = Uri.parse(BASE_URL + endPoint).buildUpon();
        Uri uri = uriBuldir
                .appendQueryParameter(QUERY_PARAM, context.getString(R.string.pref_location_default))
                .appendQueryParameter(FORMAT_PARAM, FORMAT)
                .appendQueryParameter(UNITS_PARAM, METRIC)
                .appendQueryParameter(LANG_PARAM, Locale.getDefault().getLanguage())
                .appendQueryParameter(APP_ID_PARAM, context.getString(R.string.api_key))
                .build();

        try {
            URL url = new URL(uri.toString());
            Log.d(TAG, "URL" + url);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
    //طلب الاتصال بالأنترنت باستخدام كلاس Http Url Connection

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();
        try {
            InputStream inputStream = httpURLConnection.getInputStream();
            Scanner input = new Scanner(inputStream);
            input.useDelimiter("\\A");
            boolean hasInput = input.hasNext();
            String response = null;
            if (hasInput) {
                response = input.next();
            }
            input.close();
            Log.d(TAG, response);
            return response;

        } finally {
            httpURLConnection.disconnect();
        }
    }
     */
}
