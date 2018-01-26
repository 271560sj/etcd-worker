package io.xlauncher.controller;

import io.xlauncher.entity.KeyEntity;
import io.xlauncher.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/etcd")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    /**
     * 启动worker service
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/workerService")
    public void workerService()throws Exception{
        //注册worker service
        workerService.setKeyValues();

        //删除worker service
        workerService.deleteWorkerService();

        //监控Master service
        workerService.watcherMasterService();
    }

    /**
     * 通过前端的ajax请求注册服务信息
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/registryService")
    public KeyEntity registryMasterService()throws Exception{
        KeyEntity entity = workerService.registryWorkerServices();
        return entity;
    }

    /**
     * 通过前端的ajax请求删除worker service
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/deleteService")
    public KeyEntity deleteMasterService()throws Exception{
        KeyEntity entity = workerService.deleteWorkerServices();
        return entity;
    }

    /**
     * 监控master service的服务信息，通过前端ajax进行请求
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/watcherService")
    public KeyEntity watcherMasterService()throws Exception{
        KeyEntity entity = workerService.watcherMasterServices();
        return entity;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "getIndex")
    public ModelAndView getIndex()throws Exception{
        return new ModelAndView("index");
    }
}
