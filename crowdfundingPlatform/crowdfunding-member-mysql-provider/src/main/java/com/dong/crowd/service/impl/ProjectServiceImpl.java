package com.dong.crowd.service.impl;

import com.dong.crowd.entity.po.MemberConfirmInfoPO;
import com.dong.crowd.entity.po.MemberLaunchInfoPO;
import com.dong.crowd.entity.po.ProjectPO;
import com.dong.crowd.entity.po.ReturnPO;
import com.dong.crowd.entity.vo.*;
import com.dong.crowd.mapper.*;
import com.dong.crowd.service.api.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ReturnPOMapper returnPOMapper;

    @Autowired
    private MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;

    @Autowired
    private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;

    @Autowired
    private ProjectPOMapper projectPOMapper;

    @Autowired
    private ProjectItemPicPOMapper projectItemPicPOMapper;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveProject(ProjectVO projectVO, Integer memberId) {

        ProjectPO projectPO = new ProjectPO();

        Integer tempMoney = projectVO.getMoney();

        Long money = tempMoney.longValue();

        BeanUtils.copyProperties(projectVO, projectPO);

        projectPO.setMoney(money);

        projectPO.setMemberid(memberId);

        String createdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        projectPO.setCreatedate(createdate);
        projectPO.setDeploydate(createdate);

        projectPO.setStatus(0);

        projectPOMapper.insertSelective(projectPO);

        Integer projectId = projectPO.getId();

        List<Integer> typeIdList = projectVO.getTypeIdList();
        projectPOMapper.insertTypeRelationship(typeIdList, projectId);

        List<Integer> tagIdList = projectVO.getTagIdList();
        if(tagIdList.size() == 0 || tagIdList.isEmpty()){
            tagIdList.add(4);
        }
        projectPOMapper.insertTagRelationship(tagIdList, projectId);

        List<String> detailPicturePathList = projectVO.getDetailPicturePathList();
        System.out.println("projectId: " + projectId + "\n" + "List: " + detailPicturePathList);
        projectItemPicPOMapper.insertPathList(projectId, detailPicturePathList);

        MemberLaunchInfoVO memberLaunchInfoVO = projectVO.getMemberLaunchInfoVO();
        MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();
        BeanUtils.copyProperties(memberLaunchInfoVO, memberLaunchInfoPO);
        memberLaunchInfoPO.setMemberid(memberId);

        memberLaunchInfoPOMapper.insert(memberLaunchInfoPO);

        List<ReturnVO> returnVOList = projectVO.getReturnVOList();

        List<ReturnPO> returnPOList = new ArrayList<>();

        for (ReturnVO returnVO : returnVOList) {

            ReturnPO returnPO = new ReturnPO();

            BeanUtils.copyProperties(returnVO, returnPO);

            returnPOList.add(returnPO);
        }

        returnPOMapper.insertReturnPOBatch(returnPOList, projectId);

        MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
        MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();
        BeanUtils.copyProperties(memberConfirmInfoVO, memberConfirmInfoPO);
        memberConfirmInfoPO.setMemberid(memberId);
        memberConfirmInfoPOMapper.insert(memberConfirmInfoPO);
    }

    @Override
    public List<PortalTypeVO> getPortalTypeVO() {
        return projectPOMapper.selectPortalTypeVOList();
    }

    @Override
    public DetailProjectVO getDetailProjectVO(Integer projectId) {

        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(projectId);

        Integer status = detailProjectVO.getStatus();

        switch (status) {
            case 0:
                detailProjectVO.setStatusText("In progress");
                break;
            case 1:
                detailProjectVO.setStatusText("Processing");
                break;
            case 2:
                detailProjectVO.setStatusText("Succeed");
                break;
            case 3:
                detailProjectVO.setStatusText("Closed");
                break;

            default:
                break;
        }

        String deployDate = detailProjectVO.getDeployDate();

        Date currentDay = new Date();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date deployDay = format.parse(deployDate);

            long currentTimeStamp = currentDay.getTime();

            long deployTimeStamp = deployDay.getTime();

            long pastDays = (currentTimeStamp - deployTimeStamp) / 1000 / 60 / 60 / 24;

            Integer totalDays = detailProjectVO.getDay();

            Integer lastDay = (int) (totalDays - pastDays);

            if (lastDay <= 0){
                lastDay = 10;
            }

            detailProjectVO.setLastDay(lastDay);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return detailProjectVO;
    }
}
