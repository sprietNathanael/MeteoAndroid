package tisspriet.meteoandroid;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by nathanael on 07/02/16.
 */
public class StationDAO
{
	private int stationPath;
	private int measuresPath;
	private Context context;
	public StationDAO(Context newContext, int pathToStationFile, int pathToMeasuresFile)
	{
		this.stationPath = pathToStationFile;
		this.measuresPath = pathToMeasuresFile;
		this.context = newContext;
	}

	public HashMap<String,Station> getStationList()
	{
		HashMap<String,Station> stationList = new HashMap<>();
		String buffer;
		try {
			InputStream sourceReader = context.getResources().openRawResource(stationPath);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sourceReader));
			buffer = bufferedReader.readLine();

			bufferedReader.close();
			JSONArray stationJson = new JSONArray(buffer);
			for(int i=0; i < stationJson.length(); i++){
				JSONObject jsonObject = stationJson.getJSONObject(i);

				stationList.put(jsonObject.optString("id").toString(),new Station(jsonObject));
			}
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
		return stationList;
	}

	public Station getStation(String stationSearch)
	{
		Station myStation = null;
		String buffer;
		try {
			InputStream sourceReader = context.getResources().openRawResource(stationPath);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sourceReader));
			buffer = bufferedReader.readLine();

			bufferedReader.close();
			JSONArray stationJson = new JSONArray(buffer);
			for(int i=0; i < stationJson.length(); i++){
				JSONObject jsonObject = stationJson.getJSONObject(i);
				if(jsonObject.optString("id").toString().compareTo(stationSearch) == 0)
				{
					myStation = new Station(jsonObject);
				}
			}
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
		return myStation;
	}

	public Station addReleveToStation(Station myStation)
	{
		String buffer;
		try {
			InputStream sourceReader = context.getResources().openRawResource(measuresPath);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sourceReader));
			buffer = bufferedReader.readLine();

			bufferedReader.close();
			JSONArray stationJson = new JSONArray(buffer);
			for(int i=0; i < stationJson.length(); i++){
				JSONObject jsonObject = stationJson.getJSONObject(i);
				if(jsonObject.optString("station").toString().compareTo(myStation.getName()) == 0)
				{
					myStation.addMeasure(new Measure(jsonObject));
				}
			}
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
		return myStation;
	}
}
