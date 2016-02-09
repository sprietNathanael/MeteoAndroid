package tisspriet.meteoandroid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by nathanael on 06/02/16.
 */

public class ListStation extends Activity
{
	private ListView stationList_view;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liststation_layout);
		stationList_view = (ListView)findViewById(R.id.stationList);

		/*Récupération de la liste des stations*/
		HashMap<String, Station> stationList_data = StationDAO.getStationList();

		List<HashMap<String, String>> station_IntentArray = new ArrayList<HashMap<String,
				String>>();
		HashMap<String, String> element;
		Iterator it = stationList_data.entrySet().iterator();
		/*Récupération des préférences*/
		SharedPreferences preferences = null;
		preferences = ListStation.this.getSharedPreferences(getResources().getString(R.string.favStationList_preferencesName),
															Context.MODE_WORLD_WRITEABLE);
		HashSet<String> favSet = new HashSet<>();
		favSet = (HashSet)preferences.getStringSet(getResources().getString(R.string.favStationList_preferencesArrayName), new HashSet<String>());
		/*Copie des données vers le paramètre à passer*/
		while(it.hasNext())
		{
			element = new HashMap<String, String>();
			HashMap.Entry pair = (HashMap.Entry)it.next();
			Station currentStation = (Station)pair.getValue();
			element.put(getResources().getString(R.string.station_IntentArray_name),
						currentStation.getName());
			element.put(getResources().getString(R.string.station_IntentArray_description),
						currentStation.getDescription());
			if(favSet.contains(currentStation.getName()))
			{
				element.put(getResources().getString(R.string.station_IntentArray_fav),
							getResources().getString(R.string.favStationList_preferencesArray_value_true));
			}
			else
			{
				element.put(getResources().getString(R.string.station_IntentArray_fav),
							getResources().getString(R.string
															 .favStationList_preferencesArray_value_false));
			}
			station_IntentArray.add(element);
		}
		Log.d("test5", station_IntentArray.toString());
		StationItemAdapter stationList_adapter = new StationItemAdapter(this, station_IntentArray);
		stationList_view.setAdapter(stationList_adapter);


	}
}
