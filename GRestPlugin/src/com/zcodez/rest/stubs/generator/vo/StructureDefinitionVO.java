package com.zcodez.rest.stubs.generator.vo;

/**
 * Created with IntelliJ IDEA.
 * User: zacky
 * Date: 25/12/21
 * Time: 1:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class StructureDefinitionVO {


    private String methodName;
    private String methodReturnType;
    private String methodValue;

    public StructureDefinitionVO(String methodName, String methodReturnType, String methodValue) {
        this.methodName = methodName;
        this.methodReturnType = methodReturnType;
        this.methodValue = methodValue;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodReturnType() {
        return methodReturnType;
    }

    public void setMethodReturnType(String methodReturnType) {
        this.methodReturnType = methodReturnType;
    }

    public String getMethodValue() {
        return methodValue;
    }

    public void setMethodValue(String methodValue) {
        this.methodValue = methodValue;
    }
}
