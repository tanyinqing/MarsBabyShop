package com.xlw.babyshop.utils;

import com.xlw.babyshop.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class DivideCategoryList {

	List<CategoryModel> totalList;		// 所有商品类别

	public DivideCategoryList(List<CategoryModel> totalList) {
		super();
		this.totalList = totalList;
	}

	// 获得一级子类别
	public List<CategoryModel> getOneLevel(){
		List<CategoryModel>  voList = new ArrayList<>();
		for(CategoryModel vo : totalList){
			if(vo.getParent_id().equals("0")){
				voList.add(vo);
			}
		}
		return voList;
	}

	// 获得指定类别的下一级子类别
	public List<CategoryModel> getNextLevel(String listId){
		List<CategoryModel> voList = new ArrayList<>();
		for(CategoryModel vo : totalList){
			if(vo.getParent_id().equals(listId)){
				voList.add(vo);
			}
		}
		return voList;
	}

}
