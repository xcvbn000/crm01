package com.yjxxt.crm.service;

import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.Module;
import com.yjxxt.crm.dto.TreeDto;
import com.yjxxt.crm.mapper.ModuleMapper;
import com.yjxxt.crm.mapper.PermissionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ModuleService extends BaseService<Module,Integer> {
    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private PermissionMapper permissionMapper;

    public List<TreeDto> selectModules(){
        List<TreeDto> treeDtos = moduleMapper.selectModules();
        return treeDtos;
    }

    public List<TreeDto> findModulesByRoleId(Integer roleId) {
        //查询出来所有的资源信息
        List<TreeDto> tlist = moduleMapper.selectModules();
        List<Integer> roleHasModuls = permissionMapper.selectModuleByRoleId(roleId);
        //遍历
        if(roleHasModuls!=null&& roleHasModuls.size()>0){
            for (TreeDto treeDto : tlist) {
                if (roleHasModuls.contains(treeDto.getId())) {
                    treeDto.setChecked(true);
                }
            }
        }
        return tlist;
    }
}
