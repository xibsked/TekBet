package com.sked.dataexporter;

import java.util.ArrayList;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MessageThreads extends Activity implements OnItemClickListener {

	ListView msgThreadList;
	MyCustomAdapter adapter;
	ArrayList<ListData> listThread = new ArrayList<ListData>();
	String allThread = "content://mms-sms/conversations?simple=true";
	String allSent = "content://sms/sent/";
	String allReceived = "content://sms/inbox/";
	String allDraft = "content://sms/draft/";
	Uri uri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_threads);
		uri = Uri.parse(allThread);
		msgThreadList = (ListView) findViewById(R.id.lvAllMsgThrede);
		msgThreadList.setDividerHeight(0);
		msgThreadList.setDivider(null);
		getThreadList();
		msgThreadList.setOnItemClickListener(this);

	}

	@SuppressWarnings("deprecation")
	private void getThreadList() {
		// TODO Auto-generated method stub

		// String msg = "";
		Cursor c = null;
		try {
			c = getApplicationContext().getContentResolver().query(uri, null,
					null, null, "date" + " COLLATE LOCALIZED DESC");

		
			
			//startManagingCursor(c);
			Log.d("Total SMS is", "" + c.getCount());

			// Read the sms data and store it in the list
			if (c.moveToFirst()) {
				for (int i = 0; i < c.getCount(); i++) {
					long id = Long.parseLong(c.getString(
							c.getColumnIndexOrThrow("_id")).toString());
					String snippet = null;
					snippet = c.getString(c.getColumnIndexOrThrow("snippet"))
							.toString();

					String recipient_id = c.getString(
							c.getColumnIndexOrThrow("recipient_ids"))
							.toString();
					String[] recipient_ids = recipient_id.split(" ");

					Cursor cur = getContentResolver().query(
							Uri.parse("content://mms-sms/canonical-addresses"),
							null, "_id = " + recipient_ids[0], null, null);
					String address = null;
					String name = null;
					if (cur != null) {
						cur.moveToFirst();
						address = cur.getString(
								cur.getColumnIndexOrThrow("address"))
								.toString();
						Uri uri = Uri
								.withAppendedPath(
										ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
										Uri.encode(address));
						Cursor phones = getContentResolver().query(
								uri,
								new String[] { PhoneLookup.DISPLAY_NAME,
										PhoneLookup._ID }, null, null, null);
						while (phones.moveToNext()) {

							name = phones
									.getString(phones
											.getColumnIndexOrThrow(PhoneLookup.DISPLAY_NAME));
						}
						phones.close();

						Log.d("address is ", address);
					}
					cur.close();
					ListData country = new ListData(
							id,
							snippet.length() == 0 ? "(No Subject)" : snippet,
							name == null ? (recipient_ids.length > 1 ? "Conversation("
									+ address + "..."
									: address)
									: (recipient_ids.length > 1 ? "Conversation("
											+ name + "..."
											: name), "", "", "", false);

				
					listThread.add(country);

					Log.d(" SMS is", "" + i);
					c.moveToNext();
				}

			}

			c.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//Log.d("Error at", "" + c.getCount());
			c.close();
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
		getMenuInflater().inflate(R.menu.activity_message_threads, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {

		case R.id.menu_all_msgs:
			Intent allMsgs = new Intent(this, AllMessages.class);
			startActivity(allMsgs);
			break;
		case R.id.menu_all_sent:

			Intent allSentMsgs = new Intent(this, AllSentMessages.class);
			startActivity(allSentMsgs);

			break;
		case R.id.menu_all_received:
			Intent allRcvMsgs = new Intent(this, AllReceivedMessages.class);
			startActivity(allRcvMsgs);
			break;

		case R.id.menu_export:

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
		long thread_id = country.getThreadId();
		Intent intent = new Intent(this, Conversation.class);
		intent.putExtra("thread_id", String.valueOf(thread_id));
		intent.putExtra("address", country.getName());
		startActivity(intent);

		/*
		 * Toast.makeText( this, "Thread id is"+thread_id,
		 * Toast.LENGTH_LONG).show();
		 */
	}

}
