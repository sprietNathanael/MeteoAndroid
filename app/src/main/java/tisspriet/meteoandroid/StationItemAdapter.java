package tisspriet.meteoandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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

	/*Méthodes à implémenter obligatoirement*/
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

	/*Création de la vue*/
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
		/*Chargement des éléments*/
		TextView stationName_text = (TextView)view.findViewById(R.id.itemstation_name);
		stationName_text.setText(list.get(position).get(context.getResources().getString(R.string.station_IntentArray_name)));

		TextView stationDescription_text = (TextView)view.findViewById(
				R.id.itemstation_description);
		stationDescription_text.setText(list.get(position).get(
				context.getResources().getString(R.string.station_IntentArray_description)));

		/*Gestion du clic sur l'élément*/
		LinearLayout clickable = (LinearLayout)view.findViewById(R.id.itemstation_clickable);
		clickable.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent viewStationActivity_Intent = new Intent(context, ViewStation.class);
				viewStationActivity_Intent.putExtra(context.getResources().getString(R.string.station_Extra_name), list.get(position).get(context.getResources().getString(R.string.station_IntentArray_name)));
				context.startActivity(viewStationActivity_Intent);
			}
		});

		/*Gestion des favoris*/
		final boolean fav = Boolean.parseBoolean(list.get(position).get(context.getResources().getString(R.string.station_IntentArray_fav)));
		final ImageButton favButton = (ImageButton)view.findViewById(R.id.favButton);
		if(fav)
		{
			favButton.setImageDrawable(context.getResources().getDrawable(R.drawable.favstar_on));
		}
		else
		{
			favButton.setImageDrawable(context.getResources().getDrawable(R.drawable.favstar_off));
		}
		favButton.setOnClickListener(new View.OnClickListener()
		{
			Boolean fav2 = fav;
			@Override
			public void onClick(View v)
			{
				SharedPreferences preferences = null;
				/*Récupération des favoris*/
				preferences = context.getSharedPreferences(context.getResources().getString(R.string.favStationList_preferencesName),
														   Context.MODE_WORLD_WRITEABLE);
				SharedPreferences.Editor editor = preferences.edit();
				HashSet<String> favStation_set = new HashSet<>();
				Log.d("Fav7",favStation_set.toString());
				favStation_set = (HashSet)preferences.getStringSet(context.getResources().getString(R.string.favStationList_preferencesArrayName), new HashSet<String>());
				Log.d("Fav8",favStation_set.toString());
				if(fav2)
				{
					favStation_set.remove(list.get(position).get(context.getResources().getString(
							R.string.station_IntentArray_name)).toString());
					favButton.setImageDrawable(context.getResources().getDrawable(R.drawable.favstar_off));
					fav2 = false;
				}
				else
				{
					favStation_set.add(list.get(position).get(context.getResources().getString(
							R.string.station_IntentArray_name)).toString());
					favButton.setImageDrawable(context.getResources().getDrawable(R.drawable.favstar_on));
					fav2 = true;
				}
				editor.remove(context.getResources().getString(R.string.favStationList_preferencesArrayName));
				editor.commit();
				editor.putStringSet(context.getResources().getString(
						R.string.favStationList_preferencesArrayName), favStation_set);
				editor.commit();
				favStation_set = (HashSet)preferences.getStringSet(context.getResources().getString(R.string.favStationList_preferencesArrayName), new HashSet<String>());
				Log.d("Fav9", favStation_set.toString());
			}
		});
		return view;
	}
}