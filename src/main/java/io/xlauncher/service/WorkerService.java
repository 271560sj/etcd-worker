package io.xlauncher.service;

import io.xlauncher.entity.KeyEntity;

public interface WorkerService {

    /**
     * 设置workservice的信息
     * @throws Exception
     */
    void setKeyValues()throws Exception;

    /**
     * 监控Master service的信息
     * @throws Exception
     */
    void watcherMasterService()throws Exception;

    /**
     * 删除worker service
     * @throws Exception
     */
    void deleteWorkerService()throws Exception;

    /**
     * ajax请求注册服务信息
     * @return
     * @throws Exception
     */
    KeyEntity registryWorkerServices()throws Exception;

    /**
     * ajax请求删除服务
     * @return
     * @throws Exception
     */
    KeyEntity deleteWorkerServices()throws Exception;

    /**
     * ajax请求监控服务
     * @return
     * @throws Exception
     */
    KeyEntity watcherMasterServices()throws Exception;
}
