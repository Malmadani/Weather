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
import com.example.weather.utils.CustomDateUtils;
import com.example.weather.utils.WeatherUtils;

import java.util.List;

public class DaysForecastAdapter extends RecyclerView.Adapter<DaysForecastAdapter.DaysForecastViewHolder> {

    private Context mContext;
    private List<List<Forecast>> mForecast;

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
        Forecast forecast = mForecast.get(position).get(0);

        /* Weather Icon ************************************************************************* */

        // Get the weather icon resource id based on icon string passed from the api
        int weatherImageId = WeatherUtils.getWeatherIcon(forecast.getWeathers().get(0).getIcon());

        // Display weather condition icon
        holder.weatherIcone.setImageResource(weatherImageId);

        /* Weather Date ************************************************************************* */

        // Get human readable string using getFriendlyDateString utility method and display it
        String dateString = CustomDateUtils.getFriendlyDateString(mContext, forecast.getDt(), false);

        /* Display friendly date string */
        holder.date.setText(dateString);

        /* Weather Description ****************************************************************** */

        // Get weather condition description
        String description = forecast.getWeathers().get(0).getDescription();

        // Display weather description
        holder.weatherDescription.setText(description);


        /* High (max) temperature *************************************************************** */

        // Read high temperature from forecast object
        double highTemperature = forecast.getMain().getTempMax();

        // Get formatted high temperature string
        String highTemperatureString = mContext.getString(R.string.format_temperature, highTemperature);

        // Display high temperature
        holder.highTemperature.setText(highTemperatureString);


        /* Low (min) temperature **************************************************************** */

        // Read low temperature from forecast object
        double lowTemperature = forecast.getMain().getTempMin();

        // Get formatted low temperature string
        String lowTemperatureString = mContext.getString(R.string.format_temperature, lowTemperature);

        // Display low temperature
        holder.lowTemperature.setText(lowTemperatureString);

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

    public void updateData(List<List<Forecast>> forecasts) {
        this.mForecast = forecasts;
        notifyDataSetChanged();
    }
}
