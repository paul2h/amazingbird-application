package com.kiwi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.kiwi.dao.Dao;
import com.kiwi.dao.DaoManager;
import com.kiwi.model.DataModel;

public class Service {
	@Autowired
	DaoManager daoManager;

	public String testProcess() {
		String result = "";
		Dao dao = daoManager.getDao();
		dao.creatTable();
		dao.insertData();
		List<DataModel> datas = dao.getData();
		for (DataModel data : datas) {
			result += data.get_id() + "  " + data.get_column1() + "   "
					+ data.get_column2() + "\n";
		}
		return result;
	}
}
