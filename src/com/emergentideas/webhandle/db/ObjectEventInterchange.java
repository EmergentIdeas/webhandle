package com.emergentideas.webhandle.db;
import static com.emergentideas.webhandle.db.ObjectChangeListener.ChangeType.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import com.emergentideas.webhandle.Init;
import com.emergentideas.webhandle.Type;

@Resource
@Type("com.emergentideas.webhandle.db.ObjectEventInterchange")
public class ObjectEventInterchange implements ObjectChangeListener<Object> {
	
	protected List<ListenerDef> allListeners = Collections.synchronizedList(new ArrayList<ListenerDef>());

	@Override
	public ChangeType[] listenFor() {
		return new ChangeType[] { PRE_UPDATE, POST_UPDATE, PRE_PERSIST, POST_PERSIST, PRE_REMOVE, POST_REMOVE, POST_LOAD };
	}

	@Override
	public void changeEvent(ChangeType type, Object t) {
		synchronized (allListeners) {
			for(ListenerDef def : allListeners) {
				if(def.shouldNotify(type, t)) {
					def.notify(type, t);
				}
			}
		}
	}
	
	public void addListener(ObjectChangeListener l) {
		ListenerDef def = new ListenerDef(l);
		allListeners.add(def);
	}

	@Init
	public void init() {
		JPAGlobalListener.setListener(this);
	}
	
	
}
