package com.wavegis.basic_construction;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.wavegis.global.tools.LogTool;
import com.wavegis.model.DataModel;

public class Service {

	@Autowired
	private Dao dao;

	private Logger logger;

	public Service() {
		initLog();
	}

	private void initLog() {
		logger = LogTool.getLogger(Service.class.getName());
		showMessage("log初始化完成");
	}

	public String testProcess() {
		String result = "";
		dao.creatTable();
		dao.insertData();
		List<DataModel> datas = dao.getData();
		for (DataModel data : datas) {
			result += data.get_id() + "  " + data.get_column1() + "   " + data.get_column2() + "\n";
		}
		return result;
	}

	private void showMessage(String message) {
		logger.fatal(message);
	}
}
