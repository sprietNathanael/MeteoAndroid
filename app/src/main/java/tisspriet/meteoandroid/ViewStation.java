package tisspriet.meteoandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by nathanael on 06/02/16.
 */
public class ViewStation extends Activity
{
	private TextView stationName_text;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewstation_layout);
		stationName_text = (TextView)findViewById(R.id.stationName_text);
		Intent intentLauncher = getIntent();
		String intentStationName = intentLauncher.getStringExtra("id");
		if(intentLauncher != null)
		{
			stationName_text.setText(intentStationName);
		}

	}
}
