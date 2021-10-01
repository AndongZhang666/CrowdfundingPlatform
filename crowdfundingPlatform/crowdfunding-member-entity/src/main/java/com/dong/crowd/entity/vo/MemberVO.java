package com.dong.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String loginacct;

    private String userpswd;

    private String phoneNum;

    private String username;

    private String email;

    private String verificationCode;
}
