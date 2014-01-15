package com.sked.dataexporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import android.content.Context;
import android.widget.Toast;

public class Utility {

	public static void copy(File src, File dst) throws IOException {
		Runtime.getRuntime().exec("chmod 077" + src);
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);

		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	public static String formatTextContacts(ArrayList<ListData> listThread) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < listThread.size(); i++) {
			ListData row = listThread.get(i);
			if (row.isSelected()) {
				sb.append("==============================").append("\n")
						.append(row.getName()).append("\n")
						.append(row.getSnippet()).append("\n")
						.append("==============================").append("\n");
				;
			}

		}

		return sb.toString();
	}

	public static String formatTextMessages(ArrayList<ListData> listThread) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < listThread.size(); i++) {
			ListData row = listThread.get(i);
			if (row.isSelected()) {
				sb.append("==============================").append("\n")
						.append(row.getName()).append("\n")
						.append(row.getSnippet()).append("\n")
						.append("==============================").append("\n");
				;
			}

		}

		return sb.toString();
	}

	public static String formatJsonContacts(ArrayList<ListData> listThread) {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append("\n").append("\"" + "contacts" + "\"")
				.append(":\t").append("[").append("\t");
		for (int i = 0; i < listThread.size(); i++) {
			ListData row = listThread.get(i);
			if (row.isSelected()) {
				sb.append("\n{").append("\n").append("\t")
						.append("\"" + "contact_id" + "\"").append(":")
						.append("\"" + row.getThreadId() + "\"").append("\n")
						.append("\t").append("\"" + "contact_name" + "\"")
						.append(":").append("\"" + row.getName() + "\"")
						.append("\n").append("\t")
						.append("\"" + "contact_number" + "\"").append(":")
						.append("\"" + row.getSnippet() + "\"").append("\n")
						.append("},");

			}

		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("\n").append("]").append("\n").append("}");

		return sb.toString();
	}

	public static void save(String sBody, Context context, File path,
			String fileName) {
		// TODO Auto-generated method stub
		File location = path;
		/*
		 * EBdroid = new File(Environment.getExternalStorageDirectory(),
		 * "Message");
		 */
		if (!location.exists())
			location.mkdirs();
		File billFile = new File(location, fileName);
		try {
			FileWriter writer = new FileWriter(billFile);
			writer.append(sBody);
			writer.flush();
			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		Toast.makeText(context, "Saved Suceesfully in /SDcard/DataExport/",
				Toast.LENGTH_LONG).show();
	}

	public static boolean isOneRowSelected(ArrayList<ListData> listThread) {
		// TODO Auto-generated method stub
		for (int i = 0; i < listThread.size(); i++) {
			ListData row = listThread.get(i);
			if (row.isSelected())
				return true;
		}
		return false;
	}
}
