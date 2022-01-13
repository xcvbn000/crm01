package com.yjxxt.crm.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.SaleChance;
import com.yjxxt.crm.mapper.SaleChanceMapper;
import com.yjxxt.crm.query.SaleChanceQuery;
import com.yjxxt.crm.utils.AssertUtil;
import com.yjxxt.crm.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Spliterator;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {
    @Autowired(required = false)
    private SaleChanceMapper saleChanceMapper;
    /**
     * code
     * msg
     * count有多少条数据
     * data，数据是什么
     * @param query
     * @return
     */
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery query){
        //实例化Map
        Map<String,Object> map=new HashMap();
        //实例化分页
        PageHelper.startPage(query.getPage(),query.getLimit());
        //开始分页
        PageInfo<SaleChance> plist=new PageInfo<SaleChance>(selectByParams(query));
        //准备数据
        map.put("code",0);
        map.put("msg","success");
        map.put("count",plist.getTotal());
        map.put("data",plist.getList());
        return map;
    }
    //添加
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance){
        //验证
        //客户名非空
        //联系人非空
        //联系电话 非空
        checkSalChanceParam(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //state 0,1（0未分配）
        //非分配
        if(StringUtils.isBlank(saleChance.getAssignMan())){
            //未分配状态
            saleChance.setState(0);
            //未开发状态
            saleChance.setDevResult(0);
        }
        if(StringUtils.isNotBlank(saleChance.getAssignMan())){
            //已分配状态
            saleChance.setState(1);
            //已开发状态
            saleChance.setDevResult(1);
            saleChance.setAssignTime(new Date());
        }

        //设定默认值，state.devResult(0未开发，1开发中，2开发成功了，3开发失败)
          saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        //是否有效
        saleChance.setIsValid(1);
        //分配时间
        //添加是否成功
        AssertUtil.isTrue(insertSelective(saleChance)<1,"添加失败了");

    }

    /**
     * 验证封装
     * @param customerName 客户名称
     * @param linkMan 联系人
     * @param linkPhone 手机
     */
    private void checkSalChanceParam(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"请输入客户名称");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"请输入联系人");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"请输入联系人电话");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"请输入合法的手机号");
    }

    /**
     * 验证：
     * 1当前对象的id
     * 2客户名非空
     * 3联系人非空
     * 4电话非空
     *
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void changeSaleChance(SaleChance saleChance){
        //验证
        SaleChance temp = selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(temp==null,"待修改的记录不存在");
        checkSalChanceParam(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //原来未分配,状态state和开发devResult
        if(StringUtils.isBlank(temp.getAssignMan()) && StringUtils.isNotBlank(saleChance.getAssignMan())){
            saleChance.setDevResult(1);
            saleChance.setState(1);
            saleChance.setAssignTime(new Date());
        }
        //原来已分配变成未分配
        if(StringUtils.isNotBlank(temp.getAssignMan()) && StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setDevResult(0);
            saleChance.setState(0);
            saleChance.setAssignTime(null);
            saleChance.setAssignMan("");
        }
        //设定默认值
        saleChance.setUpdateDate(new Date());
        //修改是否成功
        AssertUtil.isTrue(updateByPrimaryKeySelective(saleChance)<1,"修改不成功");
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void removeSaleChanceIds(Integer[] ids){
        //验证
        AssertUtil.isTrue(ids==null || ids.length==0,"请选择要删除的数据");
        //删除是否为空
        AssertUtil.isTrue(deleteBatch(ids)!=ids.length,"批量删除失败");

    }
}
