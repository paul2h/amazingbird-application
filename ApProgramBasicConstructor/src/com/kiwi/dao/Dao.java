package com.kiwi.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.kiwi.model.DataModel;

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
		reader = Resources.getResourceAsReader("./myBatisConfig.xml");// Reader會自動關掉 若有多個Connector要重複讀取
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, "DaoEnvironment");
		sqlSession = sqlSessionFactory.openSession();
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
