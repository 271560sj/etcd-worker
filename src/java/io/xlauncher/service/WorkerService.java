package io.xlauncher.service;

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
}
