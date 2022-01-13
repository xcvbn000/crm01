var treeObj;
$(function () {
    loadModuleInfo();
});


function loadModuleInfo() {
    //发送Ajax查询所有的资源模块信息
    //{id:1,pId:-1,name:zhangs}
    $.ajax({
        type: "post",
        url: ctx + "/module/findModules",
        data:{"roleId":$("#roleId").val()},
        dataType: "json",
        success: function (data) {
            console.log(data);
            var setting = {
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                view: {
                    showLine: false
                },
                check: {
                    enable: true
                },
                callback: {
                    onCheck: zTreeOnCheck
                }
            };

            var zNodes = data;

            $(document).ready(function () {
                 treeObj=  $.fn.zTree.init($("#test1"), setting, zNodes);
            });

        }
    });
}

function zTreeOnCheck(event, treeId, treeNode) {
   var nodes=treeObj.getCheckedNodes(true);//获取每个授权信息的对象
   var roleId=$("#roleId").val();

   //收集数据发送ajax请求
    var mids="mids=";
    for(var i=0;i<nodes.length;i++){
        if(i<nodes.length-1){
            mids=mids+nodes[i].id+"&mids=";
        }else{
            mids=mids+nodes[i].id;
        }
    }

    $.ajax({
        type:"post",
        url:ctx+"/role/addGrant",
        data:mids+"&roleId="+roleId,
        dataType:"json",
        success:function (data) {
            alert(data.msg);
        }
    })
}
