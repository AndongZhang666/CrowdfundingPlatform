package com.dong.crowd.service.api;

import com.dong.crowd.entity.vo.DetailProjectVO;
import com.dong.crowd.entity.vo.PortalTypeVO;
import com.dong.crowd.entity.vo.ProjectVO;

import java.util.List;

public interface ProjectService {
    void saveProject(ProjectVO projectVO, Integer memberId);

    List<PortalTypeVO> getPortalTypeVO();

    DetailProjectVO getDetailProjectVO(Integer projectId);

}
