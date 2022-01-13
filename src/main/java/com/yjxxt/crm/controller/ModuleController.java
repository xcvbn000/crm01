package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.dto.TreeDto;
import com.yjxxt.crm.service.ModuleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {
    @Resource
    private ModuleService moduleService;


    @RequestMapping("findModules")
    @ResponseBody
    public List<TreeDto> findModules(Integer roleId){
        return moduleService.findModulesByRoleId(roleId);
    }
}
