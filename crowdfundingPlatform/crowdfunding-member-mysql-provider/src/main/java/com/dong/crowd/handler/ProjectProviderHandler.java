package com.dong.crowd.handler;

import com.dong.crowd.ResultEntity;
import com.dong.crowd.entity.vo.DetailProjectVO;
import com.dong.crowd.entity.vo.PortalTypeVO;
import com.dong.crowd.entity.vo.ProjectVO;
import com.dong.crowd.service.api.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectProviderHandler {

    @Autowired
    private ProjectService projectService;

    @RequestMapping("get/portal/type/project/data")
    public ResultEntity<List<PortalTypeVO>> getPortalTypeProjectDataRemote(){
        try {
            List<PortalTypeVO> portalTypeVO = projectService.getPortalTypeVO();
            return ResultEntity.successWithData(portalTypeVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @RequestMapping("get/project/detail/remote/{projectId}")
    public ResultEntity<DetailProjectVO> getDetailProjectVORemote(@PathVariable("projectId") Integer projectId){
        try {
            DetailProjectVO detailProjectVO = projectService.getDetailProjectVO(projectId);
            return ResultEntity.successWithData(detailProjectVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @RequestMapping("save/project/vo/remote")
    public ResultEntity saveProjectVORemote(@RequestBody ProjectVO projectVO, @RequestParam Integer memberId){
        try {
            projectService.saveProject(projectVO, memberId);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

}
