package com.dong.crowd.handler;


import com.dong.crowd.CrowdConstant;
import com.dong.crowd.ResultEntity;
import com.dong.crowd.SMSUtil;
import com.dong.crowd.api.MySQLRemoteService;
import com.dong.crowd.api.RedisRemoteService;
import com.dong.crowd.config.AWSSMSProperties;
import com.dong.crowd.entity.po.MemberPO;
import com.dong.crowd.entity.vo.MemberLoginVO;
import com.dong.crowd.entity.vo.MemberVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.dong.crowd.CrowdConstant.*;

/**
 * @author Andong Zhang
 * @create 2021-06-19-1:20
 */

@Controller
public class MemberHandler {
    @Autowired
    private AWSSMSProperties awssmsProperties;
    @Autowired
    private RedisRemoteService redisRemoteService;
    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/auth/do/member/register")
    public String register(MemberVO memberVO, ModelMap modelMap){

        String phoneNum = memberVO.getPhoneNum();
        String key = REDIS_CODE_PREFIX + phoneNum;
        ResultEntity<String> redisResult = redisRemoteService.getRedisStringValueByKey(key);
        String result = redisResult.getResult();

        if(ResultEntity.FAILED.equals(result)) {
            modelMap.addAttribute("message", redisResult.getMessage());
            return "member-reg";
        }

        String storedCode = redisResult.getData();

        if(storedCode == null){
            modelMap.addAttribute("message", "Sorry, your verification code is expired, please try again");
            return  "member-reg";
        }

        String formCode = memberVO.getVerificationCode();

        if(!Objects.equals(storedCode, formCode)){
            modelMap.addAttribute("message", "Sorry, your verification code is not correct, please try again");
            return "member-reg";
        }
        redisRemoteService.removeRedisKeyRemote(key);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(memberVO.getUserpswd());
        memberVO.setUserpswd(encode);
        MemberPO memberPO = new MemberPO();
        BeanUtils.copyProperties(memberVO,memberPO);
        ResultEntity<String> stringResultEntity = mySQLRemoteService.saveMember(memberPO);

        if(ResultEntity.FAILED.equals(stringResultEntity.getResult())){
            modelMap.addAttribute("message", stringResultEntity.getMessage());
            return "member-reg";
        }
        return "member-login";
    }


    @ResponseBody
    @RequestMapping("/auth/member/send/short/message.json")
    public ResultEntity<String> sendMessage(@RequestParam("phoneNum") String phoneNum) {

        ResultEntity<String> sendSMSResult = SMSUtil.sendSMS(phoneNum, awssmsProperties.getAWSAccessKeyId(), awssmsProperties.getAWSSecretKeyId());
        if (ResultEntity.SUCCESS.equals(sendSMSResult.getResult())) {
            String code = sendSMSResult.getData();
            String key = REDIS_CODE_PREFIX + phoneNum;
            ResultEntity<String> SMSResult = redisRemoteService.setRedisKeyValueRemoteWithTimeout(key, code, 15L, TimeUnit.MINUTES);
            if(ResultEntity.SUCCESS.equals(SMSResult.getResult())){
                return ResultEntity.successWithoutData();
            }else{
                return SMSResult;
            }
        }else{
            return sendSMSResult;
        }
    }

    @RequestMapping("/auth/member/do/login")
    public String login(
            @RequestParam("loginacct") String loginAcct,
            @RequestParam("userpswd") String userPswd,
            ModelMap modelMap,
            HttpSession session
                        ){
        ResultEntity<MemberPO> resultEntity = mySQLRemoteService.getMemberPOByLoginAcctRemote(loginAcct);
        if(resultEntity.getResult().equals(ResultEntity.FAILED)){
            modelMap.addAttribute("message", resultEntity.getMessage());
            return "member-login";
        }

        MemberPO data = resultEntity.getData();
        if(data == null){
            modelMap.addAttribute("message", MESSAGE_LOGIN_FAILED);
            return "member-login";
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if(!bCryptPasswordEncoder.matches(userPswd, data.getUserpswd())){
            System.out.println("username: " + data.getUserpswd());
            modelMap.addAttribute("message", MESSAGE_LOGIN_FAILED);
            return "member-login";
        }

        MemberLoginVO memberLoginVO = new MemberLoginVO(data.getId(), data.getUsername(), data.getEmail());
        session.setAttribute(ATTR_NAME_LOGIN_MEMBER, memberLoginVO);

        return "redirect:http://18.221.173.223/auth/member/to/center/page";

    }

    @RequestMapping("/auth/member/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

}
