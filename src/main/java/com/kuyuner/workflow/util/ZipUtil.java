package com.kuyuner.workflow.util;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

public class ZipUtil {
    private static final int BUFFER = 1024;

    /**
     * 将单个文件或文件夹压缩成zip文件
     *
     * @param path 文件或文件夹的绝对路径
     * @param zipFile     要生成的zip文件
     * @throws IOException
     */
    public static void compress(String path, File zipFile) throws IOException {
        File fileOrFolder = new File(path);
        if (!fileOrFolder.exists()) {
            throw new RuntimeException(path + "不存在！");
        }
        FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
        CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
        ZipOutputStream out = new ZipOutputStream(cos);

        if (fileOrFolder.isDirectory()) {
            File[] files = fileOrFolder.listFiles();
            for (int i = 0; i < files.length; i++) {
                compress(files[i], out, "");
            }
        }else{
            compress(fileOrFolder,out, "");
        }
        out.close();
    }

    private static void compress(File file, ZipOutputStream out, String basedir) throws IOException {
        /* 判断是目录还是文件 */
        if (file.isDirectory()) {
            compressDirectory(file, out, basedir);
        } else {
            compressFile(file, out, basedir);
        }
    }

    /**
     * 压缩文件夹
     *
     * @param dir
     * @param out
     * @param basedir
     * @throws IOException
     */
    private static void compressDirectory(File dir, ZipOutputStream out, String basedir) throws IOException {
        if (!dir.exists()){
            return;
        }

        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            /* 递归 */
            compress(files[i], out, basedir + dir.getName() + File.separator);
        }
    }

    /**
     * 压缩一个文件
     *
     * @param file
     * @param out
     * @param basedir
     * @throws IOException
     */
    private static void compressFile(File file, ZipOutputStream out, String basedir) throws IOException {
        if (!file.exists()) {
            return;
        }
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        ZipEntry entry = new ZipEntry(basedir + file.getName());
        out.putNextEntry(entry);
        int count;
        byte data[] = new byte[BUFFER];
        while ((count = bis.read(data, 0, BUFFER)) != -1) {
            out.write(data, 0, count);
        }
        bis.close();
    }
}