package com.dong.crowd.service.api;

import com.dong.crowd.entity.po.MemberPO;

/**
 * @author Andong Zhang
 * @create 2021-05-19-0:25
 */

public interface MemberService {

    MemberPO getMemberPOByLoginAcct(String loginAcct);

    void saveMember(MemberPO memberPO);

}
