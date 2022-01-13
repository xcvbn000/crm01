 layui.use(['form','jquery','jquery_cookie','layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

     //监听提交
     //表单的提交就是触发下面的函数
     form.on('submit(login)', function(data){
         /*layer.alert(JSON.stringify(data.field), {
             title: '最终的提交信息'
         })
          */
            var fieldData=data.field;
            if(fieldData.username=='undefinded' || fieldData.username==''){
                layer.msg("用户名不能为空");
                return ;
            }

            if(fieldData.password=='undefinded' || fieldData.password==''){
                layer.msg("密码不能为空");
                return ;
            }
        //发送ajax,从前端到后端
         $.ajax({
             type:"post",
             url:ctx+"/user/login",
             data:{
                 "userName":fieldData.username,
                 "userPwd":fieldData.password
             },
             dataType:"json",
             success:function (result){
                 if(result.code==200){
                     //layer.msg("登陆成功了",{icon:5});
                     //window.location.href=ctx+"/main"
                     layer.msg("登陆成功了",function (){
                         //将用户数据保存到cookie
                         $.cookie("userIdStr",result.result.userIdStr);
                         $.cookie("userName",result.result.userName);
                         $.cookie("trueName",result.result.trueName);


                         //是否选中记录我
                         if($("input[type='checkbox']").is(":checked")){
                             //设置有效时间7天
                             $.cookie("userIdStr",result.result.userIdStr,{expires:7});
                             $.cookie("userName",result.result.userName,{expires:7});
                             $.cookie("trueName",result.result.trueName,{expires:7});

                         }                         //跳转页面
                         window.location.href=ctx+"/main";
                     })

                 }else {
                     //失败的提示
                     layer.msg(result.msg);
                 }
             }
         })
         //取消默认行为
         return false;
     });
});