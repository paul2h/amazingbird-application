package com.kiwi.dao;

import java.io.IOException;
import java.util.List;

import com.kiwi.model.DataModel;

public class DaoManager {

	static DaoManager instance;
	private Dao dao;

	public static void main(String[] args) {
		DaoManager dm = DaoManager.getInstance();
		Dao dao = dm.getDao();
		dao.creatTable();
		dao.insertData();
		List<DataModel> datas = dao.getData();
		for(DataModel data : datas){
			System.out.println(data.get_id() + "  " + data.get_column1() + "   " + data.get_column2());
		}
	}
	
	public DaoManager() {
		instance = this;
	}

	public static DaoManager getInstance() {
		if(instance == null){
			instance = new DaoManager();
		}
		return instance;
	}

	public Dao getDao() {
		if (dao == null) {
			try {
				dao = Dao.getInstance();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dao;
	}

}
