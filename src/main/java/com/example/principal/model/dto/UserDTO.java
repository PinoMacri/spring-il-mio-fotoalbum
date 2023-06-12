package com.example.principal.model.dto;

import java.util.List;

public class UserDTO {
	private int id;
    private String username;
    private List<RoleDTO> roles;
    public UserDTO() {}
	public UserDTO(int id, String username, List<RoleDTO> roles) {
		this.id = id;
		this.username = username;
		this.roles = roles;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<RoleDTO> getRoles() {
		return roles;
	}
	public void setRoles(List<RoleDTO> roles) {
		this.roles = roles;
	}
	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", username=" + username + "]";
	}
}
