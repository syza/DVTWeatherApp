package com.weather.dvt.dvtweatherapp.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.weather.dvt.dvtweatherapp.Pojo.Weather;
import com.weather.dvt.dvtweatherapp.R;
import com.weather.dvt.dvtweatherapp.adapter.WeatherAdapter;
import com.weather.dvt.dvtweatherapp.constant.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Forest extends Fragment implements LocationListener {


    View view;

    private String locationProvider;
    private static String FOREST="Forest";

    //Add a new TextView to your activity_main to display the address
    private LocationManager locationManager;

    RelativeLayout outLayout,midLayout;
    Window window;
    ListView listHolder;
    TextView txtcurrentTemp,txtcurrentTempDescr,txtminTemperature,txtmaxTemperature,txtcurrentTemperature;


    ArrayList<Weather> listOfWeather,listOfWeatherSorted;

    static double OBTAINLATITUDE;
    static double OBTAINLONGITUDE;
    DecimalFormat decimalForm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        //BINDING THE XML WITH JAVA
        view = inflater.inflate(R.layout.forest_layout,container,false);

        return view;
    }

    @SuppressLint({"NewApi", "ResourceAsColor"})
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        outLayout = view.findViewById(R.id.imageLayout);
        midLayout =view.findViewById(R.id.minLayout);


        //
        window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        txtcurrentTemp  = view.findViewById(R.id.curTempValue);
        txtcurrentTempDescr  = view.findViewById(R.id.tempDecription);

        txtminTemperature  = view.findViewById(R.id.minTemp);
        txtmaxTemperature  = view.findViewById(R.id.maxTemp);
        txtcurrentTemperature  = view.findViewById(R.id.currentTemp);
        listHolder  = view.findViewById(R.id.forcastList);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        //Rounding off latitude and latitude to 2 decimal places
        decimalForm = new DecimalFormat("##.00");

        // The indication of  the application criteria for selecting a location provider. Providers maybe ordered according to accuracy,
        // power usage, ability to report altitude, speed, and bearing, and monetary cost.
        Criteria criteria = new Criteria();
        locationProvider = locationManager.getBestProvider(criteria, false);

        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            Log.d(FOREST,"Provider " + locationProvider + " has been selected.");
            onLocationChanged(location);
        } else {

            Log.d(FOREST,"Latitude coordinate not found");
            Log.d(FOREST,"Longitude coordinate not found");
        }

        //PULLING FORECAST REQUEST FOR 5DAYS
        synchronizedWeatherForecast(String.valueOf(decimalForm.format(OBTAINLATITUDE)),String.valueOf(decimalForm.format(OBTAINLONGITUDE)));
    }
    //##################################################################################################################

    //##################################################################################################################

    public void synchronizedWeather(String cityLatitude,String cityLongitude)
    {
        //Weather http url that takes latitude and longitude and perform get request
        String openWeatherApiUrl ="http://api.openweathermap.org/data/2.5/weather?lat="+cityLatitude+"&lon="+cityLongitude+"&appid=3f45a4729d355a847c955380a81b1f49&mode=json&units=metric";

        JsonObjectRequest volleyJsonRequest = new JsonObjectRequest(Request.Method.GET, openWeatherApiUrl, null, new Response.Listener<JSONObject>() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    JSONObject main_object = response.getJSONObject(Constant.JSON_OBJ_NAME_KEY);
                    JSONArray array = response.getJSONArray(Constant.JSON_WEATHER_OBJ_KEY);
                    JSONObject object = array.getJSONObject(0);

                    String curTemp = String.valueOf(main_object.getDouble(Constant.CURRENT_TEMP_KEY));
                    String miniumTemp = String.valueOf(main_object.getDouble(Constant.MINM_TEMP_KEY));
                    String maximumTemp = String.valueOf(main_object.getDouble(Constant.MAXX_TEMP_KEY));

                    String iconDescr = object.getString(Constant.JSON_OBJ_NAME_KEY);
                    String tempDescr = object.getString(Constant.WEATHER_DECRIPTION_KEY);

                    Resources res = getResources();
                    Drawable drawable = null;

                    if(iconDescr.equals(Constant.SUNNY_KEY))
                    {
                        drawable = res.getDrawable(R.drawable.forest_sunny);//added to the res folder
                        window.setStatusBarColor(getActivity().getResources().getColor(R.color.status_bar_sunny));
                        midLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.sunny));
                        outLayout.setBackground(drawable);

                    }else if(iconDescr.equals(Constant.CLOUD_KEY))
                    {
                        drawable = res.getDrawable(R.drawable.forest_cloudy);//added to the res folder
                        window.setStatusBarColor(getActivity().getResources().getColor(R.color.status_bar_cloudy));
                        midLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.cloudy));
                        outLayout.setBackground(drawable);

                    }else if(iconDescr.equals(Constant.CLEAR_KEY)){

                        drawable = res.getDrawable(R.drawable.forest_sunny);//added to the res folder
                        window.setStatusBarColor(getActivity().getResources().getColor(R.color.status_bar_sunny));
                        midLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.sunny));
                        outLayout.setBackground(drawable);

                    }else if(iconDescr.equals(Constant.RAIN_KEY)) {

                        drawable = res.getDrawable(R.drawable.forest_rainy);//added to the res folder
                        window.setStatusBarColor(getActivity().getResources().getColor(R.color.status_bar_rainy));
                        midLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.rainy));
                        outLayout.setBackground(drawable);
                    }

                    //METHOD THAT CONVERT AND ROUND OFF THE DOUBLE TEMPERATURE
                    txtcurrentTemp.setText(String.valueOf((int)(Double.parseDouble(curTemp))));
                    txtcurrentTempDescr.setText(tempDescr);

                    txtminTemperature.setText(String.valueOf((int)((Double.parseDouble(miniumTemp)))));
                    txtmaxTemperature.setText(String.valueOf((int)((Double.parseDouble(maximumTemp)))));
                    txtcurrentTemperature.setText(String.valueOf((int)((Double.parseDouble(curTemp)))));


                }catch(JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(volleyJsonRequest);
    }


    //##################################################################################################################


    //##################################################################################################################
    public void synchronizedWeatherForecast(String cityLatitude,String cityLongitude)
    {

        //Weather http url that takes latitude and longitude and perform get request
        String openWeatherApiUrl ="http://api.openweathermap.org/data/2.5/forecast?lat="+cityLatitude+"&lon="+cityLongitude+"&appid=3f45a4729d355a847c955380a81b1f49&mode=json&units=metric";

        //
        JsonObjectRequest volleyJsonRequest = new JsonObjectRequest(Request.Method.GET, openWeatherApiUrl, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(JSONObject response) {
                try
                {

                    //INTERACTION TO THE LIST
                    JSONArray array = response.getJSONArray("list");

                    listOfWeather = new ArrayList<>();
                    for(int x=0; x<array.length();x++)
                    {
                        JSONObject object = array.getJSONObject(x);
                        JSONObject main_object = object.getJSONObject(Constant.JSON_OBJ_NAME_KEY);

                        JSONArray arrayWeather = object.getJSONArray(Constant.JSON_WEATHER_OBJ_KEY);
                        JSONObject objectWeather = arrayWeather.getJSONObject(0);



                        //formatting date
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = null;
                        try {
                            date = sdf.parse(object.getString("dt_txt"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);

                        listOfWeather.add(new Weather(main_object.getDouble(Constant.MAXX_TEMP_KEY), objectWeather.getString(Constant.JSON_OBJ_NAME_KEY), objectWeather.getString("description"), new SimpleDateFormat("yyyy-MM-dd").format(date)));
                    }

                    //Setting the list to the adapter
                    WeatherAdapter adapter = new WeatherAdapter(getActivity(),listOfWeather);
                    listHolder.setAdapter(adapter);


                }catch(JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(volleyJsonRequest);
    }


    //##################################################################################################################
    //The permission you choose determines the accuracy of the location returned by the API
    //##################################################################################################################
    @Override
    public void onLocationChanged(Location location) {
        //You had this as int. It is advised to have Lat/Loing as double.
         OBTAINLATITUDE = location.getLatitude();
        OBTAINLONGITUDE = location.getLongitude();

        Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(OBTAINLATITUDE, OBTAINLONGITUDE, 1);

            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i=0; i<maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }

            //Passing the latitude and longitude as parameters
            synchronizedWeather(String.valueOf(decimalForm.format(OBTAINLATITUDE)),String.valueOf(decimalForm.format(OBTAINLONGITUDE)));


        } catch (IOException e) {
            // Handle IOException
        } catch (NullPointerException nullPointerException) {
            // Handle NullPointerException
            Log.d( FOREST, nullPointerException.getMessage());
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getActivity(), "Enabled new provider " + provider,
            Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getActivity(), "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(locationProvider, 800, 1, this);
    }

}
