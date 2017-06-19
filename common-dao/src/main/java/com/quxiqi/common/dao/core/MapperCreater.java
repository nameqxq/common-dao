package com.quxiqi.common.dao.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.quxiqi.common.dao.bean.Mapper;
import com.quxiqi.common.dao.exception.CommonDaoException;

public class MapperCreater {
	
	private Configrution conf;
	
	MapperCreater(Configrution conf){
		this.conf = conf;
	}
	
	public Object createMapper(Mapper mapper) throws CommonDaoException{
		//获取classPath
		String classPath = Configrution.CLASS_PATH;
		//全类名
		String mapperName = mapper.getMapperName();
		// 当前编译器
        JavaCompiler cmp = ToolProvider.getSystemJavaCompiler();
        //Java 标准文件管理器
        StandardJavaFileManager fm = cmp.getStandardFileManager(null,null,null);
        //Java 文件对象
        JavaFileObject jfo = new StringJavaObject(mapperName,mapper.toString());
        // 编译参数，类似于javac <options> 中的options
        List<String> optionsList = new ArrayList<String>();
        // 编译文件的存放地方，注意：此处是为Eclipse 工具特设的
        optionsList.addAll(Arrays.asList("-d",classPath));
        // 要编译的单元
        List<JavaFileObject> jfos = Arrays.asList(jfo);
        // 设置编译环境
        JavaCompiler.CompilationTask task = cmp.getTask(null, fm, null,optionsList,null,jfos);
        
        if(task.call()){
    		Class<?> c = null;
    		URL[] urls = null;
			try {
				urls = new URL[]{new URL("file:/"+classPath)};
			} catch (MalformedURLException e) {
				throw new CommonDaoException("mapper 创建失败===>"+e.getMessage(),e.getCause());
			} 
			try (URLClassLoader classLoader =new URLClassLoader(urls)){
				c = classLoader.loadClass(mapperName);
			} catch (ClassNotFoundException e) {
				throw new CommonDaoException("mapper 创建失败===>"+e.getMessage(),e.getCause());
			} catch (IOException e1) {
				//classloader关闭失败
				e1.printStackTrace();
			}
    		
    		ConcurrentMap<String,Object> cache = conf.getCache();
    		Object mapperObj = conf.getSession().getMapper(c);
    		cache.put(mapper.getXmlPath(), mapperObj);
    		
    		return mapperObj;
        	
        }
        throw new CommonDaoException("mapper 创建失败===> 动态编译失败：\r\n"+mapper.toString());
	}
}
