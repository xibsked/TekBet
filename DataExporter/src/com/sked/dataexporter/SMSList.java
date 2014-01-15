package com.sked.dataexporter;

import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class SMSList extends ListActivity {
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		List<SMSData> smsList = new ArrayList<SMSData>();

		Uri uri = Uri.parse("content://sms/");
		// String msg = "";
		Cursor c = null;
		try {
			c = getApplicationContext().getContentResolver().query(uri, null,
					null, null, null);

			startManagingCursor(c);
			Log.d("Total SMS is", "" + c.getCount());

			// Read the sms data and store it in the list
			if (c.moveToFirst()) {
				for (int i = 0; i < c.getCount(); i++) {
					if (i != 2 && i < 60) {
						SMSData sms = new SMSData();
						/*
						 * msg=msg+"+====================================+";
						 * msg=msg+"\n"; msg=msg+c.getString(
						 * c.getColumnIndexOrThrow("address")).toString();
						 * msg=msg+"\n";
						 * msg=msg+"+~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~+";
						 * 
						 * 
						 * msg=msg+"\n"; msg=msg+c.getString(
						 * c.getColumnIndexOrThrow("body")).toString();
						 * msg=msg+"\n";
						 * msg=msg+"+~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~+";
						 * 
						 * msg=msg+"\n"; msg=msg+new
						 * Date(Long.parseLong(c.getString(
						 * c.getColumnIndexOrThrow("date")).toString()));
						 * msg=msg+"\n";
						 * msg=msg+"+====================================+";
						 * msg=msg+"\n"; msg=msg+"\n"; msg=msg+"\n";
						 * msg=msg+"\n";
						 */
						sms.setBody(c
								.getString(c.getColumnIndexOrThrow("body"))
								.toString());
						sms.setNumber(c.getString(
								c.getColumnIndexOrThrow("body")).toString());
						smsList.add(sms);
						Log.d(" SMS is", "" + i);
					}
					c.moveToNext();
				}
			}
			c.close();
			// save(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("Error at", "" + c.getCount());
			e.printStackTrace();
		}
		/*
		 * Uri uriSms = Uri.parse("content://sms/");
		 * 
		 * Cursor cursor = this.getContentResolver() .query(uriSms, new String[]
		 * { "_id", "address", "date", "body", "type", "read" }, "type="+1,
		 * null, "date" + " COLLATE LOCALIZED ASC"); if (cursor != null) {
		 * cursor.moveToLast(); if (cursor.getCount() > 0) {
		 * 
		 * do { SMSData sms = new SMSData();
		 * sms.setBody(cursor.getString(cursor.
		 * getColumnIndexOrThrow("body")).toString());
		 * sms.setNumber(cursor.getString
		 * (cursor.getColumnIndexOrThrow("address")).toString());
		 * Log.d("sent msg are"
		 * ,""+cursor.getString(cursor.getColumnIndexOrThrow(
		 * "body")).toString()); smsList.add(sms); } while
		 * (cursor.moveToPrevious()); } }
		 */

		// Set smsList in the ListAdapter
		setListAdapter(new ListAdapter(this, smsList));

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		SMSData sms = (SMSData) getListAdapter().getItem(position);

		Toast.makeText(getApplicationContext(), sms.getBody(),
				Toast.LENGTH_LONG).show();

	}

}
