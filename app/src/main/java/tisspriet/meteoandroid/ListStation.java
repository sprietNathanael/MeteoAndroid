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

		HashMap<String, Station> stationList_data = StationDAO.getStationList();
		List<HashMap<String, String>> stationList_list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> element;
		Iterator it = stationList_data.entrySet().iterator();
		SharedPreferences preferences = null;
		preferences = ListStation.this.getSharedPreferences("favStationList",
															Context.MODE_WORLD_WRITEABLE);
		HashSet<String> favSet = new HashSet<>();
		favSet = (HashSet)preferences.getStringSet("fav", new HashSet<String>());
		while(it.hasNext())
		{
			element = new HashMap<String, String>();
			HashMap.Entry pair = (HashMap.Entry)it.next();
			Station currentStation = (Station)pair.getValue();
			element.put("id", currentStation.getName());
			element.put("description", currentStation.getDescription());
			if(favSet.contains(currentStation.getName()))
			{
				element.put("fav", "true");
			}
			else
			{
				element.put("fav", "false");
			}
			stationList_list.add(element);
			//it.remove();
		}
		StationItemAdapter stationList_adapter = new StationItemAdapter(this, stationList_list);
		stationList_view.setAdapter(stationList_adapter);


	}
}
