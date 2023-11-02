package com.qnkj.common.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;


public class XmlReader {
    private Document doc;
    public XmlReader(String xml) throws Exception {
        doc =  parseDocumentByString(xml);
    }
    //String Xml 转 Dom节点
    public static Document parseDocumentByString(String xmlContent) throws SAXException,
            IOException, Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);// 否则无法识别namespace
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        return factory.newDocumentBuilder().parse(new InputSource(new StringReader(xmlContent)));
    }

    public Map<String,String> getMap(String key) {
        HashMap<String,String> map = new HashMap();
        Node head  = doc.getElementsByTagName(key).item(0);
        NodeList childNodes = head.getChildNodes();
        for (int i=0;i<childNodes.getLength();i++){
            if(childNodes.item(i).getNodeType() == Node.ELEMENT_NODE){
                map.put(childNodes.item(i).getNodeName(),childNodes.item(i).getTextContent());
            }
        }
        return map;
    }
    public boolean existKey(String key) {
        NodeList nodes = doc.getElementsByTagName(key);
        if (nodes.getLength() > 0) {
            return true;
        }
        return false;
    }

    public String get(String key) {
        NodeList nodes = doc.getElementsByTagName(key);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return null;
    }

}
