package com.wavegis.dao;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.wavegis.global.GlobalConfig;
import com.wavegis.model.DataModel;

/**
 * 給Service取得Dao的Dao總管理物件
 * 
 * <pre>
 * 使用方法:mybatis
 */
public class Dao {
	static Dao instance;

	/* 取得config檔的reader */
	private Reader reader;

	// #[[DaoConnector List
	private DaoConnector daoConnector;
	// ]] Dao List */

	// #[[ myBatis物件
	private SqlSessionFactory sqlSessionFactory;
	private SqlSession sqlSession;

	// ]]myBatis物件

	// #[[ 建置用Method
	public Dao() throws IOException {
		Resources.setCharset(Charset.forName("UTF-8"));
		try {
			reader = Resources.getResourceAsReader(GlobalConfig.MyBatisConfig_XML_Path);
		} catch (Exception e) {
			// 若匯出後要吃這個路徑
			reader = Resources.getResourceAsReader(GlobalConfig.MyBatisConfig_XML_Path_Output);
		}
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, "DaoEnvironment");
		sqlSession = sqlSessionFactory.openSession(false);// autocommit = false
		daoConnector = sqlSession.getMapper(DaoConnector.class);
	}

	public static Dao getInstance() throws IOException {
		if (instance == null) {
			instance = new Dao();
		}
		return instance;
	}
	// ]]

	// #[[ 指令用Method
	public void creatTable() {
		daoConnector.creatDemoTable();
		sqlSession.commit();
	}

	public void insertData() {
		daoConnector.insertData();
		sqlSession.commit();
	}

	public List<DataModel> getData() {
		return daoConnector.getData();
	}
	// ]]
}
