package com.yjxxt.crm.controller;

import com.yjxxt.crm.annotation.RequiredPermission;
import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.Role;
import com.yjxxt.crm.query.RoleQuery;
import com.yjxxt.crm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {
    @Autowired
    private RoleService roleService;

    @RequestMapping("list")
    @RequiredPermission(code="60")
    @ResponseBody
    public Map<String,Object> list(RoleQuery roleQuery){
        return roleService.findRoleByParam(roleQuery);
    }

    @RequestMapping("findRoles")
    @ResponseBody
    public List<Map<String,Object>> sayRoles(Integer userId){
        return roleService.findRoles(userId);
    }

    @RequestMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer roleId,Integer[] mids){
        roleService.addGrant(roleId,mids);
        return success("授权成功");
    }

    @RequestMapping("index")
    public String index(){
        return "role/role";
    }

    @RequestMapping("toAddOrUpdate")
    public String toAddOrUpdate(Integer id, Model model){
        if(id!=null){
            //判断是否为修改，打开修改页面的时候会自动把原信息填充
            Role role = roleService.selectByPrimaryKey(id);
            model.addAttribute("role",role);
        }
        return "role/add_update";
    }
    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId, Model model){
        model.addAttribute("roleId",roleId);
        return "role/grant";
    }

    @RequestMapping("save")
    @ResponseBody
    public ResultInfo save(Role role){
        roleService.addRole(role);
        return success("角色添加成功");
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(Role role){
        roleService.changeRole(role);
        return success("角色修改成功");
    }
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(Role role){
        roleService.removeRoleById(role.getId());
        return success("角色修改成功");
    }

}
