package lnstark.lbatis.core.mapper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import lnstark.lbatis.exception.MapperParseException;
import lnstark.lbatis.exception.SqlSessionException;
import lnstark.lbatis.util.Validator;

public class MapperResolver {

	private String xmlMapperPath;
	
	private Document document = null;
	
	private Map<String, MapperNode> nodes;
	
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
		File xmlMapper = new File(xmlMapperPath);
		if(!xmlMapper.exists())
			throw new SqlSessionException(xmlMapperPath + " not found!");
		Document document = null;
		SAXReader reader = new SAXReader();
		try {
			document = reader.read(xmlMapper);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}

	public String getSql(String methodName, Object[] args) {
		String content = nodes.get(methodName).content;
		return content;
		
	}
	
	/**
	 * parse mapper.xml
	 * @throws ClassNotFoundException 
	 */
	public void parseMapper() throws ClassNotFoundException {
		Element root = document.getRootElement();
		for (Element e : root.elements()) {
			String id = e.attributeValue("id");
			if (Validator.isNull(id))
				throw new MapperParseException("id should not be empty(occured while parsing " + xmlMapperPath + ").");
			String parameterType = e.attributeValue("parameterType");
			String resultType = e.attributeValue("resultType");
			MapperNode node = new MapperNode(id);
			if (!Validator.isNull(parameterType)) {
				Class<?> c = Class.forName(parameterType);
				node.parameterType = c;
			}
			if (!Validator.isNull(resultType)) {
				Class<?> c = Class.forName(resultType);
				node.resultType = c;
			}
			
			nodes.put(id, node);
		}
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
