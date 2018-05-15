package com.guddqs.refactor.generator;

/**
 * @author wq
 * @date 2018/5/15
 */
public class GeneratorTaskVo {

    private String className;
    private String packageName;
    private String modelPath;
    private String filePath;
    public GeneratorTaskVo() {
    }
    public GeneratorTaskVo(String className, String packageName, String modelPath, String filePath) {
        this.className = className;
        this.packageName = packageName;
        this.modelPath = modelPath;
        this.filePath = filePath;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
