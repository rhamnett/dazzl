package org.hamnett.adm.util;

import java.util.List;

public class LWRFConfig {
	
	private String room;
	private String name;
	private List<String> devices;
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getDevices() {
		return devices;
	}
	public void setDevices(List<String> devices) {
		this.devices = devices;
	}


}
