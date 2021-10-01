package com.dong.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberLoginVO implements Serializable {

    private static final Long serialVersionId = 1L;

    private Integer id;

    private String userName;

    private String email;
}
