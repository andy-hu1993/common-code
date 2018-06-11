package org.andy.common;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yitai.common.exception.ReportException;
import com.yitai.common.util.aes.base64.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static final String UTF8 = "UTF-8";

    public static String getRelPath() {
        String templatePath = FileUtil.class.getClassLoader().getResource("/").getPath();
        String rootPath = "";
        //windows下
        if ("\\".equals(File.separator)) {
            rootPath = templatePath.replace("/", "\\");
        }

        //linux下
        if ("/".equals(File.separator)) {
            rootPath = templatePath.replace("\\", "/");
        }

        return rootPath;
    }

    /**
     * 把数据写至文件中
     *
     * @param filePath
     * @param data
     */
    public static void writeFile(String filePath, String data) {
        FileOutputStream fos = null;
        OutputStreamWriter writer = null;
        try {
            fos = new FileOutputStream(new File(filePath));
            writer = new OutputStreamWriter(fos, "UTF-8");
            writer.write(data);
        } catch (Exception ex) {
            //logger.error(ex.getMessage());
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static Boolean downLoadFile(String urlPath, File storePath) {
        Boolean createSuccess = false;
        try {
            if (!(urlPath.endsWith(".xlsx") || urlPath.endsWith(".xls"))) {
                return createSuccess;
            }
            String fileName = urlPath.substring(urlPath.lastIndexOf("/") + 1, urlPath.length());
            URL url = new URL(urlPath);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();
            int length = 0;
            byte[] b = new byte[1024];
            File file = new File(storePath, fileName);
            BufferedOutputStream buffer = new BufferedOutputStream(new FileOutputStream(file));
            while ((length = input.read(b)) != -1) {
                buffer.write(b, 0, length);
            }
            buffer.flush();
            buffer.close();
            input.close();
            connection.disconnect();
            createSuccess = true;
        } catch (Exception e) {
            logger.info(String.format("%s,%s", e, e.getMessage()));
        }
        return createSuccess;
    }

    /**
     * 删除文件
     *
     * @param filePath
     */
    public static void deletePath(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }

    /**
     * 把数据写至文件中
     *
     * @param fileContent
     */
    public static void writeFile(String filePath, String name, String fileContent) throws IOException {
        FileOutputStream fos = null;
        FileChannel fcout = null;
        try {
            File file = new File(filePath);
            file.setWritable(true);
            if (!file.exists()) {
                file.mkdirs();
            }

            fos = new FileOutputStream(filePath + name, true);
            fcout = fos.getChannel();

            ByteBuffer buf = ByteBuffer.wrap(fileContent.getBytes(UTF8));
            buf.put(fileContent.getBytes(UTF8));
            buf.flip();

            fcout.write(buf);
        } catch (IOException e) {
            throw new IOException("写文件内容操作出错:" + e.getMessage(), e);
        } finally {
            if (null != fcout) {
                try {
                    fcout.close();
                } catch (IOException e) {
                    throw new IOException("关闭流出错异常:" + e.getMessage(), e);
                }
            }
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    throw new IOException("关闭流出错异常:" + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 文件复制
     *
     * @param oldfile 旧文件file对象
     * @param newPath 新的路径
     */
    public static void copy(File oldfile, String newPath) throws IOException {
        try {
            if (oldfile.exists()) {
                copy(new FileInputStream(oldfile.getPath()), newPath);
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 文件复制
     *
     * @param inStream 文件输入流
     * @param newPath  新的路径
     */
    public static void copy(InputStream inStream, String newPath) throws IOException {
        int byteread = 0;
        FileOutputStream fs = null;
        try {
            fs = new FileOutputStream(newPath);
            byte[] buffer = new byte[4096];
            while ((byteread = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        } finally {
            CloseableUtil.close(fs, inStream);
        }
    }

    /**
     * 下载文件。
     *
     * @param response
     * @param fullPath 文件的全路径
     * @param fileName 文件名称。
     * @throws IOException
     */
    public static String downLoadFile(HttpServletRequest request,
                                      HttpServletResponse response, String fullPath, String fileName)
        throws IOException {
        OutputStream outp = response.getOutputStream();
        File file = new File(fullPath);
        if (file.exists()) {
            downLoadFile(request, response, new FileInputStream(fullPath), fileName);
            return "success";
        } else {
            return "文件不存在!";
        }
    }

    public static void downLoadFile(HttpServletRequest request,
                                    HttpServletResponse response, InputStream in, String fileName)
        throws UnsupportedEncodingException, IOException {

        OutputStream outp = response.getOutputStream();

        response.setContentType("APPLICATION/OCTET-STREAM");
        String filedisplay = fileName;
        String agent = (String)request.getHeader("USER-AGENT");
        // firefox，谷歌 Trident是标识是ie浏览器 特别处理ie11 的问题
        if (agent != null && agent.indexOf("MSIE") == -1 && agent.indexOf("Trident") == -1) {
            String enableFileName = "=?UTF-8?B?" + (new String(Base64.getBase64(filedisplay))) + "?=";
            response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName);
        } else {
            // ie
            filedisplay = URLEncoder.encode(filedisplay, "utf-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);
        }
        try {
            outp = response.getOutputStream();
            byte[] b = new byte[1024];
            int i = 0;
            while ((i = in.read(b)) > 0) {
                outp.write(b, 0, i);
            }
            outp.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
                in = null;
            }
            if (outp != null) {
                outp.close();
                outp = null;
                response.flushBuffer();
            }
        }
    }

    public static String readFile(String filePath) throws IOException {
        try {

            File file = new File(filePath);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                return readFile(file);
            } else {
                throw new IOException("找不到指定的文件" + filePath);
            }
        } catch (Exception e) {
            throw new IOException("读取文件内容出错:" + e.getMessage(), e);
        }
    }

    private static String readFile(File file)
        throws UnsupportedEncodingException, FileNotFoundException,
        IOException {
        return readInputStream(new FileInputStream(file));
    }

    public static String readInputStream(InputStream inputStream) throws UnsupportedEncodingException,
        IOException {
        StringBuffer html = new StringBuffer();
        InputStreamReader read = new InputStreamReader(inputStream, UTF8);// 考虑到编码格式
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt = null;
        while ((lineTxt = bufferedReader.readLine()) != null) {
            html.append(lineTxt);
        }
        read.close();
        return html.toString();
    }

    /**
     * 获取文件后缀
     *
     * @param fileName 文件名
     * @return
     */
    public static String getFileSuffix(String fileName) {
        String fileExtName = "";
        if (fileName.contains(".")) {
            fileExtName = fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            logger.info("Fail to upload file, because the format of filename is illegal.");
            return null;
        }
        return fileExtName;
    }

    /**
     * @param @param file    设定文件
     * @return void    返回类型
     * @throws
     * @Title: judeFileExists
     * @Description: 判断文件是否存在
     * @author NiuYangYang
     * @date 2017年7月3日 下午6:20:45
     */
    public static void judeFileExists(File file) {

        if (file.exists()) {
            System.out.println("file exists");
        } else {
            System.out.println("file not exists, create it ...");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @param @param file    设定文件
     * @return void    返回类型
     * @throws
     * @Title: judeDirExists
     * @Description: 判断文件夹是否存在, 不存在创建
     * @author NiuYangYang
     * @date 2017年7月3日 下午6:21:04
     */
    public static void judeDirExists(File file) {

        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * 读取文件为一个Set<String>每行为一个元素
     *
     * @param path 文件路径
     * @return
     * @author andy.hu
     */
    public static Set<String> readFileToSet(String path) {
        FileReader reader = null;
        BufferedReader br = null;
        Set<String> set = new HashSet<String>();
        try {
            reader = new FileReader(path);
            br = new BufferedReader(reader);
            String str = null;
            while ((str = br.readLine()) != null) {
                if (StringUtil.isNotBlank(str)) {
                    set.add(str);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return set;
    }
}
