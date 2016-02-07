package tisspriet.meteoandroid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by nathanael on 07/02/16.
 */
public class StationDAO
{

	public StationDAO()
	{}

	public HashMap<String,Station> getStationList()
	{
		HashMap<String,Station> stationList = new HashMap<>();
		String buffer;
		try {
			// FileReader reads text files  in the default encoding.
			FileReader sourceReader = new FileReader("station.txt");

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(sourceReader);
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

	public Station getStationList(String stationSearch)
	{
		Station myStation = null;
		String buffer;
		try {
			// FileReader reads text files  in the default encoding.
			FileReader sourceReader = new FileReader("station.txt");

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(sourceReader);
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
			// FileReader reads text files  in the default encoding.
			FileReader sourceReader = new FileReader("measures.txt");

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(sourceReader);
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
