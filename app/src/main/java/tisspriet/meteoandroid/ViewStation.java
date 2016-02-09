package tisspriet.meteoandroid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
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
	private ListView measuresList_view;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewstation_layout);

		stationName_text = (TextView)findViewById(R.id.viewStation_name);
		stationDescription_text = (TextView)findViewById(R.id.viewStation_description);
		stationPosition_text = (TextView)findViewById(R.id.viewStation_position);

		Intent intentLauncher = getIntent();
		String intentStationName = intentLauncher.getStringExtra(getResources().getString(R.string.station_Extra_name));
		/*récupération de la station*/
		final Station selectedStation = StationDAO.getStation(intentStationName);

		stationName_text.setText(intentStationName);
		stationDescription_text.setText(selectedStation.getDescription());
		stationPosition_text.setText(selectedStation.getPostition());

		measuresList_view = (ListView)findViewById(R.id.measuresList);
		ArrayList<Measure> measuresList_data = selectedStation.getAllMeasures();
		List<HashMap<String, String>> measures_IntentArray = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> element;
		/*Copie des données vers le paramètre à passer*/
		for(int i = 0 ; i < measuresList_data.size() ; i++)
		{
			element = new HashMap<String, String>();
			element.put(getResources().getString(R.string.measure_IntentArray_date),
						measuresList_data.get(i).getDate());
			element.put(getResources().getString(R.string.measure_IntentArray_temperature),
						String.format(getResources().getString(R.string.temperatureFormat), measuresList_data.get(i).getTemperature()));
			element.put(getResources().getString(R.string.measure_IntentArray_condition), measuresList_data.get(i).getCondition());
			measures_IntentArray.add(element);
		}
		MeasureItemAdapter measureList_adapter = new MeasureItemAdapter(this, measures_IntentArray);
		measuresList_view.setAdapter(measureList_adapter);
		ImageButton mapsButton = (ImageButton)findViewById(R.id.buttonToMaps);
		mapsButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Uri gmmIntentUri = Uri.parse("geo:0,0?q="+selectedStation.getLatitude()+","+selectedStation.getLongitude()+"("+selectedStation.getName()+")");
				Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
				mapIntent.setPackage("com.google.android.apps.maps");
				if(mapIntent.resolveActivity(getPackageManager()) != null)
				{
					startActivity(mapIntent);
				}
			}
		});



	}
}
