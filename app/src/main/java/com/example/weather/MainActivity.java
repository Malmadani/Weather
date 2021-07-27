package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.media.audiofx.EnvironmentalReverb;
import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weather.Fragments.PrimaryWeatherInfoFragmnet;
import com.example.weather.Fragments.SecondaryWeatherInfoFragment;
import com.example.weather.adapters.DaysForecastAdapter;
import com.example.weather.adapters.HoursForecastAdapter;
import com.example.weather.entity.Forecast;
import com.example.weather.entity.ForecastLists;
import com.example.weather.entity.WeatherInfo;
import com.example.weather.network.NetworkUtils;
import com.example.weather.utils.OpenWeatherDataParser;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private HoursForecastAdapter hoursAdapter;
    private DaysForecastAdapter daysAdapter;

    private RecyclerView mHoursForecastRecycler;
    private RecyclerView mDaysForecastRecycler;

    private HeaderFragmentAdapter headerFragmentAdapter;
    private ViewPager mViewPager;

    private NetworkUtils mNetworkUtils;

    private static final String  TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNetworkUtils = NetworkUtils.getInstance(this);

        mViewPager = findViewById(R.id.viewpager);
        headerFragmentAdapter = new HeaderFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(headerFragmentAdapter);

        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(mViewPager);

        mHoursForecastRecycler = findViewById(R.id.rv_hours_forecast);
        hoursAdapter = new HoursForecastAdapter(this);
        mHoursForecastRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mHoursForecastRecycler.setAdapter(hoursAdapter);

        mDaysForecastRecycler = findViewById(R.id.rv_days_forecast);
        daysAdapter = new DaysForecastAdapter(this);
        mDaysForecastRecycler.setLayoutManager(new LinearLayoutManager(this));
        mDaysForecastRecycler.setAdapter(daysAdapter);

        requestWeatherInfo();
        requestForecastInfo();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mNetworkUtils.cancelRequests(TAG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings){
            startActivity(new Intent(getBaseContext(), SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestWeatherInfo(){

        String  weatherRequestUrl = NetworkUtils.getWeatherUrl(MainActivity.this).toString();

        JsonObjectRequest weatherRequest = new JsonObjectRequest(
                Request.Method.GET,
                weatherRequestUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Forecasts Request Received!");
                        WeatherInfo weatherInfo = null;
                        try {
                            weatherInfo = OpenWeatherDataParser.getWeatherInfoObjectFormJson(response);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        if (weatherInfo != null){
                            headerFragmentAdapter.ubdateData(weatherInfo);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        weatherRequest.setTag(TAG);

        mNetworkUtils.addToRequestQueue(weatherRequest);
    }

    private void requestForecastInfo(){
        // The getForecastsUrl method will return the URL that we need to get the JSON for the upcoming forecasts
        String forecastsRequestUrl = NetworkUtils.getForecastUrl(MainActivity.this).toString();

        // Request a string response from the provided URL.
        JsonObjectRequest forecastsListRequest = new JsonObjectRequest(Request.Method.GET, forecastsRequestUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Forecasts Request Received");
                        ForecastLists forecastLists = null;
                        try {
                            // Get ForecastLists object from json response
                            forecastLists = OpenWeatherDataParser.getForecastInfoObjectFormJson(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (forecastLists != null
                                && forecastLists.getHoursForecasts() != null
                                && forecastLists.getDaysForecast() != null) {
                            hoursAdapter.updateData(forecastLists.getHoursForecasts());
                            daysAdapter.updateData(forecastLists.getDaysForecast());
                            mHoursForecastRecycler.setVisibility(View.VISIBLE);
                            mDaysForecastRecycler.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Set tag to the request
        forecastsListRequest.setTag(TAG);

        // Add the request to the RequestQueue.
        mNetworkUtils.addToRequestQueue(forecastsListRequest);
    }

    /**
     * أنشاء دالة لتنفيذ الAsyncTask
     *
            public void requestWeatherInfo(){
                new getRequestWeatherInfoFormUrl.execute();
            }
    **/

    /**
     * أستخراج البيانات وتخزينها في كلاس WeatherInfo
     *
                    class  getRequestWeatherInfoFormUrl extends AsyncTask<Void, Integer, WeatherInfo>{

                        @Override
                        protected WeatherInfo doInBackground(Void... voids) {
                            URL weatherURL = NetworkUtils.getWeatherUrl(getBaseContext());
                            WeatherInfo weatherInfo;
                            try {
                                String weatherJsonResponse = NetworkUtils.getResponseFromHttpUrl(weatherURL);
                                weatherInfo = OpenWeatherDataParser.getWeatherInfoObjectFormJson(weatherJsonResponse);
                                return weatherInfo;
                            }catch (IOException | JSONException e){
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(WeatherInfo weatherInfo) {
                            super.onPostExecute(weatherInfo);
                            if (weatherInfo != null){
                                headerFragmentAdapter.ubdateData(weatherInfo);
                            }
                        }
    **/

    class HeaderFragmentAdapter extends FragmentPagerAdapter{

        List<Fragment> fragments;

        public HeaderFragmentAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

            fragments = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new PrimaryWeatherInfoFragmnet();

                case 1:
                    return new SecondaryWeatherInfoFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            fragments.add(fragment);
            return fragment;
        }

        void ubdateData(WeatherInfo weatherInfo){
            ((PrimaryWeatherInfoFragmnet)fragments.get(0)).ubdateWeatherInfo(weatherInfo);
            ((SecondaryWeatherInfoFragment)fragments.get(1)).ubdateWeatherInfo(weatherInfo);
        }
    }
}