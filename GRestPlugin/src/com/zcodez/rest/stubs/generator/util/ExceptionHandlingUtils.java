package com.zcodez.rest.stubs.generator.util;

/**
 * Created with IntelliJ IDEA.
 * User: zacky
 * Date: 3/1/22
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExceptionHandlingUtils {



    public String getExceptionShortName(String _key, String _propertyName){
        return _key.replaceAll("<propzGeneralName>", _propertyName);
    }

    public String getExceptionDescription(String _key, String _propertyName, String propertyGeneralName){
        String _updatedDescription = _key.replaceAll("<propzGeneralName>", propertyGeneralName);
        return _updatedDescription.replaceAll("<propzName>", _propertyName);
    }

}
