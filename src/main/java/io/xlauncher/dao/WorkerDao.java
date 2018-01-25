package io.xlauncher.dao;

import io.xlauncher.entity.KeyEntity;

public interface WorkerDao {

    /**
     * 设置workerservice的Key和value值
     * @param url
     * @param parames
     * @return
     * @throws Exception
     */
    KeyEntity setKeyValues(String url, Object[] parames)throws Exception;

    /**
     * 监控Master service的服务信息
     * @param url
     * @return
     * @throws Exception
     */
    KeyEntity watcherMasterService(String url)throws Exception;

    /**
     * 删除worker service的服务信息
     * @param url
     * @return
     * @throws Exception
     */
    KeyEntity deleteWorkerSerive(String url)throws Exception;
}
