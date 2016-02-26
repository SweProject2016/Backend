package de.hwrberlin.it2014.sweproject.model;

public class Entity {
	
	private String path;
	private int status;
	private Object entity;
	
	public Entity(String path, int status, Object entity) {
		this.path = path;
		this.status = status;
		this.entity = entity;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Object getEntity() {
		return entity;
	}
	public void setEntity(Object entity) {
		this.entity = entity;
	}

}
