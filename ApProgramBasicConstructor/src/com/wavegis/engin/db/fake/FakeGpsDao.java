package com.wavegis.engin.db.fake;

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
public class FakeGpsDao {
	static FakeGpsDao instance;

	/* 取得config檔的reader */
	private Reader reader;

	// #[[DaoConnector List
	private FakeGpsDaoConnector daoConnector;
	// ]] Dao List */

	// #[[ myBatis物件
	private SqlSessionFactory sqlSessionFactory;
	private SqlSession sqlSession;

	// ]]myBatis物件

	// #[[ 建置用Method
	public FakeGpsDao() {
		Resources.setCharset(Charset.forName("UTF-8"));
		try {
			reader = Resources.getResourceAsReader(GlobalConfig.MyBatisConfig_XML_Path);
		} catch (Exception e) {
			// 若匯出後要吃這個路徑
			try {
				reader = Resources.getResourceAsReader(GlobalConfig.MyBatisConfig_XML_Path_Output);
			} catch (IOException e1) {
				System.out.println("DAO設定檔取得錯誤!!");
				e1.printStackTrace();
			}
		}
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, "FakeDataDaoEnvironment");
		sqlSession = sqlSessionFactory.openSession(false);// autocommit = false
		daoConnector = sqlSession.getMapper(FakeGpsDaoConnector.class);
		instance = this;
	}

	public static FakeGpsDao getInstance() {
		if (instance == null) {
			instance = new FakeGpsDao();
		}
		return instance;
	}
	// ]]

	// #[[ 指令用Method

	public void updateGpsData() {
		daoConnector.updateGpsData();
		sqlSession.commit();
	}

	// ]]
}
