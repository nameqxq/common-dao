package com.quxiqi.common.dao.exception;

@SuppressWarnings("serial")
public class CommonDaoException extends Exception {
	
	public CommonDaoException(String messege){
		super(messege);
	}
	
	public CommonDaoException(String messege,Throwable cause){
		super(messege,cause);
	}
}
