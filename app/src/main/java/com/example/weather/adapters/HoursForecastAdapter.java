package com.example.weather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.entity.Forecast;
import com.example.weather.network.NetworkUtils;
import com.example.weather.utils.CustomDateUtils;
import com.example.weather.utils.WeatherUtils;

import java.util.List;

public class HoursForecastAdapter extends RecyclerView.Adapter<HoursForecastAdapter.HoursForecastViewHolder> {

    private Context mContext;
    private List<Forecast> mForecast;

    public HoursForecastAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public HoursForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_hour_forecast, parent, false);
        return new HoursForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HoursForecastAdapter.HoursForecastViewHolder holder, int position) {
        Forecast forecast = mForecast.get(position);

        int weatherImageIcon = WeatherUtils.getWeatherIcon(forecast.getWeathers().get(0).getIcon());

        holder.weatherIcon.setImageResource(weatherImageIcon);

        String hourClockString = CustomDateUtils.getHourOfDay(forecast.getDt());

        holder.time.setText(hourClockString);

        double highTemperature = forecast.getMain().getTempMax();
        String highTemperatureString = mContext.getString(R.string.format_temperature, highTemperature);
        holder.temperature.setText(highTemperatureString);

    }

    @Override
    public int getItemCount() {
        if (mForecast == null) {
            return 0;
        } else {
            return mForecast.size();
        }
    }

    protected static class HoursForecastViewHolder extends RecyclerView.ViewHolder {

        private ImageView weatherIcon;
        private TextView time;
        private TextView temperature;

        public HoursForecastViewHolder(@NonNull View itemView) {
            super(itemView);

            weatherIcon = itemView.findViewById(R.id.weather_icon);
            time = itemView.findViewById(R.id.time);
            temperature = itemView.findViewById(R.id.temperature);
        }
    }

    public void updateData(List<Forecast> forecasts) {
        mForecast = forecasts;
        notifyDataSetChanged();
    }
}
