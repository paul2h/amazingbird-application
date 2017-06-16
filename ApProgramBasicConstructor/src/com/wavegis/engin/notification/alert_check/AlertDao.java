package com.wavegis.engin.notification.alert_check;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.wavegis.global.GlobalConfig;
import com.wavegis.model.SMSAlertData;

/**
 * 給Service取得Dao的Dao總管理物件
 * 
 * <pre>
 * 使用方法:mybatis
 */
public class AlertDao {
	static AlertDao instance;

	/* 取得config檔的reader */
	private Reader reader;

	// #[[DaoConnector List
	private AlertDaoConnector daoConnector;
	// ]] Dao List */

	// #[[ myBatis物件
	private SqlSessionFactory sqlSessionFactory;
	private SqlSession sqlSession;

	// ]]myBatis物件

	// #[[ 建置用Method
	public AlertDao() throws IOException {
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
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, "AlertDaoEnvironment");
		sqlSession = sqlSessionFactory.openSession(false);// autocommit = false
		daoConnector = sqlSession.getMapper(AlertDaoConnector.class);
	}

	public static AlertDao getInstance() throws IOException {
		if (instance == null) {
			instance = new AlertDao();
		}
		return instance;
	}
	// ]]

	// #[[ 指令用Method

	public List<SMSAlertData> getAlertData() {
		sqlSession.clearCache();// 要加這個才會清除暫存
		return daoConnector.getData();
	}
	// ]]
}
