package com.zcodez.rest.stubs.generator.action;

import com.intellij.codeInsight.template.TemplateManager;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.zcodez.rest.stubs.generator.process.JSON2GClazz;


/**
 * Created with IntelliJ IDEA.
 * User: zacky
 * Date: 1/1/22
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class GClazzGeneratorAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        // TODO: insert action logic here
        Project p = event.getProject();

        PsiFile psiFile = event.getData(DataKeys.PSI_FILE);

        String _fileType = psiFile.getFileType().getName();
        String _language = psiFile.getLanguage().getDisplayName();
        VirtualFile vFile = event.getData(DataKeys.VIRTUAL_FILE);
        String _fileExtension = vFile.getExtension();
        String _fileName = vFile.getName();
        String _filePath = vFile.getPath();
        String _canPath = vFile.getCanonicalPath();
        String _url = vFile.getUrl();




        event.getPresentation().setEnabled(psiFile.getLanguage().isKindOf(JavaLanguage.INSTANCE));


        //VirtualFile vFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        //String fileName = vFile != null ? vFile.getName() : null;

       /* Messages.showMessageDialog(p, "Hello, Welcom to IntellJ IDEA plugin development. Lang: " +_language + " Type: " +_fileType, "Welcome-123",
                Messages.getInformationIcon());*/



        JSON2GClazz _instance = JSON2GClazz.getInstance();
        boolean _isStubsGenerated = _instance.processFile(_filePath);

        if(_isStubsGenerated){
            Messages.showMessageDialog(p, "Stubs generated successfully, ! ","G-REST Plugin",Messages.getInformationIcon());
        }else{
            Messages.showMessageDialog(p, "Issues in generating stub file. Please check output.json file on the same path.","G-REST Plugin",Messages.getErrorIcon());
        }

    }


    @Override
    public void update(AnActionEvent event) {

        VirtualFile vFile = event.getData(DataKeys.VIRTUAL_FILE);
        String _fileExtension = vFile.getExtension();
        String _fileName = vFile.getName();

        if("json".equalsIgnoreCase(_fileExtension)){
            event.getPresentation().setEnabledAndVisible(true);
        }else{
            event.getPresentation().setEnabledAndVisible(false);
        }
    }



}
