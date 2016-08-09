package com.game.wego.gate.vo.response;

public class Failed extends Result {
	public Failed() {
		super(FAIL, null, null);
	}

	public Failed(String info) {
		super(FAIL, info, null);
	}

	public Failed(Object data) {
		super(FAIL, null, data);
	}
	public Failed(String info, Object data) {
		super(FAIL, info, data);
	}
}
