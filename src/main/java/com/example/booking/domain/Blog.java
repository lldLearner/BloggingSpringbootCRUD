package com.example.booking.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Blog {

	@Id
	private String id;
	private String content;

	public Blog() {
		// TODO Auto-generated constructor stub
	}
	
	public Blog(String id, String content) {
		super();
		this.id = id;
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
