package com.luke.cms.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FooBean {
	private final long id;
    private String content;

    public FooBean(long id, String content) {
        this.id = id;
        this.content = content;
    }
    
    public long getId() {
		return id;
	}
    
    public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
