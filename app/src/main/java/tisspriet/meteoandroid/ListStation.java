package tisspriet.meteoandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.internal.view.menu.MenuView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import java.util.ArrayList;
import java.util.HashMap;
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
		StationDAO myDao = new StationDAO(ListStation.this, R.raw.stations, R.raw.measures);
		HashMap<String, Station> stationList_data = myDao.getStationList();
		List<HashMap<String, String>> stationList_list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> element;
		Iterator it = stationList_data.entrySet().iterator();
		while(it.hasNext())
		{
			element = new HashMap<String, String>();
			HashMap.Entry pair = (HashMap.Entry)it.next();
			Station currentStation = (Station)pair.getValue();
			element.put("id", currentStation.getName());
			element.put("description", currentStation.getDescription());
			if(currentStation.getName().compareTo("Alboussière") == 0)
			{
				element.put("fav", "true");
			}
			else
			{
				element.put("fav","false");
			}
			stationList_list.add(element);
			it.remove();
		}
		StationItemAdapter stationList_adapter = new StationItemAdapter(this, stationList_list);

		stationList_view.setAdapter(stationList_adapter);


	}
}
