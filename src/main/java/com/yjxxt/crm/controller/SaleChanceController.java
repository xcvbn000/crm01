package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.SaleChance;
import com.yjxxt.crm.query.SaleChanceQuery;
import com.yjxxt.crm.service.SaleChanceService;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
    @Autowired
    private SaleChanceService saleChanceService;
    @Autowired
    private UserService userService;

    //营销管理里的营销机会模块跳转页面，列表展示
    @RequestMapping("index")
    public String index(){
        return "saleChance/sale_chance";
    }

    @RequestMapping("addOrUpdateDialog")
    public String addOrUpdate(Integer id,Model model){
        if(id!=null){
            //查询用户信息
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            model.addAttribute("saleChance",saleChance);
        }
        return "saleChance/add_update";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> saylist(SaleChanceQuery saleChanceQuery){
        Map<String, Object> map = saleChanceService.querySaleChanceByParams(saleChanceQuery);
        return map;
    }


    @RequestMapping("save")
    @ResponseBody
    public ResultInfo save(HttpServletRequest req,SaleChance saleChance){
        //获取登录用户的id
        int id = LoginUserUtil.releaseUserIdFromCookie(req);
        String trueName = userService.selectByPrimaryKey(id).getTrueName();
        //创建人
        saleChance.setCreateMan(trueName);
        saleChanceService.addSaleChance(saleChance);
        return success("添加成功了");
    }



    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(HttpServletRequest req,SaleChance saleChance){
       saleChanceService.changeSaleChance(saleChance);
       return success("修改成功了");
    }

    @RequestMapping("dels")
    @ResponseBody
    public ResultInfo deletes(Integer[] ids){
       saleChanceService.removeSaleChanceIds(ids);
       return success("删除成功");
    }


}
