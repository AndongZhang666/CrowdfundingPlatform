package com.dong.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberConfirmInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;
    // account
    private String paynum;
    // ID
    private String cardnum;
}
