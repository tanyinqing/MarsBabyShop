package com.xlw.babyshop.model;

/**
 * 地址信息
 *
 */
public class AddressModel {
	private int id;
	private String name;
	private String address_area;
	private String address_detail;
	
	public AddressModel() {
	}
	
	public AddressModel(int id, String name, String address_area, String address_detail) {
		super();
		this.id = id;
		this.name = name;
		this.address_area = address_area;
		this.address_detail = address_detail;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress_area() {
		return address_area;
	}
	public void setAddress_area(String address_area) {
		this.address_area = address_area;
	}
	public String getAddress_detail() {
		return address_detail;
	}
	public void setAddress_detail(String address_detail) {
		this.address_detail = address_detail;
	}
}	
