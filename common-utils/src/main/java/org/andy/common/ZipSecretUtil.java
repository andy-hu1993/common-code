package org.andy.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

import de.idyl.winzipaes.AesZipFileDecrypter;
import de.idyl.winzipaes.AesZipFileEncrypter;
import de.idyl.winzipaes.impl.AESDecrypter;
import de.idyl.winzipaes.impl.AESDecrypterBC;
import de.idyl.winzipaes.impl.AESEncrypter;
import de.idyl.winzipaes.impl.AESEncrypterBC;
import de.idyl.winzipaes.impl.ExtZipEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZipSecretUtil {
    private static final Logger logger = LoggerFactory.getLogger(ZipSecretUtil.class);

    /**
     * 使用指定密码将给定文件或文件夹压缩成指定的输出ZIP文件
     *
     * @param srcFile  需要压缩的文件或文件夹
     * @param destPath 输出路径
     * @param passwd   压缩文件使用的密码
     */
    public static Boolean zip(String srcFile, String destPath, String passwd) {
        Boolean createZip = true;
        AESEncrypter encrypter = new AESEncrypterBC();
        AesZipFileEncrypter zipFileEncrypter = null;
        try {
            zipFileEncrypter = new AesZipFileEncrypter(destPath, encrypter);
            /**
             * 此方法是修改源码后添加,用以支持中文文件名
             */
            // zipFileEncrypter.setEncoding("utf8");
            File sFile = new File(srcFile);
            /**
             * AesZipFileEncrypter提供了重载的添加Entry的方法,其中: add(File f, String
             * passwd) 方法是将文件直接添加进压缩文件
             *
             * add(File f, String pathForEntry, String passwd)
             * 方法是按指定路径将文件添加进压缩文件 pathForEntry - to be used for addition of the
             * file (path within zip file)
             */
            doZip(sFile, zipFileEncrypter, "", passwd);
        } catch (IOException e) {
            logger.info(String.format("加密文件出现异常%s", e.getMessage()));
            createZip = false;
        } finally {
            try {
                zipFileEncrypter.close();
            } catch (IOException e) {
                logger.info(String.format("加密文件出现异常%s", e.getMessage()));
                createZip = false;
            }
        }

        return createZip;
    }

    /**
     * 具体压缩方法,将给定文件添加进压缩文件中,并处理压缩文件中的路径
     *
     * @param file         给定磁盘文件(是文件直接添加,是目录递归调用添加)
     * @param encrypter    AesZipFileEncrypter实例,用于输出加密ZIP文件
     * @param pathForEntry ZIP文件中的路径
     * @param passwd       压缩密码
     * @throws IOException
     */
    private static void doZip(File file, AesZipFileEncrypter encrypter,
                              String pathForEntry, String passwd) throws IOException {
        if (file.isFile()) {
            if (file.getName().endsWith("zip")) {
                return;
            }
            pathForEntry += file.getName();
            encrypter.add(file, pathForEntry, passwd);
            return;
        }
        pathForEntry += file.getName() + File.separator;
        for (File subFile : file.listFiles()) {
            doZip(subFile, encrypter, pathForEntry, passwd);
        }
    }

    /**
     * 使用给定密码解压指定压缩文件到指定目录
     *
     * @param inFile 指定Zip文件
     * @param outDir 解压目录
     * @param passwd 解压密码
     */
    public static String unzip(String inFile, String outDir, String passwd) {
        String returnPath = null;
        File outDirectory = new File(outDir);
        if (!outDirectory.exists()) {
            outDirectory.mkdir();
        }
        AESDecrypter decrypter = new AESDecrypterBC();
        AesZipFileDecrypter zipDecrypter = null;
        File extractDir = null;
        try {
            zipDecrypter = new AesZipFileDecrypter(new File(inFile), decrypter);
            AesZipFileDecrypter.charset = "utf-8";
            /**
             * 得到ZIP文件中所有Entry,但此处好像与JDK里不同,目录不视为Entry
             * 需要创建文件夹,entry.isDirectory()方法同样不适用,不知道是不是自己使用错误 处理文件夹问题处理可能不太好
             */
            List<ExtZipEntry> entryList = zipDecrypter.getEntryList();
            for (ExtZipEntry entry : entryList) {
                String eName = entry.getName();
                String dir = eName.substring(0,
                    eName.lastIndexOf(File.separator) + 1);
                extractDir = new File(outDir, dir);
                if (!extractDir.exists()) {
                    extractDir.mkdirs();
                }
                /**
                 * 抽出文件
                 */
                File extractFile = new File(outDir + File.separator + eName);
                zipDecrypter.extractEntry(entry, extractFile, passwd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DataFormatException e) {
            e.printStackTrace();
        } finally {
            try {
                zipDecrypter.close();
                //				fetchFile(outDir);
                if (null != extractDir) {
                    returnPath = extractDir.getPath();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return returnPath;
    }

    public static void fetchFile(String outDir) {
        try {
            File[] fileList = new File(outDir).listFiles();
            if (null != fileList && fileList.length > 0) {
                for (File file : fileList) {
                    if (file.isDirectory()) {
                        fetchFile(file.getPath());
                    } else {
                        BufferedOutputStream out = new BufferedOutputStream(
                            new FileOutputStream(outDir + File.separator + file.getName()));
                        BufferedInputStream input = new BufferedInputStream(
                            new FileInputStream(file.getAbsoluteFile()));
                        int length = 0;
                        byte[] b = new byte[1024];
                        while ((length = input.read(b)) != -1) {
                            out.write(b, 0, length);
                        }
                        input.close();
                        out.flush();
                        out.close();
                    }
                }
            }
        } catch (Exception e) {
            logger.info(String.format("读取文件目录下的文件失败%s,%s", e, e.getMessage()));
        }
    }

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        /**
         * 压缩测试 可以传文件或者目录
         */
        //		 zip("D:/tmp/朝阳学校_84", "D:/tmp/create.zip", "zyh");
        unzip("D:/tmp/guotaoxiaoxue_85.zip", "D:/tmp/logs", "ln76es46k6782vd7");
        //		fetchFile("D:/tmp/logs");
    }

}
