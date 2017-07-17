package com.wavegis.global;

import java.util.HashSet;
import java.util.Set;

import com.wavegis.engin.connection.ftp.FTPFileTransEngin;
import com.wavegis.engin.connection.tcp.nio_socket.kenkul.KenkulLORAReceiveEngin;
import com.wavegis.engin.connection.tcp.nio_socket.raw_data_trans.RawDataReceiveEngin;
import com.wavegis.engin.connection.tcp.nio_socket.raw_data_trans.RawDataSendEngin;
import com.wavegis.engin.connection.tcp.socket.jian_hua.SensorReceivingEngin;
import com.wavegis.engin.connection.web_check.WebMonitorEngin;
import com.wavegis.engin.connection.ws.cwb.CWBDataFileGetEngin;
import com.wavegis.engin.connection.ws.data_analysis.CWBTyphoonTextEngin;
import com.wavegis.engin.connection.ws.others.hsinchu.HsinchuWebServiceEngin;
import com.wavegis.engin.connection.ws.others.hsinchu.city.HsinchuCityWebServiceEngin;
import com.wavegis.engin.connection.ws.others.maoli.MaoliWebSeriveEngin;
import com.wavegis.engin.connection.ws.others.taoyuan.TaoyuanWebServiceEngin;
import com.wavegis.engin.connection.ws.soap.center.CenterWSEngin;
import com.wavegis.engin.connection.ws.soap.wavegis.WavegisWSEngin;
import com.wavegis.engin.db.alert_check.AlertAnalysisEngin;
import com.wavegis.engin.db.data_check.PokerDBEngin;
import com.wavegis.engin.db.fake.gps_insert.FakeGpsCarInsertEngin;
import com.wavegis.engin.db.fake.gps_update.FakeGpsCarUpdateEngin;
import com.wavegis.engin.db.insert.rain.RainDataInsertEngin;
import com.wavegis.engin.db.insert.raw.RawDataInsertEngin;
import com.wavegis.engin.db.insert.water.WaterDataInsertEngin;
import com.wavegis.engin.db.read_conf.DBConfigEngin;
import com.wavegis.engin.image.cctv.fake_image.FakeImageEngin;
import com.wavegis.engin.image.cctv.from_fold.ImageEngin;
import com.wavegis.engin.image.cctv.mjpeg.CCTVEngin;
import com.wavegis.engin.image.cctv.screem_save.ScreemSaveEngin;
import com.wavegis.engin.image.http_image.HttpImageGetEngin;
import com.wavegis.engin.image.http_image_typhoon.TyphoonImageEngin;
import com.wavegis.engin.image.qpesums.QpesumsReadEngin;
import com.wavegis.engin.image.trans.ImageTransClientEngin;
import com.wavegis.engin.image.trans.ImageTransServerEngin;
import com.wavegis.engin.notification.gmail.MailSendEngin;
import com.wavegis.engin.notification.sms.SMSSendEngin;

public class EnginListSetting {
	
	@SuppressWarnings("serial")
	public static final Set<String> standbyEnginIDs_ALL = new HashSet<String>() {
		{
			add(AlertAnalysisEngin.enginID);
			add(CCTVEngin.enginID);
			add(CWBTyphoonTextEngin.enginID);
			add(CWBDataFileGetEngin.enginID);
			add(CenterWSEngin.enginID);
			add(DBConfigEngin.enginID);
			add(FakeImageEngin.enginID);
			add(FakeGpsCarUpdateEngin.enginID);
			add(HttpImageGetEngin.enginID);
			add(ImageEngin.enginID);
			add(ImageTransServerEngin.enginID);
			add(ImageTransClientEngin.enginID);
			add(KenkulLORAReceiveEngin.enginID);
			add(MailSendEngin.enginID);
			add(PokerDBEngin.enginID);
			add(QpesumsReadEngin.enginID);
			add(RainDataInsertEngin.enginID);
			add(RawDataInsertEngin.enginID);
			add(RawDataReceiveEngin.enginID);
			add(RawDataSendEngin.enginID);
			add(ScreemSaveEngin.enginID);
			add(SensorReceivingEngin.enginID);
			add(SMSSendEngin.enginID);
			add(WavegisWSEngin.enginID);
			add(WebMonitorEngin.enginID);
			add(WaterDataInsertEngin.enginID);
		}
	};
	
	@SuppressWarnings("serial")
	public static final Set<String> standbyEnginIDs_ChiayiCounty = new HashSet<String>() {
		{
			add(FakeGpsCarUpdateEngin.enginID);
			add(FakeGpsCarInsertEngin.enginID);
		}
	};

	@SuppressWarnings("serial")
	public static final Set<String> standbyEnginIDs_ChiayiCity = new HashSet<String>() {
		{
			add(AlertAnalysisEngin.enginID);
			add(CCTVEngin.enginID);
			add(DBConfigEngin.enginID);
			add(ImageEngin.enginID);
			add(QpesumsReadEngin.enginID);
			add(WaterDataInsertEngin.enginID);
			add(SMSSendEngin.enginID);
			add(SensorReceivingEngin.enginID);
		}
	};

	@SuppressWarnings("serial")
	public static final Set<String> standbyEnginIDs_Keelung = new HashSet<String>() {
		{
			add(CCTVEngin.enginID);
			add(DBConfigEngin.enginID);
		}
	};

	@SuppressWarnings("serial")
	public static final Set<String> standbyEnginIDs_WebMonitor = new HashSet<String>() {
		{
			add(WebMonitorEngin.enginID);
			add(MailSendEngin.enginID);
			add(DBConfigEngin.enginID);
		}
	};
	
	@SuppressWarnings("serial")
	public static final Set<String> standbyEnginIDs_Wra02 = new HashSet<String>() {
		{
			add(AlertAnalysisEngin.enginID);// TODO
			add(CCTVEngin.enginID);
			add(DBConfigEngin.enginID);
			add(FTPFileTransEngin.enginID);
			add(HsinchuCityWebServiceEngin.enginID);// FIXME
			add(HsinchuWebServiceEngin.enginID);
			add(MaoliWebSeriveEngin.enginID);// TODO
			add(RainDataInsertEngin.enginID);
			add(RawDataInsertEngin.enginID);
			add(SMSSendEngin.enginID);
			add(TaoyuanWebServiceEngin.enginID);
			add(TyphoonImageEngin.enginID);//TODO
		}
	};

	
	@SuppressWarnings("serial")
	public static final Set<String> standbyEnginIDs_Yilan = new HashSet<String>() {
		{
			add(CCTVEngin.enginID);
			add(DBConfigEngin.enginID);
		}
	};


}
