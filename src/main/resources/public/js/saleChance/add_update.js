layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    /**
     * 监听submit事件
     * 实现营销机会的添加与更新
     */
    form.on("submit(addOrUpdateSaleChance)", function (data) {
        var index =layer.msg("数据正在提交，请稍等", {
                icon:16,
                time:false,
                shade:0.8
            });
        //判断if是否为空，判断传到controller哪一层
        url=ctx+"/sale_chance/save";
        if($("input[name=id]").val()){
            url=ctx+"/sale_chance/update";
        }
        $.ajax({
            type:"post",
            url:url,
            data:data.field,
            dataType:"json",
            success:function (data){
                if(data.code==200){
                    //提示信息
                    layer.msg("添加完成",{icon:5});
                    //关闭加载层
                    layer.close(index);
                    //关闭ifream
                    layer.closeAll("iframe");
                    window.parent.location.reload();
                }else {
                    layer.msg(data.msg,{icon:6});
                }
            }
        })

        return false; //阻止表单提交
    });
    //取消功能
    $("#closeBtn").click(function(){
        //获取当前弹出层的索引值
        var idx=parent.layer.getFrameIndex(window.name);
        //根据索引关闭
        parent.layer.close(idx);
    });

    //通过ajax添加下拉框
    var assignMan=$("input[name='man']").val();
    $.ajax({
        type:"post",
        url:ctx+"/user/sales",
        dataType: "json",
        success:function (data){
            for (var i in data) {
                if(data[i].id=assignMan){
                    $("#assignMan").append("<option selected value='"+data[i].id+"'>"+data[i].uname+"</option>");
                }else {
                    $("#assignMan").append("<option value='"+data[i].id+"'>"+data[i].uname+"</option>");
                }
            }
            //重新渲染
            layui.form.render("select");
        }


    })

});