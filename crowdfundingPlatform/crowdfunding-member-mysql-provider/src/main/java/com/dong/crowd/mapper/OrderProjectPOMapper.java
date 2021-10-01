package com.dong.crowd.mapper;

import com.dong.crowd.entity.po.OrderProjectPO;
import com.dong.crowd.entity.po.OrderProjectPOExample;
import java.util.List;

import com.dong.crowd.entity.vo.OrderProjectVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface OrderProjectPOMapper {
    int countByExample(OrderProjectPOExample example);

    int deleteByExample(OrderProjectPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OrderProjectPO record);

    int insertSelective(OrderProjectPO record);

    List<OrderProjectPO> selectByExample(OrderProjectPOExample example);

    OrderProjectPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OrderProjectPO record, @Param("example") OrderProjectPOExample example);

    int updateByExample(@Param("record") OrderProjectPO record, @Param("example") OrderProjectPOExample example);

    int updateByPrimaryKeySelective(OrderProjectPO record);

    int updateByPrimaryKey(OrderProjectPO record);

    OrderProjectVO selectOrderProjectVO(Integer returnId);
}