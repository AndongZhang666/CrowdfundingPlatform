package com.dong.crowd.handler;

import com.dong.crowd.CrowdConstant;
import com.dong.crowd.PayPalClient;
import com.dong.crowd.ResultEntity;
import com.dong.crowd.api.MySQLRemoteService;
import com.dong.crowd.entity.vo.AddressVO;
import com.dong.crowd.entity.vo.MemberLoginVO;
import com.dong.crowd.entity.vo.OrderProjectVO;
import com.dong.crowd.entity.vo.OrderVO;
import com.dong.crowd.config.PaypalProperties;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Andong Zhang
 * @create 2021-05-21-19:59
 */
@Controller
public class OrderHandler {

    @Autowired
    MySQLRemoteService mySQLRemoteService;

    @Autowired
    PaypalProperties properties;

    @RequestMapping("/save/address")
    public String saveAddress(AddressVO addressVO, HttpSession session) {

        ResultEntity<String> resultEntity = mySQLRemoteService.saveAddressRemote(addressVO);

        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");

        Integer returnCount = orderProjectVO.getReturnCount();

        return ("redirect:http://18.221.173.223/order/confirm/order/" + returnCount);

    }

    @RequestMapping("/confirm/order/{returnCount}")
    public String showConfirmOrderInfo(@PathVariable("returnCount") Integer returnCount,
                                       HttpSession session) {

        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");

        orderProjectVO.setReturnCount(returnCount);

        session.setAttribute("orderProjectVO", orderProjectVO);

        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);

        Integer memberId = memberLoginVO.getId();

        ResultEntity<List<AddressVO>> resultEntity = mySQLRemoteService.getAddressVORemote(memberId);

        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            List<AddressVO> addressVOList = resultEntity.getData();

            session.setAttribute("addressVOList", addressVOList);
        }

        return "confirm_order";

    }


    @RequestMapping("/confirm/return/info/{projectId}/{returnId}")
    public String showReturnConfirmInfo(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("returnId") Integer returnId,
            HttpSession session) {

        ResultEntity<OrderProjectVO> resultEntity =

                mySQLRemoteService.getOrderProjectVORemote(projectId, returnId);

        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {

            OrderProjectVO orderProjectVO = resultEntity.getData();

            session.setAttribute("orderProjectVO", orderProjectVO);
        }

        return "confirm_return";
    }

    @RequestMapping("/generate/order")
    public String generateOrder(HttpSession session, OrderVO orderVO) {

        //1. get orderProjectVO from session
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");

        orderVO.setOrderProjectVO(orderProjectVO);

        //2.generate order Num
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        String user = UUID.randomUUID().toString().replace("-", "").toUpperCase();

        String orderNum = time + user;

        orderVO.setOrderNum(orderNum);

        //3.calculate order amount
        Double orderAmount = (double) (orderProjectVO.getSupportPrice() *
                orderProjectVO.getReturnCount() + orderProjectVO.getFreight());

        orderVO.setOrderAmount(orderAmount);

        session.setAttribute("orderVO", orderVO);
        //4.call paypal interface

        String successUrl = "http://18.221.173.223/order/paypal/success";
        String cancelUrl = "http://18.221.173.223/order/paypal/cancel";

        String paypalUrl = PayPalClient.pay(
                orderVO.getOrderAmount(),
                "USD",
                orderVO.getOrderNum(),
                "paypal",
                "sale",
                "Andong Crowdfunding",
                cancelUrl,
                successUrl,
                properties.getClientId(),
                properties.getClientSecret(),
                properties.getMode());

        return paypalUrl;

    }

    @RequestMapping(method = RequestMethod.GET, value = "/paypal/cancel")
    public String cancelPay() {
        return "success";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/paypal/success")
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, HttpSession session) {
        try {
            Payment payment = PayPalClient.executePayment(paymentId,
                    payerId,
                    properties.getClientId(),
                    properties.getClientSecret(),
                    properties.getMode());
            if (payment.getState().equals("approved")) {
                String transactionId = payment.getTransactions().get(0).getRelatedResources().get(0).getSale().getId();
                OrderVO orderVO = (OrderVO) session.getAttribute("orderVO");
                orderVO.setPayOrderNum(transactionId);
                ResultEntity<String> resultEntity = mySQLRemoteService.saveOrderRemote(orderVO);
                if (resultEntity.getResult().equals(ResultEntity.SUCCESS))
                    return "redirect:http://18.221.173.223/order/pay/success"; //set by yourself
                session.removeAttribute("orderVO");
                session.removeAttribute("orderProjectVO");
                return "redirect:http://18.221.173.223/order/pay/success";
            }
        } catch (PayPalRESTException e) {
        }
        return "redirect:http://18.221.173.223/";//set by yourself
    }
}
