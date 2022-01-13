layui.use(['form','jquery','jquery_cookie','layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

   //监听表单提交事件,确认信息修改保存的按钮监听
    form.on("submit(saveBtn)",function (data){
        $.ajax({
            type:"post",
            url:ctx+"/user/setting",
            data:{
                "userName":data.field.userName,
                "phone":data.field.phone,
                "email":data.field.email,
                "trueName":data.field.trueName,
                "id":data.field.id
            },
            dataType:"json",
            success:function (result){
                //判断是否修改成功
                if(result.code==200){
                    //修改成功
                    layer.msg("保存成功了",function (){
                        //清空之前的cookie
                        $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"});
                        $.removeCookie("userName",{domain:"localhost",path:"/crm"});
                        $.removeCookie("trueName",{domain:"localhost",path:"/crm"});
                        //页面跳转
                        window.parent.location.href=ctx+"/index";
                    })
                }else {
                    layer.msg(result.msg)
                }
            }

        })
    })
});