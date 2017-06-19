package com.quxiqi.common.dao.bean;

import java.util.List;

public class MapperMethod {

		private String methodName;
		private List<String> params;
		private String result;
		public MapperMethod(){}
		public MapperMethod(String methodName, List<String> params, String result) {
			super();
			this.methodName = methodName;
			this.params = params;
			this.result = result;
		}
		public String getMethodName() {
			return methodName;
		}
		public MapperMethod setMethodName(String methodName) {
			this.methodName = methodName;
			return this;
		}
		public List<String> getParams() {
			return params;
		}
		public MapperMethod setParams(List<String> params) {
			this.params = params;
			return this;
		}
		public String getResult() {
			return result;
		}
		public MapperMethod setResult(String result) {
			this.result = result;
			return this;
		}
		@Override
		public String toString() {
			StringBuilder str = new StringBuilder("\r\n\t");
			str.append(result);
			str.append(" ");
			str.append(methodName);
			str.append("(");
			if(params!=null){
				int len = params.size();
				for(int i=0;i<len-1;i++){
					str.append(params.get(i));
					str.append(" param"+i+",");
				}
				str.append(params.get(len-1));
				str.append(" param"+(len-1));
			}
			str.append(");");
			return str.toString();
		}
		
}
