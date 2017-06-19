package com.quxiqi.common.dao.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.type.TypeAliasRegistry;

import com.quxiqi.common.dao.bean.CompileResultMap;
import com.quxiqi.common.dao.exception.CommonDaoException;

public class Configrution {
	//储存别名的字符串表现形式
	private final Map<String, String> aliases = new HashMap<>();
	
	private final ConcurrentMap<String,Object> cache = new ConcurrentHashMap<>();
	
	private final SqlSession session ;
	
	public static final String CLASS_PATH =
			Configrution.class.getClassLoader().getResource("").getPath();
	//怎么处理标签resulMap属性（返回值list/bean）
	public final CompileResultMap COMPILE_RESULTMAP;
	
	//单例
	private static Configrution conf = null;
	public Map<String, String> getAliases() {
		return aliases;
	}
	public SqlSession getSession() {
		return session;
	}
	public ConcurrentMap<String,Object> getCache(){
		return cache;
	}
	//common-dao主要模块创建
	CommonDao commonDao = new CommonDao(this);
	MapperCreater mapperCreater = new MapperCreater(this);
	XMLParser parser = new XMLParser(this);
	
	private Configrution(SqlSession session,CompileResultMap COMPILE_RESULTMAP){
		this.session = session;
		if(COMPILE_RESULTMAP == null)
			this.COMPILE_RESULTMAP = CompileResultMap.AUTO_MATCHING;
		else
			this.COMPILE_RESULTMAP = COMPILE_RESULTMAP;
		Configuration configuration = session.getConfiguration();
		
		TypeAliasRegistry typeAliasRegistry = configuration.getTypeAliasRegistry();
		
		Map<String, Class<?>> typeAliases = typeAliasRegistry.getTypeAliases();

		for(String key:typeAliases.keySet())
			aliases.put(key,typeAliases.get(key).getName());
		
	}
	
	/**
	 * 此方法用来获取单例的Configrution对象，值得注意的是，此处加锁的粒度级较
	 * 所以请尽量避免重复调用本方法
	 * 
	 * @param session 需要一个Mybatis的SqlSession对象，该对象将成为common-dao的运行时环境
	 * @return
	 * @throws Exception
	 */
	public synchronized static Configrution getInstance(final SqlSession session,final CompileResultMap COMPILE_RESULTMAP) throws CommonDaoException{
		if(conf==null){
			conf = new Configrution(session,COMPILE_RESULTMAP);
		}
		return conf;
	}
	public synchronized static Configrution getInstance(final SqlSession session) throws CommonDaoException{
		return getInstance(session,CompileResultMap.AUTO_MATCHING);
	}
	public CommonDao getCommonDao(){
		return commonDao; 
	}
	
}
