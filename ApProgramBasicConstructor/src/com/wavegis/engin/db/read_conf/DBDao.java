package com.wavegis.engin.db.read_conf;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.wavegis.global.GlobalConfig;
import com.wavegis.model.CCTVData;

/**
 * 給Service取得Dao的Dao總管理物件
 * 
 * <pre>
 * 使用方法:mybatis
 */
public class DBDao {
	static DBDao instance;

	/* 取得config檔的reader */
	private Reader reader;

	// #[[DaoConnector List
	private DBDaoConnector daoConnector;
	// ]] Dao List */

	// #[[ myBatis物件
	private SqlSessionFactory sqlSessionFactory;
	private SqlSession sqlSession;

	// ]]myBatis物件

	// #[[ 建置用Method
	public DBDao() {
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
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, "ConfigDaoEnvironment");
		sqlSession = sqlSessionFactory.openSession(false);// autocommit = false
		daoConnector = sqlSession.getMapper(DBDaoConnector.class);
		instance = this;
	}

	public static DBDao getInstance() {
		if (instance == null) {
			instance = new DBDao();
		}
		return instance;
	}
	// ]]

	// #[[ 指令用Method
	public List<CCTVData> getCCTVData(){
		sqlSession.commit();
		return daoConnector.getCCTVData();
	}
	// ]]
}
