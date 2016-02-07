package tisspriet.meteoandroid;

import org.json.JSONObject;

/**
 * Created by nathanael on 07/02/16.
 */
public class Measure
{
	private String date;
	private int temperature;
	private String condition;

	public Measure(String date, int temperature, String condition)
	{
		this.date = date;
		this.temperature = temperature;
		this.condition = condition;
	}

	public Measure(JSONObject jsonObject)
	{
		this.date = jsonObject.optString("quand").toString();
		this.temperature = Integer.parseInt(jsonObject.optString("temp").toString());
		this.condition = jsonObject.optString("condition").toString();
	}

	public String getDate()
	{
		return date;
	}

	public int getTemperature()
	{
		return temperature;
	}

	public String getCondition()
	{
		return condition;
	}

	@Override
	public String toString()
	{
		return date + " : "+ temperature +"°C "+ condition;
	}
}
