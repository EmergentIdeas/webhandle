package com.emergentideas.webhandle.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emergentideas.webhandle.Name;

@Name("dbConfiguration")
public class DbConfiguration {
	protected List<String> classNames = Collections.synchronizedList(new ArrayList<String>());
	
	protected String unitName;
	protected String provider;
	protected String driver;
	protected String url;
	protected String user;
	protected String password;
	protected Map<String, String> arbitraryProperties = Collections.synchronizedMap(new HashMap<String, String>());
	
	
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<String> getClassNames() {
		return classNames;
	}
	public Map<String, String> getArbitraryProperties() {
		return arbitraryProperties;
	}
	
	public void setHibernateHbm2ddlAuto(String ddlType) {
		arbitraryProperties.put("hibernate.hbm2ddl.auto", ddlType);
	}
}
