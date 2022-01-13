layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
//角色列表展示
    var tableIns = table.render({
        elem: '#roleList',
        url : ctx+'/role/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "roleListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: "id", title:'编号',fixed:"true", width:80},
            {field: 'roleName', title: '角色名', minWidth:50, align:"center"},
            {field: 'roleRemark', title: '角色备注', minWidth:100,
                align:'center'},
            {field: 'createDate', title: '创建时间', align:'center',minWidth:150},
            {field: 'updateDate', title: '更新时间', align:'center',minWidth:150},
            {title: '操作', minWidth:150,
                templet:'#roleListBar',fixed:"right",align:"center"}
        ]]
    });
// 多条件搜索重载
    $(".search_btn").on("click",function(){
        table.reload("roleListTable",{
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                roleName: $("input[name='roleName']").val()
            }
        })
    });

    //头部工具栏
    table.on('toolbar(roles)', function(obj){
        var checkStatus = table.checkStatus(obj.config.id);
        switch(obj.event) {
            case 'add':
                //跳转页面
                openAddOrUpdateRolePage();
                break;
            case 'grant'://授权
                toRoleGrant(checkStatus.data);
                break;
        }
    });


    //行内工具栏的绑定
    //监听行工具事件
    table.on('tool(roles)', function(obj){
        var data = obj.data;
        if(obj.event === 'del'){
            layer.confirm('真的删除行么', function(index){
               $.post(ctx+"/role/delete",{id:data.id},function (result){
                   if(result.code==200){
                       layer.msg("删除成功",{icon:6});
                       //加载
                       tableIns.reload();
                   }else{
                       layer.msg(result.msg(),{icon:5});
                   }
                })
            });
        } else if(obj.event === 'edit'){
            openAddOrUpdateRolePage(data.id);
        }
    });

    /**
     * 添加或修改
     * @param id
     */
    function openAddOrUpdateRolePage(id){
        var title="<h2>角色模块添加</h2>"
        var url=ctx+"/role/toAddOrUpdate"
        if(id){
            title="<h2>角色模块修改</h2>";
            url=url+"?id="+id;
        }
        //弹出层
        layui.layer.open({
            title : title,
            type : 2,
            area:["600px","280px"],
            maxmin:true,
            content : url
        });
    }

    function toRoleGrant(datas){
        //不可以批量授权
        if(datas.length==0){
            layer.msg("请选择待授权角色记录",{icon:5});
        }
        if(datas.length>1){
            layer.msg("暂不支持授权修改角色",{icon:5})
        }
        var url=ctx+"/role/toAddGrantPage?roleId="+datas[0].id;
        var title="<h2>角色管理-角色授权</h2>"
        layui.layer.open({
            title:title,
            maxmin:true,
            content: url,
            type:2,
            area:["600px","280px"]
        })
    }
});