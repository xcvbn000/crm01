package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.Permission;
import com.yjxxt.crm.bean.Role;
import com.yjxxt.crm.mapper.ModuleMapper;
import com.yjxxt.crm.mapper.PermissionMapper;
import com.yjxxt.crm.mapper.RoleMapper;
import com.yjxxt.crm.query.RoleQuery;
import com.yjxxt.crm.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RoleService extends BaseService<Role,Integer> {
    @Autowired(required = false)
    private RoleMapper roleMapper;
    @Autowired(required = false)
    private PermissionMapper permissionMapper;
    @Autowired(required = false)
    private ModuleMapper moduleMapper;

    /**
     * 查询所有的角色信息
     * @return
     */
    public List<Map<String,Object>> findRoles(Integer userId){
        return roleMapper.selectRoles(userId);
    }

    /**
     * 角色的条件查询以及分页
     * @param roleQuery
     * @return
     */
    public Map<String,Object> findRoleByParam(RoleQuery roleQuery){
        Map<String,Object> map=new HashMap<String,Object>();
        //开启分页单位
        PageHelper.startPage(roleQuery.getPage(),roleQuery.getLimit());
        PageInfo<Role> rlist=new PageInfo<>(selectByParams(roleQuery));
        //准备数据
        map.put("code",0);
        map.put("msg","success");
        map.put("count",rlist.getTotal());
        map.put("data",rlist.getList());
        return map;
    }

    /**
     * 在角色表中添加角色
     * @param role
     */
    public void addRole(Role role){
        //验证
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名不能为空");
        //判断角色名是否唯一
        AssertUtil.isTrue(roleMapper.selectRoleByName(role.getRoleName())!=null,"该角色名已存在");
        //在角色表中其他属性赋值
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        //判断是否添加成功
        AssertUtil.isTrue(insertHasKey(role)<1,"角色添加失败了");

    }

    /**
     * 在角色表中修改数据
     * @param role
     */
    public void changeRole(Role role){
        //验证
        //通过id获取到角色信息
        Role temp = roleMapper.selectByPrimaryKey(role.getId());
        AssertUtil.isTrue(temp==null || role.getId()==null,"待修改角色不存在");
        //传入的角色名是否唯一
        Role temp2 = roleMapper.selectRoleByName(role.getRoleName());
        AssertUtil.isTrue(temp2!=null && !(temp2.getId().equals(role.getId())),"角色名已存在");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名");
        role.setUpdateDate(new Date());
        //判断是否成功
        AssertUtil.isTrue(updateByPrimaryKeySelective(role)<1,"修改角色不成功");
    }


    public void removeRoleById(Integer id) {
        //查询待删除对象存在与否
        Role role = selectByPrimaryKey(id);
        AssertUtil.isTrue(id==null || role==null,"请选择要删除的数据");
        role.setIsValid(0);
        //判断是否删除成功
        AssertUtil.isTrue(updateByPrimaryKeySelective(role)<1,"删除失败");

    }

    /**
     * 授权角色添加资源
     * @param roleId
     * @param mids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addGrant(Integer roleId,Integer[] mids) {
        Role role = selectByPrimaryKey(roleId);
        AssertUtil.isTrue(roleId == null || role==null, "请选择角色");
        //授权角色之前删除之前的授权资源
        //第一步获取之前的资源
        int count=permissionMapper.countRoleModuleByRoleId(roleId);
        //删除
        if(count>0){
            int i = permissionMapper.deleteRoleModuleByRoleId(roleId);
            AssertUtil.isTrue(i!=count,"资源删除失败");
        }

        if(mids != null && mids.length > 0){
            List<Permission> plist = new ArrayList<>();
            for (Integer mid : mids) {
                //准备存储对象
                Permission permission = new Permission();
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mid).getOptValue());
                permission.setModuleId(mid);
                permission.setRoleId(roleId);
                plist.add(permission);
            }
            AssertUtil.isTrue(permissionMapper.insertBatch(plist) != plist.size(), "授权失败");
        }



    }
}
