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
		textInfoStationsName.setText("Alboussière");
		textInfoStationsDescription.setText("Station d'altitude du pays de Crussol");
		textInfoStationsPosition.setText("GPS : 44.9434 N 4.72924 O 547 m");
		textInfoStationsMeasure.setText("Date : 2015-11-27 18:47:57 -> 18°");

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
