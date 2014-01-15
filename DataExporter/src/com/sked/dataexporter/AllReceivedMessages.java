package com.sked.dataexporter;

import java.io.File;
import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class AllReceivedMessages extends Activity {

	ListView msgThreadList;
	MyCustomAdapter adapter;
	ArrayList<ListData> listThread = new ArrayList<ListData>();
	String allRcvMsgs = "content://sms/inbox/";
	Uri uri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_received_messages);
		uri = Uri.parse(allRcvMsgs);
		msgThreadList = (ListView) findViewById(R.id.lvReceivedMessages);
		getThreadList();
	}

	@SuppressWarnings("deprecation")
	private void getThreadList() {
		// TODO Auto-generated method stub

		// String msg = "";
		Cursor c = null;
		try {
			c = getApplicationContext().getContentResolver().query(uri, null,
					null, null, "date" + " COLLATE LOCALIZED DESC");

			startManagingCursor(c);
			Log.d("Total SMS is", "" + c.getCount());

			// Read the sms data and store it in the list
			if (c.moveToFirst()) {
				for (int i = 0; i < c.getCount(); i++) {
					ListData country = new ListData(
							Long.parseLong(c.getString(
									c.getColumnIndexOrThrow("_id")).toString()),
							c.getString(c.getColumnIndexOrThrow("body"))
									.toString(),
							((c.getString(c.getColumnIndexOrThrow("address")) != null ? (c
									.getString(c
											.getColumnIndexOrThrow("address"))
									.toString()) : "No Number")), "", "", "",
							false);

					listThread.add(country);

					Log.d(" SMS is", "" + i);
					c.moveToNext();
				}

			}

			c.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("Error at", "" + c.getCount());
			e.printStackTrace();
		} finally {
			c.close();
		}

		adapter = new MyCustomAdapter(this, R.layout.custom_row, listThread);
		msgThreadList.setAdapter(adapter);

		Log.d(" listThread is", listThread.toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_all_received_messages, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {

		case R.id.menu_export:

			if (Utility.isOneRowSelected(listThread)) {
				try {
					Utility.save(Utility.formatTextMessages(listThread), this,
							new File(Environment.getExternalStorageDirectory()
									+ "/DataExpoert/", "Messages"),
							"AllReceivedMessages" + ".txt");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Toast.makeText(this, "Nothing to Export..!!",
						Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.menu_select_all:

			selectAll();
			break;

		}

		return true;
	}

	private void selectAll() {
		// TODO Auto-generated method stub
		ArrayList<ListData> selectedList = new ArrayList<ListData>();
		for (int i = 0; i < listThread.size(); i++) {
			ListData row = listThread.get(i);
			row.setSelected(true);
			selectedList.add(row);
		}
		adapter = null;
		adapter = new MyCustomAdapter(this, R.layout.custom_row, selectedList);
		msgThreadList.setAdapter(adapter);
	}
}
