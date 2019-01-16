package com.accenture.streaming;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserActivity implements Serializable{

	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = 3056767384095027403L;
	
	private String username;
	private String action;
	private String id;
	private Timestamp ts;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getId() {
		return id;
	}
	public void setId(String uid) {
		this.id = uid;
	}
	public Timestamp getTs() {
		return ts;
	}
	public void setTs(Timestamp ts) {
		this.ts = ts;
	}
	
	
}
