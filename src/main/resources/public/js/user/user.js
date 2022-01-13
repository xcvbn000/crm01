layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    /**
     * 用户列表展示
     */
    var tableIns = table.render({
        elem: '#userList',
        url : ctx+'/user/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "userListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: "id", title:'编号',fixedu:"true", width:80},
            {field: 'userName', title: '用户名', minWidth:50, align:"center"},
            {field: 'email', title: '用户邮箱', minWidth:100, align:'center'},
            {field: 'phone', title: '用户电话', minWidth:100, align:'center'},
            {field: 'trueName', title: '真实姓名', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center',minWidth:150},
            {field: 'updateDate', title: '更新时间', align:'center',minWidth:150},
            {title: '操作', minWidth:150, templet:'#userListBar',fixed:"right",align:"center"}
        ]]
    });

    //表单的重载
    //    实现搜索功能，也就是页面重载
    $(".search_btn").click(function (){
        tableIns.reload({
            where:{
                userName:$("input[name=userName]").val(),
                phone:$("input[name=phone]").val(),
                email:$("input[name=email]").val()
            },
            page:{
                curr:1//重新从第一页开始加载
            }
        });
    });
    //头部工具栏的绑定
    //监听行工具事件
    table.on('toolbar(users)', function(obj){
        var checkStatus = table.checkStatus(obj.config.id);
        switch(obj.event) {
            case 'add':
                //layer.msg("添加");
                openAddOrUpdateUserPage();//传到controller层的函数，controller层跳转添加的相关页面
                break;
            case 'del':
                //layer.msg("删除");
                deleteUser(checkStatus.data);
                break;
        }
    });
    //行内工具栏的绑定
    //监听行工具事件
    table.on('tool(users)', function(obj){
        var data = obj.data;
        //console.log(obj)
        if(obj.event === 'del'){
            layer.confirm('真的删除行么', function(index){
                obj.del();
                layer.close(index);
            });
        } else if(obj.event === 'edit'){
            //layer.msg("修改")
            openAddOrUpdateUserPage(data.id);
        }
    });

    function openAddOrUpdateUserPage(userId){
        var title="<h2>用户模块--添加</h2>";
        var url=ctx+"/user/addOrUpdatePage";
        //判断是修改还是添加
        if(userId){
            title="<h2>用户模块--修改</h2>";
            url=url+"?id="+userId;
        }
        layer.open({
            title:title,
            content:url,
            type:2,
            area:["650px","400px"],
            maxmin:true
        })

    }

    //批量删除
    function deleteUser(dates){
        if(dates.length==0){
            layer.msg("请选择要删除的数据");
            return ;
        }
        layer.confirm("确定要删除数据吗",{
            btn:["确定","取消"]
        },function (index){
            //关闭
            layer.close(index);
            //收集数据
            var ids="&ids=";
            for (var i = 0; i < dates.length; i++) {
                if(i<dates.length-1){
                    ids=ids+dates[i].id+"&ids=";
                }else{
                    ids=ids+dates[i].id;
                }
                //发送ajax删除数据
                $.post(ctx+"/user/delete",ids,function (result){
                    if(result.code==200){
                        //重新加载数据，刷新页面
                        tableIns.reload();
                    }else{
                        //提示信息
                        layer.msg(result.msg,{icon:5})
                    }
                },"json");
            }
        })
    }
});