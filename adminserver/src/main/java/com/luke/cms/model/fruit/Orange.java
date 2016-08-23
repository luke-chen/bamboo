package com.luke.cms.model.fruit;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("orange") // This is bean name
@Scope("prototype")  // It is not singleton mode
public class Orange implements Fruit {
	private String made;
	
	@Override
	public void setMade(String made) {
		this.made = made;
	}

	@Override
	public String getName() {
		return "I am Orange. made in "+made;
	}

}
