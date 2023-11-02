package com.qnkj.core.plugins.compiler;

import lombok.extern.slf4j.Slf4j;

import javax.tools.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Slf4j
public class DynamicCompile {

    /**
     * 编译出类
     *
     * @param javaFile java文件
     *
     * @return 目标类
     */
    public static Boolean compile(String javaFile, String targetFile) throws Exception {
        List<String> paths = new ArrayList<String>();
        paths.add(javaFile);
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        JavaFileManager.Location oLocation = StandardLocation.CLASS_OUTPUT;
        fileManager.setLocation(oLocation,Arrays.asList(new File[] { new File(targetFile) }));
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(paths);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager,diagnostics, null, null, compilationUnits);
        boolean result = task.call();
        fileManager.close();
        if (result == true) {
            log.info("编译成功 : {} ",javaFile);
        } else {
            //print the Diagnostic's information
            for  (Diagnostic oDiagnostic : diagnostics.getDiagnostics()) {
                log.error("发现编译错误[第" + oDiagnostic.getLineNumber() + "行] : " + oDiagnostic.getSource().toString());
                log.error("错误原因: " + oDiagnostic.getMessage(new Locale("en")));
            }
        }
        return result;
    }

}
