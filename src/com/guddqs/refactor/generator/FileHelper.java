package com.guddqs.refactor.generator;

import java.io.*;

/**
 * @author wq
 * @date 2018/5/14
 */
public class FileHelper {

    static String readFile(String path) throws IOException {
        StringBuilder back = new StringBuilder();
        File file = new File(path);
        if (file.exists() && !file.isDirectory()) {
            FileReader fr = new FileReader(file);
            char[] buff = new char[1024];
            int len = -1;
            while ((len = fr.read(buff)) != -1) {
                back.append(new String(buff, 0, len));
            }
            fr.close();
        }
        return back.toString();
    }

    static boolean writeFile(String content, File dir, File file) throws IOException {
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                System.out.println("创建文件夹失败");
            }
        }
        if (file.exists()) {
            if (!"true".equals(Main.OVERRIDE)) {
                System.out.println("不能覆盖文件" + file.getPath());
                return false;
            }else{
                System.out.println("覆盖文件-->" + file.getPath());
            }
        }
        FileWriter fw = new FileWriter(file);
        fw.write(content);
        fw.flush();
        fw.close();
        return true;
    }

    static String readFile(InputStream resourceAsStream) throws IOException {
        StringBuilder back = new StringBuilder();
        BufferedInputStream bf = new BufferedInputStream(resourceAsStream);
        byte[] buff = new byte[1024];
        int len = -1;
        while ((len = bf.read(buff)) != -1) {
            back.append(new String(buff, 0, len, "utf-8"));
        }
        bf.close();
        return back.toString();
    }
}
