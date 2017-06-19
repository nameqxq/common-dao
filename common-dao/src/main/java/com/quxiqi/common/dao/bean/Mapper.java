package com.quxiqi.common.dao.bean;

import java.util.LinkedList;
import java.util.List;

public class Mapper {
	//mapper接口全类名（namespace）
	private String mapperName;
	//xml路径
	private String xmlPath;
	//mapper简单类名
	private String simpleName;
	private List<MapperMethod> methods = new LinkedList<>();
	public String getMapperName() {
		return mapperName;
	}
	public void setMapperName(String mapperName) {
		this.mapperName = mapperName;
		simpleName = mapperName.substring(mapperName.lastIndexOf('.')+1);
	}
	public String getSimpleName(){
		return simpleName;
	}
	public String getXmlPath() {
		return xmlPath;
	}
	public void setXmlPath(String xmlPath) {
		this.xmlPath = xmlPath;
	}
	public void putMethod(MapperMethod mm){
		methods.add(mm);
	}
	public void putMethod(String methodName,List<String> params,String result){
		methods.add(new MapperMethod(methodName, params, result));
	}
	public List<MapperMethod> getMethods(){
		return methods;
	}
	@Override
	public String toString() {
		int index = mapperName.lastIndexOf('.');
		String packageStr = mapperName.substring(0, index);
		String SimpleClassName = mapperName.substring(index+1);
		String enter = "\r\n";
		StringBuilder str = new StringBuilder("package "); 
		str.append(packageStr);
		str.append(";");
		str.append(enter);
		str.append("public interface ");
		str.append(SimpleClassName);
		str.append("{");
		
		for(MapperMethod mm:methods){
			str.append(mm.toString());
		}
		str.append(enter);
		str.append("}");
		return str.toString();
	}
}
