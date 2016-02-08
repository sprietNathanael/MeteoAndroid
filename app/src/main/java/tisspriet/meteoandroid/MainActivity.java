package tisspriet.meteoandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class MainActivity extends Activity
{
	private ListView favStationList;

	private Button buttonTo_ListStationActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		favStationList = (ListView)findViewById(R.id.favStationList);

		buttonTo_ListStationActivity = (Button)findViewById(R.id.buttonTo_ListStationActivity);
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

	}

	@Override
	protected void onResume()
	{
		super.onResume();
		StationDAO myDao = new StationDAO(MainActivity.this, R.raw.stations, R.raw.measures);
		SharedPreferences preferences = null;
		preferences = MainActivity.this.getSharedPreferences("favStationList", Context.MODE_WORLD_WRITEABLE);
		Log.d("Preferences",
			  ((HashSet)preferences.getStringSet("fav", new HashSet<String>())).toString());
		HashSet<String> favSet = new HashSet<>();
		favSet = (HashSet)preferences.getStringSet("fav", new HashSet<String>());
		ArrayList<Station> favStation_data = new ArrayList<>();
		for(String i : favSet)
		{
			favStation_data.add(myDao.getStation(i));
		}
		List<HashMap<String, String>> favStation_list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> element;
		for(int i = 0 ; i < favStation_data.size() ; i++)
		{
			myDao.addReleveToStation(favStation_data.get(i));
			element = new HashMap<String, String>();
			element.put("name", favStation_data.get(i).getName());
			element.put("description", favStation_data.get(i).getDescription());
			element.put("measure", String.format("%f °C", favStation_data.get(i).getLastTemperature()));
			element.put("condition", favStation_data.get(i).getLastCondition());
			element.put("tendance", String.format("%d", favStation_data.get(i).getTendance()));
			favStation_list.add(element);
		}
		ListAdapter favStation_adapter = new SimpleAdapter(this, favStation_list,
														   R.layout.favsation_listitem,
														   new String[]{"name", "description",
																		"measure", "condition",
																		"tendance"},
														   new int[]{R.id.favStationText_name,
																	 R.id
																			 .favStationText_decription,
																	 R.id.favStationText_measure,
																	 R.id.favStationText_condition,
																	 R.id
																			 .favStationText_tendance});
		favStationList.setAdapter(favStation_adapter);
	}
}
