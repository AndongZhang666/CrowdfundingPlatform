package com.dong.crowd.handler;

import com.dong.crowd.CrowdConstant;
import com.dong.crowd.ResultEntity;
import com.dong.crowd.entity.po.MemberPO;
import com.dong.crowd.service.api.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Andong Zhang
 * @create 2021-05-21-20:23
 */

@RestController
public class MemberProviderController {

    @Autowired
    private MemberService memberService;

    @RequestMapping("/get/memberPO/by/login/acct/remote")
    ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginAcct") String loginAcct){
        try {
            MemberPO memberPOByLoginAcct = memberService.getMemberPOByLoginAcct(loginAcct);

            return ResultEntity.successWithData(memberPOByLoginAcct);

        } catch (Exception e) {

            e.printStackTrace();

            return ResultEntity.fail(e.getMessage());
        }
    }

    @RequestMapping("/save/member/remote")
    public ResultEntity<String> saveMember(@RequestBody MemberPO memberPO){
        try {
            memberService.saveMember(memberPO);
        } catch (Exception e) {

            if(e instanceof DuplicateKeyException){
                return ResultEntity.fail(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
            return ResultEntity.fail(e.getMessage());
        }
        return ResultEntity.successWithoutData();
    }
}
