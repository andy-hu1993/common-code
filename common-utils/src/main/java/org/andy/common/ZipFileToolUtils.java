package org.andy.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实现对zip文件解压
 * 注意只支持zip文件，上传时候需要验证文件类型是否为zip格式的文件
 *
 * @author LIUYANGBING
 */
public class ZipFileToolUtils {
    private static final Logger logger = LoggerFactory.getLogger(ZipFileToolUtils.class);

    public static void streamToFile(InputStream input, String oldFileName, String dirPath) {
        try {
            byte[] b = new byte[input.available()];
            input.read(b);
            File file = new File(dirPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(file, oldFileName);
            OutputStream out = new FileOutputStream(file);
            out.write(b);
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.info(String.format("流生成文件失败%s,%s", e, e.getMessage()));
        }

    }

    /**
     * @param input
     * @param oldFileName
     * @param dirPath
     */
    public static void decodeZipFile(InputStream input, String oldFileName, String dirPath) {
        long startTime = System.currentTimeMillis();
        try {
            //第一步进行解压操作
            ZipEntry zipEntry = null;
            ZipInputStream zipInput = new ZipInputStream(input, Charset.forName("GBK"));
            File createFile = null;
            while ((zipEntry = zipInput.getNextEntry()) != null) {
                String zipName = zipEntry.getName();
                //判断是目录
                if (zipEntry.isDirectory()) {
                    createFile = new File(dirPath, zipName);
                    if (!createFile.exists()) {
                        createFile.mkdirs();
                    }
                } else {
                    //判断是文件
                    String newFileName = null;
                    String createDirPath = null;
                    if (zipName.indexOf("/") != -1) {
                        newFileName = zipName.substring(zipName.lastIndexOf("/") + 1);
                        createDirPath = zipName.substring(0, zipName.lastIndexOf("/"));
                    } else {
                        newFileName = zipName;
                    }
                    File newFile = null;
                    if (null != createDirPath) {
                        newFile = new File(dirPath, createDirPath);
                    } else {
                        newFile = new File(dirPath);
                    }
                    newFile = new File(newFile, newFileName);

                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(newFile));
                    int length = 0;
                    byte[] b = new byte[zipInput.available()];
                    while ((length = zipInput.read(b)) != -1) {
                        out.write(b, 0, length);
                    }
                    out.flush();
                    out.close();
                }
            }
            zipInput.close();
            input.close();
        } catch (Exception e) {
            logger.info(String.format("解压文件出现异常信息%s,%s", e, e.getMessage()));
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("总共执行时间为" + totalTime);
    }

    public static Boolean createZip(String createFilePath, String resourceFile) {
        Boolean createZip = true;
        try {
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(createFilePath));
            ZipEntry entry = null;
            File[] fileList = new File(resourceFile).listFiles();
            InputStream input = null;
            int length = 0;
            byte[] b = new byte[1024];
            for (File file : fileList) {
                entry = new ZipEntry(file.getName());
                zipOut.putNextEntry(entry);
                input = new FileInputStream(file);
                while ((length = input.read(b)) != -1) {
                    zipOut.write(b, 0, length);
                }
                entry.clone();
            }
            zipOut.flush();
            zipOut.closeEntry();
            zipOut.close();

        } catch (Exception e) {
            createZip = false;
            logger.info(String.format("批量压缩文件失败%s,%s", e, e.getMessage()));
        }
        return createZip;
    }
}
