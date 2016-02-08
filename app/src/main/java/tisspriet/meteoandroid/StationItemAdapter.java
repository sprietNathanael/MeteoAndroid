package tisspriet.meteoandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by nathanael on 07/02/16.
 */
public class StationItemAdapter extends BaseAdapter implements ListAdapter
{
	private List<HashMap<String, String>> list = new ArrayList<>();
	private Context context;


	public StationItemAdapter(Context context, List<HashMap<String, String>> list)
	{
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount()
	{
		return list.size();
	}

	@Override
	public Object getItem(int pos)
	{
		return list.get(pos);
	}

	@Override
	public long getItemId(int pos)
	{
		return 0;
		//just return 0 if your list items do not have an Id variable.
	}

	@Override
	public View getView(final int position, final View convertView, ViewGroup parent)
	{
		View view = convertView;
		if(view == null)
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.station_listitem, null);
		}

		//Handle TextView and display string from your list
		TextView stationName_text = (TextView)view.findViewById(R.id.itemstation_name);
		stationName_text.setText(list.get(position).get("id"));
		TextView stationDescription_text = (TextView)view.findViewById(
				R.id.itemstation_description);
		stationDescription_text.setText(list.get(position).get("description"));
		final boolean fav = Boolean.parseBoolean(list.get(position).get("fav"));

		//Handle buttons and add onClickListeners
		final ImageButton favButton = (ImageButton)view.findViewById(R.id.favButton);
		if(fav)
		{
			favButton.setImageDrawable(context.getResources().getDrawable(R.drawable.favstar_on));
		}
		else
		{
			favButton.setImageDrawable(context.getResources().getDrawable(R.drawable.favstar_off));
		}

		LinearLayout clickable = (LinearLayout)view.findViewById(R.id.itemstation_clickable);


		clickable.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent viewStationActivity_Intent = new Intent(context, ViewStation.class);
				viewStationActivity_Intent.putExtra("id", list.get(position).get("id"));
				context.startActivity(viewStationActivity_Intent);
			}
		});

		favButton.setOnClickListener(new View.OnClickListener()
		{
			Boolean fav2 = fav;
			@Override
			public void onClick(View v)
			{
				SharedPreferences preferences = null;
				preferences = context.getSharedPreferences("favStationList",
														   Context.MODE_WORLD_WRITEABLE);
				SharedPreferences.Editor editor = preferences.edit();
				HashSet<String> favSet = new HashSet<>();
				favSet = (HashSet)preferences.getStringSet("fav", new HashSet<String>());

				Log.d("Preferences",((HashSet)preferences.getStringSet("fav",new HashSet<String>())).toString());

				if(fav2)
				{
					favSet.remove(list.get(position).get("id").toString());
					favButton.setImageDrawable(context.getResources().getDrawable(R.drawable.favstar_off));
					fav2 = false;
				}
				else
				{
					favSet.add(list.get(position).get("id").toString());
					favButton.setImageDrawable(context.getResources().getDrawable(R.drawable.favstar_on));
					fav2 = true;
				}
				editor.remove("fav");
				editor.commit();
				editor.putStringSet("fav", favSet);
				editor.commit();
			}
		});
		return view;
	}
}