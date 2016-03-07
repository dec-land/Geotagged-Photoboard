package com.beta;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Photo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long photoID;
	private double lat;
	private double lon;
	private String description = "";
	private String address = "";
	private String date;
	private int status;
	private String event;

	protected Photo() {
	}

	Photo(String description, double lat, double lon, String address, String d, int status, String event) {
		this.description = description;
		this.lat = lat;
		this.address = address;
		this.lon = lon;
		this.date = d;
		this.status = status;
		this.event = event;
	}

	Photo(double lat, double lon, String address, String d, int status, String event) {
		this.lat = lat;
		this.lon = lon;
		this.address = address;
		this.date = d;
		this.status = status;
		this.event = event;
	}

	public long getID() {
		return this.photoID;
	}

	public String getDesc() {
		return this.description;
	}

	public double getLat() {
		return this.lat;
	}

	public double getLon() {
		return this.lon;
	}

	public String getDate() {
		return this.date;
	}

	public int getStatus() {
		return this.status;
	}

	public String getAddress() {
		return this.address;
	}

	public String getEvent() {
		return this.event;
	}

	public String toString() {
		return String.format("Photo[id=%d, description='%s', lat='%f', lon='%f']", new Object[] {
				Long.valueOf(this.photoID), this.description, Double.valueOf(this.lat), Double.valueOf(this.lon) });
	}
}
