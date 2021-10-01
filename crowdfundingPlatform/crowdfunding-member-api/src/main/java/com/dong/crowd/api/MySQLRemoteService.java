package com.dong.crowd.api;
import com.dong.crowd.ResultEntity;
import com.dong.crowd.entity.po.MemberPO;
import com.dong.crowd.entity.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Andong Zhang
 * @create 2021-05-22-22:23
 */

@FeignClient("dong-crowd-mysql")
public interface MySQLRemoteService {
    @RequestMapping("/get/memberPO/by/login/acct/remote")
    ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginAcct") String loginAcct);

    @RequestMapping("/save/member/remote")
    ResultEntity<String> saveMember(@RequestBody MemberPO memberPO);

    @RequestMapping("save/project/vo/remote")
    ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO, @RequestParam("memberId") Integer memberId);

    @RequestMapping("get/portal/type/project/data")
    ResultEntity<List<PortalTypeVO>> getPortalTypeProjectDataRemote();

    @RequestMapping("get/project/detail/remote/{projectId}")
    public ResultEntity<DetailProjectVO> getDetailProjectVORemote(@PathVariable("projectId") Integer projectId);

    @RequestMapping("/save/address/remote")
    ResultEntity<String> saveAddressRemote(@RequestBody AddressVO addressVO);

    @RequestMapping("/save/order/remote")
    ResultEntity<String> saveOrderRemote(@RequestBody OrderVO orderVO);

    @RequestMapping("get/order/project/vo/remote")
    ResultEntity<OrderProjectVO> getOrderProjectVORemote(@RequestParam("projectId") Integer projectId, @RequestParam("returnId") Integer returnId);

    @RequestMapping("/get/address/vo/remote")
    ResultEntity<List<AddressVO>> getAddressVORemote(@RequestParam("memberId") Integer memberId);
}
