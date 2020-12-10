package com.fa.ebs.ocr.common;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

/**
 * Description:
 *
 * @Author: 伍成林
 * @Date： 2020年 09月14日
 */
public class Base64MultipartFile implements MultipartFile {

    private final byte[] imgContent;

    private final String header;

    private String name;

    private String originalFilename;

    public Base64MultipartFile(byte[] imgContent, String header) {
        this.imgContent = imgContent;
        this.header = header.split(";")[0];
    }


    @Override
    public String getName() {
        if (StringUtils.isBlank(name)){
            this.name = UUID.randomUUID().toString().replaceAll("-","")+"." + header.split("/")[1];
        }
        return name;
    }

    @Override
    public String getOriginalFilename() {
        this.originalFilename = this.getName();
        return this.originalFilename;
    }

    @Override
    public String getContentType() {
        return header.split(":")[1];
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(imgContent);
    }
}
