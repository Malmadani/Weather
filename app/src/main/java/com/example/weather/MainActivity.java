package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.os.Bundle;
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

        List<Forecast> hoursForecast = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            hoursForecast.add(new Forecast());
        }

        List<Forecast> daysForecast = new ArrayList<>();
        for (int i = 0; i< 7; i++){
            daysForecast.add(new Forecast());
        }

        hoursAdapter.updateData(hoursForecast);
        daysAdapter.updateData(daysForecast);

        requestWeatherInfo();
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
        mNetworkUtils.addToRequestQueue(weatherRequest);
    }

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