package com.wavegis.engin.ws.taoyuan;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.wavegis.engin.AnalysisEngin;

public class ConvertToBeanAnalysisEngin implements AnalysisEngin<DynaBean>{
	BasicDynaClass dynaClass = null;
	DynaBean dynaBean = null;
	DynaProperty[] dynaPropertys = null;
	String rootName = null;
	
	public ConvertToBeanAnalysisEngin(){
		dynaPropertys = new DynaProperty[]{
			new DynaProperty("DATATIME", Date.class)
			, new DynaProperty("LON", Double.class)
			, new DynaProperty("LAT", Double.class)
			, new DynaProperty("STATION", String.class)
			, new DynaProperty("STATION_ID", String.class)
			, new DynaProperty("TOWN", String.class)
			, new DynaProperty("REVETMENT_M", Double.class)
			, new DynaProperty("WATERHEIGHT_M", Double.class)
			, new DynaProperty("PHOTO_PATH", String.class)
			, new DynaProperty("STREAM_HTML", String.class)
			, new DynaProperty("STATUS", String.class)
		};
		//dynaClass = new BasicDynaClass();
		dynaClass = new BasicDynaClass("WaterLevel", null, dynaPropertys);
	}
	
	@Override
	public List<DynaBean> analysisOriginalData(String originalMessage){
		List<DynaBean> beanList = null;
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(originalMessage)));
			
			if(doc.hasChildNodes()){
				beanList = new ArrayList<DynaBean>();
				dynaBean = dynaClass.newInstance();
				rootName = doc.getFirstChild().getNodeName();
				
				getNodeData(beanList, doc.getChildNodes(), "");
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return beanList;
	}
	
	private void getNodeData(List<DynaBean> beanList, NodeList nodeList, String lastNodeName){
		if(nodeList == null || nodeList.getLength() == 0){
			return ;
		}
		int nodeListLeangth = nodeList.getLength();
		
		for(int i = 0 ; i < nodeListLeangth ; i++){
			Node node = nodeList.item(i);
			
			if(node.getNodeType() == Node.ELEMENT_NODE){
				if(node.hasChildNodes()){
					String nodeName = node.getNodeName();
					
					getNodeData(beanList, node.getChildNodes(), node.getLastChild().getNodeName());
					
					//	每個分群第一個有資料的節點
					if(node.getPreviousSibling() == null){
						try {
							dynaBean = dynaClass.newInstance();
						} catch(IllegalAccessException e){
							e.printStackTrace();
						} catch(InstantiationException e){
							e.printStackTrace();
						}
					}
					if(!rootName.equals(node.getParentNode().getNodeName())){
						if(lastNodeName.isEmpty()){
							continue;
						}
						String nodeText = node.getTextContent();
						Object nodeValue = nodeText;
						
						switch(nodeName){
							case "DATATIME":
								try {
									nodeValue = new SimpleDateFormat("yyyy/MM/dd a hh:mm:ss", Locale.TAIWAN).parse(nodeText);
								} catch(ParseException e){
									e.printStackTrace();
								}
								break;
							case "LON":
								nodeValue = Double.parseDouble(nodeText);
								break;
							case "LAT":
								nodeValue = Double.parseDouble(nodeText);
								break;
							case "REVETMENT_M":
								nodeValue = Double.parseDouble(nodeText);
								break;
							case "WATERHEIGHT_M":
								nodeValue = Double.parseDouble(nodeText);
								break;
							default:
								break;
						}
						dynaBean.set(nodeName, nodeValue);
						
						if(nodeName.equals(lastNodeName)){
							beanList.add(dynaBean);
						}
					}
				}
			}
		}
	}
}
