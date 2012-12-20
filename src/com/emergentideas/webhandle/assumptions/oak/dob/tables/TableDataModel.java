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
	
	// For each column, the template to use to display the data
	protected List<String> templates = new ArrayList<String>();
	
	// The template to use when none is specified
	protected String defaultTemplateName = "esc";
	
	protected String createNewURL;
	
	
	// the pattern for the edit url
	protected int columnToLink = -1;
	protected String editURLPrefix;
	protected String editIdProperty;
	protected String editURLSuffix;
	
	// the pattern for the delete url
	protected String deleteURLPrefix;
	protected String deleteIdProperty;
	protected String deleteURLSuffix;
	
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
	
	public TableDataModel setTemplates(String... templates) {
		this.templates.clear();
		
		for(String template : templates) {
			this.templates.add(template);
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
	
	/**
	 * Configures this table to link to a URL for editing.
	 * @param column The column of the table that will be linked.  O indexed.
	 * @param editURLPrefix The URL portion proceeding the id.  Can be blank but not null.
	 * @param editIdProperty the property name of the focus object that is used to generate the id.  Required
	 * @param editURLSuffix The URL portion that follows the id.  Can be blank but not null;
	 * @return
	 */
	public TableDataModel setEditURLPattern(int column, String editURLPrefix, String editIdProperty, String editURLSuffix)  {
		this.columnToLink = column;
		this.editURLPrefix = editURLPrefix;
		this.editIdProperty = editIdProperty;
		this.editURLSuffix = editURLSuffix;
		return this;
	}
	
	/**
	 * Configures this table to link to a URL for deleting.
	 * @param deleteURLPrefix The URL portion proceeding the id.  Can be blank but not null.
	 * @param deleteIdProperty The property name of the focus object that is used to generate the id.  Required.
	 * @param deleteURLSuffix The URL portion that follows the id.  Can be blank but not null.
	 * @return
	 */
	public TableDataModel setDeleteURLPattern(String deleteURLPrefix, String deleteIdProperty, String deleteURLSuffix) {
		this.deleteURLPrefix = deleteURLPrefix;
		this.deleteIdProperty = deleteIdProperty;
		this.deleteURLSuffix = deleteURLSuffix;
		return this;
	}
	
	public TableDataModel addItem(Object item) {
		this.items.add(item);
		return this;
	}
	
	public List<String> getHeaders() {
		return headers;
	}
	
	protected String createDeleteURL(Object focus) {
		return createURL(focus, deleteURLPrefix, deleteIdProperty, deleteURLSuffix);
	}
	
	protected String createClickURL(Object focus) {
		return createURL(focus, editURLPrefix, editIdProperty, editURLSuffix);
	}
	
	protected String createURL(Object focus, String prefix, String idProperty, String suffix) {
		if(focus == null) {
			return null;
		}
		
		if(prefix == null || idProperty == null || suffix == null) {
			return null;
		}
		
		AppLocation loc = new AppLocation();
		loc.add(focus);
		return prefix + loc.get(idProperty) + suffix;
		
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
			for(int i = 0; i < properties.size(); i++) {
				String property = properties.get(i);
				Cell c = new Cell(loc.get(property), i);
				if(i == columnToLink) {
					c.setClickURL(createClickURL(item));
				}
				rowValues.add(c);
			}
			results.add(new Row(rowValues).setFocus(item));
		}
		
		return results;
	}
	
	protected String getTemplateName(int columnNumber) {
		if(columnNumber <= templates.size() - 1) {
			return templates.get(columnNumber);
		}
		return defaultTemplateName;
	}

	
	public class Row {
		protected List<Object> values;
		protected Object focus;

		public Row() {
			values = new ArrayList<Object>();
		}
		
		public Row(List<Object> values) {
			this.values = values;
		}
		
		public List<Object> getValues() {
			return values;
		}

		public Row setValues(List<Object> values) {
			this.values = values;
			return this;
		}

		public String getDeleteURL() {
			return createDeleteURL(focus);
		}

		public Object getFocus() {
			return focus;
		}

		public Row setFocus(Object focus) {
			this.focus = focus;
			return this;
		}

		
	}
	
	public class Cell {
		protected Object value;
		protected String clickURL;
		protected int columnNumber;
		
		public Cell() {}
		
		public Cell(Object value, int columnNumber) {
			this.value = value;
			this.columnNumber = columnNumber;
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
		
		public String getTemplateName() {
			return TableDataModel.this.getTemplateName(columnNumber);
		}
		
		public String getPropertyName() {
			return properties.get(columnNumber);
		}
		
		public String getLabel() {
			return headers.get(columnNumber);
		}
		
		public int getColumnNumber() {
			return columnNumber;
		}

		public void setColumnNumber(int columnNumber) {
			this.columnNumber = columnNumber;
		}

		public String toString() {
			if(value != null) {
				return value.toString();
			}
			return "";
		}
	}

	public String getDeleteURLPrefix() {
		return deleteURLPrefix;
	}

	public void setDeleteURLPrefix(String deleteURLPrefix) {
		this.deleteURLPrefix = deleteURLPrefix;
	}
	
	
}
