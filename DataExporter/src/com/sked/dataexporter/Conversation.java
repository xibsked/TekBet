package com.sked.dataexporter;

import java.util.Date;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class Conversation extends Activity {
	private DiscussArrayAdapter adapter;
	private ListView lv;
	// private LoremIpsum ipsum;
	// private EditText editText1;
	TextView txt_list_empty;
	// private static Random random;
	String allThread = "content://sms/";
	String allSent = "content://sms/sent/";
	String allReceived = "content://sms/inbox/";
	String allDraft = "content://sms/draft/";
	Uri uri;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getIntent().getStringExtra("address"));
		setContentView(R.layout.activity_discuss);
		// random = new Random();
		// ipsum = new LoremIpsum();
		uri = Uri.parse(allThread);

		lv = (ListView) findViewById(R.id.listView1);
		txt_list_empty = (TextView) findViewById(R.id.tvEmtyText);
		lv.setDividerHeight(0);
		lv.setDivider(null);

		adapter = new DiscussArrayAdapter(getApplicationContext(),
				R.layout.listitem_discuss);

		lv.setAdapter(adapter);

		/*
		 * editText1 = (EditText) findViewById(R.id.editText1);
		 * editText1.setOnKeyListener(new OnKeyListener() { public boolean
		 * onKey(View v, int keyCode, KeyEvent event) { // If the event is a
		 * key-down event on the "enter" button if ((event.getAction() ==
		 * KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) { //
		 * Perform action on key press adapter.add(new OneComment(false,
		 * editText1.getText() .toString())); editText1.setText(""); return
		 * true; } return false; } });
		 */

		addItems();
		// Log.d("thread id",this.getIntent().getStringExtra("thread_id"));
	}

	@SuppressWarnings("deprecation")
	private void addItems() {

		Log.d("get in add items", "addItems()");
		// String msg = "";
		Cursor c = null;

		try {
			c = getApplicationContext().getContentResolver()
					.query(uri,
							new String[] { "_id", "address", "date", "body",
									"type", "read" },
							"thread_id=?",
							new String[] { this.getIntent().getStringExtra(
									"thread_id") },
							"date" + " COLLATE LOCALIZED ASC");
			if (c.getCount() == 0)
				txt_list_empty.setVisibility(View.VISIBLE);
			else
				txt_list_empty.setVisibility(View.GONE);

			startManagingCursor(c);
			Log.d("Total SMS is", "" + c.getCount());

			// Read the sms data and store it in the list
			if (c.moveToFirst()) {
				for (int i = 0; i < c.getCount(); i++) {
					if (true) {

						adapter.add(new OneComment(c
								.getString(c.getColumnIndexOrThrow("type"))
								.toString().equalsIgnoreCase("1"), c.getString(
								c.getColumnIndexOrThrow("body")).toString()
								+ "\n"
								+ new Date(Long.parseLong(c.getString(
										c.getColumnIndexOrThrow("date"))
										.toString()))));
						Log.d(" SMS in conv  is", "" + i);
					}
					c.moveToNext();
				}
			}
			c.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			c.close();
			Log.d("Error at", "" + c.getCount());
			e.printStackTrace();
		} finally {
			c.close();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_discuss, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_thread:
			txt_list_empty.setText("No Conversations !");
			uri = Uri.parse(allThread);
			adapter = null;
			adapter = new DiscussArrayAdapter(getApplicationContext(),
					R.layout.listitem_discuss);

			lv.setAdapter(adapter);
			addItems();

			break;
		case R.id.menu_sent:
			txt_list_empty.setText("No Messages Yet Sent !");
			uri = Uri.parse(allSent);
			adapter = null;
			adapter = new DiscussArrayAdapter(getApplicationContext(),
					R.layout.listitem_discuss);

			lv.setAdapter(adapter);
			addItems();
			break;
		case R.id.menu_inbox:
			txt_list_empty.setText("No Messages Yet received !");
			uri = Uri.parse(allReceived);
			adapter = null;
			adapter = new DiscussArrayAdapter(getApplicationContext(),
					R.layout.listitem_discuss);

			lv.setAdapter(adapter);
			addItems();
			break;
		case R.id.menu_draft:
			txt_list_empty.setText("No Drafts !");
			adapter = null;
			adapter = new DiscussArrayAdapter(getApplicationContext(),
					R.layout.listitem_discuss);

			lv.setAdapter(adapter);
			uri = Uri.parse(allDraft);
			addItems();
			break;

		}
		return true;
	}
}