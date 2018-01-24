package io.xlauncher.service.impl;

import com.alibaba.fastjson.JSONObject;
import io.xlauncher.dao.WorkerDao;
import io.xlauncher.entity.KeyEntity;
import io.xlauncher.entity.RegistryEntity;
import io.xlauncher.service.WorkerService;
import io.xlauncher.utils.ReadPropertiesUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkerServiceImpl implements WorkerService {

    @Autowired
    WorkerDao workerDao;

    //获取配置文件信息
    @Autowired
    ReadPropertiesUtils properties;
    //设置worker service的信息
    public void setKeyValues() throws Exception {
        //构造请求的URL
        String url = getUrls();
        //构造请求的参数
        Object[] parames = getParameValues();

        //运行线程
        SetKeyValuesThread setKeyValuesThread = new SetKeyValuesThread(url,parames);
        Thread thread = new Thread(setKeyValuesThread);
        thread.start();

    }

    //监控Master service的信息变化
    public void watcherMasterService() throws Exception {
        //构造请求的URL
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("http://").append(properties.getProperty("worker.registry.etcd.ip") + ":" + properties.getProperty("worker.registry.etcd.port"))
                .append("/v2/keys/").append(properties.getProperty("worker.listen.wait.key")).append("?wait=true").
                append(Integer.parseInt(properties.getProperty("worker.listen.wait.index")) == 0 ? "" : "&waitIndex=" + Integer.parseInt(properties.getProperty("worker.listen.wait.index")));
        String url = stringBuffer.toString();

        //构造请求参数
        Object[] parames = getParameValues();

        //运行线程
        WatcherMasterServiceThread watcherMasterServiceThread = new WatcherMasterServiceThread(url,parames);
        Thread thread = new Thread(watcherMasterServiceThread);
        thread.start();
    }

    //删除worker service
    public void deleteWorkerService() throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("http://").append(properties.getProperty("worker.registry.etcd.ip") + ":" + properties.getProperty("worker.registry.etcd.port"))
                .append("/v2/keys/").append(properties.getProperty("worker.registry.key"))
                .append(Boolean.parseBoolean(properties.getProperty("worker.registry.recursive")) == true
                        && Boolean.parseBoolean(properties.getProperty("worker.registry.dir")) == false
                        ? "?recursive=" + Boolean.parseBoolean(properties.getProperty("worker.registry.recursive"))
                        : "?dir=" + Boolean.parseBoolean(properties.getProperty("worker.registry.dir")));
        String url = stringBuffer.toString();
        DeleteworkerServiceThread deleteworkerServiceThread = new DeleteworkerServiceThread(url);
        Thread thread = new Thread(deleteworkerServiceThread);
        thread.start();
    }

    //获取URL
    private String getUrls(){
        StringBuffer url = new StringBuffer();

        url.append("http://").append(properties.getProperty("worker.registry.etcd.ip") + ":" + properties.getProperty("worker.registry.etcd.port"))
                .append("/v2/keys/").append(properties.getProperty("worker.registry.key")).append("?value={value}");
        if (Integer.parseInt(properties.getProperty("worker.registry.ttl")) > 0){
            url.append("&ttl={ttl}");//ttl设置Key的生存时间
        }
        if (Boolean.parseBoolean(properties.getProperty("worker.registry.dir")) == true){
            url.append("&dir={dir}");//设置Key是否为目录
        }
        if (Boolean.parseBoolean(properties.getProperty("worker.registry.recursive"))){
            url.append("&recursive={recursive}");//设置是否进行级联操作
        }
        return url.toString();
    }

    private Object[] getParameValues(){
        //构造注册的worker的服务信息
        RegistryEntity entity = new RegistryEntity();
        entity.setServiceIP(properties.getProperty("worker.registry.service.ip"));
        entity.setServicePort(properties.getProperty("worker.registry.service.port"));
        entity.setServiceName(properties.getProperty("worker.registry.service.name"));

        Object[] parames = new Object[4];
        parames[0] = JSONObject.toJSONString(entity);
        parames[1] = Integer.parseInt(properties.getProperty("worker.registry.ttl"));
        parames[2] = Boolean.parseBoolean(properties.getProperty("worker.registry.dir"));
        parames[3] = Boolean.parseBoolean(properties.getProperty("worker.registry.recursive"));
        return parames;
    }

    class SetKeyValuesThread implements Runnable{

        //打印日志
        Log log = LogFactory.getLog(SetKeyValuesThread.class);
        //请求的URL
        private String url;

        //传递的参数
        private Object[] parames;

        //构造方法,为URL与传递的参数赋值
        public SetKeyValuesThread(String url, Object[] parames){
            this.url = url;
            this.parames = parames;
        }

        //线程运行方式
        public void run() {
            //计数
            int count = 0;
            while (true){
                try {
                    //进行 worker service的信息注册
                    KeyEntity entity = workerDao.setKeyValues(url,parames);
                    //将注册后的返回信息打印到日志输出
                    log.info("Registry: " + JSONObject.toJSONString(entity));
                    Thread.sleep(1000 * 60);
                    count ++;
                    if (count > 20){
                        break;
                    }
                }catch (Exception e){
                    log.error("WorkerServiceImpl,SetKeyValues error",e);
                }
            }
        }
    }

    //监控Master service的线程
    class WatcherMasterServiceThread implements Runnable{

        //记录日志
        Log log = LogFactory.getLog(WatcherMasterServiceThread.class);
        //请求的URL
        private String url;

        //传递的参数
        private Object[] parames;

        //构造方法,将请求URL与参数赋值
        public WatcherMasterServiceThread(String url, Object[] parames){
            this.url = url;
            this.parames = parames;
        }

        //线程实现run
        public void run() {
            while (true){
                try {
                    //进行监控的方法
                    KeyEntity entity = workerDao.watcherMasterService(url,parames);
                    if (entity.getNode() != null){
                        //获取监控到的Master service的信息
                        String register = entity.getNode().getValue();
                        RegistryEntity registryEntity = JSONObject.parseObject(register,RegistryEntity.class);
                        //打印到日志
                        log.info("Watcher: " + JSONObject.toJSONString(registryEntity));
                    }
                    Thread.sleep(1000 * 60);
                }catch (Exception e){
                    log.error("WorkerServiceImpl,WatcherMasterService error",e);
                }
            }
        }
    }

    class DeleteworkerServiceThread implements Runnable{

        //打印日志
        Log log =LogFactory.getLog(DeleteworkerServiceThread.class);

        //请求的URL
        private String url;

        public DeleteworkerServiceThread(String url){
            this.url = url;
        }

        //执行删除操作线程
        public void run() {
            try {
                //计数
                int count = 0;
                while (true){
                    KeyEntity entity = workerDao.deleteWorkerSerive(url);
                    if (entity.getNode() != null){
                        String service = entity.getNode().getValue();
                        RegistryEntity registryEntity = JSONObject.parseObject(service,RegistryEntity.class);
                        log.info("Delete: "+ JSONObject.toJSONString(registryEntity));
                    }
                    count ++;
                    if (count > 20){
                        break;
                    }

                    Thread.sleep(1000 * 60);
                }
            }catch (Exception e){
                log.error("WorkerServiceImpl,DeleteworkerServiceThread,delete worker service error",e);
            }
        }
    }
}
