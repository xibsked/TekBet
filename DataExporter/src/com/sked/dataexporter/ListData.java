package com.sked.dataexporter;

public class ListData {
	long thread_id = (long) -1;
	String snippet = null;
	String name = null;
	String label_3 = "";
	String label_4 = "";
	String label_5 = "";
	boolean selected = false;

	public ListData(long _id, String snippet, String name, String label_3,
			String label_4, String label_5, boolean selected) {
		super();
		this.thread_id = _id;
		this.snippet = snippet;
		this.name = name;
		this.label_3 = label_3;
		this.label_4 = label_4;
		this.label_5 = label_5;
		this.selected = selected;
	}

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getThreadId() {
		return thread_id;
	}

	public void setThreadId(int _id) {
		this.thread_id = _id;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void setLabel4(String label_4) {
		this.label_4 = label_4;
	}

	public void setLabel5(String label_5) {
		this.label_5 = label_5;
	}

	public void setLabel3(String label_3) {
		this.label_3 = label_3;
	}

	public String getLabel3() {
		return label_3;
	}

	public String getLabel4() {
		return label_4;
	}

	public String getLabel5() {
		return label_5;
	}
}