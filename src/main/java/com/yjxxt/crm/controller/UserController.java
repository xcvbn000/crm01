package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.model.UserModel;
import com.yjxxt.crm.query.UserQuery;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;


    @RequestMapping("login")
    @ResponseBody
    public ResultInfo say(User user){
        ResultInfo resultInfo=new ResultInfo();
            UserModel userModel = userService.userLogin(user.getUserName(), user.getUserPwd());
            resultInfo.setResult(userModel);
        return resultInfo;
    }
    @RequestMapping("setting")
    @ResponseBody
    public ResultInfo sayUpdate(User user){
        ResultInfo resultInfo=new ResultInfo();
        userService.updateByPrimaryKeySelective(user);
        return resultInfo;
    }

     @RequestMapping("save")
     @ResponseBody
     public ResultInfo save(User user){
            userService.addUser(user);
            return success("用户添加成功");
    }

    @RequestMapping("update")
     @ResponseBody
     public ResultInfo update(User user){
            userService.changeUser(user);
            return success("用户修改成功");
    }
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer[] ids){
        userService.removeUserIds(ids);
        return success("批量删除成功");
    }



    @RequestMapping("updatePwd")
    @ResponseBody
    public ResultInfo updatePwd(HttpServletRequest req,String oldPassword,String newPassword,String confirmPassword){
        ResultInfo resultInfo=new ResultInfo();
        //封装中可以通过req获取到userId
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //修改密码操作
        userService.changeUserPwd(userId,oldPassword,newPassword,confirmPassword);

        return resultInfo;
    }

    @RequestMapping("toPasswordPage")
    public String updatePwd(){
        return "user/password";
    }

   @RequestMapping("index")
    public String index(){
        return "user/user";
    }

    @RequestMapping("addOrUpdatePage")
    public String addOrUpdatePage(Integer id, Model model){
        if(id!=null){
            User user = userService.selectByPrimaryKey(id);
            model.addAttribute("user",user);
        }
        return "user/add_update";
    }

    @RequestMapping("toSettingPage")
    public String setting(HttpServletRequest req){
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        User user = userService.selectByPrimaryKey(userId);
        req.setAttribute("user",user);
        return "user/setting";
    }

    //查询所有销售人员
    @RequestMapping("sales")
    @ResponseBody
    public List<Map<String,Object>> findSales(){
        List<Map<String,Object>> list = userService.querySales();
        return list;
    }
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> list(UserQuery userQuery){
        return userService.findUserByParams(userQuery);

    }





}
