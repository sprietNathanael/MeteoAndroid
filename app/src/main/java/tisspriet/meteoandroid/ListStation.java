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
		Station alboussiere = new Station("Alboussière","Station d'altitude du pays de Crussol","44.9434","4.72924","547");
		Station montelimar = new Station("Montélimar","Station du vrai début du Sud","44.5569","4.7495","86");
		HashMap<String,Station> stationList_data = new HashMap<>();
		stationList_data.put(alboussiere.getName(),alboussiere);
		stationList_data.put(montelimar.getName(),montelimar);
		List<HashMap<String, String>> stationList_list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> element;
		Iterator it = stationList_data.entrySet().iterator();
		while(it.hasNext())
		{
			element = new HashMap<String, String>();
			HashMap.Entry pair = (HashMap.Entry)it.next();
			Station currentStation = (Station) pair.getValue();
			element.put("id", currentStation.getName());
			element.put("description", currentStation.getDescription());
			stationList_list.add(element);
			it.remove();
		}
		ListAdapter stationList_adapter = new SimpleAdapter(this, stationList_list,
															android.R.layout.simple_list_item_2,
															new String[]{"id", "description"},
															new int[]{android.R.id.text1,
																	  android.R.id.text2});
		stationList_view.setAdapter(stationList_adapter);
		stationList_view.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				TwoLineListItem itemView = (TwoLineListItem)view;
				TextView itemViewName = (TextView)itemView.getChildAt(0);
				Intent viewStationActivity_Intent = new Intent(ListStation.this, ViewStation
						.class);
				viewStationActivity_Intent.putExtra("id", itemViewName.getText().toString());

				startActivity(viewStationActivity_Intent);

			}
		});
	}
}