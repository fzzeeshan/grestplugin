package com.zcodez.rest.stubs.generator.process;

import com.sun.xml.internal.ws.util.StringUtils;
import com.zcodez.rest.stubs.generator.util.ExceptionHandlingUtils;
import com.zcodez.rest.stubs.generator.util.GClazzFileUtils;
import com.zcodez.rest.stubs.generator.vo.ExceptionInfoVO;
import com.zcodez.rest.stubs.generator.vo.OutputDataVO;
import com.zcodez.rest.stubs.generator.vo.StructureDefinitionVO;
import com.zcodez.rest.stubs.generator.util.GClazzConstants;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zacky
 * Date: 24/12/21
 * Time: 8:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class JSON2GClazz {

    private static JSON2GClazz INSTANCE;
    private static final String $CONFIG_FILE_NAME = "config.json";
    private static String $JSON_FILE = "";
    private static String $OUTPUT_DIR = "";
    private static String $CONFIG_FILE_DIR = "";
    private static String $PACKAGE_NAME = "";

    private static final String $CLASS_TEMPLATE = "resources/templates/ClassModel.gtf";
    private static String $DECLARATIONS_TEMPLATE = "resources/templates/Declarations.gtf";
    private static final String $PACKAGE_TEMPLATE = "resources/templates/PackageModel.gtf";
    private static final String $HEADER_TEMPLATE = "resources/templates/HeaderModel.gtf";
    private static final String $GET_TEMPLATE = "resources/templates/GetModel.gtf";
    private static final String $SET_TEMPLATE = "resources/templates/SetModel.gtf";


    private static final String $BUILDER_STRING_TEMPLATE = "resources/templates/BuilderStringPropertyModel.gtf";
    private static final String $BUILDER_INTEGER_TEMPLATE = "resources/templates/BuilderIntegerPropertyModel.gtf";
    private static final String $BUILDER_BOOLEAN_TEMPLATE = "resources/templates/BuilderBooleanPropertyModel.gtf";
    private static final String $BUILDER_OBJECT_TEMPLATE = "resources/templates/BuilderObjectPropertyModel.gtf";
    private static final String $BUILDER_CLASS_TEMPLATE = "resources/templates/BuilderClassModel.gtf";

    private static String $JIRA_REFERENCE = "JIRA Reference";
    private static String $JIRA_DESCRIPTION = "Place description here";
    private static String $AUTHOR_NAME = "AUTHOR";
    private static String $RELEASE_VERSION = "1.0";

    private static ArrayList<String> _generatedClazzezList = new ArrayList<String>();
    private static ArrayList<String> _erroredClazzezList = new ArrayList<String>();



    public JSONParser jsonParser = new JSONParser();
    public HashMap<String, ArrayList<StructureDefinitionVO>> _clazzStructureMap = new HashMap<String, ArrayList<StructureDefinitionVO>>();
    private StringBuilder _outputFileData, _propzMethodBuilder = null;
    private static boolean isSettersRequired = false, isGettersRequired = false;

    private GClazzFileUtils _fileUtil = new GClazzFileUtils();
    private static ArrayList<ExceptionInfoVO> _exceptionsList = new ArrayList<ExceptionInfoVO>();

    private ExceptionHandlingUtils _exceptionHandlingUtil = new ExceptionHandlingUtils();

    private JSON2GClazz() {
    }

    public static JSON2GClazz getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new JSON2GClazz();
        }
        return INSTANCE;
    }

    /**
     *
     * @param _JSONFILE
     * @return
     */
    public boolean processFile(String _JSONFILE){

        boolean _processSuccessful = true;

        $JSON_FILE = _JSONFILE;
        $OUTPUT_DIR = _JSONFILE.substring(0, _JSONFILE.lastIndexOf("/")+1);
        $CONFIG_FILE_DIR = $OUTPUT_DIR;
        $PACKAGE_NAME = extractPackageName(_JSONFILE);

        boolean isConfigLoaded = loadConfigurationFile();

        if(!isConfigLoaded){
            OutputDataVO _outputObject = new OutputDataVO(_erroredClazzezList,_generatedClazzezList,_exceptionsList);
            _fileUtil.writeOutputFile($OUTPUT_DIR,_outputObject);
            return false;
        }


        if(isSettersRequired || isGettersRequired){
            $DECLARATIONS_TEMPLATE = "resources/templates/DeclarationsWOAnnotation.gtf";
        }
        System.out.println($DECLARATIONS_TEMPLATE);

        // Instantiate the Factory
        FileReader reader = null;

        JSON2GClazz _instance = getInstance();
        Object _obj = null;
        try {
            reader = _fileUtil.loadFile($JSON_FILE);
            if(reader != null){
                System.out.println("Reader not null");
                _obj = jsonParser.parse(reader);
            }

            // process JSON securely
            System.out.println("JSON2GClazz: Am I JSON Array? " +_instance.isJSONArray(_obj));
            System.out.println("JSON2GClazz: Am I JSON Object? " +_instance.isJSONbject(_obj));



            if(_instance.isJSONbject(_obj)) {
                parseJSONObjects(null, (JSONObject) _obj);
            } else if(_instance.isJSONArray(_obj)){
                parseJSONArrays((JSONArray) _obj);
            }  else {
                //Any other object format present then need to handle it
            }

            String _packageData = generatePackage($PACKAGE_NAME);
            String _headerData = null;

            if(!isGettersRequired && !isSettersRequired){
                _headerData = generateHeaders();
            }


            ArrayList<String> _dataBuilderPropzList = new ArrayList<String>();


            for (Map.Entry<String, ArrayList<StructureDefinitionVO>> entry : _clazzStructureMap.entrySet()) {
                _propzMethodBuilder = null;
                StringBuilder _propzDataBuilder = new StringBuilder();
                StringBuilder _usesDataBuilder = new StringBuilder();

                ArrayList<String> _declarationsList = new ArrayList<String>();
                ArrayList<StructureDefinitionVO> _listOfStructureDefinitionVO = entry.getValue();

                String _clazzName = StringUtils.capitalize(entry.getKey());

                _usesDataBuilder.append("uses " +$PACKAGE_NAME + "." +_clazzName);
                _usesDataBuilder.append("\n");

                for(StructureDefinitionVO _eachObject : _listOfStructureDefinitionVO){
                    _declarationsList.add(getInstance().generateDeclarations(_eachObject));
                    _propzMethodBuilder = isGettersRequired ?  getInstance().generateGetters(_eachObject) : _propzMethodBuilder;
                    _propzMethodBuilder = isSettersRequired ? getInstance().generateSetters(_eachObject) : _propzMethodBuilder;

                    if(_eachObject.getMethodReturnType().equalsIgnoreCase("String")){
                        _propzDataBuilder.append(generateContent($BUILDER_STRING_TEMPLATE, _eachObject.getMethodName(), _eachObject.getMethodValue()));
                        _propzDataBuilder.append("\n");
                    }else if(_eachObject.getMethodReturnType().equalsIgnoreCase("boolean")){
                        _propzDataBuilder.append(generateContent($BUILDER_BOOLEAN_TEMPLATE, _eachObject.getMethodName(), _eachObject.getMethodValue()));
                        _propzDataBuilder.append("\n");
                    }else if(_eachObject.getMethodReturnType().equalsIgnoreCase("int")){
                        _propzDataBuilder.append(generateContent($BUILDER_INTEGER_TEMPLATE, _eachObject.getMethodName(), _eachObject.getMethodValue()));
                        _propzDataBuilder.append("\n");
                    }else{
                        String _dependentClazzName = StringUtils.capitalize(_eachObject.getMethodName());
                        if(_eachObject.getMethodReturnType().indexOf("[]")<0){
                            _usesDataBuilder.append("uses " +$PACKAGE_NAME + "." +_dependentClazzName);
                            _usesDataBuilder.append("\n");
                        }
                        String _dependetBuilderName = _dependentClazzName + "DataBuilder_Dlg";
                        _propzDataBuilder.append(generateContent($BUILDER_OBJECT_TEMPLATE, _dependetBuilderName, _eachObject.getMethodValue()));
                        _propzDataBuilder.append("\n");
                    }

                }


                _outputFileData = wrapClazzStructure(_clazzName, _packageData, _headerData, _declarationsList, _propzMethodBuilder);

                _fileUtil.generateGosuClass($OUTPUT_DIR, _outputFileData, _clazzName);
                //_generatedClazzezList.add(_clazzName);



                String _builderName = _clazzName + "DataBuilder_Dlg";
                String _builderPackageName = $PACKAGE_NAME + ".databuilder";
                String _builderDirectory = $OUTPUT_DIR + "/databuilder";

                generateDataBuilderClazz(_builderDirectory, _clazzName, _builderName, _builderPackageName, _propzDataBuilder, _usesDataBuilder);

                _outputFileData = null;
            }
        } catch (FileNotFoundException e) {
            _processSuccessful = false;
            e.printStackTrace();
        } catch (ParseException e) {
            _processSuccessful = false;
            e.printStackTrace();
        }catch (IOException e) {
            _processSuccessful = false;
            e.printStackTrace();
        }  catch (Exception e) {
            _processSuccessful = false;
            e.printStackTrace();
        }finally{
            OutputDataVO _outputObject = new OutputDataVO(_erroredClazzezList,_generatedClazzezList,_exceptionsList);
            _fileUtil.writeOutputFile($OUTPUT_DIR,_outputObject);
        }


         return _processSuccessful;
    }

    /**
     *
     * @return
     */
    private boolean loadConfigurationFile() {
        FileReader reader = null;

        try{
            /*Messages.showMessageDialog(p, "Hello, Welcom to IntellJ IDEA plugin development. Name: " + $JSON_FILE + "\n" +
                    " OUTPUT_DIR: " + $CONFIG_FILE_DIR + "\n" +
                    " CONFIG_FILE: " + $CONFIG_FILE_NAME+ "\n",
                    "GClazz",
                    Messages.getInformationIcon());*/

            reader = _fileUtil.loadFile($CONFIG_FILE_DIR + $CONFIG_FILE_NAME);
            if(reader != null){
                JSONObject _jsonObj = (JSONObject) jsonParser.parse(reader);
                if(_jsonObj!=null){

                    Object _value = _jsonObj.get(GClazzConstants.$JIRA_REFERENCE);
                    if(_value!=null){
                        $JIRA_REFERENCE = _value.toString();
                        _value = null;
                    }else{
                        _exceptionsList.add(
                                new ExceptionInfoVO(GClazzConstants.$EXCEPTION_CODE_INFO,
                                        GClazzConstants.$100_EXCEPTION_INFO_VALUE,
                                        _exceptionHandlingUtil.getExceptionShortName(GClazzConstants.$100_SHORT_NAME,"JIRA Reference"),
                                        _exceptionHandlingUtil.getExceptionDescription(GClazzConstants.$100_SHORT_NAME, "jiraReference", "JIRA Reference")));
                    }

                    _value = _jsonObj.get(GClazzConstants.$JIRA_DESCRIPTION);
                    if(_value!=null){
                        $JIRA_DESCRIPTION = _value.toString();
                        _value = null;
                    }else{
                        _exceptionsList.add(
                                new ExceptionInfoVO(GClazzConstants.$EXCEPTION_CODE_INFO,
                                        GClazzConstants.$100_EXCEPTION_INFO_VALUE,
                                        _exceptionHandlingUtil.getExceptionShortName(GClazzConstants.$100_SHORT_NAME, "JIRA Reference"),
                                        _exceptionHandlingUtil.getExceptionDescription(GClazzConstants.$100_SHORT_NAME,"jiraDescription", "JIRA Description")));
                    }

                    _value = _jsonObj.get(GClazzConstants.$AUTHOR);
                    if(_value!=null){
                        $AUTHOR_NAME = _value.toString();
                        _value = null;
                    }else{
                        _exceptionsList.add(
                                new ExceptionInfoVO(GClazzConstants.$EXCEPTION_CODE_INFO,
                                        GClazzConstants.$100_EXCEPTION_INFO_VALUE,
                                        _exceptionHandlingUtil.getExceptionShortName(GClazzConstants.$100_SHORT_NAME,"Author"),
                                        _exceptionHandlingUtil.getExceptionDescription(GClazzConstants.$100_SHORT_NAME,"author", "Author")));
                    }

                    /*_value = _jsonObj.get("release-version");
                    if(_value!=null){
                        $RELEASE_VERSION = _value.toString();
                        _value = null;
                    }else{
                        _exceptionsList.add(
                                new ExceptionInfoVO(GClazzConstants.$EXCEPTION_CODE_INFO,
                                        GClazzConstants.$100_EXCEPTION_INFO_VALUE,
                                        _exceptionHandlingUtil.getExceptionShortName("JIRA Reference"),
                                        _exceptionHandlingUtil.getExceptionDescription("jiraDescription", "JIRA Description")));
                    }*/

                    _value = _jsonObj.get(GClazzConstants.$GET_REQUIRED);
                    if(_value!=null){
                        isGettersRequired = Boolean.parseBoolean(_value.toString());
                        _value = null;
                    }else{
                        _exceptionsList.add(
                                new ExceptionInfoVO(GClazzConstants.$EXCEPTION_CODE_INFO,
                                        GClazzConstants.$101_EXCEPTION_INFO_VALUE,
                                        _exceptionHandlingUtil.getExceptionShortName(GClazzConstants.$101_SHORT_NAME,"Getters"),
                                        _exceptionHandlingUtil.getExceptionDescription(GClazzConstants.$101_DESCRIPTION, "getters-required", "getters")));
                    }

                    _value = _jsonObj.get(GClazzConstants.$SET_REQUIRED);
                    if(_value!=null){
                        isSettersRequired = Boolean.parseBoolean(_value.toString());
                        _value = null;
                    }else{
                        _exceptionsList.add(
                                new ExceptionInfoVO(GClazzConstants.$EXCEPTION_CODE_INFO,
                                        GClazzConstants.$101_EXCEPTION_INFO_VALUE,
                                        _exceptionHandlingUtil.getExceptionShortName(GClazzConstants.$101_SHORT_NAME,"Setters"),
                                        _exceptionHandlingUtil.getExceptionDescription(GClazzConstants.$101_DESCRIPTION, "setters-required", "setters")));
                    }

                    System.out.println("Author: " +$AUTHOR_NAME);
                    System.out.println("isGettersReqd: " +isGettersRequired);
                    System.out.println("isSettersReqd: " +isSettersRequired);
                    /*Messages.showMessageDialog(p, "Hello, Welcom to IntellJ IDEA plugin development. Name: " + $JSON_FILE +
                            " $JIRA_REFERENCE: " + $JIRA_REFERENCE + "\n" +
                            " $JIRA_DESCRIPTION: " + $JIRA_DESCRIPTION + "\n" +
                            " $AUTHOR_NAME: " + $AUTHOR_NAME + "\n" +
                            " $RELEASE_VERSION: " + $RELEASE_VERSION + "\n" ,
                            "GClazz",
                            Messages.getInformationIcon());*/

                    System.out.println(
                            "JIRA REF: " +$JIRA_REFERENCE +
                                    " JIRA DESC: " +$JIRA_DESCRIPTION +
                                    " AUTHOR NAME: " +$AUTHOR_NAME +
                                    " REL VERSION: " +$RELEASE_VERSION
                    );

                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            _exceptionsList.add(
                    new ExceptionInfoVO(GClazzConstants.$EXCEPTION_CODE_ERROR,
                            GClazzConstants.$400_EXCEPTION_ERROR_VALUE,
                            GClazzConstants.$400_SHORT_NAME,
                            GClazzConstants.$400_DESCRIPTION));
            return false;
        } catch (ParseException e) {
            _exceptionsList.add(
                    new ExceptionInfoVO(GClazzConstants.$EXCEPTION_CODE_ERROR,
                            GClazzConstants.$401_EXCEPTION_ERROR_VALUE,
                            GClazzConstants.$401_SHORT_NAME,
                            GClazzConstants.$401_DESCRIPTION));
            return false;
        }catch (IOException e) {
            _exceptionsList.add(
                    new ExceptionInfoVO(GClazzConstants.$EXCEPTION_CODE_ALERT,
                            GClazzConstants.$501_EXCEPTION_ALERT_VALUE,
                            GClazzConstants.$501_SHORT_NAME,
                            GClazzConstants.$501_DESCRIPTION));
            return false;
        }  catch (Exception e) {
            _exceptionsList.add(
                    new ExceptionInfoVO(GClazzConstants.$EXCEPTION_CODE_ALERT,
                            GClazzConstants.$502_EXCEPTION_ALERT_VALUE,
                            GClazzConstants.$502_SHORT_NAME,
                            GClazzConstants.$502_DESCRIPTION));
            return false;
        }
        return false;
    }



    private StringBuilder wrapClazzStructure(String _clazzName, String _packageData, String _headerData, ArrayList<String> _declarationsList, StringBuilder _propzMethodBuilder ){
        _outputFileData = _outputFileData==null ? new StringBuilder(): _outputFileData;
        InputStream _inputStream = _fileUtil.loadTemplateFileAsStream($CLASS_TEMPLATE);
        Scanner _scannerInstance = new Scanner(_inputStream).useDelimiter("\\A");
        String _clazzTemplate = _scannerInstance.hasNext() ? _scannerInstance.next() : "";
        try{

            _outputFileData.append(_packageData);

            if(_headerData!=null){
                _outputFileData.append(_headerData);
            }

            //Read File Content
            _clazzTemplate = _clazzTemplate.replaceAll("<clazzName>", _clazzName);
            _clazzTemplate = _clazzTemplate.replaceAll("<authorName>", $AUTHOR_NAME);
            _clazzTemplate = _clazzTemplate.replaceAll("<jiraReference>", $JIRA_REFERENCE);
            _clazzTemplate = _clazzTemplate.replaceAll("<jiraDescription>", $JIRA_DESCRIPTION);
            _outputFileData.append(_clazzTemplate);
            _outputFileData.append("\n\n");

            //Declarations section
            for(String _eachProperty : _declarationsList) {
                _outputFileData.append(_eachProperty);
            }

            if(_propzMethodBuilder!=null){
                _outputFileData.append(_propzMethodBuilder);
            }
            _outputFileData.append("\n\n");
            _outputFileData.append(GClazzConstants.$CLOSE_CURLY_BRACE);
            //System.out.println(_outputFileData);
        }catch(Exception e){
            e.printStackTrace();
        }
        return _outputFileData;
    }

    /**
     * To auto-generate method codes for the given Entity and its
     * attributes
     *
     * @param _structureDefnInstance
     * @return
     */
    public String generateDeclarations(StructureDefinitionVO _structureDefnInstance){

        StringBuilder _localBuilder = new StringBuilder();
        InputStream _inputStream = _fileUtil.loadTemplateFileAsStream($DECLARATIONS_TEMPLATE);
        Scanner _scannerInstance = new Scanner(_inputStream).useDelimiter("\\A");
        String _declarationTemplate = _scannerInstance.hasNext() ? _scannerInstance.next() : "";

        try{
            //Read File Content
            _declarationTemplate = _declarationTemplate.replaceAll("<fieldName>", _structureDefnInstance.getMethodName());
            _declarationTemplate = _declarationTemplate.replaceAll("<dataType>", _structureDefnInstance.getMethodReturnType());
            _localBuilder.append(_declarationTemplate);
            _localBuilder.append("\n\n");
        }catch(Exception e){
            e.printStackTrace();
        }

        return _localBuilder.toString();

    }


    /**
     * To auto-generate method codes for the given Entity and its
     * attributes
     *
     * @param
     * @return
     */
    public String generatePackage(String _packageName){
        StringBuilder _localBuilder = new StringBuilder();
        InputStream _inputStream = _fileUtil.loadTemplateFileAsStream($PACKAGE_TEMPLATE);
        Scanner _scannerInstance = new Scanner(_inputStream).useDelimiter("\\A");
        String _packageTemplate = _scannerInstance.hasNext() ? _scannerInstance.next() : "";

        try{
            //Read File Content
            _packageTemplate = _packageTemplate.replaceAll("<packageName>", _packageName);
            _localBuilder.append(_packageTemplate);
            _localBuilder.append("\n");
        }catch(Exception e){
            e.printStackTrace();
        }

        return _localBuilder.toString();
    }

    /**
     * To auto-generate method codes for the given Entity and its
     * attributes
     *
     * @param
     * @return
     */
    public String generateHeaders(){
        StringBuilder _localBuilder = new StringBuilder();
        InputStream _inputStream = _fileUtil.loadTemplateFileAsStream($HEADER_TEMPLATE);
        Scanner _scannerInstance = new Scanner(_inputStream).useDelimiter("\\A");
        String _headerTemplate = _scannerInstance.hasNext() ? _scannerInstance.next() : "";

        try{
            //Read File Content
            _localBuilder.append(_headerTemplate);
            _localBuilder.append("\n");
        }catch(Exception e){
            e.printStackTrace();
        }

        return _localBuilder.toString();
    }


    /**
     * To auto-generate method codes for the given Entity and its
     * attributes
     *
     * @param
     * @return
     */
    public String generateContent(String _templateName, String fieldName, String _methodValue){
        StringBuilder _localBuilder = new StringBuilder();
        InputStream _inputStream = _fileUtil.loadTemplateFileAsStream(_templateName);
        Scanner _scannerInstance = new Scanner(_inputStream).useDelimiter("\\A");
        String _template = _scannerInstance.hasNext() ? _scannerInstance.next() : "";

        try{
            //Read File Content
            _template = _template.replaceAll("<<propertyName>>", fieldName);
            _template = _template.replaceAll("<<propertyValue>>", _methodValue);
            _localBuilder.append(_template);
            //_localBuilder.append("\n");
        }catch(Exception e){
            e.printStackTrace();
        }

        return _localBuilder.toString();
    }


    /**
     * To auto-generate method codes for the given Entity and its
     * attributes
     *
     * @param _structureDefnInstance
     * @return
     */
    public StringBuilder generateGetters(StructureDefinitionVO _structureDefnInstance){
        _propzMethodBuilder = _propzMethodBuilder==null ? new StringBuilder(): _propzMethodBuilder;
        StringBuilder _localBuilder = new StringBuilder();
        InputStream _inputStream = _fileUtil.loadTemplateFileAsStream($GET_TEMPLATE);
        Scanner _scannerInstance = new Scanner(_inputStream).useDelimiter("\\A");
        String _propzTemplate = _scannerInstance.hasNext() ? _scannerInstance.next() : "";

        try{
            String _fieldName = _structureDefnInstance.getMethodName();
            _propzTemplate = _propzTemplate.replaceAll("<fieldName>", _fieldName);
            _propzTemplate = _propzTemplate.replaceAll("<methodName>", StringUtils.capitalize(_fieldName));
            _propzTemplate = _propzTemplate.replaceAll("<dataType>", _structureDefnInstance.getMethodReturnType());
            _localBuilder.append(_propzTemplate);
            _localBuilder.append("\n\n");
        }catch(Exception e){
            e.printStackTrace();
        }

        _propzMethodBuilder.append(_localBuilder);

        return _propzMethodBuilder;

    }

    /**
     * To auto-generate method codes for the given Entity and its
     * attributes
     *
     * @param _structureDefnInstance
     * @return
     */
    public StringBuilder generateSetters(StructureDefinitionVO _structureDefnInstance){
        _propzMethodBuilder = _propzMethodBuilder==null ? new StringBuilder(): _propzMethodBuilder;
        StringBuilder _localBuilder = new StringBuilder();

        InputStream _inputStream = _fileUtil.loadTemplateFileAsStream($SET_TEMPLATE);
        Scanner _scannerInstance = new Scanner(_inputStream).useDelimiter("\\A");
        String _propzTemplate = _scannerInstance.hasNext() ? _scannerInstance.next() : "";
        try{
            String _fieldName = _structureDefnInstance.getMethodName();
            String _methodName = StringUtils.capitalize(_fieldName);
            _propzTemplate = _propzTemplate.replaceAll("<fieldName>", _fieldName);
            _propzTemplate = _propzTemplate.replaceAll("<methodName>", _methodName);
            _propzTemplate = _propzTemplate.replaceAll("<dataType>", _structureDefnInstance.getMethodReturnType());
            _localBuilder.append(_propzTemplate);
            _localBuilder.append("\n\n");
        }catch(Exception e){
            e.printStackTrace();
        }

        _propzMethodBuilder.append(_localBuilder);

        return _propzMethodBuilder;

    }


    private String extractPackageName(String _sourceFile){

        int _srcIndex = _sourceFile.indexOf("src");
        String _packageName =   _srcIndex>=0 ?
                        _sourceFile.substring(_srcIndex + 4, _sourceFile.lastIndexOf("/")) :
                        _sourceFile.substring(0, _sourceFile.lastIndexOf("/"));
        _packageName = _packageName.replaceAll("/", ".");
        return _packageName;
    }



    private void parseJSONObjects(String _parentNode, JSONObject _jsonObject){
        try{
        Set<Map.Entry<String, Object>> entrySet = _jsonObject.entrySet();
        ArrayList<StructureDefinitionVO> _currentJSONObjectList = new ArrayList<StructureDefinitionVO>();

        _parentNode = _parentNode == null ? "Root" : _parentNode;

        for(Map.Entry<String,Object> entry : entrySet){
            StructureDefinitionVO _localObject = null;
            Object _entryValue = entry.getValue();
            String _entryKey = entry.getKey();

            if(isStringObject(_entryValue)){
                _localObject = new StructureDefinitionVO(_entryKey, "String", _entryValue.toString());
                _currentJSONObjectList.add(_localObject);
            }else if(isBooleanObject(_entryValue)){
                _localObject = new StructureDefinitionVO(_entryKey, "boolean", _entryValue.toString());
                _currentJSONObjectList.add(_localObject);
            }else if(isIntegerObject(_entryValue)){
                _localObject = new StructureDefinitionVO(_entryKey, "int", _entryValue.toString());
                _currentJSONObjectList.add(_localObject);
            }else if(isJSONArray(_entryValue)){
                String _returnType = parseJSONArrays((JSONArray)_entryValue);
                if(_returnType == null){
                    //log no sample data to create stub class
                    System.out.println("Empty object....");
                }else if("Object".equalsIgnoreCase(_returnType)){
                    //Need to handle complex object logic
                    JSONObject _firstObject = ((JSONObject) ((JSONArray) _entryValue).get(0));
                    parseJSONObjects(_entryKey, _firstObject);
                    _localObject = new StructureDefinitionVO(_entryKey, StringUtils.capitalize(_entryKey), _entryValue.toString());
                    _currentJSONObjectList.add(_localObject);
                    System.out.print("Complex object....");
                } else{
                    _localObject = new StructureDefinitionVO(_entryKey, _returnType, _entryValue.toString());
                    _currentJSONObjectList.add(_localObject);
                }
            }else if(isJSONbject(_entryValue)){
                _localObject = new StructureDefinitionVO(_entryKey, StringUtils.capitalize(_entryKey), _entryValue.toString());
                _currentJSONObjectList.add(_localObject);
                //complex object
                parseJSONObjects(_entryKey, (JSONObject)_entryValue);
            }else{

                _exceptionsList.add(
                        new ExceptionInfoVO(GClazzConstants.$EXCEPTION_CODE_ALERT,
                                GClazzConstants.$500_EXCEPTION_ALERT_VALUE,
                                _exceptionHandlingUtil.getExceptionShortName(GClazzConstants.$500_SHORT_NAME,_entryKey),
                                _exceptionHandlingUtil.getExceptionDescription(GClazzConstants.$500_DESCRIPTION, _entryKey, _entryKey)));
                _erroredClazzezList.add(_parentNode);
                throw new Exception();
                //System.out.println("Something else");
            }
        }
            _generatedClazzezList.add(StringUtils.capitalize(_parentNode));
            _clazzStructureMap.put(_parentNode, _currentJSONObjectList);
        }catch(Exception ex){
            System.out.println("Handling Exception");
        }
    }

    private static String parseJSONArrays(JSONArray _jsonArrayObject){

        for(Object _eachKey : _jsonArrayObject){
            return getInstance().isStringObject(_eachKey) ? "String[]" :
                    getInstance().isBooleanObject(_eachKey) ? "Boolean[]" :
                            getInstance().isIntegerObject(_eachKey) ? "Integer[]" : "Object";
        }
        return null;
    }


    /**
     * Wraps the auto-generated method code with Class structure
     *
     * @param
     * @param _methodBuilder
     * @return StringBuilder
     */
    public boolean generateDataBuilderClazz(String _path,
                                            String _clazzName,
                                            String _builderName,
                                            String _packageName,
                                            StringBuilder _methodBuilder,
                                            StringBuilder _usesBuilder){

        //StringBuilder _localBuilder = new StringBuilder();
        //File file = loadFile($CLASS_TEMPLATE);
        //File is found
        //System.out.println("File Found : " + file.exists());

        StringBuilder _localBuilder = new StringBuilder();
        InputStream _inputStream = loadTemplateFileAsStream($BUILDER_CLASS_TEMPLATE);
        Scanner _scannerInstance = new Scanner(_inputStream).useDelimiter("\\A");
        String _classTemplate = _scannerInstance.hasNext() ? _scannerInstance.next() : "";

        try{
            //Read File Content
            //String data = new String(Files.readAllBytes(file.toPath()));
            _classTemplate = _classTemplate.replaceAll("<<className>>", _clazzName);
            _classTemplate = _classTemplate.replaceAll("<<builderName>>", _builderName);
            _classTemplate = _classTemplate.replaceAll("<<authorName>>", $AUTHOR_NAME);
            _classTemplate = _classTemplate.replaceAll("<<jiraReference>>", $JIRA_REFERENCE);
            _classTemplate = _classTemplate.replaceAll("<<jiraDescription>>", $JIRA_DESCRIPTION);


            Date date = Calendar.getInstance().getTime();
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String _currentDate = formatter.format(date);
            formatter = new SimpleDateFormat("hh:mm:ss.SSS a");
            String _currentTime = formatter.format(date);


            _classTemplate = _classTemplate.replaceAll("<<today>>", _currentDate);
            _classTemplate = _classTemplate.replaceAll("<<time>>", _currentTime);

            _classTemplate = _classTemplate.replaceAll("<<defaultProperties>>", _methodBuilder.toString());
            _classTemplate = _classTemplate.replaceAll("<<usesStatement>>", _usesBuilder.toString());
            _classTemplate = _classTemplate.replaceAll("<<packageName>>", _packageName);
            _localBuilder.append(_classTemplate);
            _localBuilder.append("\n\n");
            System.out.println(_classTemplate);

            File _file = new File(_path);
            _file.mkdir();
            FileWriter fw = new FileWriter( _path +"/" +_builderName+ ".gs");
            fw.write(_localBuilder.toString());
            fw.close();

        }catch(Exception e){
            e.printStackTrace();
            return false;

        }

        //_localBuilder.append(_methodBuilder);
        //_localBuilder.append("\n\n");
        //_localBuilder.append(EntityBuilderConstants.$CLOSE_CURLY_BRACE);

        return true;

    }





    /**
     * Load the file for the given path in parameter
     *
     * @param _filePath
     * @return File
     */
    public InputStream loadTemplateFileAsStream(String _filePath){

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(_filePath);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + _filePath);
        } else {
            return inputStream;
        }
    }


    private boolean isJSONbject(Object _object){
        return _object instanceof JSONObject ? true : false;
    }


    private boolean isJSONArray(Object _objecct){
        return _objecct instanceof JSONArray ? true : false;
    }


    private boolean isStringObject (Object _object){
        return _object instanceof String ? true : false;
    }

    private boolean isBooleanObject (Object _object){
        return _object instanceof Boolean ? true : false;
    }

    private boolean isIntegerObject (Object _object){
        Integer _resultValue = -1;
         try{
             //_resultValue = _object!=null ? Integer.parseInt(_object.toString()) : _resultValue;
             _resultValue = Integer.parseInt(_object.toString());
         }catch (NumberFormatException ne){
             return false;
         }catch(Exception ex){
             return false;
         }
        return _resultValue>=0 ? true : false;
    }


    /**
     * Main method to run locally and evaluate the results
     * @param args
     */
    public static void main(String[] args)
    {
        //JSON parser object to parse read file
        String dummy = "resources/templates/test/sample.json";
        String fileName = "D:/.MyProjectz/intellij-workspace/GRestPlugin/src/resources/templates/test/sample.json";
        getInstance().processFile(fileName);

    }

}
