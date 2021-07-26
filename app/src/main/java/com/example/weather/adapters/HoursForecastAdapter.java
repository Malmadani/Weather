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
        holder.weatherIcon.setImageResource(R.drawable.ic_clear_sky);

        holder.time.setText("12:00 PM");

        holder.temperature.setText("19°");
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
