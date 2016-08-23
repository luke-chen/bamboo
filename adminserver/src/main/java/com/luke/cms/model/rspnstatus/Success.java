package com.luke.cms.model.rspnstatus;

public class Success extends Result {
	public Success() {
		super(1, null, null);
	}
	
	public Success(Object data) {
		super(1, null, data);
	}
	
	public Success(String info, Object data) {
		super(1, info, data);
	}
}
