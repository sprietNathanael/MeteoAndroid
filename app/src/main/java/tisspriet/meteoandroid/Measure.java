package tisspriet.meteoandroid;

import org.json.JSONObject;

/**
 * Created by nathanael on 07/02/16.
 */
public class Measure
{
	private String date;
	private float temperature;
	private String condition;

	public Measure(String date, float temperature, String condition)
	{
		this.date = date;
		this.temperature = temperature;
		this.condition = condition;
	}

	public Measure(JSONObject jsonObject)
	{
		this.date = jsonObject.optString("quand").toString();
		this.temperature = Float.parseFloat(jsonObject.optString("temp").toString());
		this.condition = jsonObject.optString("condition").toString();
	}


	public String getDate()
	{
		return date;
	}

	public float getTemperature()
	{
		return temperature;
	}

	public String getCondition()
	{
		return condition;
	}

}
