package com.emergentideas.webhandle.apps.oak.crud;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.BeanInfoUtils;
import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.Inject;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.NotNull;
import com.emergentideas.webhandle.Wire;
import com.emergentideas.webhandle.assumptions.oak.RequestMessages;
import com.emergentideas.webhandle.assumptions.oak.dob.tables.TableDataModel;
import com.emergentideas.webhandle.assumptions.oak.interfaces.User;
import com.emergentideas.webhandle.composites.db.Db;
import com.emergentideas.webhandle.handlers.Handle;
import com.emergentideas.webhandle.handlers.HttpMethod;
import com.emergentideas.webhandle.output.Show;
import com.emergentideas.webhandle.output.Template;
import com.emergentideas.webhandle.output.Wrap;

public class CRUDHandle<T> {
	
	protected EntityManager entityManager;
	protected Class<?> entityType;
	protected Logger log = SystemOutLogger.get(getClass());


	@Handle(value = "/create", method = HttpMethod.POST)
	@Template
	@Wrap("app_page")
	public Object createPost(InvocationContext context, @NotNull @Inject T focus, Location location, RequestMessages messages) {

		if(validateCreate(focus, messages)) {
			entityManager.persist(focus);
			return new Show(getUrlPrefix() + "/list");
		}
		
		location.add(focus);
		return getTemplatePrefix() + "create";
	}

	
	@Handle(value = "/create", method = HttpMethod.GET)
	@Template
	@Wrap("app_page")
	public Object createGet(InvocationContext context, User user, Location location) {
		return getTemplatePrefix() + "create";
	}
	
	@Handle(value = "/{id:\\d+}", method = HttpMethod.GET)
	@Template
	@Wrap("app_page")
	public Object editGet(InvocationContext context, User user, Integer id, Location location) {
		Object entity = entityManager.find(getEntityClass(), id);
		location.add(entity);
		return getTemplatePrefix() + "edit";
	}
	
	@Handle(value = "/{id:\\d+}", method = HttpMethod.POST)
	@Template
	@Wrap("app_page")
	public Object editPost(InvocationContext context, User user, @Db("id") @Inject T focus, Location location, RequestMessages messages) {
		if(validateEdit(focus, messages)) {
			return new Show("list");
		}
		entityManager.detach(focus);
		location.add(focus);
		return getTemplatePrefix() + "edit";
	}
	
	@Handle(value = "/{id:\\d+}/delete", method = HttpMethod.POST)
	@Template
	@Wrap("app_page")
	public Object deletePost(InvocationContext context, User user, @Db("id") T focus, Location location, RequestMessages messages) {
		entityManager.remove(focus);
		return new Show(getUrlPrefix() + "/list");
	}
	
	@Handle(value = {"/list", "", "/"}, method = HttpMethod.GET)
	@Template
	@Wrap("app_page")
	public Object list(InvocationContext context, User user, Location location, HttpServletRequest request) {
		
		List<T> all = findEntitiesToShow(context, user, request);
		all = sortEntities(all);
		location.put("all", all);
		
		TableDataModel table = createDataTable(all);
	
		location.put("table", table);

		return getTemplatePrefix() + "list";
	}
	
	/**
	 * Sorts or returns a new list of sorted entities
	 * @param all
	 * @return The list passed if it was sorted in place or a new list if a new one was created.
	 */
	protected List<T> sortEntities(List<T> all) {
		return all;
	}
	
	/**
	 * Finds the entities to show
	 * @param context
	 * @param user
	 * @return
	 */
	@SuppressWarnings(value = "unchecked")
	protected List<T> findEntitiesToShow(InvocationContext context, User user, HttpServletRequest request) {
		return entityManager.createQuery("select r from " + getEntityShortName() + " r").getResultList();
	}
	
	/**
	 * Creates the data table to be shown by the list view
	 * @param all All of the entities to show
	 * @return
	 */
	protected TableDataModel createDataTable(List<T> all) {
		List<String> propertyNames = determinePropertyNames();
		List<String> headers = determinePropertyLabels(propertyNames);
		
		String prefix = getUrlPrefix();
		if(prefix.endsWith("/") == false) {
			prefix += "/";
		}
		
		String idProperty = BeanInfoUtils.determineIdPropertyNameWithGetter(getEntityClass());
		TableDataModel table = new TableDataModel()
		.setHeaders(headers.toArray(new String[headers.size()]))
		.setProperties(propertyNames.toArray(new String[propertyNames.size()]))
		.setDeleteURLPattern(prefix, idProperty, "/delete")
		.setEditURLPattern(0, prefix, idProperty, "")
		.setCreateNewURL(prefix + "create")
		.setItems(all.toArray());
		
		modifyDataTable(all, table);
		
		return table;
	}
	
	/**
	 * Determines with property names should be shown for this table.  By default, this examines
	 * the entity class for non-object, non-id, properties.
	 * @return
	 */
	protected List<String> determinePropertyNames() {
		return BeanInfoUtils.determineNonIdNonObjectPropertyNamesWithGetters(getEntityClass());
	}
	
	/**
	 * For the property names, determine what labels should be shown for each property.
	 * @param propertyNames The properties that will be shown in the table.
	 * @return
	 */
	protected List<String> determinePropertyLabels(List<String> propertyNames) {
		return BeanInfoUtils.formatCamelCasePropertyNames(propertyNames);
	}
	
	/**
	 * A hook to add template specs, change column headers, etc
	 * @param allEntities
	 * @param table
	 */
	protected void modifyDataTable(List<T> allEntities, TableDataModel table) {
		
	}

	/**
	 * Return true if valid, false otherwise
	 * @param focus
	 * @param messages
	 * @return
	 */
	protected boolean validateEdit(T focus, RequestMessages messages) {
		return true;
	}
	
	/**
	 * Return true if valid, false otherwise
	 * @param focus
	 * @param messages
	 * @return
	 */
	protected boolean validateCreate(T focus, RequestMessages messages) {
		return true;
	}
	
	protected Class<?> getEntityClass() {
		if(entityType == null) {
			entityType = (Class<?>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		}
		return entityType;
	}
	
	
	protected String getEntityShortName() {
		String name = getEntityClass().getName();
		name = name.substring(name.lastIndexOf('.') + 1);
		return name;
	}
	
	/**
	 * Returns the url prefix which this handle uses like <code>/people</code>.  By default, this is the first
	 * string of the value attribute of this class's {@link Handle} attribute.
	 * @return
	 */
	protected String getUrlPrefix() {
		Handle h = ReflectionUtils.getAnnotationOnClass(getClass(), Handle.class);
		if(h != null) {
			return h.value()[0];
		}
		return "";
	}
	
	protected String getTemplatePrefix() {
		return "";
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Wire
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	
}
