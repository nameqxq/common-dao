package com.quxiqi.common.dao.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.quxiqi.common.dao.bean.CompileResultMap;
import com.quxiqi.common.dao.bean.Mapper;
import com.quxiqi.common.dao.bean.MapperMethod;
import com.quxiqi.common.dao.exception.CommonDaoException;

public class XMLParser {
	
	private Configrution conf;
	XMLParser(Configrution conf){
		this.conf = conf;
	}
	@SuppressWarnings("unchecked")
	public Mapper parserXML(String path) throws CommonDaoException{
		
		SAXReader reader = new SAXReader();
		Document document = null;
		try(FileInputStream is = new FileInputStream(Configrution.CLASS_PATH+"/"+path);){
			document = reader.read(is);  
		}catch (IOException | DocumentException e) {
			throw new CommonDaoException("xml 解析错误===>"+e.getMessage(),e.getCause());
		}
		Element rootElement = document.getRootElement();
		String rootName = rootElement.getName();
		if(!"mapper".equals(rootName))
			throw new CommonDaoException("xml 不符合规定！");
		String mapperName = rootElement.attribute("namespace").getValue();
		
		Mapper mapper = new Mapper();
		mapper.setXmlPath(path);
		mapper.setMapperName(mapperName);
		
		//保存所有select标签元素，以供最后处理
		List<Element> selectElement = new LinkedList<>();
		
		//保存resultMap标签的 <id,type>
		Map<String,String> resultMaps = new HashMap<>();
		Iterator<Element> iterator = rootElement.elementIterator();
		while(iterator.hasNext()){
			Element ele = iterator.next();
			String eleName = ele.getName();
			switch(eleName){
				case "resultMap":{
					String resultType = ele.attribute("type").getValue();
					String realType = conf.getAliases().getOrDefault(resultType, resultType);
					resultMaps.put(ele.attribute("id").getValue(),realType);
					break;
				}
				case "select":{
					selectElement.add(ele);
					break;
				}
				case "insert":{
					SIUDHandler(ele,mapper,false);
					break;
				}
				case "update":{
					SIUDHandler(ele,mapper,false);
					break;
				}
				case "delete":{
					SIUDHandler(ele,mapper,false);
					break;
				}
				default : break;
			}
		}
		//对select标签的处理必须在处理完所有result标签之后
		for(Element ele:selectElement)
			SIUDHandler(ele,mapper,true,resultMaps);
		
		return mapper;
	}
	
	private MapperMethod SIUDHandler(Element ele,Mapper mapper,boolean isSelect){
		String methodName = ele.attribute("id").getValue();
		Attribute apType = ele.attribute("parameterType");
		String parameterType = null;
		if(apType!=null)
			parameterType = apType.getValue();
		
		List<String> params = null;
		if(parameterType!=null){
			String realParamType = conf.getAliases().getOrDefault(parameterType.toLowerCase(), parameterType);
			params = new LinkedList<>();
			params.add(realParamType);
		}
		MapperMethod method = new MapperMethod().setMethodName(methodName).setParams(params);
		if(isSelect){
			return method;
		}else{
			mapper.putMethod(method.setResult("void"));
			return method;
		}
	}
	
	private MapperMethod SIUDHandler(Element ele,Mapper mapper,boolean isSelect,Map<String,String> resultMaps){
		MapperMethod method = SIUDHandler(ele,mapper,true);
		
		Attribute rMap = ele.attribute("resultMap");
		Attribute rType = ele.attribute("resultType");
		String resultStr = "void";
		if(rMap!=null)
			if(CompileResultMap.AUTO_MATCHING == conf.COMPILE_RESULTMAP){
				String id = ele.attribute("id").getValue();
				if(id.toLowerCase().contains("list"))
					resultStr = "list";
				else
					resultStr = rMap.getValue();
			}else if(CompileResultMap.ALWAYS_LIST == conf.COMPILE_RESULTMAP)
				resultStr = "list";
			else
				resultStr = rMap.getValue();
		else if(rType!=null)
			resultStr = rType.getValue();
		
		//获取返回类型的全类名,别名全由小写储存
		String realResultType = resultMaps.getOrDefault(resultStr, resultStr);
		String realResult = conf.getAliases().getOrDefault(realResultType.toLowerCase(), realResultType);
		
		mapper.putMethod(method.setResult(realResult));
		return method;
	}
}
