package tisspriet.meteoandroid;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nathanael on 07/02/16.
 */
public class Station
{
	private String name;
	private String description;
	private String latitude;
	private String longitude;
	private String altitude;
	private ArrayList<Measure> measuresList;

	public Station(String name, String description, String latitude, String longitude,
				   String altitude)
	{
		this.name = name;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
		this.measuresList = new ArrayList<>();
	}

	public Station(JSONObject jsonObject)
	{
		this.name = jsonObject.optString("id").toString();
		this.description = jsonObject.optString("libelle").toString();
		this.latitude = jsonObject.optString("latitude").toString();
		this.longitude = jsonObject.optString("longitude").toString();
		this.altitude = jsonObject.optString("altitude").toString();
		this.measuresList = new ArrayList<>();
	}

	public String getLastTCondition()
	{
		if(measuresList.size() > 0)
		{
			return getLastMeasure().getCondition();
		}
		return null;
	}

	public int getTendance()
	{
		if(measuresList.size() > 1)
		{
			float temp1 = getLastTemperature();
			float temp2 = measuresList.get(measuresList.size() - 2).getTemperature();
			if(temp1 < temp2)
			{
				return 1;
			}
			else if(temp1 > temp2)
			{
				return 2;
			}
			else
			{
				return 0;
			}

		}
		return -1;

	}

	public float getLastTemperature()
	{
		if(measuresList.size() != 0)
		{
			return getLastMeasure().getTemperature();
		}
		else
		{
			return Integer.parseInt(null);
		}
	}

	public Measure getLastMeasure()
	{
		return measuresList.get(measuresList.size() - 1);
	}

	public void addMeasure(Measure mesureToAdd)
	{
		measuresList.add(mesureToAdd);
	}

	public ArrayList<Measure> getAllMeasures()
	{
		return measuresList;
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public String gpsString()
	{
		return latitude + " N " + longitude + " E " + altitude + " m";
	}
}
