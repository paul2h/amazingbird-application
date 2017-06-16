package com.wavegis.engin.db.data_check;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.wavegis.global.GlobalConfig;
import com.wavegis.model.CheckDBData;

/**
 * 給Service取得Dao的Dao總管理物件
 * 
 * <pre>
 * 使用方法:mybatis
 */
public class PokerDao {
	static PokerDao instance;

	/* 取得config檔的reader */
	private Reader reader;

	// #[[DaoConnector List
	private PokerDaoConnector daoConnector;
	// ]] Dao List */

	// #[[ myBatis物件
	private SqlSessionFactory sqlSessionFactory;
	private SqlSession sqlSession;

	// ]]myBatis物件

	// #[[ 建置用Method
	public PokerDao() throws IOException {	
		Resources.setCharset(Charset.forName("UTF-8"));
		try {
			reader = Resources.getResourceAsReader(GlobalConfig.MyBatisConfig_XML_Path);
		} catch (Exception e) {
			// 若匯出後要吃這個路徑
			try {
				reader = Resources.getResourceAsReader(GlobalConfig.MyBatisConfig_XML_Path_Output);
			} catch (IOException e1) {
				e1.printStackTrace();
				System.out.println("DAO初始化錯誤!!");
			}
		}
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, "PokerDaoEnvironment");
		sqlSession = sqlSessionFactory.openSession(false);// autocommit = false
		daoConnector = sqlSession.getMapper(PokerDaoConnector.class);
	}

	public static PokerDao getInstance() throws IOException {
		if (instance == null) {
			instance = new PokerDao();
		}
		return instance;
	}
	// ]]

	// #[[ 指令用Method

	public List<CheckDBData> getAlertData() {
		sqlSession.clearCache();// 要加這個才會清除暫存
		return daoConnector.getData();
	}
	// ]]
}
