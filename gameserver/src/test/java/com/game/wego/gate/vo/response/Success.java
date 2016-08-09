package com.game.wego.gate.vo.response;

public class Success extends Result {
	public Success() {
		super(SUCCESS, null, null);
	}

	public Success(Object data) {
		super(SUCCESS, null, data);
	}

	public Success(String info, Object data) {
		super(SUCCESS, info, data);
	}
}
