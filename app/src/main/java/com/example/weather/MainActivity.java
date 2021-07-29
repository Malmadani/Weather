package com.example.weather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.weather.Fragments.PrimaryWeatherInfoFragmnet;
import com.example.weather.Fragments.SecondaryWeatherInfoFragment;
import com.example.weather.adapters.DaysForecastAdapter;
import com.example.weather.adapters.HoursForecastAdapter;
import com.example.weather.entity.ForecastLists;
import com.example.weather.entity.WeatherForecasts;
import com.example.weather.entity.WeatherInfo;
import com.example.weather.network.NetworkUtils;
import com.example.weather.utils.OpenWeatherDataParser;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_SETTINGS = 133;
    private HoursForecastAdapter hoursAdapter;
    private DaysForecastAdapter daysAdapter;

    private RecyclerView mHoursForecastRecycler;
    private RecyclerView mDaysForecastRecycler;

    private HeaderFragmentAdapter headerFragmentAdapter;
    private ViewPager mViewPager;

    private NetworkUtils mNetworkUtils;

    private Call<WeatherInfo> mWeatherInfoCall;
    private Call<WeatherForecasts> mForecastInfoCall;

    private static final String TAG = MainActivity.class.getSimpleName();

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
    }

    @Override
    protected void onStop() {
        super.onStop();
        mForecastInfoCall.cancel();
        mWeatherInfoCall.cancel();
    }

    /**
            @Override
            protected void onStop() {
                super.onStop();
                mNetworkUtils.cancelRequests(TAG);
            }
    **/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            startActivityForResult(new Intent(getBaseContext(), SettingsActivity.class), REQUEST_SETTINGS);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SETTINGS && resultCode == RESULT_OK) {
            requestWeatherInfo();
            requestForecastInfo();
        }
    }

    private void requestWeatherInfo() {
        mWeatherInfoCall = mNetworkUtils.getmApiInterface().getWeatherInfo(mNetworkUtils.getQueryMap());
        mWeatherInfoCall.enqueue(new Callback<WeatherInfo>() {
            @Override
            public void onResponse(Call<WeatherInfo> call, Response<WeatherInfo> response) {
                if (response.code() == 200){
                    WeatherInfo weatherInfo = response.body();
                    headerFragmentAdapter.ubdateData(weatherInfo);
                }
            }

            @Override
            public void onFailure(Call<WeatherInfo> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestForecastInfo() {
        mForecastInfoCall = mNetworkUtils.getmApiInterface().getForecastInfo(mNetworkUtils.getQueryMap());
        mForecastInfoCall.enqueue(new Callback<WeatherForecasts>() {
            @Override
            public void onResponse(Call<WeatherForecasts> call, Response<WeatherForecasts> response) {
                if (response.code() == 200){
                    WeatherForecasts weatherForecasts = response.body();
                    ForecastLists forecastLists = OpenWeatherDataParser.getForecastsDataFromWeatherforecasts(weatherForecasts);
                    hoursAdapter.updateData(forecastLists.getHoursForecasts());
                    daysAdapter.updateData(forecastLists.getDaysForecast());
                }
            }

            @Override
            public void onFailure(Call<WeatherForecasts> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
            private void requestWeatherInfo() {

                String weatherRequestUrl = NetworkUtils.getWeatherUrl(MainActivity.this).toString();

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

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (weatherInfo != null) {
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


    private void requestForecastInfo() {
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
     * <p>
     * class  getRequestWeatherInfoFormUrl extends AsyncTask<Void, Integer, WeatherInfo>{
     *
     * @Override protected WeatherInfo doInBackground(Void... voids) {
     * URL weatherURL = NetworkUtils.getWeatherUrl(getBaseContext());
     * WeatherInfo weatherInfo;
     * try {
     * String weatherJsonResponse = NetworkUtils.getResponseFromHttpUrl(weatherURL);
     * weatherInfo = OpenWeatherDataParser.getWeatherInfoObjectFormJson(weatherJsonResponse);
     * return weatherInfo;
     * }catch (IOException | JSONException e){
     * e.printStackTrace();
     * }
     * return null;
     * }
     * @Override protected void onPostExecute(WeatherInfo weatherInfo) {
     * super.onPostExecute(weatherInfo);
     * if (weatherInfo != null){
     * headerFragmentAdapter.ubdateData(weatherInfo);
     * }
     * }
     **/

    class HeaderFragmentAdapter extends FragmentPagerAdapter {

        List<Fragment> fragments;

        public HeaderFragmentAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

            fragments = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
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

        void ubdateData(WeatherInfo weatherInfo) {
            ((PrimaryWeatherInfoFragmnet) fragments.get(0)).ubdateWeatherInfo(weatherInfo);
            ((SecondaryWeatherInfoFragment) fragments.get(1)).ubdateWeatherInfo(weatherInfo);
        }
    }
}