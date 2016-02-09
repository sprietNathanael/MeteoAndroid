package tisspriet.meteoandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
	private ListView lastMeasuresList_view;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewstation_layout);
		stationName_text = (TextView)findViewById(R.id.viewStation_name);
		stationDescription_text = (TextView)findViewById(R.id.viewStation_description);
		stationPosition_text = (TextView)findViewById(R.id.viewStation_position);
		Intent intentLauncher = getIntent();
		String intentStationName = intentLauncher.getStringExtra("id");
		Station selectedStation = StationDAO.getStation(intentStationName);
		stationName_text.setText(intentStationName);
		stationDescription_text.setText(selectedStation.getDescription());
		stationPosition_text.setText(selectedStation.gpsString());

		lastMeasuresList_view = (ListView)findViewById(R.id.lastMeasuresList);
		ArrayList<Measure> measuresList_data = selectedStation.getAllMeasures();
		List<HashMap<String, String>> measuresList_list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> element;
		for(int i = 0 ; i < measuresList_data.size() ; i++)
		{
			element = new HashMap<String, String>();
			element.put("date", measuresList_data.get(i).getDate());
			element.put("temperature",
						String.format("%f °C", measuresList_data.get(i).getTemperature()));
			element.put("condition", measuresList_data.get(i).getCondition());
			measuresList_list.add(element);
		}
		MeasureItemAdapter measureList_adapter = new MeasureItemAdapter(this, measuresList_list);
		lastMeasuresList_view.setAdapter(measureList_adapter);


	}
}
