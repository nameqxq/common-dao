package com.quxiqi.common.dao.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

import com.quxiqi.common.dao.bean.Mapper;
import com.quxiqi.common.dao.exception.CommonDaoException;

public class CommonDao{
	private final Configrution conf;
	private static final Logger LOG = Logger.getLogger(CommonDao.class);
	CommonDao(Configrution conf){
		this.conf = conf;
	}
	/**
	 * 数据库CRDU的通用方法。
	 * 该方法用于处理多参数的sql映射，但实际上本次版本并未实现，仅用于被另一重载方法引用
	 * @param xmlPath xml路径名（相对于classpath的路径）
	 * @param elementId 标签id
	 * @param objs mapper接口方法入参(数组)
	 * @param is 入参类型(数组);
	 * @param r mapper接口返回值类型
	 * @return 返回mapper接口方法返回值
	 * @throws Exception
	 */
	public <R> R execute(String xmlPath,String elementId, Object[] objs, @SuppressWarnings("rawtypes") Class[] is, Class<R> r) throws CommonDaoException {
		//获取缓存
		ConcurrentMap<String, Object> cache = conf.getCache();
		Object mapperObj = null;
		//无则解析，有则取缓
		if(!cache.containsKey(xmlPath)){
			
			Mapper mapper = conf.parser.parserXML(xmlPath);
			mapperObj = conf.mapperCreater.createMapper(mapper);
			
			LOG.info("\r\nDynamic compile the mapper interface===》\r\n"+mapper);
		}else{
			mapperObj = cache.get(xmlPath);
			LOG.info("loading cache "+xmlPath+" success! ===> "+mapperObj);
		}
		
		
		Method method = null;
		try {
			method = mapperObj.getClass().getMethod(elementId, is);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new CommonDaoException("CommonDao 调用失败===>"+e.getMessage(),e.getCause());
		}
		
		Object result = null;
		try {
			result = method.invoke(mapperObj, objs);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new CommonDaoException("CommonDao 调用失败===>"+e.getMessage(),e.getCause());
		}
		
		if(result!=null)
			return r.cast(result);
		return null;
	}
	/**
	 * 数据库CRDU的通用方法。
	 * @param xmlPath xml路径名（相对于classpath的路径）
	 * @param elementId 标签id
	 * @param obj mapper接口方法入参
	 * @param i 入参类型;
	 * @param r mapper接口返回值类型
	 * @return 返回mapper接口方法返回值
	 * @throws Exception
	 */
	public <I,R> R execute(String xmlPath,String elementId, Object obj, Class<I> i, Class<R> r) throws CommonDaoException {
		return execute(xmlPath, elementId, new Object[]{obj}, new Class[]{i}, r);
	}
}
