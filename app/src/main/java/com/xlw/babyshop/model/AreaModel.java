package com.xlw.babyshop.model;

import java.util.List;

/**
 * 地区
 *
 */
public class AreaModel {

	private int id;					// 地区ID
	private String value;			// 名称
	private List<AreaModel> sub_area;	// 下一级地址

	public AreaModel() {
	}

	public AreaModel(int id, String value, List<AreaModel> sub_area) {
		super();
		this.id = id;
		this.value = value;
		this.sub_area = sub_area;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<AreaModel> getSub_area() {
		return sub_area;
	}

	public void setSub_area(List<AreaModel> sub_area) {
		this.sub_area = sub_area;
	}

	@Override
	public String toString() {
		return value;
	}
}
