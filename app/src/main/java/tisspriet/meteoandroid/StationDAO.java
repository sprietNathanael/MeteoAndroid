package tisspriet.meteoandroid;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Created by nathanael on 07/02/16.
 */
public class StationDAO
{
	private static int stationPath;
	private static int measuresPath;
	private static Context context;
	private static HashMap<String, Station> stationList;

	private StationDAO()
	{}
	public static void makeDAO(Context newContext,int pathToStationFile, int pathToMeasuresFile)
	{
		context = newContext;
		stationPath = pathToStationFile;
		measuresPath = pathToMeasuresFile;
		String path = context.getFilesDir().getAbsolutePath() + File.separator + "station.json";
		File file = new File(path);
		stationList = new HashMap<>();

		if(file.exists())
		{
			String buffer;
			try
			{
				InputStream sourceReader = new FileInputStream(file);

				// Always wrap FileReader in BufferedReader.
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(sourceReader));
				buffer = bufferedReader.readLine();

				bufferedReader.close();
				JSONArray stationJson = new JSONArray(buffer);
				for(int i = 0 ; i < stationJson.length() ; i++)
				{
					JSONObject jsonObject = stationJson.getJSONObject(i);

					stationList.put(jsonObject.optString("id").toString(), new Station
							(jsonObject));
				}
				sourceReader.close();
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
		}
		else
		{
			String buffer;
			try
			{
				InputStream sourceReader = context.getResources().openRawResource(stationPath);
				PrintWriter cacheWriter = new PrintWriter(path);

				// Always wrap FileReader in BufferedReader.
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(sourceReader));
				buffer = bufferedReader.readLine();
				cacheWriter.print(buffer);


				bufferedReader.close();
				JSONArray stationJson = new JSONArray(buffer);
				for(int i = 0 ; i < stationJson.length() ; i++)
				{
					JSONObject jsonObject = stationJson.getJSONObject(i);
					stationList.put(jsonObject.optString("id").toString(), new Station
							(jsonObject));
				}
				sourceReader.close();
				cacheWriter.close();
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
		}

	}

	public static HashMap<String, Station> getStationList()
	{
		return stationList;
	}

	public static Station getStation(String stationSearch)
	{
		Log.d("test",stationList.toString());
		if(stationList.containsKey(stationSearch))
		{
			return stationList.get(stationSearch);
		}
		else
		{
			return null;
		}
	}

	public static Station addReleveToStation(Station myStation)
	{
		myStation.dumpAllMeasures();
		String buffer;
		try
		{
			InputStream sourceReader = context.getResources().openRawResource(measuresPath);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader
																	   (sourceReader));
			buffer = bufferedReader.readLine();

			bufferedReader.close();
			JSONArray stationJson = new JSONArray(buffer);
			for(int i = 0 ; i < stationJson.length() ; i++)
			{
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
