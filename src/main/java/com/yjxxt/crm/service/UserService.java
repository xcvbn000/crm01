package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.bean.UserRole;
import com.yjxxt.crm.mapper.UserMapper;
import com.yjxxt.crm.mapper.UserRoleMapper;
import com.yjxxt.crm.model.UserModel;
import com.yjxxt.crm.query.UserQuery;
import com.yjxxt.crm.utils.AssertUtil;
import com.yjxxt.crm.utils.Md5Util;
import com.yjxxt.crm.utils.PhoneUtil;
import com.yjxxt.crm.utils.UserIDBase64;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserService extends BaseService<User,Integer> {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    public UserModel userLogin(String userName,String userPwd){
        checkUserLoginParam(userName,userPwd);
        //用户是否存在
        User temp=userMapper.selectUserByName(userName);
        AssertUtil.isTrue(temp==null,"用户不存在");
        //密码是否正确
        checkUserPwd(userPwd,temp.getUserPwd());
        //构建返回对象
        return buildUserInfo(temp);
    }

    /**
     * 构建返回对象封装起来，返回目标对象
     * @param temp
     * @return
     */
    private UserModel buildUserInfo(User temp) {
        UserModel userModel=new UserModel();
        //从数据库取出数据之后对id进行加密
        userModel.setUserIdStr(UserIDBase64.encoderUserID(temp.getId()));
        userModel.setUserName(temp.getUserName());
        userModel.setTrueName(temp.getTrueName());
        return userModel;
    }

    /**
     * 封装用户密码非空
     * @param userName
     * @param userPwd
     */
    private void checkUserLoginParam(String userName, String userPwd) {
        //用户非空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户不能为空");
        //密码非空
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"密码不能为空");
    }

    /**
     * 封装密码是否正确
     * @param userPwd
     * @param userPwd1
     */
    private void checkUserPwd(String userPwd, String userPwd1) {
        userPwd= Md5Util.encode(userPwd);
        AssertUtil.isTrue(!userPwd.equals(userPwd1),"密码不正确");
    }

    /**
     * 修改密码
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     */
    public void changeUserPwd(Integer userId,String oldPassword,String newPassword,String confirmPassword){
        //用户登录了才能修改，cookie里有userId
        User user = userMapper.selectByPrimaryKey(userId);
        //密码验证,下面1-6都属于密码校验
        checkPasswordParam(user,oldPassword,newPassword,confirmPassword);
        //6修改密码并加密
        user.setUserPwd(Md5Util.encode(newPassword));
        //确认修改是否成功看影响行数
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改失败了");
    }

    /**
     * 密码校验
     * @param user
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     */
    private void checkPasswordParam(User user, String oldPassword, String newPassword, String confirmPassword) {
        AssertUtil.isTrue(user==null,"用户未登录或不存在");
        //1原始密码不能为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"请输入原始密码");
        //原始密码是否正确
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldPassword))),"原始密码不正确");
        //2新密码不能和原始密码一致
        AssertUtil.isTrue(newPassword.equals(oldPassword),"新密码和原始密码不能一致");
        //3新密码不能为空
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"新密码不能为空");
        //4确认密码不能为空
        AssertUtil.isTrue(StringUtils.isBlank(confirmPassword),"确认密码不能为空");
        //5确认密码和新密码一致
        AssertUtil.isTrue(!(confirmPassword.equals(newPassword)),"新密码和确认密码不一致");
    }
    //查询所有的销售人员
    public List<Map<String,Object>> querySales(){
        return userMapper.selectSales();
    }

    //查询所有用户信息
    public Map<String,Object> findUserByParams(UserQuery userQuery){
        //实例化Map
        Map<String,Object> map=new HashMap<>();
        //初始化分页单位
        PageHelper.startPage(userQuery.getPage(),userQuery.getLimit());
        //开始分页
        PageInfo<User> plist=new PageInfo<>(selectByParams(userQuery));
        //准备数据
        map.put("code",0);
        map.put("msg","success");
        map.put("count",plist.getTotal());
        map.put("data",plist.getList());
        return map;
    }

    /**
     * 添加用户并校验，赋默认值
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user){
        //验证
        checkUser(user);
        //设定默认值
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        //密码加密
        user.setUserPwd(Md5Util.encode("123456"));
        //验证是否添加成功
        AssertUtil.isTrue(insertHasKey(user)<1,"添加失败了");
        relaionUserRole(user.getId(),user.getRoleIds());
    }

    /**
     * 关联中间表，操作中间表
     * @param userId 用户id
     * @param roleIds 角色id
     */
    private void relaionUserRole(Integer userId, String roleIds) {
        System.out.println(userId);
        //准备要存储数据的集合里
        List<UserRole> urlist=new ArrayList<UserRole>();
        AssertUtil.isTrue(StringUtils.isBlank(roleIds),"请选择角色信息");
        //统计当前用户有多少角色（修改时）
        int count=userRoleMapper.countUserRoleNum(userId);
        if(count>0){
            AssertUtil.isTrue( userRoleMapper.deleteUserRoleByUserId(userId)!=count,"用户删除失败");
        }
        String[] roleStrId=roleIds.split(",");
        for(String rid:roleStrId){
            //准备对象
            UserRole userRole=new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(Integer.parseInt(rid));
            userRole.setCreateDate(new Date());
            userRole.setUpdateDate(new Date());
            //存放到集合里
            urlist.add(userRole);
        }
        //判断批量添加是否成功
        //因为批量添加到t_user_role表中，所以调入UserRoleMapper里的方法
        AssertUtil.isTrue(userRoleMapper.insertBatch(urlist)!=urlist.size(),"用户角色添加不成功");
    }

    /**
     * 封装添加用户时的验证
     * @param user
     */
    private void checkUser(User user) {
        //用户非空且唯一
        AssertUtil.isTrue(StringUtils.isBlank(user.getUserName()),"用户名不能为空");
        User temp = userMapper.selectUserByName(user.getUserName());
        if(user.getId()==null){//添加
            AssertUtil.isTrue(temp!=null ,"用户名已存在");
        }else {
            AssertUtil.isTrue(temp!=null && !(user.getId().equals(temp.getId())),"用户名已存在请修改");
        }

        //邮箱，非空
        AssertUtil.isTrue(StringUtils.isBlank(user.getEmail()),"邮箱不能为空");
        //手机号非空，格式正确
        AssertUtil.isTrue(StringUtils.isBlank(user.getPhone()),"手机号不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(user.getPhone()),"请输入合法的手机号");
    }

    /**
     * 用户信息修改
     * @param user
     */
    @Transactional(propagation =Propagation.REQUIRED)
    public void changeUser(User user){
        //根据id查询用户的信息
        User temp = userMapper.selectByPrimaryKey(user.getId());
        AssertUtil.isTrue(temp==null,"待修改的用户不存在");
        checkUser(user);
        user.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(user)<1,"修改不成功");
        relaionUserRole(user.getId(),user.getRoleIds());
    }


    @Transactional(propagation =Propagation.REQUIRED)
    public void removeUserIds(Integer[] ids){
        //删除用户角色关联表的信息
        for(Integer userId:ids){
            int count=userRoleMapper.countUserRoleNum(userId);
            if(count>0){
                AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!=count,"用户角色删除失败");
            }
        }
        //验证
        AssertUtil.isTrue(ids==null || ids.length==0,"请选择要删除的数据");
        //判断删除成功与否
        AssertUtil.isTrue(userMapper.deleteBatch(ids)<1,"批量删除失败");
    }
}
