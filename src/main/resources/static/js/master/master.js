"use strict"
define([
    "jquery",
    "vue",
],function($,Vue){
     var vue = new Vue({
         el: "#index",
         data: {
           path: {
              registryMasterInfosPath: "/etcd/workerService",

           }
         },
         mounted:function(){
             var _self = this;
             _self.registryMasterInfos();
         },
         methods: {
             registryMasterInfos: function(){
                 var _self = this;
                 $.ajax({
                    url: _self.path.registryMasterInfosPath,
                    type: "GET",
                    dataType: "json",
                    success: function(data){
                        alert("request success!");
                    },
                    error: function(data){
                    }

                 })
             }
         }

     });
})