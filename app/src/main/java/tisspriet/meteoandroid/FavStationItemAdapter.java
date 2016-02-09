package tisspriet.meteoandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
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
public class FavStationItemAdapter extends BaseAdapter implements ListAdapter
{
	private List<HashMap<String, String>> list = new ArrayList<>();
	private Context context;


	public FavStationItemAdapter(Context context, List<HashMap<String, String>> list)
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
			view = inflater.inflate(R.layout.favsation_listitem, null);
		}

		//Handle TextView and display string from your list
		TextView stationName_text = (TextView)view.findViewById(R.id.favStationText_name);
		stationName_text.setText(list.get(position).get("name"));
		TextView stationDescription_text = (TextView)view.findViewById(
				R.id.favStationText_decription);
		stationDescription_text.setText(list.get(position).get("description"));
		TextView stationMeasure_text = (TextView)view.findViewById(R.id.favStationText_measure);
		stationMeasure_text.setText(list.get(position).get("measure"));

		LinearLayout clickable = (LinearLayout)view.findViewById(R.id.favStation_clickable);


		clickable.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent viewStationActivity_Intent = new Intent(context, ViewStation.class);
				viewStationActivity_Intent.putExtra("id", list.get(position).get("name"));
				context.startActivity(viewStationActivity_Intent);
			}
		});
		ImageView conditionImage = (ImageView)view.findViewById(R.id.favStation_condition);
		switch(list.get(position).get("condition"))
		{
			case "ensoleillé":
				conditionImage.setImageDrawable(context.getResources().getDrawable(R.drawable
																						   .sun));
				break;
			case "nuageux":
				conditionImage.setImageDrawable(
						context.getResources().getDrawable(R.drawable.cloud));
				break;
			case "brouillard":
				conditionImage.setImageDrawable(context.getResources().getDrawable(R.drawable
																						   .fog));
				break;
			case "neigeux":
				conditionImage.setImageDrawable(
						context.getResources().getDrawable(R.drawable.snow));
				break;
			case "mitigé":
				conditionImage.setImageDrawable(
						context.getResources().getDrawable(R.drawable.suncloud));
				break;
			case "pluvieux":
				conditionImage.setImageDrawable(
						context.getResources().getDrawable(R.drawable.rain));
				break;
			case "orageux":
				conditionImage.setImageDrawable(
						context.getResources().getDrawable(R.drawable.thunderstorm));
				break;
			case "tempête":
				conditionImage.setImageDrawable(
						context.getResources().getDrawable(R.drawable.storm));
				break;
			default:
				conditionImage.setImageDrawable(
						context.getResources().getDrawable(R.drawable.viewnull));
				break;
		}
		ImageView tendanceImage = (ImageView)view.findViewById(R.id.favStationText_tendance);
		switch(Integer.parseInt(list.get(position).get("tendance")))
		{
			case 0:
				tendanceImage.setImageDrawable(
						context.getResources().getDrawable(R.drawable.tempequ));
				break;
			case 1:
				tendanceImage.setImageDrawable(
						context.getResources().getDrawable(R.drawable.tempdown));
				break;
			case 2:
				tendanceImage.setImageDrawable(
						context.getResources().getDrawable(R.drawable.tempup));
				break;
		}
		return view;
	}
}