package com.example.sjliandong;



import java.util.ArrayList;
import java.util.List;

public class City {
	private String id;
	private String name;
	private List<District> districts;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public List<District> getDistricts() {
		return districts;
	}
	public void setDistricts(List<District> districts) {
		this.districts = districts;
	}
	@Override
	public String toString() {
		return name;
	}
	

}
