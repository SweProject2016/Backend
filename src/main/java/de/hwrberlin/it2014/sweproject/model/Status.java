package de.hwrberlin.it2014.sweproject.model;

public class Status {
	
	public enum ServerStatus{
		RUNNING,SHUTDOWN,NOT_RESPONDING,
	}
	
	private ServerStatus database;
	private ServerStatus server;
	
	public ServerStatus getDatabaseStatus() {
		return database;
	}
	public void setDatabaseStatus(ServerStatus database) {
		this.database = database;
	}
	public ServerStatus getServer() {
		return server;
	}
	public void setServer(ServerStatus server) {
		this.server = server;
	}

}
