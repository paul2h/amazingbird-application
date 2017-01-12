package com.wavegis.engin.db.insert.water;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.wavegis.global.GlobalConfig;
import com.wavegis.model.RainData;
import com.wavegis.model.WaterData;

/**
 * 給Service取得Dao的Dao總管理物件
 * 
 * <pre>
 * 使用方法:mybatis
 */
public class WaterDataDao {
	static WaterDataDao instance;

	/* 取得config檔的reader */
	private Reader reader;

	// #[[DaoConnector List
	private WaterDataDaoConnector daoConnector;
	// ]] Dao List */

	// #[[ myBatis物件
	private SqlSessionFactory sqlSessionFactory;
	private SqlSession sqlSession;

	// ]]myBatis物件

	// #[[ 建置用Method
	public WaterDataDao() {
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
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, "InsertDaoEnvironment");
		sqlSession = sqlSessionFactory.openSession(false);// autocommit = false
		daoConnector = sqlSession.getMapper(WaterDataDaoConnector.class);
		instance = this;
	}

	public static WaterDataDao getInstance() {
		if (instance == null) {
			instance = new WaterDataDao();
		}
		return instance;
	}
	// ]]

	// #[[ 指令用Method
	public int insertRainData(List<RainData> rainData) {
		int result = daoConnector.insertRainData(rainData);
		sqlSession.commit();
		
		return result;
	}
	
	public int insertWaterData(List<WaterData> waterData) {
		int result = daoConnector.insertWaterData(waterData);
		sqlSession.commit();
		
		return result;
	}
	// ]]
}
