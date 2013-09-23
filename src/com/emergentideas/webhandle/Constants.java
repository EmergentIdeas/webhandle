package com.emergentideas.webhandle;

public interface Constants {

	// expected sources
	public static final String USER_INFORMATION_SOURCE_NAME = "userInfo";
	public static final String ANNOTATION_PROPERTIES_SOURCE_NAME = "annotationProperties";
	public static final String REQUEST_BODY_SOURCE_NAME = "requestBody";
	public static final String REQUEST_HEADER_SOURCE_NAME = "requestHeader";

	// expected transformers
	public static final String DB_TO_OBJECT_TRANSFORMER_NAME_DEFAULT = "dbObjectSourceTransformer";
	
	// expected parameter names
	public static final String HANDLER_RESPONSE = "response";
	
	// location locations
	public static final String ENV_LOCATION = "envLocation";
	public static final String APP_LOCATION = "appLocation";
	public static final String SESSION_LOCATION = "sessionLocation";
	public static final String REQUEST_LOCATION = "requestLocation";
	
	// locations of interesting objects
	public static final String LOCATION_OF_REQUEST = "request";
	public static final String LOCATION_OF_WEB_APP_CONTEXT_PATH = "webAppContextPath";
	
	// names of interesting objects/data
	public static final String NAME_OF_PERSISTENCE_UNIT = "persistenceUnitName";
	
	// the name of the app's class loader
	public static final String CLASS_LOADER_NAME = "app-class-loader";
	
	// the name of the string which is the apps root on disk
	public static final String APPLICATION_ON_DISK_LOCATION = "application-on-disk-location";


	public static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";
	
}
