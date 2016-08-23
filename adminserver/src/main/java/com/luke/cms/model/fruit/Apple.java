package com.luke.cms.model.fruit;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Apple implements Fruit {
	private String made;
	
	@Override
	public void setMade(String made) {
		this.made = made;
	}
	
	@Override
	public String getName() {
		return "I am Apple made in "+made;
	}

}
