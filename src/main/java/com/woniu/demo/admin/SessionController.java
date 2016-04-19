package com.woniu.demo.admin;

import com.woniu.base.util.Utils;
import com.woniu.base.web.BaseController;
import com.woniu.base.web.auth.UserContext;
import com.woniu.demo.admin.user.entity.User;
import com.woniu.demo.admin.user.service.UserService;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class SessionController extends BaseController {
    @Inject
    private UserService userService;

    public static class LoginForm {
        @NotEmpty
        private String name;
        @NotEmpty
        private String password;
        @NotEmpty
        private String code;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "admin/index";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String showLogin(HttpServletRequest request, Model model) {
        if (getCurrentUser(request) != null) {
            return "redirect:/admin/";
        }
        model.addAttribute("loginForm", new LoginForm());
        return "admin/login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, HttpServletResponse response,
                        @Valid LoginForm loginForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errorMsg", "用户名密码和验证码都不能为空");
            return "admin/login";
        }
        String token = Utils.toString(request.getSession().getAttribute(CAPTCHA_TOKEN_SESSION_ATTRIBUTE));
        if(!Utils.isBlankEmpty(token) && token.equalsIgnoreCase(loginForm.getCode())){
            User user = userService.login(loginForm.getName(),
                    loginForm.getPassword());
            if (user == null) {
                model.addAttribute("errorMsg", "用户名密码不匹配");
                return "admin/login";
            }
            request.getSession().setAttribute(UserContext.USER_ID_SESSION_ATTRIBUTE, user.getId());
            return "redirect:/admin/";
        }else{
            model.addAttribute("errorMsg", "验证码错误");
            return "admin/login";
        }
    }

    @RequestMapping(value = "logout")
    public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        request.getSession().invalidate();
        return "redirect:/admin/login";
    }

}
