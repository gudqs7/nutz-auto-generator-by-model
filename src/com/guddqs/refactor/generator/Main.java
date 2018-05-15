package com.guddqs.refactor.generator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {

    public static String OVERRIDE = "false";
    private static String MODE = "file";
    private static String PACKAGE = "";
    private static String FILE = "";
    private static String SUFFIX = "java";
    private static String BASE_PACKAGE = "java";
    private static Map<String, GeneratorTaskVo> classNames = new HashMap<>();

    static {
        Properties config = new Properties();
        try {
            config.load(new FileReader("generator-config.properties"));
            MODE = config.getProperty("generator.mode");
            PACKAGE = config.getProperty("generator.package");
            FILE = config.getProperty("generator.file");
            SUFFIX = config.getProperty("generator.suffix");
            BASE_PACKAGE = config.getProperty("generator.basepackage");
            OVERRIDE = config.getProperty("generator.override");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("start generator:");
        if ("file".equals(MODE)) {
            String[] file = FILE.split(",");
            for (int i = 0; i < file.length; i++) {
                String filePath = file[i];
                String className = filePath.substring(filePath.lastIndexOf('.') + 1, filePath.length());
                String packageName = filePath.substring(0, filePath.lastIndexOf('.'));
                filePath = "src/" + filePath.replaceAll("\\.", "/").concat("." + SUFFIX);
                generateSingleFile(filePath, className, packageName);
            }
        } else {
            if (PACKAGE != null && !"".equals(PACKAGE)) {
                String[] packageArr = PACKAGE.split(",");
                for (String packageName : packageArr) {
                    String packagePath = "src/" + packageName.replaceAll("\\.", "/");

                    System.out.println("Generator With: " + packagePath);
                    System.out.println("-----------------------------");

                    File packageDir = new File(packagePath);
                    if (packageDir.exists() && packageDir.isDirectory()) {
                        File[] modelFiles = packageDir.listFiles();
                        if (modelFiles != null) {
                            for (File modelFile : modelFiles) {
                                if (modelFile.exists() && !modelFile.isDirectory()) {
                                    String modelFilePath = modelFile.getPath();
                                    String className = modelFilePath.substring(modelFilePath.lastIndexOf('/') + 1, modelFilePath.lastIndexOf('.'));
                                    generateSingleFile(modelFilePath, className, packageName);
                                }
                            }
                        } else {
                            System.err.println("No File Under Package");
                        }

                    }
                }
            }
        }
        // write models with classNames
        for (String className : classNames.keySet()) {
            GeneratorTaskVo task = classNames.get(className);
            String content = FileHelper.readFile(task.getFilePath());
            content = replaceFile(content, className, task.getPackageName(), classNames);
            boolean success = FileHelper.writeFile(content, new File(task.getModelPath()), new File(task.getModelPath() + "/" + className + "Model.java"));
            System.out.println("Write Model--> " + success);
        }
    }

    private static void generateSingleFile(String filePath, String className, String packageName) throws IOException {
        System.out.println("Generator with:" + filePath);

        String modelPath = "src/" + (BASE_PACKAGE + ".model").replaceAll("\\.", "/");
        String daoPath = "src/" + (BASE_PACKAGE + ".dao").replaceAll("\\.", "/");
        String daoImplPath = "src/" + (BASE_PACKAGE + ".dao.impl").replaceAll("\\.", "/");
        String servicePath = "src/" + (BASE_PACKAGE + ".service").replaceAll("\\.", "/");
        String serviceImplPath = "src/" + (BASE_PACKAGE + ".service.impl").replaceAll("\\.", "/");
        String controllerPath = "src/" + (BASE_PACKAGE + ".web").replaceAll("\\.", "/");

        classNames.put(className, new GeneratorTaskVo(className, packageName, modelPath, filePath));


        Map<String, String> data = new HashMap<>();
        String daoBeanName = className.substring(0, 1).toLowerCase() + className.substring(1) + "Dao";
        String controllerUrl = BASE_PACKAGE.substring(BASE_PACKAGE.lastIndexOf('.') + 1) + "/" + className.substring(0, 1).toLowerCase() + className.substring(1);
        String serviceBeanName = className.substring(0, 1).toLowerCase() + className.substring(1) + "Service";
        String daoImplName = className + "DaoImpl";
        String serviceImplName = className + "ServiceImpl";
        String controllerName = className + "Controller";
        String daoName = "I" + className + "Dao";
        String serviceName = "I" + className + "Service";

        data.put("className", className + "Model");
        data.put("controllerUrl", controllerUrl);
        data.put("basePackage", BASE_PACKAGE);
        data.put("daoName", daoName);
        data.put("daoImplName", daoImplName);
        data.put("daoBeanName", daoBeanName);
        data.put("serviceName", serviceName);
        data.put("serviceBeanName", serviceBeanName);
        data.put("serviceImplName", serviceImplName);
        data.put("controllerName", controllerName);
        data.put("date", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

        String daoContent = getTemplate("mytemplate/daotemp.txt", data);

        String daoImplContent = getTemplate("mytemplate/daoimpltemp.txt", data);

        String serviceContent = getTemplate("mytemplate/servicetemp.txt", data);

        String serviceImplContent = getTemplate("mytemplate/serviceimpltemp.txt", data);

        String controllerContent = getTemplate("mytemplate/controlltemp.txt", data);

        boolean success = FileHelper.writeFile(daoContent, new File(daoPath), new File(daoPath + "/" + daoName + ".java"));
        System.out.println("Write Dao--> " + success);
        success = FileHelper.writeFile(daoImplContent, new File(daoImplPath), new File(daoImplPath + "/" + daoImplName + ".java"));
        System.out.println("Write DaoImpl--> " + success);
        success = FileHelper.writeFile(serviceContent, new File(servicePath), new File(servicePath + "/" + serviceName + ".java"));
        System.out.println("Write Service--> " + success);
        success = FileHelper.writeFile(serviceImplContent, new File(serviceImplPath), new File(serviceImplPath + "/" + serviceImplName + ".java"));
        System.out.println("Write ServiceImpl--> " + success);
        success = FileHelper.writeFile(controllerContent, new File(controllerPath), new File(controllerPath + "/" + controllerName + ".java"));
        System.out.println("Write Controller--> " + success);
    }

    private static String getTemplate(String path, Map<String, String> data) throws IOException {
        String content = FileHelper.readFile(Main.class.getClassLoader().getResourceAsStream(path));
        if (data != null) {
            for (String key : data.keySet()) {
                content = content.replaceAll("\\{\\{" + key + "\\}\\}", data.get(key));
            }
        }
        return content;
    }

    private static String replaceFile(String content, String className, String packageName, Map<String, GeneratorTaskVo> allClassNames) {
        content = content.replaceAll("class " + className, "class " + className + "Model");
        content = content.replaceAll("package " + packageName, "package " + BASE_PACKAGE + ".model");
        content = content.replaceAll("public *?" + className, "public " + className + "Model");
        for (String otherClassName : allClassNames.keySet()) {
            if (otherClassName != null && !otherClassName.equals(className) && !className.contains(otherClassName)) {
                content = content.replaceAll("([^get])" + otherClassName + "([^Vo])", "$1" + otherClassName + "Model$2");
            }
        }
        return content;
    }
}
