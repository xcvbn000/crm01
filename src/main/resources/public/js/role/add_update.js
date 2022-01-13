layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;


    form.on("submit(addOrUpdateRole)",function (obj){
        //弹出层
        var index=top.layer.msg("数据正在加载中",{json:16,time:false,shade:0.8});

        //添加时跳转的controller
        var url=ctx+"/role/save";
        //判断是否有id，有则为修改，跳转到controller的修改
        if($("input[name='id']").val()){
            url=ctx+"/role/update";
        }
        $.post(url,obj.field,function (result){
            if(result.code==200){
                //定时器
                setTimeout(function(){
                    //关闭弹出层
                    top.layer.close(index);
                    //提示一下
                    top.layer.msg("添加ok",{icon:6});
                    //关闭iftrme
                    top.layer.closeAll("iframe");
                    //成功之后刷新
                    parent.location.reload();
                },500)

            }else{
                layer.msg(result.msg,{icon:5});
            }
        },"json");
        return false;
    })

    //取消按钮
    $("#closeBtn").click(function (){
        var index=parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    })
});