package com.xlw.babyshop.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xlw.babyshop.model.AreaModel;

import java.util.ArrayList;
import java.util.List;

public class AreaDao extends BaseDao {
	public static String TABLE = "pub_cant";

	public AreaDao(Context context) {
		super(context);
	}

	public List<AreaModel> getAllProvince() {
		return callBack(TYPE_READ, new DaoCallBack<List<AreaModel>>() {

			@Override
			public List<AreaModel> invoke(SQLiteDatabase conn) {
				cursor = conn.query(TABLE, null, " super_code = ?", new String[] { "CN" }, null, null, null, null);
				AreaModel area;
				List<AreaModel> areas = new ArrayList<AreaModel>();
				while (cursor.moveToNext()) {
					area = new AreaModel();
					fillArea(cursor, area);
					areas.add(area);
				}
				return areas;
			}
		});
	}
	
	public List<AreaModel> findBySuperCode(final int superCode) {
		return callBack(TYPE_READ, new DaoCallBack<List<AreaModel>>() {

			@Override
			public List<AreaModel> invoke(SQLiteDatabase conn) {
				cursor = conn.query(TABLE, null, " super_code = ?", new String[] { Integer.toString(superCode) }, null, null, null, null);
				AreaModel area;
				List<AreaModel> areas = new ArrayList<AreaModel>();
				while (cursor.moveToNext()) {
					area = new AreaModel();
					fillArea(cursor, area);
					areas.add(area);
				}
				return areas;
			}
		});
	}
	
	public AreaModel findByCantCode(final int cantCode)  {
		return callBack(TYPE_READ, new DaoCallBack<AreaModel>() {

			@Override
			public AreaModel invoke(SQLiteDatabase conn) {
				cursor = conn.query(TABLE, null, " cant_code = ?", new String[] { Integer.toString(cantCode) }, null, null, null, null);
				AreaModel area = new AreaModel();
				if (cursor.moveToFirst()) {
					fillArea(cursor, area);
				}
				return area;
			}
		});
	}
	
	private void fillArea(Cursor cursor, AreaModel area) {
		area.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("cant_code"))));
		area.setValue(cursor.getString(cursor.getColumnIndex("cant_name")));
	}
}
