package com.wavegis.engin.ws;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.wavegis.engin.AnalysisEngin;

public class ConvertXMLAnalysisEngin implements AnalysisEngin<DynaBean>{
	HashMap<String, Object> parentMap = new HashMap<String, Object>();
	BasicDynaClass dynaClass = null;
	DynaBean dynaBean = null;
	
	public ConvertXMLAnalysisEngin(String dynaName, DynaProperty[] dynaPropertys){
		dynaClass = new BasicDynaClass(dynaName, null, dynaPropertys);
	}
	
	@Override
	public List<DynaBean> analysisOriginalData(String originalMessage){
		List<DynaBean> beanList = null;
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(originalMessage)));
			
			if(doc.hasChildNodes()){
				beanList = new ArrayList<DynaBean>();
				dynaBean = dynaClass.newInstance();
				
				getNodeData(beanList, doc.getChildNodes());
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return beanList;
	}
	
	private void getNodeData(List<DynaBean> beanList, NodeList nodeList){
		if(nodeList == null || nodeList.getLength() == 0){
			return ;
		}
		int nodeListLeangth = nodeList.getLength();
		
		for(int i = 0 ; i < nodeListLeangth ; i++){
			Node node = nodeList.item(i);
			
			if(node.getNodeType() == Node.ELEMENT_NODE){
				String nodeName = node.getNodeName();
				String parentName = node.getParentNode().getNodeName();
				
				//	每個分群第一個節點, 記錄上層節點
				if(node.getPreviousSibling() == null){
					parentMap.put(parentName, "Y");
				}
				if(node.hasChildNodes()){
					getNodeData(beanList, node.getChildNodes());
					
					Node childNode = node.getFirstChild();
					
					if(childNode.getNodeType() == Node.TEXT_NODE){
						String nodeText = node.getTextContent().trim();
						
						dynaBean.set(nodeName, nodeText);
					}
				}
				//	每個分群最後一個節點, 並不為上層節點群
				if(node.getNextSibling() == null && !parentMap.containsKey(nodeName)){
					beanList.add(dynaBean);
					
					try {
						dynaBean = dynaClass.newInstance();
					} catch(IllegalAccessException e){
						e.printStackTrace();
					} catch(InstantiationException e){
						e.printStackTrace();
					}
				}
			}
		}
	}
}
