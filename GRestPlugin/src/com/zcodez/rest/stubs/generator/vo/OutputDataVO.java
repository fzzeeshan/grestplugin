package com.zcodez.rest.stubs.generator.vo;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: zacky
 * Date: 3/1/22
 * Time: 5:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class OutputDataVO {

    private ArrayList<String> erroredClasses;
    private ArrayList<String> generatedClasses;
    private ArrayList<ExceptionInfoVO> exceptionInfo;

    public OutputDataVO( ArrayList<String> erroredClasses, ArrayList<String> generatedClasses, ArrayList<ExceptionInfoVO> exceptionInfo) {
        this.erroredClasses = erroredClasses;
        this.generatedClasses = generatedClasses;
        this.exceptionInfo = exceptionInfo;
    }

    public ArrayList<String> getErroredClasses() {
        return erroredClasses;
    }

    public void setErroredClasses(ArrayList<String> erroredClasses) {
        this.erroredClasses = erroredClasses;
    }

    public ArrayList<String> getGeneratedClasses() {
        return generatedClasses;
    }

    public void setGeneratedClasses(ArrayList<String> generatedClasses) {
        this.generatedClasses = generatedClasses;
    }

    public ArrayList<ExceptionInfoVO> getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(ArrayList<ExceptionInfoVO> exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }
}
