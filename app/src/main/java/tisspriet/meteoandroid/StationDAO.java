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
		String path = context.getFilesDir().getAbsolutePath() + File.separator + context.getResources().getString(R.string.stationCacheFilename);
		File stationFile = new File(path);
		stationList = new HashMap<>();
		String datePath = context.getFilesDir().getAbsolutePath() + File.separator + context.getResources().getString(R.string.lastupdateCacheFilename);
		File dateFile = new File(datePath);
		boolean tooOld = true;

		/*Récupération de la liste des stations*/
		/*Si il y a déjà eu une entrée dans le cache*/
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
					/*Récupération de la date du cache*/
					Date oldDate = (new SimpleDateFormat(context.getResources().getString(R.string.dateFormat))).parse(buffer);
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
		/*Si il y a le fichier de stations dans le cache est qu'il est moins vieux d'un jour par rapport à la dernière mise à jour*/
		if(stationFile.isFile() && !tooOld)
		{
			String buffer;
			try
			{
				InputStream sourceReader = new FileInputStream(stationFile);

				// Always wrap FileReader in BufferedReader.
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(sourceReader));
				buffer = bufferedReader.readLine();

				bufferedReader.close();
				/*Récupération de la liste des stations du cache*/
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
		/*Si il n'y a rien dans le cache ou que le fichier est trop vieux*/
		else
		{
			/*Mise à jour de la liste des stations*/
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
		/*Mise à jour des relevés d'une station*/
		new DownloadRelevesList().execute(myStation);
		return myStation;
	}

	/*Téléchargement de la liste des stations*/
	private static class DownloadStationList extends AsyncTask
	{
		@Override
		protected Object doInBackground(Object[] params)
		{
			HttpGet httpGet = new HttpGet(
					context.getResources().getString(R.string.stationList_URL));
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
			httpGet.addHeader(BasicScheme.authenticate(
					new UsernamePasswordCredentials(context.getResources().getString(R.string.server_username), context.getResources().getString(R.string.server_password)), "UTF-8", false));

			HttpResponse httpResponse = null;
			String buffer = null;
			try
			{
				httpResponse = httpClient.execute(httpGet);
				HttpEntity responseEntity = httpResponse.getEntity();
				buffer = "";
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(responseEntity.getContent()));
				buffer = reader.readLine();
				String path = context.getFilesDir().getAbsolutePath() + File.separator +
						context.getResources().getString(R.string.stationCacheFilename);
				String datePath = context.getFilesDir().getAbsolutePath() + File.separator +
						context.getResources().getString(R.string.lastupdateCacheFilename);
				PrintWriter cacheWriter = new PrintWriter(path);
				PrintWriter cacheDateWriter = new PrintWriter(datePath);

				cacheWriter.print(buffer);
				String buffer2 = new SimpleDateFormat(context.getResources().getString(R.string.dateFormat)).format(new Date());
				cacheDateWriter.print(buffer2);
				reader.close();
				cacheWriter.close();
				cacheDateWriter.close();

				JSONArray stationJson = new JSONArray(buffer);
				for(int i = 0 ; i < stationJson.length() ; i++)
				{
					JSONObject jsonObject = stationJson.getJSONObject(i);
					stationList.put(jsonObject.optString(context.getResources().getString(R.string.station_Json_name)).toString(), new Station
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
			return buffer;

		}
	}

	/*Téléchargement des relevé*/
	private static class DownloadRelevesList extends AsyncTask<Station, Void, String>
	{
		@Override
		protected String doInBackground(Station... stations)
		{
			Station station = stations[0];
			HttpGet httpGet = new HttpGet(
					context.getResources().getString(R.string.measures_URL)
							+ station.getName());
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
			httpGet.addHeader(BasicScheme.authenticate(
					new UsernamePasswordCredentials(context.getResources().getString(R.string.server_username), context.getResources().getString(R.string.server_password)), "UTF-8", false));

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
			/*Sauvegarde des relevés dans l'objet*/
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


