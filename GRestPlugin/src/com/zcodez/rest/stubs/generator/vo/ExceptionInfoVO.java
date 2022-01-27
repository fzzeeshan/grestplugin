package com.zcodez.rest.stubs.generator.vo;

/**
 * Created with IntelliJ IDEA.
 * User: zacky
 * Date: 3/1/22
 * Time: 12:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExceptionInfoVO {


    private String severity;
    private String code;
    private String shortName;
    private String description;

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public ExceptionInfoVO(String severity, String code, String shortName, String description) {
        this.severity = severity;
        this.code = code;
        this.shortName = shortName;
        this.description = description;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
