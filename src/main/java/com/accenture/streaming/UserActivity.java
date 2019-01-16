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
	private String uid;
	private Timestamp ts;
	
	private String customerName;
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerAddress() {
		return customerAddress;
	}
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	private String customerAddress;
	
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
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public Timestamp getTs() {
		return ts;
	}
	public void setTs(Timestamp ts) {
		this.ts = ts;
	}
	
	
}
