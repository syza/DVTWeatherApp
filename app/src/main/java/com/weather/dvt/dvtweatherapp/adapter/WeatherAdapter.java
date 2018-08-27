package com.weather.dvt.dvtweatherapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.weather.dvt.dvtweatherapp.Pojo.Weather;
import com.weather.dvt.dvtweatherapp.R;
import com.weather.dvt.dvtweatherapp.constant.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class WeatherAdapter extends BaseAdapter {

    //attributes of the weather adapter
    private Context context;
    private ArrayList<Weather> listOfForcast;

    //Constructor that will be used when want to instantiate that adapter object
    public WeatherAdapter(Context context, ArrayList<Weather> listOfForcast) {
        this.context = context;
        this.listOfForcast = listOfForcast;
    }

    @Override
    public int getCount() {
        return listOfForcast.size();
    }

    @Override
    public Object getItem(int position) {
        return listOfForcast.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "NewApi"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Binding adapter xml with java
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);

        //Initialising the weather object from the list that is passed in that current index
        Weather weatherObj = listOfForcast.get(position);

        //declaration of my value holder
        TextView txtDay;
        ImageView icons;
        TextView maxTemp;

        //binding xml with java code
        txtDay = convertView.findViewById(R.id.dayName);
        icons = convertView.findViewById(R.id.tempIcon);
        maxTemp = convertView.findViewById(R.id.maxTemp);

        //formatting date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(weatherObj.getWeatherDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);


        //FILTERING THE DATE
        switch (String.valueOf((new SimpleDateFormat("EEE").format(cal.getTime()))))
        {
            case "Mon":
                    txtDay.setText(Constant.MON_KEY);
                break;
            case "Tue":
                txtDay.setText(Constant.TUE_KEY);
                break;
            case "Wed":
                txtDay.setText(Constant.WEN_KEY);
                break;
            case "Thu":
                txtDay.setText(Constant.THUR_KEY);
                break;
            case "Fri":
                txtDay.setText(Constant.FRI_KEY);
                break;
            case "Sat":
                txtDay.setText(Constant.SAT_KEY);
                break;
            case "Sun":
                txtDay.setText(Constant.SUN_KEY);
                break;
        }


        //FILTERING THE WEATHER ICONS
        switch (weatherObj.getIconIdentifies())
        {
            case "Clear":
                icons.setImageResource(R.drawable.clear);
                break;
            case "Clouds":
                icons.setImageResource(R.drawable.partlysunny);
                break;
            case "Rain":
                icons.setImageResource(R.drawable.rain);
                break;
        }

        maxTemp.setText(String.valueOf((int)((Double.parseDouble(String.valueOf(weatherObj.getMarTemperature()))))));
        return convertView;
    }
}
