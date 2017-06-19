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
import com.wavegis.model.WebMonitorFocusData;
import com.wavegis.model.WaterData;

/**
 * 給Service取得Dao的Dao總管理物件
 * 
 * <pre>
 * 使用方法:mybatis
 */
public class DBConfigDao {
	static DBConfigDao instance;

	/* 取得config檔的reader */
	private Reader reader;

	// #[[DaoConnector List
	private DBConfigDaoConnector daoConnector;
	private ProcalDBConfigDaoConnector procalDaoConnector;
	// ]] Dao List */

	// #[[ myBatis物件
	private SqlSessionFactory sqlSessionFactory;
	private SqlSession sqlSession;
	private SqlSessionFactory procalSqlSessionFactory;
	private SqlSession procalSqlSession;

	// ]]myBatis物件

	// #[[ 建置用Method
	public DBConfigDao() {
		Resources.setCharset(Charset.forName("UTF-8"));
		// locator4 DB
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
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, "PokerDaoEnvironment");
		sqlSession = sqlSessionFactory.openSession(false);// autocommit = false
		daoConnector = sqlSession.getMapper(DBConfigDaoConnector.class);

		// procal DB
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
		procalSqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, "ProcalConfigDaoEnvironment");
		procalSqlSession = procalSqlSessionFactory.openSession(false);
		procalDaoConnector = procalSqlSession.getMapper(ProcalDBConfigDaoConnector.class);
		instance = this;
	}

	public static DBConfigDao getInstance() {
		if (instance == null) {
			instance = new DBConfigDao();
		}
		return instance;
	}
	// ]]

	// #[[ 指令用Method
	public List<CCTVData> getCCTVData() {
		sqlSession.commit();// 清掉暫存
		return daoConnector.getCCTVData();
	}

	public List<WaterData> getRiverBottomDatas() {
		sqlSession.clearCache();// 清掉暫存
		return procalDaoConnector.getStationBottomDatas();
	}
	
	public List<WebMonitorFocusData> getWebFocusDatas(){
		sqlSession.clearCache();
		return daoConnector.getWebFocusData();
	}
	// ]]
}
