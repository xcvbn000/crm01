package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.User;
import org.apache.ibatis.annotations.MapKey;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


public interface UserMapper extends BaseMapper<User,Integer> {


    User selectUserByName(String userName);
    //查询所有销售人员
    @MapKey("")
    List<Map<String,Object>> selectSales();
}