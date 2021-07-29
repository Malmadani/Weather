package com.example.weather.network;

import android.content.Context;

//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.Volley;
import com.example.weather.R;
import com.example.weather.utils.SharedPreferencesHelper;

import java.util.HashMap;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    //private RequestQueue mRequestQueue;

    public static NetworkUtils getInstance(Context context) {
        if (sInstane == null) {
            synchronized (LOCK) {
                if (sInstane == null) sInstane = new NetworkUtils(context);
            }
        }
        return sInstane;
    }

    /**
     * public RequestQueue getRequestQueue() {
     if (mRequestQueue == null) {
     mRequestQueue = new Volley().newRequestQueue(mContext);
     }
     return mRequestQueue;
     }
     **/
    /**
     public <T> void addToRequestQueue(Request<T> request) {
     getRequestQueue().add(request);
     }
     **/
    /**
     * public void cancelRequests(String tag) {
     * getRequestQueue().cancelAll(tag);
     * }
     **/

    private OpenWeatherApiInterface mApiInterface;

    private NetworkUtils(Context context) {
        mContext = context.getApplicationContext();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        mApiInterface = retrofit.create(OpenWeatherApiInterface.class);
        //mRequestQueue = getRequestQueue();
    }

    public OpenWeatherApiInterface getmApiInterface() {
        return mApiInterface;
    }

    /**
     * public static URL getWeatherUrl(Context context) {
     * return buildUrl(context, WEATHER_ENDPOINT);
     * }
     * <p>
     * public static URL getForecastUrl(Context context) {
     * return buildUrl(context, FORECAST_ENDPOINT);
     * <p>
     * }
     **/

    public HashMap<String, String> getQueryMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put(QUERY_PARAM, SharedPreferencesHelper.getPreferedWeatherLocation(mContext));
        map.put(FORMAT_PARAM, FORMAT);
        map.put(UNITS_PARAM, SharedPreferencesHelper.getPreferedMeasurementSystem(mContext));
        map.put(LANG_PARAM, Locale.getDefault().getLanguage());
        map.put(APP_ID_PARAM, mContext.getString(R.string.api_key));

        return map;
    }

    /**
     private static URL buildUrl(Context context, String endPoint) {
     Uri.Builder uriBuldir = Uri.parse(BASE_URL + endPoint).buildUpon();
     Uri uri = uriBuldir
     .appendQueryParameter(QUERY_PARAM, SharedPreferencesHelper.getPreferedWeatherLocation(context))
     .appendQueryParameter(FORMAT_PARAM, FORMAT)
     .appendQueryParameter(UNITS_PARAM, SharedPreferencesHelper.getPreferedMeasurementSystem(context))
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
     **/
}
