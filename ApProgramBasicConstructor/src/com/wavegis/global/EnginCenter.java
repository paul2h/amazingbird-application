package com.wavegis.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.wavegis.engin.prototype.Engin;
import com.wavegis.engin.prototype.EnginView;

public class EnginCenter {

	/** 所有的Engin清單(增減Engin改這個就好 View會自己變) */
	private static final Engin[] ALL_Engins = {
			new AlertAnalysisEngin()
			, new CCTVEngin()
			, new CWBTyphoonTextEngin()
			, new CWBDataFileGetEngin()
			, new CenterWSEngin()
			, new DBConfigEngin()
			, new FakeImageEngin()
			, new FakeGpsCarUpdateEngin()
			, new FakeGpsCarInsertEngin()
			, new FTPFileTransEngin()
			, new HttpImageGetEngin()
			, new HsinchuCityWebServiceEngin()
			, new HsinchuWebServiceEngin()
			, new ImageEngin()
			, new ImageTransServerEngin()
			, new ImageTransClientEngin()
			, new KenkulLORAReceiveEngin()
			, new MaoliWebSeriveEngin()
			, new MailSendEngin()
			, new PokerDBEngin()
			, new QpesumsReadEngin()
			, new TaoyuanWebServiceEngin()
			, new TyphoonImageEngin()
			, new RainDataInsertEngin()
			, new RawDataInsertEngin()
			, new RawDataReceiveEngin()
			, new RawDataSendEngin()
			, new ScreemSaveEngin()
			, new SensorReceivingEngin()
			, new SMSSendEngin()
			, new WavegisWSEngin()
			, new WebMonitorEngin()
			, new WaterDataInsertEngin()
	};
	

	/**
	 * 設定要使用的Engin
	 * */
	private static final Set<String> standbyEnginIDs = EnginListSetting.standbyEnginIDs_Wra02;//FIXME 更換縣市需設定
	
	@SuppressWarnings("serial")
	public static final ArrayList<Engin> Engins = new ArrayList<Engin>(){
		{
			for(Engin engin : ALL_Engins){
				if (standbyEnginIDs.contains(engin.getEnginID())) {
					add( engin);
				}				
			}
		}
	};

	@SuppressWarnings("serial")
	private static final HashMap<String, EnginView> EnginViewMap = new HashMap<String, EnginView>() {
		{
			for (Engin engin : Engins) {
				if (standbyEnginIDs.contains(engin.getEnginID())) {
					put(engin.getEnginID(), engin.getEnginView());
				}
			}
		}
	};

	@SuppressWarnings("serial")
	public static final List<EnginView> EnginViews = new ArrayList<EnginView>(){
		{
			addAll(EnginViewMap.values());
		}
	};
}
