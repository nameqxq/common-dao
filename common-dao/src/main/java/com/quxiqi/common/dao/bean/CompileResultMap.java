package com.quxiqi.common.dao.bean;
/**
 *	该枚举类用来描述common-dao该如何解析标签中的resultMap属性
 * @author qxq
 * 
 */
public enum CompileResultMap {
	/**
	 * ALWAYS_LIST表示所有含有resultMap属性的标签对应的方法的返回值都将解析成list
	 */
	ALWAYS_LIST,
	/**
	 * ALWAYS_BEAN表示所有含有resultMap属性的标签对应的方法的返回值都将解析为该resultMap对应的bean
	 */
	ALWAYS_BEAN,
	/**
	 * AUTO_MATCHING表示自动匹配--当且仅但标签中有resultMap属性，且标签的ID中包含list字符串时
	 * 才将返回值解析成list
	 */
	AUTO_MATCHING
}
