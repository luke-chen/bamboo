package com.luke.cms.model.fruit;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public interface Fruit {
	public void setMade(String made);
	public String getName();
}
