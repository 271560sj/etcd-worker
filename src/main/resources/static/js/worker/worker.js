"use strict"
define([
    "jquery",
    "vue",
],function($,Vue){
     var vue = new Vue({
         el: "#index",
         data: {
             path: {
                 registryWorkInfosPath: "/etcd/registryService",//注册worker service的信息
                 deleteWorkerServicePath: "/etcd/deleteService",//删除worker service的信息
                 WatcherMasterServicePath: "/etcd/watcherService",//监控Master service的信息
             },
             objData: {
                 nodes: [],//记录Node信息的数组
                 nodeInfo: {
                     action: "",//操作类型
                     node: {//node的信息
                         key: "",//key的信息
                         value: "",//key值
                         ttl: 0,//生存时间
                         dir: false,//是否是目录
                     },
                 }
             }
         },
         mounted:function(){
             var _self = this;
             setInterval(_self.registryWorkerService(),1000 * 8);
             setInterval(_self.deleteWorkerService,1000 * 9);
             setInterval(_self.watcherMasterService,1000 * 10);
         },
         methods: {
             registryWorkerService: function(){
                 var _self = this;
                 $.ajax({
                    url: _self.path.registryWorkInfosPath,
                    type: "POST",
                    dataType: "json",
                    success: function(data){
                        // if(data != undefined){
                        //     var nodes = _self.objData.nodeInfo;
                        //     nodes.action = data.action;
                        //     if (data.node != undefined){
                        //         nodes.node.key = data.node.key;
                        //         if (data.node.value != undefined){
                        //             nodes.node.value = data.node.value;
                        //         }
                        //         nodes.node.ttl = data.node.ttl;
                        //         nodes.node.dir = data.node.dir;
                        //     }
                        //     _self.objData.nodes.push(nodes);
                        // }
                        _self.dealData(data,"Worker");
                    },
                    error: function(data){
                    }

                 })
             },
             deleteWorkerService: function(){
                 var _self = this;
                 $.ajax({
                     url: _self.path.deleteWorkerServicePath,
                     type: "POST",
                     dataType: "json",
                     success: function (data) {
                         // if(data != undefined){
                         //     var nodes = _self.objData.nodeInfo;
                         //     nodes.action = data.action;
                         //     if (data.node != undefined){
                         //         nodes.node.key = data.node.key;
                         //         if (data.node.value != undefined){
                         //             nodes.node.value = data.node.value;
                         //         }
                         //         nodes.node.ttl = data.node.ttl;
                         //         nodes.node.dir = data.node.dir;
                         //     }
                         //     _self.objData.nodes.push(nodes);
                         // }
                         _self.dealData(data,"Worker");
                     },
                     error: function (data) {

                     }
                 })
             },
             watcherMasterService: function () {
                 var _self = this;
                 $.ajax({
                     url: _self.path.WatcherMasterServicePath,
                     type: "POST",
                     dataType: "json",
                     success: function (data) {
                         // if(data != undefined){
                         //     var nodes = _self.objData.nodeInfo;
                         //     nodes.action = data.action;
                         //     if (data.node != undefined){
                         //         nodes.node.key = data.node.key;
                         //         if (data.node.value != undefined){
                         //             nodes.node.value = data.node.value;
                         //         }
                         //         nodes.node.ttl = data.node.ttl;
                         //         nodes.node.dir = data.node.dir;
                         //     }
                         //     _self.objData.nodes.push(nodes);
                         // }
                         _self.dealData(data,"Master");
                     },
                     error: function (data) {

                     }
                 })
             },
             dealData:function (data,doing) {
                 var _self = this;
                 if(data != undefined){
                     var nodes = _self.objData.nodeInfo;
                     if (data.errorCode == 0){
                         nodes.action = doing + " " + data.action;
                         if (data.node != undefined){
                             nodes.node.key = data.node.key;
                             nodes.node.value = data.node.value;
                             nodes.node.ttl = data.node.ttl;
                             nodes.node.dir = data.node.dir;
                         }
                         _self.objData.nodes.push(nodes);
                     }
                 }
             }
         }

     });
})