package com.wavegis.basic_construction;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.wavegis.global.GlobalConfig;

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
	public Dao() {
		Resources.setCharset(Charset.forName("UTF-8"));
		try {
			reader = Resources.getResourceAsReader(GlobalConfig.MyBatisConfig_XML_Path);
		} catch (Exception e) {
			// 若匯出後要吃這個路徑
			try {
				reader = Resources.getResourceAsReader(GlobalConfig.MyBatisConfig_XML_Path_Output);
			} catch (IOException e1) {
				e1.printStackTrace();
				System.out.println("DAO初始化失敗!!");
			}
		}
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, "DaoEnvironment");
		sqlSession = sqlSessionFactory.openSession(false);// autocommit = false
		daoConnector = sqlSession.getMapper(DaoConnector.class);
	}

	public static Dao getInstance() {
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

	// ]]
}
