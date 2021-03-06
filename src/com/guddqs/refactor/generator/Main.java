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
    private static String PREFIX = "src/";
    private static String MAPPER_PREFIX = "src/mybatis-mapper";
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
            PREFIX = config.getProperty("generator.prefix");
            MAPPER_PREFIX = config.getProperty("generator.mapper.prefix");
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
                filePath = PREFIX + filePath.replaceAll("\\.", "/").concat("." + SUFFIX);
                generateSingleFile(filePath, className, packageName);
            }
        } else {
            if (PACKAGE != null && !"".equals(PACKAGE)) {
                String[] packageArr = PACKAGE.split(",");
                for (String packageName : packageArr) {
                    String packagePath = PREFIX + packageName.replaceAll("\\.", "/");

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

        String modelPath = PREFIX + (BASE_PACKAGE + ".entity").replaceAll("\\.", "/");
        String daoPath = PREFIX + (BASE_PACKAGE + ".mapper").replaceAll("\\.", "/");
        System.out.println(BASE_PACKAGE + ": " + BASE_PACKAGE.lastIndexOf('.'));
        String mapperPath = MAPPER_PREFIX + "/" + (BASE_PACKAGE.substring(BASE_PACKAGE.lastIndexOf('.') + 1, BASE_PACKAGE.length()));
        String servicePath = PREFIX + (BASE_PACKAGE + ".service").replaceAll("\\.", "/");
        String serviceImplPath = PREFIX + (BASE_PACKAGE + ".service.impl").replaceAll("\\.", "/");
        String controllerPath = PREFIX + (BASE_PACKAGE + ".web").replaceAll("\\.", "/");

        classNames.put(className, new GeneratorTaskVo(className, packageName, modelPath, filePath));


        Map<String, String> data = new HashMap<>();
        String daoBeanName = className.substring(0, 1).toLowerCase() + className.substring(1) + "Mapper";
        String controllerUrl = BASE_PACKAGE.substring(BASE_PACKAGE.lastIndexOf('.') + 1) + "/" + className.substring(0, 1).toLowerCase() + className.substring(1);
        String serviceBeanName = className.substring(0, 1).toLowerCase() + className.substring(1) + "Service";
        String serviceImplName = className + "ServiceImpl";
        String controllerName = className + "Controller";
        String daoName = className + "Mapper";
        String serviceName = "I" + className + "Service";

        data.put("className", className + "Model");
        data.put("controllerUrl", controllerUrl);
        data.put("basePackage", BASE_PACKAGE);
        data.put("daoName", daoName);
        data.put("daoBeanName", daoBeanName);
        data.put("serviceName", serviceName);
        data.put("serviceBeanName", serviceBeanName);
        data.put("serviceImplName", serviceImplName);
        data.put("controllerName", controllerName);
        data.put("date", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

        String daoContent = getTemplate("mytemplate/daotemp.txt", data);

        String mapperContent = getTemplate("mytemplate/mapper.txt", data);

        String serviceContent = getTemplate("mytemplate/servicetemp.txt", data);

        String serviceImplContent = getTemplate("mytemplate/serviceimpltemp.txt", data);

        String controllerContent = getTemplate("mytemplate/controlltemp.txt", data);

        boolean success = FileHelper.writeFile(daoContent, new File(daoPath), new File(daoPath + "/" + daoName + ".java"));
        System.out.println("Write Dao--> " + success);
        success = FileHelper.writeFile(mapperContent, new File(mapperPath), new File(mapperPath + "/" + daoName + ".xml"));
        System.out.println("Write Mapper--> " + success);
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
        content = content.replaceAll("package " + packageName, "package " + BASE_PACKAGE + ".entity");
        content = content.replaceAll("public *?" + className, "public " + className + "Model");
        for (String otherClassName : allClassNames.keySet()) {
            if (otherClassName != null && !otherClassName.equals(className) && !className.contains(otherClassName)) {
                content = content.replaceAll("([^get])" + otherClassName + "([^Vo])", "$1" + otherClassName + "Model$2");
            }
        }
        return content;
    }
}
