package com.quxiqi.common.dao.core;

import java.io.IOException;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

public class StringJavaObject extends SimpleJavaFileObject{
    // 源代码
    private String content = "";
    // 遵循Java 规范的类名及文件
    public StringJavaObject(String _javaFileName,String _content){
        super(_createStringJavaObjectUri(_javaFileName),Kind.SOURCE);
        content = _content;
    }
    // 产生一个URL 资源路径
    private static URI _createStringJavaObjectUri(String name){
        // 注意此处没有设置包名
        return URI.create("String:///" + name.replace('.', '/') + Kind.SOURCE.extension);
    }
    // 文本文件代码
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors)
        throws IOException {
            return content;
    }
}

