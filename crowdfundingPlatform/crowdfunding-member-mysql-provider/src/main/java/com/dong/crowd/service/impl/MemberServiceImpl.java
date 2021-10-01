package com.dong.crowd.service.impl;

import com.dong.crowd.entity.po.MemberPO;
import com.dong.crowd.entity.po.MemberPOExample;
import com.dong.crowd.mapper.MemberPOMapper;
import com.dong.crowd.service.api.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberPOMapper memberPOMapper;


    public MemberPO getMemberPOByLoginAcct(String loginAcct) {
        MemberPOExample example = new MemberPOExample();
        MemberPOExample.Criteria criteria = example.createCriteria();
        criteria.andLoginacctEqualTo(loginAcct);
        List<MemberPO> memberPO = memberPOMapper.selectByExample(example);
        if(memberPO.size() == 0 || memberPO == null){
            return null;
        }
        return memberPO.get(0);
    }

    public void saveMember(MemberPO memberPO) {
        memberPOMapper.insertSelective(memberPO);
    }
}
