package io.xlauncher.dao.impl;

import io.xlauncher.dao.WorkerDao;
import io.xlauncher.entity.KeyEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WorkerDaoImpl implements WorkerDao {

    //打印日志
    Log log = LogFactory.getLog(WorkerDaoImpl.class);

    //http请求方式
    RestTemplate restTemplate = new RestTemplate();

    //注册worker的service信息
    public KeyEntity setKeyValues(String url, Object[] parames) throws Exception {
        KeyEntity entity = new KeyEntity();

        try {
            //构造请求消息头
            HttpHeaders headers = new HttpHeaders();
            HttpEntity httpEntity = new HttpEntity(null, headers);
            //请求etcd
            ResponseEntity<KeyEntity> responseEntity =  restTemplate.exchange(url, HttpMethod.PUT,httpEntity,KeyEntity.class,parames);
            //判断返回结果
            if (responseEntity.getStatusCodeValue() == 200 || responseEntity.getStatusCodeValue() == 201){
                entity = responseEntity.getBody();
            }
        }catch (Exception e){
            log.error("WorkerDaoImpl,setKeyValues,Create or set key and values err",e);
        }finally {
            return entity;
        }
    }

    //监控Master service 的信息
    public KeyEntity watcherMasterService(String url, Object[] parames) throws Exception {
        KeyEntity entity = new KeyEntity();

        //请求etcd响应
        try {
            //构造请求的消息头
            HttpHeaders headers = new HttpHeaders();
            HttpEntity httpEntity = new HttpEntity(null,headers);
            //请求etcd
            ResponseEntity<KeyEntity> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, KeyEntity.class, parames);
            //判断返回结果
            if (responseEntity.getStatusCodeValue() == 200){
                entity = responseEntity.getBody();
            }
        }catch (Exception e){
            log.error("WorkerDaoImpl,setKeyValues,Watcher master service error",e);
        }finally {
            return entity;
        }
    }

    public KeyEntity deleteWorkerSerive(String url) throws Exception {
        KeyEntity entity = new KeyEntity();
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity httpEntity = new HttpEntity(null,headers);
            ResponseEntity<KeyEntity> responseEntity = restTemplate.exchange(url,HttpMethod.DELETE,httpEntity,KeyEntity.class);
            if (responseEntity.getStatusCodeValue() == 200){
                entity = responseEntity.getBody();
            }
        }catch (Exception e){
            log.error("WorkerDaoImpl,deleteWorkerSerive,delete worker service error",e);
        }finally {
            return entity;
        }
    }
}
