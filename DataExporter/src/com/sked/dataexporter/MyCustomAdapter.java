package com.sked.dataexporter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class MyCustomAdapter extends ArrayAdapter<ListData> {

	private ArrayList<ListData> countryList;
	Context bContext;

	public MyCustomAdapter(Context context, int textViewResourceId,
			ArrayList<ListData> countryList) {

		super(context, textViewResourceId, countryList);
		bContext = context;
		this.countryList = new ArrayList<ListData>();
		this.countryList.addAll(countryList);
	}

	private class ViewHolder {
		TextView name;
		TextView snippet;
		CheckBox selected;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		Log.d("ConvertView", String.valueOf(position));

		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) bContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.custom_row, null);

			holder = new ViewHolder();
			holder.name = (TextView) convertView
					.findViewById(R.id.tvThreadName);
			holder.snippet = (TextView) convertView
					.findViewById(R.id.tvSnippet);
			holder.selected = (CheckBox) convertView
					.findViewById(R.id.cbSelected);
			convertView.setTag(holder);

			holder.selected.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Log.d("Tag is", "" + ((View) v.getParent()).getTag());
					CheckBox cb = (CheckBox) v;
					ListData country = (ListData) cb.getTag();

					country.setSelected(cb.isChecked());
				}
			});
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ListData country = countryList.get(position);
		holder.snippet.setText(country.getSnippet());
		holder.name.setText(country.getName());
		holder.selected.setChecked(country.isSelected());
		holder.selected.setTag(country);

		return convertView;

	}
}
