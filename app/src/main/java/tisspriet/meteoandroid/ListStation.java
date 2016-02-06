package tisspriet.meteoandroid;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nathanael on 06/02/16.
 */
public class ListStation extends Activity
{
	private ListView stationList_view;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liststation_layout);
		stationList_view = (ListView)findViewById(R.id.stationList);
		String[][] stationList_data = new String[][]{
				{"Alboussière", "Station d'altitude du pays de Crussol"},
				{"Montélimar", "Station du vrai début du Sud"}};
		List<HashMap<String, String>> stationList_list = new ArrayList<HashMap<String,String>>();
		HashMap<String,String> element;
		for(int  i = 0; i < stationList_data.length;i++)
		{
			element =  new HashMap<String, String>();
			element.put("id",stationList_data[i][0]);
			element.put("description",stationList_data[i][1]);
			stationList_list.add(element);
		}
		ListAdapter stationList_adapter = new SimpleAdapter(this, stationList_list, android.R.layout.simple_list_item_2, new String[]{"id","description"},new int[]{android.R.id.text1,android.R.id.text2});
		stationList_view.setAdapter(stationList_adapter);
	}
}
