package com.zcodez.rest.stubs.generator.util;

/**
 * Created with IntelliJ IDEA.
 * User: zacky
 * Date: 31/8/21
 * Time: 7:42 AM
 * To change this template use File | Settings | File Templates.
 */


public class GClazzConstants {
    public static final String $OPEN_CURLY_BRACE = "{";
    public static final String $CLOSE_CURLY_BRACE = "}";

    public static final String $NODE_ENTITY = "entity";
    public static final String $NODE_COLUMN = "column";
    public static final String $NODE_EXTENSION = "extension";
    public static final String $NODE_INDEX = "index";
    public static final String $NODE_ARRAY = "array";
    public static final String $NODE_IMPLEMENTS_ENTITY = "implementsEntity";

    public static final String $EXCEPTION_FILE = "output.json";


    public static final String $ATTR_TABLE = "table";
    public static final String $ATTR_DESCRIPTION = "desc";
    public static final String $ATTR_NAME = "name";
    public static final String $ATTR_TYPE = "type";
    public static final String $ATTR_NULLOK = "nullok";


    public static final String $ENTITY_ENDS_WITH_INS = "_Ins";
    public static final String $ENTITY_ENDS_WITH_EXT = "_Ext";



    public static final int ONE_ATTR_TYPE_VARCHAR = 1;
    public static final int ONE_ATTR_TYPE_SHORTTEXT = 1;
    public static final int ONE_ATTR_TYPE_MEDIUMTEXT = 1;
    public static final int ONE_ATTR_TYPE_TEXT = 1;

    public static final int TWO_ATTR_TYPE_INTEGER = 2;
    public static final int TWO_ATTR_TYPE_PERCENTAGE = 2;
    public static final int TWO_ATTR_TYPE_CURRENCY_AMOUNT = 2;

    public static final int THREE_ATTR_TYPE_DATE = 3;
    public static final int THREE_ATTR_TYPE_DATE_ONLY = 3;

    public static final int FOUR_ATTR_TYPE_BIT = 4;


    public static final String $JIRA_REFERENCE = "jiraReference";
    public static final String $JIRA_DESCRIPTION = "jiraDescription";
    public static final String $AUTHOR = "author";
    public static final String $GET_REQUIRED = "getters-required";
    public static final String $SET_REQUIRED = "setters-required";
    public static String $PARENT_OBJECT = "parent-object";


    public static final String $EXCEPTION_CODE_INFO = "INFO";
    public static final String $EXCEPTION_CODE_ERROR = "ERROR";
    public static final String $EXCEPTION_CODE_ALERT = "ALERT";

    public static final String $100_EXCEPTION_INFO_VALUE = "100";
    public static final String $101_EXCEPTION_INFO_VALUE = "101";

    public static final String $500_EXCEPTION_ALERT_VALUE = "500";
    public static final String $501_EXCEPTION_ALERT_VALUE = "501";
    public static final String $502_EXCEPTION_ALERT_VALUE = "502";

    public static final String $400_EXCEPTION_ERROR_VALUE = "400";
    public static final String $401_EXCEPTION_ERROR_VALUE = "401";


    public static final String $100_SHORT_NAME = "<propzGeneralName> not configured";
    public static final String $100_DESCRIPTION = "Configure '<propzName>' property if you need <propzGeneralName> in comments on your stub classes.";

    public static final String $101_SHORT_NAME = "<propzGeneralName> not configured";
    public static final String $101_DESCRIPTION = "No <propzGeneralName> property set in your configuration file. Configure '<propzName>' property as 'true' if you need <propzGeneralName> methods to be generated.";

    public static final String $500_SHORT_NAME = "<propzGeneralName> not parsed";
    public static final String $500_DESCRIPTION = "Unable to parse Object '<propzName>' due to structure/data issues. Please validate the payload or raise a ticket with DEV team. Ensure to share the payload and report.";

    public static final String $501_SHORT_NAME = "Config file accessing issue";
    public static final String $501_DESCRIPTION = "Issue while accessing config file, please reach to DEV team for more information.";

    public static final String $502_SHORT_NAME = "Config file Issue";
    public static final String $502_DESCRIPTION = "Issue with config file, please reach to DEV team for more information.";

    public static final String $400_SHORT_NAME = "Config file not found";
    public static final String $400_DESCRIPTION = "Please add 'config.json' file with necessary properties. Please validate README document for reference.";

    public static final String $401_SHORT_NAME = "Config file not parsed";
    public static final String $401_DESCRIPTION = "Please validate 'config.json' file with README document for reference.";


}
