package com.fa.ebs.wechat.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

/**
 * Description:
 *
 * @Author: wuchenglin
 * @Date： 2020/7/1
 */
@Configuration
@Slf4j
public class CustomPropertySourceFactory extends DefaultPropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource encodedResource) throws IOException {
        FileSystemResourceLoader resourceLoader = new FileSystemResourceLoader();
        //取得当前活动的环境名称（因为直接获取spring.profiles.active 失败，所以才把环境名称拼在文件名后面来拿）

        String[] actives = encodedResource.getResource().getFilename().split("\\.")[0].replace(name + "-", "").split(",");
        log.info("encodedResource : {},actives : {} ,activesLength : {}, actives[0] : {}",encodedResource,Arrays.toString(actives),actives.length,actives[0]);
        //如果只有一个，就直接返回
        if (actives.length > 1) {
            return loadProperties(actives,actives.length-1);
        }else {
            return new ResourcePropertySource(encodedResource);
        }
    }
    /**
     *
     * @Title loadProperties
     * @Description:
     * @param actives
     * @param length
     * @return ResourcePropertySource
     * @throws IOException
     * @author 伍成林
     * @date 2020年7月1日
     */
    private ResourcePropertySource loadProperties(String[] actives,int length) throws IOException {
        String[] split = actives[0].split("-");
        // 拼接配置文件的名字
        String name = "/"+split[0]+"-"+actives[length]+".properties";
        if (length == 0){
            name = "/"+actives[0]+".properties"; //
        }
        if ("local".equals(actives[length])){
            name = "/application-local.properties";
        }
        Properties properties = new Properties();
        try {
            // 如果找不到当前文件则获取下一个
            properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource(name));
        }catch (FileNotFoundException exception){
            return loadProperties(actives,length-1);
        }
        String appid = properties.getProperty("gzh.appid");
        String secret = properties.getProperty( "gzh.secret");
        String token =properties.getProperty("gzh.token");
        String encodingAESKey = properties.getProperty("gzh.encodingAESKey");
        boolean isCover = StringUtils.isBlank(appid) || StringUtils.isBlank(secret) || StringUtils.isBlank(token) || StringUtils.isBlank(encodingAESKey);
        log.info("最后的APPID为：{} isCover : {}",appid,isCover);

        if (isCover && length >0){
            return loadProperties(actives,length-1);
        }
        return getResourcePropertySource(name);

    }
    /**
     *
     * @Title getResourcePropertySource
     * @Description: 通过名字读取配置文件并返回
     * @param name 配置文件的名字
     * @return ResourcePropertySource
     * @throws IOException
     * @author 伍成林
     * @date 2020年7月1日
     */
    private ResourcePropertySource getResourcePropertySource(String name) throws IOException {
        log.info("最后读取的配置文件为：{}",name);
        InputStream inputStream = this.getClass().getResourceAsStream(name);
        //转成resource
        Resource resource = new InputStreamResource(inputStream);

        return new ResourcePropertySource(name,resource);
    }
//    private Properties loadYml(EncodedResource resource) throws IOException {
//        PropertiesFactoryBean factory = new PropertiesFactoryBean();
//        factory.setResources(resource.getResource());
//        factory.afterPropertiesSet();
//        return factory.getObject();
//    }

}
