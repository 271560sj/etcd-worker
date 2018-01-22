package io.xlauncher.controller;

import io.xlauncher.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/etcd")
public class WorkerController {

    @Autowired
    WorkerService workerService;

    /**
     * 启动worker service
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/workerService")
    public void workerService()throws Exception{
        //注册worker service
        workerService.setKeyValues();
        //监控Master service
        workerService.watcherMasterService();
    }
}
