package com.sked.dataexporter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CalendarEvents extends Activity implements OnItemClickListener {

	ListView msgThreadList;
	MyCustomAdapter adapter;
	ArrayList<ListData> listThread = new ArrayList<ListData>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_threads);
		msgThreadList = (ListView) findViewById(R.id.lvAllMsgThrede);
		getThreadList();
		// msgThreadList.setOnItemClickListener(this);
		// ReadCalendar.readCalendar(this);

		msgThreadList.setOnItemClickListener(this);
	}

	
	private void getThreadList() {
		Cursor cursor = null;

		ContentResolver contentResolver = getContentResolver();

		// Fetch a list of all calendars synced with the device, their display
		// names and whether the

		try {
			cursor = contentResolver.query(
					Uri.parse("content://com.android.calendar/calendars"),
					(new String[] { "_id" }), null, null, null);
		} catch (Exception e1) {
			// TODO Auto-generated catch block

			e1.printStackTrace();
		}

		HashSet<String> calendarIds = new HashSet<String>();

		try {
			System.out.println("Count=" + cursor.getCount());
			if (cursor.getCount() > 0) {
				System.out
						.println("the control is just inside of the cursor.count loop");
				while (cursor.moveToNext()) {

					String _id = cursor.getString(0);
					// String displayName = cursor.getString(1);
					// Boolean selected = !cursor.getString(2).equals("0");

					System.out.println("Id: " + _id + " Display Name: " + ""
							+ " Selected: " + "");
					calendarIds.add(_id);
				}
			}
		} catch (AssertionError ex) {
			Log.d("AssertionError", ex.toString());
			ex.printStackTrace();
		} catch (Exception e) {
			Log.d("Exception", e.toString());
			e.printStackTrace();
		}

		// For each calendar, display all the events from the previous week to
		// the end of next week.
		for (String id : calendarIds) {
			Uri.Builder builder = Uri.parse(
					"content://com.android.calendar/instances/when")
					.buildUpon();
			// Uri.Builder builder =
			// Uri.parse("content://com.android.calendar/calendars").buildUpon();
			long now = new Date().getTime();

			ContentUris
					.appendId(builder, now - DateUtils.DAY_IN_MILLIS * 10000);
			ContentUris
					.appendId(builder, now + DateUtils.DAY_IN_MILLIS * 10000);

			Cursor eventCursor = contentResolver.query(builder.build(),
					new String[] { "title", "description", "begin", "end",
							"allDay", "eventLocation" }, "Calendar_id=" + 1,
					null, "startDay ASC, startMinute ASC");

			System.out.println("eventCursor count=" + eventCursor.getCount());
			if (eventCursor.getCount() > 0) {

				if (eventCursor.moveToFirst()) {
					do {

						final String title = eventCursor.getString(0);
						final String desc = eventCursor.getString(1);
						final Date begin = new Date(eventCursor.getLong(2));
						final Date end = new Date(eventCursor.getLong(3));
						final String allDay = eventCursor.getString(4);
						final String loc = eventCursor.getString(5);

						ListData listData = new ListData(Long.parseLong(id),
								desc, title, String.valueOf(begin),
								String.valueOf(end), loc + "xib" + allDay,
								false);
						listThread.add(listData);

					} while (eventCursor.moveToNext());
				}
			}
			break;

		}

		adapter = new MyCustomAdapter(this, R.layout.custom_row, listThread);
		msgThreadList.setAdapter(adapter);

		Log.d(" listThread is", listThread.toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_calendar_events, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_export_events:

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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

		ListData country = listThread.get(arg2);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("" + country.getThreadId());
		builder.setIcon(R.drawable.ic_launcher_calendar);
		builder.setMessage(
				"Start:\t"
						+ country.getLabel3()
						+ "\n"
						+ "End:\t"
						+ country.getLabel4()
						+ "\n"
						+ "Location:\t"
						+ country.getLabel5().split("xib")[0]
						+ "\n"
						+ "Repeat:\t"
						+ (country.getLabel5().split("xib")[1].equals( "1") ? "All Days"
								: "No"))
				.setCancelable(false)
				.setPositiveButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

							}
						});

		AlertDialog alert = builder.create();
		alert.show();

	}

}
