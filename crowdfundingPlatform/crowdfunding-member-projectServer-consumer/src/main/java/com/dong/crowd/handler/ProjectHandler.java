package com.dong.crowd.handler;

import com.dong.crowd.AWSS3Client;
import com.dong.crowd.CrowdConstant;
import com.dong.crowd.ResultEntity;
import com.dong.crowd.api.MySQLRemoteService;
import com.dong.crowd.config.AWSS3Properties;
import com.dong.crowd.entity.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProjectHandler {

    @Autowired
    private AWSS3Properties awss3Properties;

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/get/project/detail/{projectId}")
    public String getProjectDetail(@PathVariable("projectId") Integer projectId, Model model){
        ResultEntity<DetailProjectVO> resultEntity = mySQLRemoteService.getDetailProjectVORemote(projectId);

        if (resultEntity.getResult().equals(ResultEntity.SUCCESS)){

            DetailProjectVO detailProjectVO = resultEntity.getData();

            model.addAttribute("detailProjectVO", detailProjectVO);
        }

        return "project-show-detail";
    }

    @RequestMapping("/create/project/information")
    public String creatInfo(ProjectVO projectVO, MultipartFile headerPicture, List<MultipartFile> detailPictureList, HttpSession session, ModelMap map) throws IOException {

        //1. creat AWS S3 Client
        AWSS3Client client = new AWSS3Client(awss3Properties.getAWSAccessKeyId(), awss3Properties.getAWSSecretKeyId());
        //2. check headerPicture if null
        if (headerPicture.isEmpty()) {
            map.put(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_UPLOAD_ERROR);
            return "project-launch";
        }
        //3. check headerPicture if load failed
        ResultEntity<String> uploadHeaderPictureResult = client.upload(headerPicture.getInputStream(), headerPicture.getContentType(), headerPicture.getSize(), headerPicture.getOriginalFilename(), awss3Properties.getBucketName());
        if (!uploadHeaderPictureResult.getResult().equals("SUCCESS")) {
            map.put(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_UPLOAD_ERROR);
            return "project-launch";
        }
        String headerPictureUri = uploadHeaderPictureResult.getData();
        projectVO.setHeaderPicturePath(headerPictureUri);

        //4. load detailPictureList
        String detailPictureUri = null;
        List<String> detailPictureArrayList = new ArrayList<>();
        for (MultipartFile detailPicture : detailPictureList) {
            if (!detailPicture.isEmpty()) {
                ResultEntity<String> uploadDetailPictureResult = client.upload(detailPicture.getInputStream(), detailPicture.getContentType(), detailPicture.getSize(), detailPicture.getOriginalFilename(), awss3Properties.getBucketName());
                if (uploadDetailPictureResult.getResult().equals("SUCCESS")) {
                    detailPictureUri = uploadDetailPictureResult.getData();
                    detailPictureArrayList.add(detailPictureUri);
                }
            }
        }
        projectVO.setDetailPicturePathList(detailPictureArrayList);
        //save project in session

        session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);

        return "redirect:http://18.221.173.223/project/return/info/page";
    }

    @ResponseBody
    @RequestMapping("/create/upload/return/picture.json")
    public ResultEntity<String> uploadReturnPicture(@RequestParam("returnPicture") MultipartFile returnPicture) throws IOException {
        AWSS3Client client = new AWSS3Client(awss3Properties.getAWSAccessKeyId(), awss3Properties.getAWSSecretKeyId());
        ResultEntity<String> uploadHeaderPictureResult = client.upload(returnPicture.getInputStream(), returnPicture.getContentType(), returnPicture.getSize(), returnPicture.getOriginalFilename(), awss3Properties.getBucketName());
        return uploadHeaderPictureResult;
    }

    @ResponseBody
    @RequestMapping("/create/save/return.json")
    public ResultEntity<String> saveReturn(ReturnVO returnVO, HttpSession session) {
        try {

            ProjectVO projectVO = (ProjectVO)
                    session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);

            if (projectVO == null) {
                return ResultEntity.fail("Project temp file missing!");
            }

            List<ReturnVO> returnVOList = projectVO.getReturnVOList();

            if (returnVOList == null || returnVOList.size() == 0) {

                returnVOList = new ArrayList<>();

                projectVO.setReturnVOList(returnVOList);
            }

            returnVOList.add(returnVO);

            session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);

            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @RequestMapping("/create/confirm")
    public String saveConfirm(HttpSession session, MemberConfirmInfoVO memberConfirmInfoVO, ModelMap modelMap){

        ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
        if(projectVO == null){
            throw new RuntimeException();
        }

        projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);

        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);

        Integer memberId = memberLoginVO.getId();

        ResultEntity<String> saveResultEntity =  mySQLRemoteService.saveProjectVORemote(projectVO, memberId);

        String result = saveResultEntity.getResult();

        if (result.equals(ResultEntity.FAILED)){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, saveResultEntity.getMessage());
            return "project-confirm";
        }

        session.removeAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);

        return "redirect:http://18.221.173.223/project/create/success";

    }

}
