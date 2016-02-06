package tisspriet.meteoandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nathanael on 06/02/16.
 */
public class ViewStation extends Activity
{
	private TextView stationName_text;
	private TextView stationDescription_text;
	private TextView stationPosition_text;
	private TextView stationMeasure_text;
	private ListView lastMeasuresList_view;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewstation_layout);
		stationName_text = (TextView)findViewById(R.id.viewStation_name);
		stationDescription_text = (TextView)findViewById(R.id.viewStation_description);
		stationPosition_text = (TextView)findViewById(R.id.viewStation_position);
		stationMeasure_text = (TextView)findViewById(R.id.viewStation_measures);
		Intent intentLauncher = getIntent();
		String intentStationName = intentLauncher.getStringExtra("id");
		if(intentLauncher != null)
		{
			stationName_text.setText(intentStationName);
		}
		stationDescription_text.setText("Station d'altitude du pays de Crussol");
		stationPosition_text.setText("GPS : 44.9434 N 4.72924 O 547 m");
		stationMeasure_text.setText("Date : 2015-11-27 18:47:57 -> 18°");

		lastMeasuresList_view = (ListView)findViewById(R.id.lastMeasuresList);
		String[][] measuresList_data = new String[][]{{"2015-11-27 18:30:00", "18"},
													  {"2015-11-27 18:00:00", "17"},
													  {"2015-11-27 17:30:00", "19"},
													  {"2015-11-27 17:00:00", "19,5"},
													  {"2015-11-27 16:30:00", "19"},
													  {"2015-11-27 16:00:00", "20"},
													  {"2015-11-27 15:30:00", "20"},
													  {"2015-11-27 15:00:00", "21"},
													  {"2015-11-27 14:30:00", "21"},
													  {"2015-11-27 14:00:00", "21,5"},


		};
		List<HashMap<String, String>> measuresList_list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> element;
		for(int i = 0 ; i < measuresList_data.length ; i++)
		{
			element = new HashMap<String, String>();
			element.put("date", measuresList_data[i][0]);
			element.put("temperature", measuresList_data[i][1]);
			measuresList_list.add(element);
		}
		ListAdapter measuresList_adapter = new SimpleAdapter(this, measuresList_list,
															android.R.layout.simple_list_item_2,
															new String[]{"date", "temperature"},
															new int[]{android.R.id.text1,
																	  android.R.id.text2});
		lastMeasuresList_view.setAdapter(measuresList_adapter);


	}
}
