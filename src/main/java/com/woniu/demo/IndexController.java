package com.woniu.demo;

import com.woniu.base.util.Utils;
import com.woniu.base.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/")
public class IndexController extends BaseController {

//	@Inject
//	private YunDanService yunDanService;

	@RequestMapping(method = RequestMethod.GET)
	public String index(Model model) {
		return "index";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String query(HttpServletRequest request, Model model) {
		String yun_dan_hao = request.getParameter("yun_dan_hao");
		String code = request.getParameter("code");

		model.addAttribute("yun_dan_hao", yun_dan_hao);
		if(Utils.isBlankEmpty(yun_dan_hao) || Utils.isBlankEmpty(code)){
			model.addAttribute("errorMsg", "请输入订单号和验证码，进行查询");
			return "index";
		}
		String token = Utils.toString(request.getSession().getAttribute(CAPTCHA_TOKEN_SESSION_ATTRIBUTE));
		if(!code.equalsIgnoreCase(token)){
			model.addAttribute("errorMsg", "验证码输入错误");
			return "index";
		}
//		List<YunDanTable> yunDans = yunDanService.findByYunDanHao(yun_dan_hao);
//		if(yunDans != null && !yunDans.isEmpty()){
//			model.addAttribute("yunDans", yunDans);
//		}else{
			model.addAttribute("errorMsg", "暂时查不到该单号的相关记录，请稍后再试。");
//		}
		return "index";
	}
}