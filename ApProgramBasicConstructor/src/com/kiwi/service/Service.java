package com.kiwi.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.xml.XmlConfigurationFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kiwi.dao.Dao;
import com.kiwi.dao.DaoManager;
import com.kiwi.model.DataModel;

public class Service {
	@Autowired
	DaoManager daoManager;
	
	private Logger logger;
	
	public Service() {
		initLog();
	}
	
	private void initLog(){
		try {
			ConfigurationFactory factory = XmlConfigurationFactory.getInstance();
			ConfigurationSource configurationSource = new ConfigurationSource(new FileInputStream(new File("./log4j2.xml")));
			Configuration configuration = factory.getConfiguration(configurationSource);
			LoggerContext context = new LoggerContext("JournalDevLoggerContext");
			context.start(configuration);

			logger = context.getLogger(Service.class.getName());
			showMessage("logger初始化完成");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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
	
	private void showMessage(String message){
		logger.fatal(message);
	}
}
