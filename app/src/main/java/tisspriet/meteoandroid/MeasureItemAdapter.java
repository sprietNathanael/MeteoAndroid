package tisspriet.meteoandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nathanael on 07/02/16.
 */
public class MeasureItemAdapter extends BaseAdapter implements ListAdapter
{
	private List<HashMap<String, String>> list = new ArrayList<>();
	private Context context;


	public MeasureItemAdapter(Context context, List<HashMap<String, String>> list)
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
			view = inflater.inflate(R.layout.measure_listitem, null);
		}

		//Handle TextView and display string from your list
		TextView measureItem_date = (TextView)view.findViewById(R.id.measureItem_date);
		measureItem_date.setText(list.get(position).get("date"));
		TextView measureItem_temperature = (TextView)view.findViewById(
				R.id.measureItem_temperature);
		measureItem_temperature.setText(list.get(position).get("temperature"));
		ImageView conditionImage = (ImageView)view.findViewById(R.id.measureItem_condition);
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
		return view;
	}
}