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
     * @param parames
     * @return
     * @throws Exception
     */
    KeyEntity watcherMasterService(String url, Object[] parames)throws Exception;
}
