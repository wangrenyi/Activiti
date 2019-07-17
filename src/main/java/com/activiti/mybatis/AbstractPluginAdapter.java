package com.activiti.mybatis;

import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.PrimitiveTypeWrapper;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public abstract class AbstractPluginAdapter extends PluginAdapter {

    protected void wrapperField(String fieldName, TopLevelClass topLevelClass) {
        PrimitiveTypeWrapper integerWrapper = FullyQualifiedJavaType.getIntInstance().getPrimitiveTypeWrapper();

        Field limit = new Field();
        limit.setName(fieldName);
        limit.setVisibility(JavaVisibility.PRIVATE);
        limit.setType(integerWrapper);
        topLevelClass.addField(limit);

        Method setFieldMethod = new Method();
        setFieldMethod.setVisibility(JavaVisibility.PUBLIC);
        setFieldMethod.setName("set" + toUpperCase(fieldName));
        setFieldMethod.addParameter(new Parameter(integerWrapper, fieldName));
        setFieldMethod.addBodyLine("this." + fieldName + " = " + fieldName + ";");
        topLevelClass.addMethod(setFieldMethod);

        Method getFieldMethod = new Method();
        getFieldMethod.setVisibility(JavaVisibility.PUBLIC);
        getFieldMethod.setReturnType(integerWrapper);
        getFieldMethod.setName("get" + toUpperCase(fieldName));
        getFieldMethod.addBodyLine("return " + fieldName + ";");
        topLevelClass.addMethod(getFieldMethod);
    }

    protected String toUpperCase(String str) {
        if (str.length() <= 0) {
            return str;
        }
        char[] ch = str.toCharArray();
        ch[0] -= 32;
        return String.valueOf(ch);
    }
}
