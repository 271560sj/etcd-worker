package io.xlauncher.dao.impl;

import com.alibaba.fastjson.JSONObject;
import io.xlauncher.dao.WorkerDao;
import io.xlauncher.entity.KeyEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class WorkerDaoImpl implements WorkerDao {

    //打印日志
    private final static Log log = LogFactory.getLog(WorkerDaoImpl.class);

    //http请求方式
    private RestTemplate restTemplate = new RestTemplate();

    //注册worker的service信息
    public KeyEntity setKeyValues(String url, Object[] parames) throws Exception {
        KeyEntity entity = new KeyEntity();

        try {
//            //构造请求消息头
//            HttpHeaders headers = new HttpHeaders();
//            HttpEntity httpEntity = new HttpEntity(null, headers);
//            //请求etcd
//            ResponseEntity<KeyEntity> responseEntity =  restTemplate.exchange(url, HttpMethod.PUT,httpEntity,KeyEntity.class,parames);
            ResponseEntity<KeyEntity> responseEntity = restRequest(url,HttpMethod.PUT,parames);
            //判断返回结果
            if (responseEntity.getStatusCodeValue() == 200 || responseEntity.getStatusCodeValue() == 201){
                entity = responseEntity.getBody();
            }
        }catch (HttpClientErrorException e){
            entity = dealWithError(e,"WorkerDaoImpl,setKeyValues,Create or set key and values err");
        }finally {
            return entity;
        }
    }

    //监控Master service 的信息
    public KeyEntity watcherMasterService(String url) throws Exception {
        KeyEntity entity = new KeyEntity();

        //请求etcd响应
        try {
//            //构造请求的消息头
//            HttpHeaders headers = new HttpHeaders();
//            HttpEntity httpEntity = new HttpEntity(null,headers);
//            //请求etcd
//            ResponseEntity<KeyEntity> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, KeyEntity.class);
            ResponseEntity<KeyEntity> responseEntity = restRequest(url,HttpMethod.GET,new Object[]{});
            //判断返回结果
            if (responseEntity.getStatusCodeValue() == 200){
                entity = responseEntity.getBody();
            }
        }catch (HttpClientErrorException e){
            entity = dealWithError(e,"WorkerDaoImpl,setKeyValues,Watcher master service error");
        }finally {
            return entity;
        }
    }

    //删除worker service的服务信息
    public KeyEntity deleteWorkerSerive(String url) throws Exception {
        KeyEntity entity = new KeyEntity();
        try {
//            HttpHeaders headers = new HttpHeaders();
//            HttpEntity httpEntity = new HttpEntity(null,headers);
//            ResponseEntity<KeyEntity> responseEntity = restTemplate.exchange(url,HttpMethod.DELETE,httpEntity,KeyEntity.class);
            ResponseEntity<KeyEntity> responseEntity = restRequest(url,HttpMethod.DELETE,new Object[]{});
            if (responseEntity.getStatusCodeValue() == 200){
                entity = responseEntity.getBody();
            }
        }catch (HttpClientErrorException e){
            entity = dealWithError(e,"WorkerDaoImpl,deleteWorkerSerive,delete worker service error");
        }finally {
            return entity;
        }
    }

    //处理错误信息
    private KeyEntity dealWithError(HttpClientErrorException e, String message){
        String error = e.getResponseBodyAsString();
        KeyEntity entity = JSONObject.parseObject(error,KeyEntity.class);
        log.error(message,e);
        return entity;
    }

    //请求RESTful
    private ResponseEntity<KeyEntity> restRequest(String url, HttpMethod method, Object[] parames){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity(null,headers);

        return restTemplate.exchange(url,method,httpEntity,KeyEntity.class,parames);
    }
}
