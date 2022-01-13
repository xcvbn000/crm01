package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.mapper.PermissionMapper;
import com.yjxxt.crm.service.PermissionService;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

//因为index页面包含了common.ftl页面，里面大量的使用项目名，所以继承这个类里面含有生成的项目名
@Controller
public class IndexController extends BaseController {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserService userService;
    /**
     * 系统登录页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "index";
    }

    /**
     * 系统欢迎页面
     * @return
     */
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }

    /**
     * 后台主页面
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest req){
        //获取当前用户信息
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //通过用户id查询信息
        User user = userService.selectByPrimaryKey(userId);
        //存储
        req.setAttribute("user",user);
        List<String> permissions = permissionService.queryUserHasRolesHasPermissions(userId);
        req.getSession().setAttribute("permissions",permissions);
        //转发
        return "main";

    }
}
