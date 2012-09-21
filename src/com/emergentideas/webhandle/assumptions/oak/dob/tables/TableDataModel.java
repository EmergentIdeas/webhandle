package com.emergentideas.webhandle.assumptions.oak.dob.tables;

import java.util.ArrayList;
import java.util.List;

import com.emergentideas.webhandle.AppLocation;

public class TableDataModel {

	protected List<String> headers = new ArrayList<String>();
	protected List<String> properties = new ArrayList<String>();
	protected List<Object> items = new ArrayList<Object>();
	
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
	
	public List<Row> getRows() {
		List<Row> results = new ArrayList<Row>();
		
		for(Object item : items) {
			List<Object> rowValues = new ArrayList<Object>();
			AppLocation loc = new AppLocation();
			loc.add(item);
			for(String property : properties) {
				rowValues.add(loc.get(property));
			}
			results.add(new Row(rowValues));
		}
		
		return results;
	}
	
}
