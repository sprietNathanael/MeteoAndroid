package tisspriet.meteoandroid;

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
