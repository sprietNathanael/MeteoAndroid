package tisspriet.meteoandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class MainActivity extends Activity
{
	/*Déclaration des variables*/
	private ListView favStationList_view;
	private Button buttonTo_ListStationActivity;
	private Button buttonTo_RefreshStations;
	private HashMap<String, Station> stationList;

	/*Création de l'activity*/
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		StationDAO.makeDAO(MainActivity.this);

		/*Récupération de la liste des relevés*/
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
		favStationList_view = (ListView)findViewById(R.id.favStationList);

		buttonTo_ListStationActivity = (Button)findViewById(R.id.buttonTo_ListStationActivity);
		buttonTo_RefreshStations = (Button)findViewById(R.id.buttonTo_refreshStations);

		buttonTo_ListStationActivity.setOnClickListener(new View.OnClickListener()
		{
			/*Quand on clique sur le bouton de liste*/
			@Override
			public void onClick(View v)
			{
				Intent listStationActivity_Intent = new Intent(MainActivity.this,
															   ListStation.class);
				/*Création de la nouvelle activity*/
				startActivity(listStationActivity_Intent);
			}
		});
		buttonTo_RefreshStations.setOnClickListener(new View.OnClickListener()
		{
			/*Quand on clique sur le bouton de rafraichissement*/
			@Override
			public void onClick(View v)
			{
				/*re-création de l'activity*/
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
		/*Récupération des favoris*/
		preferences = MainActivity.this.getSharedPreferences(getResources().getString(R.string.favStationList_preferencesName),
															 Context.MODE_WORLD_WRITEABLE);
		HashSet<String> favSet = new HashSet<>();
		favSet = (HashSet)preferences.getStringSet(getResources().getString(R.string.favStationList_preferencesArrayName), new HashSet<String>());
		/*Récupération des Stations favorites*/
		ArrayList<Station> favStation_data = new ArrayList<>();
		for(String i : favSet)
		{
			favStation_data.add(StationDAO.getStation(i));
		}
		/*Copie des données vers le paramètre à passer*/
		List<HashMap<String, String>> favStation_IntentArray = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> element;
		for(int i = 0 ; i < favStation_data.size() ; i++)
		{
			element = new HashMap<String, String>();
			element.put(getResources().getString(R.string.station_IntentArray_name), favStation_data.get(i).getName());
			element.put(getResources().getString(R.string.station_IntentArray_description), favStation_data.get(i).getDescription());
			element.put(getResources().getString(R.string.station_IntentArray_temperature),
						String.format(getResources().getString(R.string.temperatureFormat), favStation_data.get(i).getLastTemperature()));
			element.put(getResources().getString(R.string.station_IntentArray_condition), favStation_data.get(i).getLastCondition());
			element.put(getResources().getString(R.string.station_IntentArray_tendance), String.format(
					"%d", favStation_data.get(i).getTendance()));
			favStation_IntentArray.add(element);
		}
		/*Appel de la nouvelle activity*/
		FavStationItemAdapter favStationList_adapter = new FavStationItemAdapter(this, favStation_IntentArray);
		favStationList_view.setAdapter(favStationList_adapter);
	}
}
