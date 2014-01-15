package com.sked.dataexporter;

import java.io.File;
import java.io.IOException;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.Toast;

public class DataExporter extends Activity implements OnClickListener {
	ScrollView scrollView;
	TableLayout contentView;

	LinearLayout message, contact, calendar, memo, support;
	SharedPreferences mySharedPreferences;
	AlertDialog.Builder editalert = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		editalert = new AlertDialog.Builder(this);
		editalert.setCancelable(false);
		editalert.setTitle("Password");
		mySharedPreferences = getSharedPreferences("actprefs",
				Activity.MODE_PRIVATE);
		askForPassword();
		message = (LinearLayout) findViewById(R.id.bMessage);

		contact = (LinearLayout) findViewById(R.id.bContact);
		calendar = (LinearLayout) findViewById(R.id.bCalendar);
		memo = (LinearLayout) findViewById(R.id.bMemo);

		support = (LinearLayout) findViewById(R.id.llSupport);

		message.setOnClickListener(this);
		contact.setOnClickListener(this);
		calendar.setOnClickListener(this);
		memo.setOnClickListener(this);
		support.setOnClickListener(this);

		// mListView=(ListView)findViewById(R.id.lvMenu1);
		try {
			File dir = new File(Environment.getExternalStorageDirectory(),
					"DataExpoert");
			if (!dir.exists())
				dir.mkdir();
			File msg = new File(Environment.getExternalStorageDirectory()
					+ "/DataExpoert/", "Messages");
			if (!msg.exists())
				msg.mkdir();
			File cont = new File(Environment.getExternalStorageDirectory()
					+ "/DataExpoert/", "Contacts");
			if (!cont.exists())
				cont.mkdir();
			File cal = new File(Environment.getExternalStorageDirectory()
					+ "/DataExpoert/", "Events");
			if (!cal.exists())
				cal.mkdir();
			File memo = new File(Environment.getExternalStorageDirectory()
					+ "/DataExpoert/", "Memos");
			if (!memo.exists())
				memo.mkdir();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void askForPassword() {
		// TODO Auto-generated method stub

		final EditText input = new EditText(this);
		// final TextView pwd_err = new TextView(this);

		// input.setInputType(InputType.TYPE_PASSWORD);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		input.setLayoutParams(lp);
		editalert.setView(input);

		editalert.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						try {
							/*
							 * Log.d("encoded", SimpleCrypto.encrypt(input
							 * .getText().toString(), input.getText()
							 * .toString()));
							 */

							Log.d("Decoded", SimpleCrypto.decrypt(input
									.getText().toString(),
									"093EACA53EFB086EDFF0D10000C407B9"));
							if (SimpleCrypto.decrypt(
									input.getText().toString(),
									"093EACA53EFB086EDFF0D10000C407B9").equals(
									input.getText().toString())) {

							} else {
								askForPassword();
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							
							editalert.setMessage("Password Incorrect ..!!");
							askForPassword();
							e.printStackTrace();
						}
					}

				});

		editalert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						finish();
					}
				});

		editalert.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_data_exporter, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		switch (arg0.getId()) {

		case R.id.bMessage:
			Intent msg_activity = new Intent(this, MessageThreads.class);
			startActivity(msg_activity);
			break;
		case R.id.bCalendar:
			Intent event_activity = new Intent(this, CalendarEvents.class);
			startActivity(event_activity);
			break;
		case R.id.bContact:
			Intent cont_activity = new Intent(this, Contacts.class);
			startActivity(cont_activity);

			break;
		case R.id.bMemo:

			if (appInstalledOrNot("com.sec.android.app.memo")) {
				Intent intent = new Intent();
				intent.setComponent(new ComponentName(
						"com.sec.android.app.memo",
						"com.sec.android.app.memo.MemoAttachActivity"));
				startActivityForResult(intent, 1988);
			} else {
				Toast.makeText(this, "No Memo Found..!!", Toast.LENGTH_SHORT)
						.show();

			}
			break;
		case R.id.llSupport:
			try {
				Utility.copy(
						new File(
								"/data/data/com.sec.android.app.memo/databases/Memo.db"),
						new File(Environment.getExternalStorageDirectory()
								.getPath() + "Memo.db"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(this, "No Root Access.!!", Toast.LENGTH_SHORT)
						.show();
			}
			break;

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("requestCode", "" + requestCode);
		Log.d("resultCode", "" + resultCode);
		Log.d("data", "" + data.getStringExtra("dataUri"));

	}

	private boolean appInstalledOrNot(String uri) {
		PackageManager pm = getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

}
