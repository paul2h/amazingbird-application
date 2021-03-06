package com.wavegis.engin.db.data_check;

import java.io.IOException;
import java.util.List;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.model.CheckDBData;

public class PokerDBEngin extends TimerEngin {

	public static final String enginID = "DataPokeEngin";
	private static final String enginName = "水情資料介接監控Engin";
	private static final PokerDBEnginView enginView = new PokerDBEnginView();
	private PokerDao dao = null;

	public PokerDBEngin() {
		setTimeout(5000);
	}

	@Override
	public String getEnginID() {
		return enginID;
	}

	@Override
	public String getEnginName() {
		return enginName;
	}

	@Override
	public EnginView getEnginView() {
		return enginView;
	}

	@Override
	public void timerAction() {
		if(dao == null){
			boolean initDAOSuccess = initDao();
			if(!initDAOSuccess){
				stopEngin();
				return;
			}
		}
		
		List<CheckDBData> lists = dao.getAlertData();
		for (CheckDBData data : lists) {
			showMessage("stid : " + data.getColumn_value() + ", update_time : "
					+ data.getUpdate_time());
			//TODO
		}
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);

	}

	private boolean initDao() {
		boolean isSuccess = false;
		try {
			dao = new PokerDao();
			isSuccess = true;
		} catch (IOException e) {
			showMessage("DAO初始化失敗");
			e.printStackTrace();
			isSuccess = false;
		}
		return isSuccess;
	}

}
