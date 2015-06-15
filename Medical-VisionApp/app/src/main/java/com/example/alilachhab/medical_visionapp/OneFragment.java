package com.example.alilachhab.medical_visionapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.app.Fragment;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class OneFragment extends Fragment
{

    public OneFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Array met fake data
        String[] forecastArray =
                {
                        "Today - Sunny - 07:00",
                        "Tomorrow - Foggy - 70/40",
                        "Weds - Cloudy - 72/63",
                        "Thurs - Asteroids - 75/65",
                        "Fri - Heavy Rain - 65/56",
                        "Sat - Sunny - 60/51",
                        "Sun - Sunny - 80/68"
                };

        List<String> weekForecast = new ArrayList<String>(Arrays.asList(forecastArray));

        ArrayAdapter ForecastAdapter = new ArrayAdapter<String>
                (getActivity(),
                        R.layout.list_item_forecast,
                        R.id.list_item_forecast_textview,
                        weekForecast );

        //De ArrayAdapter en ListView worden met elkaar gecombineerd.
        //Hierdoor krijg je de waardes van de array te zien in de lijst

        ListView ListView1 = (ListView) rootView.findViewById(R.id.list_item_forecast_Listview);
        ListView1.setAdapter(ForecastAdapter);

        return rootView;
    }
}
