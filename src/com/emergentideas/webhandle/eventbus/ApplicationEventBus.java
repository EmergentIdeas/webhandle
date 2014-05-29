package com.emergentideas.webhandle.eventbus;

import javax.annotation.Resource;

@Resource(name = "applicationEventBus", type = EventBus.class)
public class ApplicationEventBus extends SimpleEventBus {

}
