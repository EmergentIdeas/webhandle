package com.emergentideas.webhandle.db;

import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;

import com.emergentideas.webhandle.Constants;
import com.emergentideas.webhandle.NamedTransformer;
import com.emergentideas.webhandle.Wire;
import com.emergentideas.webhandle.composites.db.DbIdToObjectTransformer;

@NamedTransformer(Constants.DB_TO_OBJECT_TRANSFORMER_NAME_DEFAULT)
public class DbObjectTransformer extends DbIdToObjectTransformer {

	protected EntityManager entityManager;

	@Override
	protected Object loadObject(String id, Class finalParameterClass) {
		if(StringUtils.isBlank(id) || entityManager == null) {
			return null;
		}
		try {
			Object o = entityManager.find(finalParameterClass, id);
			return o;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Wire
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	
	
}
