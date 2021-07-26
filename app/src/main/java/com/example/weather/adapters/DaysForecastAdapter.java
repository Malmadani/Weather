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

import java.util.List;

public class DaysForecastAdapter extends RecyclerView.Adapter<DaysForecastAdapter.DaysForecastViewHolder> {

    private Context mContext;
    private List<Forecast> mForecast;

    public DaysForecastAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public DaysForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_day_forecast, parent, false);
        return new DaysForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DaysForecastAdapter.DaysForecastViewHolder holder, int position) {
        holder.weatherIcone.setImageResource(R.drawable.ic_clear_sky);
        holder.date.setText("Today, April 03");
        holder.weatherDescription.setText("Cloudy");
        holder.highTemperature.setText("19°");
        holder.lowTemperature.setText("10°");

    }

    @Override
    public int getItemCount() {
        if (mForecast == null) {
            return 0;
        } else {
            return mForecast.size();
        }
    }

    public static class DaysForecastViewHolder extends RecyclerView.ViewHolder {

        private ImageView weatherIcone;
        private TextView date;
        private TextView weatherDescription;
        private TextView highTemperature;
        private TextView lowTemperature;

        public DaysForecastViewHolder(@NonNull View itemView) {
            super(itemView);

            weatherIcone = itemView.findViewById(R.id.weather_icon);
            date = itemView.findViewById(R.id.date);
            weatherDescription = itemView.findViewById(R.id.weather_description);
            highTemperature = itemView.findViewById(R.id.high_temperature);
            lowTemperature = itemView.findViewById(R.id.low_temperature);
        }
    }

    public void updateData(List<Forecast> forecasts) {
        mForecast = forecasts;
        notifyDataSetChanged();
    }
}
