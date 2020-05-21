package lnstark.lbatis.core.mapper;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import lnstark.lbatis.core.exception.SqlParseException;

import lnstark.lbatis.core.exception.MapperParseException;
import lnstark.lbatis.core.exception.SqlSessionException;
import lnstark.lbatis.core.util.LLog;
import lnstark.lbatis.core.util.Validator;

/**
 * 
 * 
 * @author 	Lnstark   
 * @since 	1.0
 * @date 	2020年5月13日
 */
public class MapperResolver {

	private String xmlMapperPath;
	
	private Document document = null;
	
	private Map<String, MapperNode> nodes;
	
	private LLog log = LLog.getInstace(MapperResolver.class);
	
	private static String[] crud = {"select", "delete", "update", "insert"};
	
	public MapperResolver(String xmlMapperPath) {
		this.xmlMapperPath = xmlMapperPath;
		nodes = new HashMap<>();
		
		this.document = setDocument(xmlMapperPath);
		try {
			parseMapper();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private Document setDocument(String xmlMapperPath) {
		try {
			xmlMapperPath = URLDecoder.decode(xmlMapperPath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		File xmlMapper = new File(xmlMapperPath);
		if(!xmlMapper.exists())
			throw new SqlSessionException(xmlMapperPath + " not found!");
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = null;
		Document document = null;
//		SAXReader reader = new SAXReader();
		try {
			documentBuilder = factory.newDocumentBuilder();
			document = documentBuilder.parse(xmlMapper);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return document;
	}

	public String getSql(String methodName, Object[] args) {
		MapperNode mn = nodes.get(methodName);
		if (mn == null)
			throw new MapperParseException("sql \"" + methodName + "\" not found in " + xmlMapperPath);
		String content = mn.content;
		return content;
		
	}
	
	/**
	 * parse mapper.xml
	 * @throws ClassNotFoundException 
	 */
	public void parseMapper() throws ClassNotFoundException {
		
		Element root = document.getDocumentElement();
		log.debug("根元素：" + root.getNodeName());
		NodeList nl = root.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (!isCRUDNode(n))
				continue;
			
			String id = getNodeAttr(n, "id");
			if (Validator.isNull(id))
				throw new MapperParseException("id should not be empty(occured while parsing " + xmlMapperPath + ").");
			String parameterType = getNodeAttr(n, "parameterType");
			String resultType = getNodeAttr(n, "resultType");
			MapperNode node = new MapperNode(id);
			
			if (!Validator.isNull(parameterType)) {
				Class<?> c = Class.forName(parameterType);
				node.parameterType = c;
			}
			
			if (!Validator.isNull(resultType)) {
				Class<?> c = Class.forName(resultType);
				node.resultType = c;
			}
			
			node.content = parseContent(n);
			nodes.put(id, node);
		}
		
	}
	
	/**
	 * parse sql node
	 */
	private String parseContent(Node n) {
		NodeList children = n.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node c = children.item(i);
			// analyze if
			if ("if".equals(c.getNodeName())) {
				String statement = getNodeAttr(c, "test");
				boolean b = parseIFStatement(statement);
			}
		}
		return n.getTextContent();
	}

	private static boolean parseIFStatement(String statement) {
//		str.
		return false;
	}

	public static void main(String[] args) {
		String str = " 1 == 1 and (true or false)";
		System.out.println(parseIFStatement(str));
	}
	
	private String getNodeAttr(Node n, String attr) {
		Node attrNode = n.getAttributes().getNamedItem(attr);
		return attrNode == null ? null : attrNode.getNodeValue();
	}
	
	private boolean isCRUDNode(Node n) {
		String name = n.getNodeName();
		for (String s : crud)
			if (name.contains(s))
				return true;
		return false;
	}
	
	class MapperNode {
		
		String id;
		Class<?> parameterType 	= Object.class;
		Class<?> resultType 	= Object.class;
		String content;
		
		public MapperNode(String id) {
			super();
			this.id = id;
		}
		
	}
	
}
