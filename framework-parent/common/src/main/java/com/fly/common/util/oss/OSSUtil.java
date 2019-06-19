package com.fly.common.util.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.fly.common.config.oss.OSSConfig;
import com.fly.common.core.properties.OSSFolder;
import com.fly.common.core.properties.OSSProperties;
import com.fly.common.util.DateUtil;
import com.fly.common.util.StringUtil;
import com.netflix.client.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.stream.FileImageInputStream;
import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Configuration
public class OSSUtil {

    @Resource
    private OSSFolder ossFolder;

    @Resource
    private OSSProperties properties;

    @Resource
    private OSSConfig ossConfig;

    /**
     * 上传
     *
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public String[] upload(InputStream inputStream, FileModel pic) throws IOException {
        OSS ossClient = ossConfig.OSSClient();
        String[] fo = new String[]{"", ""};
        try {
            // 创建上传Object的Metadata
            ObjectMetadata metadata = getObjectMetadata(pic.getOriginalFilename(), pic.getFileSzie());
            // 上传文件 (上传文件流的形式)
            PutObjectResult putResult = ossClient.putObject(properties.getBucketName(), pic.getUrl(), inputStream, metadata);
//            // 设置文件的访问权限为公共读。
//            ossClient.setObjectAcl(properties.getBucketName(), pic.getUrl(), CannedAccessControlList.PublicRead);
            // 解析结果
            fo[0] = this.getUrl(pic.getUrl());
            fo[1] = pic.getUrl();

        } finally {
            ossClient.shutdown();
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return fo;
    }

    /**
     * 上传
     *
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public String[] upload(InputStream inputStream, FileModel pic, String bucketName) throws IOException {
        OSS ossClient = ossConfig.OSSClient();
        String[] fo = new String[]{"", ""};
        if (StringUtils.isEmpty(bucketName)) {
            bucketName = properties.getBucketName();
        } else {
            if (!ossClient.doesBucketExist(bucketName)) {
                ossClient.createBucket(bucketName);
            }
        }
        try {
            // 创建上传Object的Metadata
            ObjectMetadata metadata = getObjectMetadata(pic.getOriginalFilename(), pic.getFileSzie());
            // 上传文件 (上传文件流的形式)
            PutObjectResult putResult = ossClient.putObject(bucketName, pic.getUrl(), inputStream, metadata);
            // 设置文件的访问权限为公共读。
            ossClient.setObjectAcl(bucketName, pic.getUrl(), CannedAccessControlList.PublicRead);
            // 解析结果
            fo[0] = this.getUrl(pic.getUrl());
            fo[1] = pic.getUrl();

        } finally {
            ossClient.shutdown();
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return fo;
    }

    public String[] uploadObjectOSS(String file, String userId)
            throws IOException {
        OSS ossClient = ossConfig.OSSClient();
        String resultStr = null;
        String[] fo = new String[]{"", ""};

        // 以输入流的形式上传文件
        String folder = StringUtil.append(ossFolder.getFolder(), "/", userId, "/", ossFolder.getFormat(), "/");
        file = file.substring(file.lastIndexOf("."));
        log.info("上传到路径" + folder + file);
        // 文件大小
        Integer fileSize = file.length();
        // 创建上传Object的Metadata

        ObjectMetadata metadata = getObjectMetadata(file, (long) fileSize);
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file.getBytes("UTF-8"))) {
            // 上传文件 (上传文件流的形式)
            PutObjectResult putResult = ossClient.putObject(properties.getBucketName(), folder + file, byteArrayInputStream, metadata);
            // 解析结果
            resultStr = putResult.getETag();
            fo[1] = folder + file;
            fo[0] = this.getUrl(folder + file);

            ossClient.shutdown();
        }
        return fo;
    }

    // 上传视频
    public String uploadByteVideoOSS(byte[] b, String userId) throws IOException {

        OSS ossClient = ossConfig.OSSClient();

        // 以输入流的形式上传文件
        String folder = StringUtil.append(ossFolder.getFolderVideo(), "/", userId, "/", ossFolder.getFormat(), "/");
        // 文件名
        String fileName = StringUtil.append(".MP4");
        log.info("上传到路径" + folder + fileName);

        Long fileSize = (long) b.length;

        // 创建上传Object的Metadata
        ObjectMetadata metadata = getObjectMetadata(fileName, fileSize);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(b)) {
            ossClient.putObject(properties.getBucketName(), folder + fileName, byteArrayInputStream, metadata);
            ossClient.shutdown();
        }
        String filepath = folder + fileName;
        return filepath;
    }

    // 上传图片
    public String uploadByteOSS(byte[] b, String userId) throws IOException {
        OSS ossClient = ossConfig.OSSClient();
        // 以输入流的形式上传文件
        String folder = StringUtil.append(ossFolder.getFolder(), "/", userId, "/", ossFolder.getFormat(), "/");
        // 文件名
        String fileName = StringUtil.append(".jpg");

        Long fileSize = (long) b.length;
        ObjectMetadata metadata = getObjectMetadata(fileName, fileSize);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(b)) {
            ossClient.putObject(properties.getBucketName(), folder + fileName, byteArrayInputStream, metadata);
            ossClient.shutdown();
        }
        String filepath = folder + fileName;
        return filepath;
    }

    private ObjectMetadata getObjectMetadata(String fileName, Long fileSize) {
        // 创建上传Object的Metadata
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileSize);
        // 指定该Object被下载时的网页的缓存行为
        metadata.setCacheControl("no-cache");
        // 指定该Object下设置Header
        metadata.setHeader("Pragma", "no-cache");
        // 指定该Object被下载时的内容编码格式
        metadata.setContentEncoding("utf-8");
        // 文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
        // 如果没有扩展名则填默认值application/octet-stream
        metadata.setContentType(getContentType(fileName));
        // 指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
        metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
        return metadata;
    }

    public byte[] image2Bytes(String imgSrc) throws Exception {
        FileInputStream fin = new FileInputStream(new File(imgSrc));
        // 可能溢出,简单起见就不考虑太多,如果太大就要另外想办法，比如一次传入固定长度byte[]
        byte[] bytes = new byte[fin.available()];
        // 将文件内容写入字节数组，提供测试的case
        fin.read(bytes);

        fin.close();
        return bytes;
    }

    // 图片转化为byte数组
    public byte[] image2byte(String path) throws IOException {
        byte[] data = null;
        try (FileImageInputStream input = new FileImageInputStream(new File(path));
             ByteArrayOutputStream output = new ByteArrayOutputStream();) {
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
        }
        return data;
    }

    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     *
     * @param fileName 文件名
     * @return 文件的contentType
     */
    public String getContentType(String fileName) {
        // 文件的后缀名
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if (".bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if (".gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if (".jpeg".equalsIgnoreCase(fileExtension) || ".jpg".equalsIgnoreCase(fileExtension)
                || ".png".equalsIgnoreCase(fileExtension)) {
            return "image/jpeg";
        }
        if (".html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if (".txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if (".vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if (".ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if (".doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if (".xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        if (".mp4".equalsIgnoreCase(fileExtension)) {
            return "video/mp4";
        }
        if (".pdf".equalsIgnoreCase(fileExtension)) {
            return "application/pdf";
        }
        return "image/jpeg";
    }

    /**
     * 获得url链接
     *
     * @param fileName
     * @return
     */
    public String getUrl(String fileName) {
        return getUrl(properties.getBucketName(), fileName);
    }

    /**
     * 获得url链接
     *
     * @param bucketName
     * @param fileName
     * @return
     */
    public String getUrl(String bucketName, String fileName) {
        OSS ossClient = ossConfig.OSSClient();
        // 设置URL过期时间为10年 3600l* 1000*24*365*10
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, fileName, expiration);
        if (url != null) {
            return url.toString();
        }
        return "获取网址路径出错";
    }

    /**
     * 下载文件
     *
     * @param key
     * @param filename
     * @throws OSSException
     * @throws ClientException
     * @throws IOException
     */
    public void downloadFile(String key, String filename) throws OSSException, ClientException {
        OSS ossClient = ossConfig.OSSClient();

        ossClient.getObject(new GetObjectRequest(properties.getBucketName(), key), new File(filename));
    }

    /**
     * 根据key删除OSS服务器上的文件
     *
     * @param filePath
     */
    public void deleteFile(String filePath) {
        OSS ossClient = ossConfig.OSSClient();
        filePath = filePath.substring(45);
        ossClient.deleteObject(properties.getBucketName(), filePath);

    }

    /**
     * 获取图片信息
     *
     * @param file
     * @param userId
     * @return
     */
    public FileModel getFileProperty(MultipartFile file, String userId) {
        return this.getFileProperty(file, userId, ossFolder.getFormat());

    }

    /**
     * 获取文件信息
     * @param file
     * @param userId
     * @param pattern
     * @param format
     * @return
     */
    public FileModel getFileProperty(MultipartFile file, String userId, String pattern, String format) {
        FileModel fileModel = new FileModel();
        String folder = StringUtil.append(ossFolder.getFolder(), "/", userId, "/", DateUtil.formatDateTime(LocalDateTime.now(), pattern), "/", format, "/");
        // 文件名
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString();


        // 获取上传文件扩展名
        String fileExt = originalFilename.substring(originalFilename.lastIndexOf('.') + 1, originalFilename.length());
        // 对扩展名进行小写转换
        fileExt = fileExt.toLowerCase();
        fileModel.setFileSzie(file.getSize());
        fileModel.setFolder(folder);
        fileModel.setUserId(userId);
        fileModel.setOriginalFilename(originalFilename);
        fileModel.setUrl(StringUtil.append(folder, fileName, ".", fileExt));
        log.info("上传文件信息为:{}", fileModel.toString());
        return fileModel;
    }

    /**
     * 获取文件信息
     *
     * @param file
     * @param userId
     * @param format
     * @return
     */
    public FileModel getFileProperty(MultipartFile file, String userId, String format) {
        return this.getFileProperty(file, userId, DateUtil.DATE_TIME_PATTERN, format);
    }


}
