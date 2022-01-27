package com.zcodez.rest.stubs.generator.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zcodez.rest.stubs.generator.vo.ExceptionInfoVO;
import com.zcodez.rest.stubs.generator.vo.OutputDataVO;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: zacky
 * Date: 2/1/22
 * Time: 8:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class GClazzFileUtils {




    public void generateGosuClass(String _path, StringBuilder _content, String _clazzName){
        try{
            File _file = new File(_path);
            _file.mkdir();
            FileWriter fw = new FileWriter( _path +_clazzName+ ".gs");
            fw.write(_content.toString());
            fw.close();
            //Files.write(path, _content.toString().getBytes(StandardCharsets.UTF_8));
        }catch (IOException e){
            e.printStackTrace();;
        }
    }


    public void writeOutputFile(String _path, OutputDataVO _outputVO){
        try{
            if(_outputVO!=null){
                File _outputFile = new File(_path + GClazzConstants.$EXCEPTION_FILE);
                ObjectMapper mapper = new ObjectMapper();
                mapper.writerWithDefaultPrettyPrinter().writeValue(_outputFile,  _outputVO );
            }
        }catch (IOException e){
            e.printStackTrace();;
        }
    }


    /**
     * Load the file for the given path in parameter
     *
     * @param _filePath
     * @return File
     */
    public File loadTemplateFile(String _filePath){
        URL _fileURL = this.getClass().getClassLoader().getResource(_filePath);
        File _fileObject = null;
        if(_fileURL !=null){
            _fileObject =  new File(_fileURL.getFile());
        } else{
            if(_fileObject==null){
                System.out.println("New File: " +_filePath);
                _fileObject = new File(_filePath);
            }
        }
        return _fileObject;
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


    /**
     * Load the file for the given path in parameter
     *
     * @param _filePath
     * @return File
     */
    public FileReader loadFile(String _filePath) throws FileNotFoundException {
        FileReader _reader = null;
        System.out.println(_filePath);
        URL _fileURL = this.getClass().getClassLoader().getResource(_filePath);
        //try {
            if(_fileURL !=null){
                _reader =  new FileReader(_fileURL.getFile());
            }else{
                _reader = new FileReader(new File(_filePath));
            }
        //}catch (FileNotFoundException _fe){
        //    _fe.printStackTrace();
        //}
        //}

        return _reader;
    }


    /**
     * Load the file for the given path in parameter
     *
     * @param _filePath
     * @return File
     */
    public InputStream  loadFileAsStream(String _filePath){

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(_filePath);

        // the stream holding the file content
        if (inputStream == null) {
            System.out.println("file not found! " +_filePath );
            throw new IllegalArgumentException("file not found! " + _filePath);
        } else {
            return inputStream;
        }
    }

}
