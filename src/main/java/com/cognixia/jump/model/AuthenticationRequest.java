package com.cognixia.jump.model;

public class AuthenticationRequest {

	private String username;
	private String password;
	private String email;
	
	public AuthenticationRequest() {
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmail() {
		return password;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
