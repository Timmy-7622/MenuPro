package com.menu.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.menu.controller.EcpayUtil.EcpayUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;




@Controller
@RequestMapping("/checkout")
public class EcpayController {

	@PostMapping("/ecpay")
	public String ecpayCheckout(@RequestParam("totalAmount") int totalAmount,
			                    @RequestParam("phone") String phone,
			                    Model model) {
		
		String tradeNo = "Menu" + System.currentTimeMillis();
		
		Map<String, String> params = new LinkedHashMap<>();
		params.put("MerchantID", "2000132");
		params.put("MerchantTradeNo", tradeNo);
		params.put("MerchantTradeDate", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		params.put("PaymentType", "aio");
		params.put("TotalAmount", String.valueOf(totalAmount));
		params.put("TradeDesc", "購物車模擬付款");
		params.put("ItemName", "餐點訂單");
		params.put("ReturnURL", "http://localhost:8080/checkout/confirm");
		params.put("ClientBackURL", "http://localhost:8080/menu?fromEcpay=1");
		params.put("ChoosePayment", "ALL");
		
		String checkMac = EcpayUtil.generateCheckMacValue(params);
		params.put("CheckMacValue", checkMac);
		
		model.addAttribute("params", params);

		model.addAttribute("actionUrl", "https://payment-stage.ecpay.com.tw/Cashier/AioCheckOut/V5");
		return "ecpayform";
		
	}
	
	@PostMapping("/confirm")
	@ResponseBody
	public String confirmPayment(HttpServletRequest request) {
		String tradeNo = request.getParameter("MerchantTradeNo");
		String rtnCode = request.getParameter("RtnCode");
		
		if("1".equals(rtnCode)) {
			System.out.println("模擬付款成功!交易編號:" + tradeNo);
			HttpSession session = request.getSession();
			session.removeAttribute("cart");
		}else {
			System.out.println("付款失敗或取消");
		}
		
		return "1|OK";
	}
	
}
