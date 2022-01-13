layui.use(['form','jquery','jquery_cookie','layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    //监听提交
    form.on('submit(saveBtn)', function(data){
        var fieldData=data.field;
        //发送到后端数据
        $.ajax({
            type:"post",
            url:ctx+"/user/updatePwd",  //发送到controller
            data:{
                "oldPassword":fieldData.old_password,
                "newPassword":fieldData.new_password,
                "confirmPassword":fieldData.again_password
            },
            dataType:"json",
            success:function (data){//参数相当于后端返回的结果集
                if(data.code==200){
                    layer.msg("修改密码成功，系统将在三秒后退出",function (){
                        //密码修改成功后清空cookie
                        $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"})
                        $.removeCookie("userName",{domain:"localhost",path:"/crm"})
                        $.removeCookie("trueName",{domain:"localhost",path:"/crm"})
                        //清空后跳转页面
                        window.parent.location.href=ctx+"/index";
                    })
                }else {
                    //密码修改失败
                    layer.msg(data.msg)
                }
            }
        })
        //取消默认行为
        return false;
    })
})
