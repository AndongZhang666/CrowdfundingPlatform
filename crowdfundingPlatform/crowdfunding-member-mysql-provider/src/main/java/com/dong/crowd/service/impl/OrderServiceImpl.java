package com.dong.crowd.service.impl;

import com.dong.crowd.entity.po.AddressPO;
import com.dong.crowd.entity.po.AddressPOExample;
import com.dong.crowd.entity.po.OrderPO;
import com.dong.crowd.entity.po.OrderProjectPO;
import com.dong.crowd.entity.vo.AddressVO;
import com.dong.crowd.entity.vo.OrderProjectVO;
import com.dong.crowd.entity.vo.OrderVO;
import com.dong.crowd.mapper.AddressPOMapper;
import com.dong.crowd.mapper.OrderPOMapper;
import com.dong.crowd.mapper.OrderProjectPOMapper;
import com.dong.crowd.service.api.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andong Zhang
 * @create 2021-06-15-20:50
 */

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderProjectPOMapper orderProjectPOMapper;

    @Autowired
    AddressPOMapper addressPOMapper;

    @Autowired
    OrderPOMapper orderPOMapper;



    @Override
    public OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId) {

        OrderProjectVO orderProjectVO = orderProjectPOMapper.selectOrderProjectVO(returnId);

        return orderProjectVO;
    }

    @Override
    public List<AddressVO> getAddressVOList(Integer memberId) {

        AddressPOExample addressPOExample = new AddressPOExample();

        addressPOExample.createCriteria().andMemberIdEqualTo(memberId);

        List<AddressPO> addressPOList = addressPOMapper.selectByExample(addressPOExample);

        ArrayList<AddressVO> addressVOList = new ArrayList<>();

        for(AddressPO addressPO: addressPOList){
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(addressPO, addressVO);
            addressVOList.add(addressVO);
        }

        return addressVOList;
    }

    @Override
    public void saveOrder(OrderVO orderVO) {

        OrderPO orderPO = new OrderPO();

        BeanUtils.copyProperties(orderVO, orderPO);

        OrderProjectPO orderProjectPO = new OrderProjectPO();

        BeanUtils.copyProperties(orderVO.getOrderProjectVO(), orderProjectPO);

        orderPOMapper.insert(orderPO);

        Integer id = orderPO.getId();

        orderProjectPO.setOrderId(id);

        orderProjectPOMapper.insert(orderProjectPO);
    }

    @Override
    public void saveAddress(AddressVO addressVO) {

        AddressPO addressPO = new AddressPO();

        BeanUtils.copyProperties(addressVO,addressPO);

        addressPOMapper.insertSelective(addressPO);
    }


}
