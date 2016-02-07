package tisspriet.meteoandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity
{
	private TextView textInfoStationsName;
	private TextView textInfoStationsDescription;
	private TextView textInfoStationsPosition;
	private TextView textInfoStationsMeasure;
	private TextView textInfoStationsDate;
	private TextView textInfoStationsCondition;
	private TextView textInfoStationsTendance;

	private Button buttonTo_ListStationActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textInfoStationsName = (TextView)findViewById(R.id.textInfoStationName);
		textInfoStationsDescription = (TextView)findViewById(R.id.textInfoStationDescription);
		textInfoStationsPosition = (TextView)findViewById(R.id.textInfoStationPosition);
		textInfoStationsMeasure = (TextView)findViewById(R.id.textInfoStationMeasure);
		textInfoStationsDate = (TextView)findViewById(R.id.textInfoStationDate);
		textInfoStationsCondition = (TextView)findViewById(R.id.textInfoStationCondition);
		textInfoStationsTendance = (TextView)findViewById(R.id.textInfoStationTendance);


		Station alboussiere = new Station("Alboussière","Station d'altitude du pays de Crussol","44.9434","4.72924","547");
		alboussiere.addMeasure(new Measure("2015-11-27 18:45:57",18,"Nuageux"));
		alboussiere.addMeasure(new Measure("2015-11-27 18:46:57",19,"Nuageux"));
		alboussiere.addMeasure(new Measure("2015-11-27 18:47:57", 10, "Nuageux"));
		textInfoStationsName.setText(alboussiere.getName());
		textInfoStationsDescription.setText(alboussiere.getDescription());
		textInfoStationsPosition.setText(alboussiere.gpsString());
		textInfoStationsDate.setText(alboussiere.getLastMeasure().getDate());
		textInfoStationsMeasure.setText(String.format("%f °C",alboussiere.getLastTemperature()));
		textInfoStationsCondition.setText(alboussiere.getLastTCondition());
		textInfoStationsTendance.setText(String.format("%d",alboussiere.getTendance()));

		buttonTo_ListStationActivity = (Button)findViewById(R.id.buttonTo_ListStationActivity);
		buttonTo_ListStationActivity.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent listStationActivity_Intent = new Intent(MainActivity.this,
															   ListStation.class);
				startActivity(listStationActivity_Intent);
			}
		});

	}
}
