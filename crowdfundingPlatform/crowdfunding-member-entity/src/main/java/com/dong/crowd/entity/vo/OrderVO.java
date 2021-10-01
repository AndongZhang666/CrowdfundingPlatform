package com.dong.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Andong Zhang
 * @create 2021-05-23-20:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVO implements Serializable {

    private static final long serialVersionUID = 1L;
    // primary key
    private Integer id;
    // order number
    private String orderNum;
    // paypal pay number
    private String payOrderNum;
    // order amount
    private Double orderAmount;
    // if need invoice
    private Integer invoice;
    // invoice title
    private String invoiceTitle;


    private String orderRemark;
    private String addressId;
    private OrderProjectVO orderProjectVO;

}