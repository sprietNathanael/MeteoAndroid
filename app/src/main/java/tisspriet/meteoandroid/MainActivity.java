package tisspriet.meteoandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class MainActivity extends Activity
{
	private ListView favStationList;
	private Button buttonTo_ListStationActivity;
	private Button getButtonTo_refreshStations;
	private HashMap<String, Station> stationList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		StationDAO.makeDAO(MainActivity.this);
		stationList = StationDAO.getStationList();
		while(stationList.isEmpty())
		{
			;
		}
		for(String i : stationList.keySet())
		{
			StationDAO.addReleveToStation(stationList.get(i));
			while(stationList.get(i).getLastMeasure() == null)
			{
				;
			}
		}
		setContentView(R.layout.activity_main);
		favStationList = (ListView)findViewById(R.id.favStationList);

		buttonTo_ListStationActivity = (Button)findViewById(R.id.buttonTo_ListStationActivity);
		getButtonTo_refreshStations = (Button)findViewById(R.id.buttonTo_refreshStations);
		buttonTo_ListStationActivity.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent listStationActivity_Intent = new Intent(MainActivity.this,
															   ListStation.class);
				startActivity(listStationActivity_Intent);
			}
		});
		getButtonTo_refreshStations.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
				startActivity(getIntent());
			}
		});


	}

	@Override
	protected void onResume()
	{
		super.onResume();
		SharedPreferences preferences = null;
		preferences = MainActivity.this.getSharedPreferences("favStationList",
															 Context.MODE_WORLD_WRITEABLE);
		Log.d("Preferences",
			  ((HashSet)preferences.getStringSet("fav", new HashSet<String>())).toString());
		HashSet<String> favSet = new HashSet<>();
		favSet = (HashSet)preferences.getStringSet("fav", new HashSet<String>());
		ArrayList<Station> favStation_data = new ArrayList<>();
		Log.d("Need measures", "now");
		for(String i : favSet)
		{
			favStation_data.add(StationDAO.getStation(i));
		}
		List<HashMap<String, String>> favStation_list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> element;
		for(int i = 0 ; i < favStation_data.size() ; i++)
		{
			element = new HashMap<String, String>();
			element.put("name", favStation_data.get(i).getName());
			element.put("description", favStation_data.get(i).getDescription());
			element.put("measure",
						String.format("%3.1f °C", favStation_data.get(i).getLastTemperature()));
			element.put("condition", favStation_data.get(i).getLastCondition());
			element.put("tendance", String.format("%d", favStation_data.get(i).getTendance()));
			favStation_list.add(element);
		}
		FavStationItemAdapter favstationList_adapter = new FavStationItemAdapter(this, favStation_list);
		favStationList.setAdapter(favstationList_adapter);
	}
}
