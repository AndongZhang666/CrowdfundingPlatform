package com.dong.crowd.handler;

import com.dong.crowd.CrowdConstant;
import com.dong.crowd.ResultEntity;
import com.dong.crowd.api.MySQLRemoteService;
import com.dong.crowd.entity.vo.PortalTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Andong Zhang
 * @create 2021-06-19-0:25
 */

@Controller
public class PortalHandler {

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/")
    public String showPortalPage(Model model){

        ResultEntity<List<PortalTypeVO>> resultEntity = mySQLRemoteService.getPortalTypeProjectDataRemote();

        String result = resultEntity.getResult();

        if(ResultEntity.SUCCESS.equals(result)){

            List<PortalTypeVO> data = resultEntity.getData();

            model.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_DATA, data);

        }

        return "portal";
    }
}
