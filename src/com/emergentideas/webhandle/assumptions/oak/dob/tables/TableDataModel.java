package com.emergentideas.webhandle.assumptions.oak.dob.tables;

import java.util.ArrayList;
import java.util.List;

import com.emergentideas.webhandle.AppLocation;

public class TableDataModel {

	// The strings to show as headers at the top of the table
	protected List<String> headers = new ArrayList<String>();
	
	// The property names of the items that should be used as values in the table
	protected List<String> properties = new ArrayList<String>();
	
	// The items to use as rows in the table
	protected List<Object> items = new ArrayList<Object>();
	
	protected String createNewURL = "/groups/new";
	
	public TableDataModel setHeaders(String... headers) {
		this.headers.clear();
		
		for(String header : headers) {
			this.headers.add(header);
		}
		
		return this;
	}
	
	public TableDataModel setProperties(String... properties) {
		this.properties.clear();
		
		for(String property : properties) {
			this.properties.add(property);
		}
		
		return this;
	}
	
	public TableDataModel setItems(Object... items) {
		this.items.clear();
		
		for(Object item : items) {
			this.items.add(item);
		}
		
		return this;
	}
	
	public TableDataModel addItem(Object item) {
		this.items.add(item);
		return this;
	}
	
	public List<String> getHeaders() {
		return headers;
	}
	
	
	
	public String getCreateNewURL() {
		return createNewURL;
	}

	public TableDataModel setCreateNewURL(String createNewURL) {
		this.createNewURL = createNewURL;
		return this;
	}

	public List<Row> getRows() {
		List<Row> results = new ArrayList<Row>();
		
		for(Object item : items) {
			List<Object> rowValues = new ArrayList<Object>();
			AppLocation loc = new AppLocation();
			loc.add(item);
			for(String property : properties) {
				rowValues.add(new Cell(loc.get(property)).setClickURL("/hello"));
			}
			results.add(new Row(rowValues));
		}
		
		return results;
	}
	
	public class Row {
		protected List<Object> values;
		protected String deleteURL = "deletethis";

		public Row() {
			values = new ArrayList<Object>();
		}
		
		public Row(List<Object> values) {
			this.values = values;
		}
		
		public List<Object> getValues() {
			return values;
		}

		public void setValues(List<Object> values) {
			this.values = values;
		}

		public String getDeleteURL() {
			return deleteURL;
		}

		public void setDeleteURL(String deleteURL) {
			this.deleteURL = deleteURL;
		}

		
	}
	
	public class Cell {
		public Object value;
		public String clickURL;
		
		public Cell() {}
		
		public Cell(Object value) {
			this.value = value;
		}
		
		
		public Object getValue() {
			return value;
		}
		public Cell setValue(Object value) {
			this.value = value;
			return this;
		}
		public String getClickURL() {
			return clickURL;
		}
		public Cell setClickURL(String clickURL) {
			this.clickURL = clickURL;
			return this;
		}
		
		public String toString() {
			if(value != null) {
				return value.toString();
			}
			return "";
		}
	}
	
}
