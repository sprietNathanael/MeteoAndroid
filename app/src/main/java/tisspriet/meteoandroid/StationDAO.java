package tisspriet.meteoandroid;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by nathanael on 07/02/16.
 */
public class StationDAO
{
	private static Context context;
	private static HashMap<String, Station> stationList;

	private StationDAO()
	{}

	public static void makeDAO(Context newContext)
	{
		context = newContext;
		String path = context.getFilesDir().getAbsolutePath() + File.separator + "station.json";
		File file = new File(path);
		stationList = new HashMap<>();
		String datePath = context.getFilesDir().getAbsolutePath() + File.separator + "lastupdate" +
				".date";
		File dateFile = new File(datePath);
		boolean tooOld = true;


		if(dateFile.isFile())
		{
			String buffer;
			try
			{
				InputStream sourceReader = new FileInputStream(dateFile);

				// Always wrap FileReader in BufferedReader.
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(sourceReader));
				buffer = bufferedReader.readLine();

				sourceReader.close();
				try
				{
					Date oldDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(buffer);
					Date curDate = new Date();
					float difference = ((float)((curDate.getTime() / (24 * 60 * 60 * 1000)) -
							(float)(oldDate.getTime() / (24 * 60 * 60 * 1000))));
					tooOld = difference > 1.0;
				}
				catch(ParseException e)
				{
					e.printStackTrace();
				}
				bufferedReader.close();

			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}

		if(file.isFile() && !tooOld)
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
			Log.d("Mise à jour stations", "test");
			new DownloadStationList().execute();
		}

	}

	public static HashMap<String, Station> getStationList()
	{
		return stationList;
	}

	public static Station getStation(String stationSearch)
	{
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
		Log.d("Add Releve",myStation.getName());
		new DownloadRelevesList().execute(myStation);
		return myStation;
	}

	private static class DownloadStationList extends AsyncTask
	{
		@Override
		protected Object doInBackground(Object[] params)
		{
			Log.d("begin Get Station List","a");
			HttpGet httpGet = new HttpGet(
					"http://intranet.iut-valence.fr/~sprietn/php/TP4/index.php?getStationList=");
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
			httpGet.addHeader(BasicScheme.authenticate(
					new UsernamePasswordCredentials("sprietn", "NathAndro1"), "UTF-8", false));

			HttpResponse httpResponse = null;
			try
			{
				httpResponse = httpClient.execute(httpGet);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			HttpEntity responseEntity = httpResponse.getEntity();
			String buffer = "";
			try
			{
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(responseEntity.getContent()));
				buffer = reader.readLine();
				String path = context.getFilesDir().getAbsolutePath() + File.separator +
						"station" +
						".json";
				String datePath = context.getFilesDir().getAbsolutePath() + File.separator +
						"lastupdate" +
						".date";
				PrintWriter cacheWriter = new PrintWriter(path);
				PrintWriter cacheDateWriter = new PrintWriter(datePath);

				cacheWriter.print(buffer);
				String buffer2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				cacheDateWriter.print(buffer2);
				reader.close();
				cacheWriter.close();
				cacheDateWriter.close();

				JSONArray stationJson = new JSONArray(buffer);
				for(int i = 0 ; i < stationJson.length() ; i++)
				{
					JSONObject jsonObject = stationJson.getJSONObject(i);
					stationList.put(jsonObject.optString("id").toString(), new Station
							(jsonObject));
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
			Log.d("end Get Station","a");

			return buffer;
		}
	}

	private static class DownloadRelevesList extends AsyncTask<Station,Void,String>
	{
		@Override
		protected String doInBackground(Station... stations)
		{
			Station station = stations[0];
			HttpGet httpGet = new HttpGet(
					"http://intranet.iut-valence.fr/~sprietn/php/TP4/afficheReleves.php?station=" + station.getName());
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
			httpGet.addHeader(BasicScheme.authenticate(
					new UsernamePasswordCredentials("sprietn", "NathAndro1"), "UTF-8", false));

			HttpResponse httpResponse = null;
			try
			{
				httpResponse = httpClient.execute(httpGet);
			}
			catch(IOException e)
			{
				e.printStackTrace();
				Log.d("http", "erreur");
			}
			HttpEntity responseEntity = httpResponse.getEntity();
			String buffer = "";
			try
			{
				station.dumpAllMeasures();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(responseEntity.getContent()));
				buffer = reader.readLine();

				reader.close();
				JSONArray stationJson = new JSONArray(buffer);
				for(int i = 0 ; i < stationJson.length() ; i++)
				{
					JSONObject jsonObject = stationJson.getJSONObject(i);

					station.addMeasure(new Measure(jsonObject));
				}
				Log.d("end Add Releve",station.getName());
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


			return buffer;

		}

	}
}


