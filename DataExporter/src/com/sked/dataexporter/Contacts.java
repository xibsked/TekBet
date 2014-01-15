package com.sked.dataexporter;

import java.io.File;
import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class Contacts extends Activity {

	ListView msgThreadList;
	MyCustomAdapter adapter;
	ArrayList<ListData> listThread = new ArrayList<ListData>();
	Uri allContacts = ContactsContract.Contacts.CONTENT_URI;
	Uri simContacts = Uri.parse("content://icc/adn");
	Uri phoneContacts = ContactsContract.Contacts.CONTENT_URI;
	Uri allContactsWithNo = ContactsContract.Contacts.CONTENT_URI;
	String fileName = "AllContacts";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_threads);

		msgThreadList = (ListView) findViewById(R.id.lvAllMsgThrede);
		allPhoneContacts();
		adapter = new MyCustomAdapter(this, R.layout.custom_row, listThread);

		msgThreadList.setAdapter(adapter);
		// msgThreadList.setOnItemClickListener(this);
	}

	@SuppressWarnings("deprecation")
	private void getThreadList() {
		// TODO Auto-generated method stub
		// String msg = "";
		Log.d("ContactsContract.Contacts.CONTENT_URI", ""
				+ ContactsContract.Contacts.CONTENT_URI);
		ContentResolver cr = getApplicationContext().getContentResolver();
		Cursor cur = null;
		try {
			cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null,
					null, ContactsContract.Contacts.DISPLAY_NAME);

			startManagingCursor(cur);
			Log.d("Total contacts are", "" + cur.getCount());

			// Read the sms data and store it in the list
			if (cur.moveToFirst()) {
				for (int i = 0; i < cur.getCount(); i++) {
					String contactNumber = "No Phone No.";
					// int contactNumberType = -1;

					String id = cur.getString(cur
							.getColumnIndex(BaseColumns._ID));
					String nameOfContact = cur
							.getString(cur
									.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

					if (Integer
							.parseInt(cur.getString(cur
									.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
						Cursor phones = cr
								.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
										null,
										ContactsContract.CommonDataKinds.Phone.CONTACT_ID
												+ " = ?", new String[] { id },
										null);

						while (phones.moveToNext()) {
							contactNumber = phones.getString(phones
									.getColumnIndex(Phone.NUMBER));
							/*
							 * contactNumberType = phones.getInt(phones
							 * .getColumnIndex(Phone.TYPE));
							 */

						}
						phones.close();

					}

					ListData country = new ListData(Long.parseLong(id),
							contactNumber, nameOfContact, "", "", "", false);
					listThread.add(country);
					cur.moveToNext();

				}

			}

			cur.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("Error at", "" + cur.getCount());
			e.printStackTrace();
		} finally {
			cur.close();
		}

		Log.d(" listThread is", listThread.toString());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_contacts, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_all_contacts:
			fileName = "AllContacts";
			adapter = null;
			listThread = null;
			listThread = new ArrayList<ListData>();
			getThreadList();
			adapter = new MyCustomAdapter(this, R.layout.custom_row, listThread);
			msgThreadList.setAdapter(adapter);

			break;
		case R.id.menu_phone:
			fileName = "PhoneContacts";
			adapter = null;
			listThread = null;
			listThread = new ArrayList<ListData>();
			allPhoneContacts();
			adapter = new MyCustomAdapter(this, R.layout.custom_row, listThread);
			msgThreadList.setAdapter(adapter);

			break;
		case R.id.menu_sim:
			fileName = "SimContacts";
			adapter = null;
			listThread = null;
			listThread = new ArrayList<ListData>();
			allSIMContact();
			adapter = new MyCustomAdapter(this, R.layout.custom_row, listThread);
			msgThreadList.setAdapter(adapter);
			break;
		case R.id.menu_export:

			if (Utility.isOneRowSelected(listThread)) {
				try {
					Utility.save(Utility.formatJsonContacts(listThread), this,
							new File(Environment.getExternalStorageDirectory()
									+ "/DataExpoert/", "Contacts"), fileName
									+ ".txt");
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

	private void allPhoneContacts() {
		// TODO Auto-generated method stub
		Cursor phones = null;

		try {
			phones = getContentResolver().query(Phone.CONTENT_URI, null, null,
					null, ContactsContract.Contacts.DISPLAY_NAME);
			Log.d("All Phone Contacts ", "" + phones.getCount());
			int i = 0;
			while (phones.moveToNext()) {
				i++;
				String name = phones
						.getString(phones
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				String phoneNumber = phones
						.getString(phones
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

				Log.d("Contacts ", "" + i);
				ListData country = new ListData(Integer.parseInt("1"),
						phoneNumber, name, "", "", "", false);
				listThread.add(country);

			}
			phones.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			phones.close();
		} finally {
			phones.close();
		}
	}

	private void allSIMContact() {
		try {
			String m_simPhonename = null;
			String m_simphoneNo = null;
			String m_id = null;

			Cursor cursorSim = this.getContentResolver().query(simContacts,
					null, null, null, ContactsContract.Contacts.DISPLAY_NAME);

			Log.i("PhoneContact", "total: " + cursorSim.getCount());

			while (cursorSim.moveToNext()) {
				m_id = cursorSim.getString(cursorSim.getColumnIndex("_id"));
				m_simPhonename = cursorSim.getString(cursorSim
						.getColumnIndex("name"));
				m_simphoneNo = cursorSim.getString(cursorSim
						.getColumnIndex("number"));

				m_simphoneNo.replaceAll("\\D", "");
				m_simphoneNo.replaceAll("&", "");
				m_simPhonename = m_simPhonename.replace("|", "");

				ListData country = new ListData(Integer.parseInt(m_id),
						m_simphoneNo, m_simPhonename, "", "", "", false);
				listThread.add(country);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
