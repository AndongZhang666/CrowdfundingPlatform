package com.dong.crowd.service.api;

import com.dong.crowd.entity.vo.AddressVO;
import com.dong.crowd.entity.vo.OrderProjectVO;
import com.dong.crowd.entity.vo.OrderVO;

import java.util.List;

/**
 * @author Andong ZHang
 * @create 2021-06-22-22:24
 */


public interface OrderService {

    void saveAddress(AddressVO addressVO);

    OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId);

    List<AddressVO> getAddressVOList(Integer memberId);

    void saveOrder(OrderVO orderVO);
}
