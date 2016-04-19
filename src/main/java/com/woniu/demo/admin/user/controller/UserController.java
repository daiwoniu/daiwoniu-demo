package com.woniu.demo.admin.user.controller;

import com.woniu.base.db.Pagination;
import com.woniu.base.util.JsonResponse;
import com.woniu.base.util.Utils;
import com.woniu.base.web.BaseController;
import com.woniu.base.web.validation.Confirm;
import com.woniu.demo.admin.user.entity.User;
import com.woniu.demo.admin.user.service.UserService;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 用户
 */
@Controller
@RequestMapping("/admin/user")
public class UserController extends BaseController {

	@Inject
	private UserService userService;

	@ModelAttribute("user")
	public User initUser() {
		return new User();
	}

	@RequestMapping(method = RequestMethod.GET)
	public String index(HttpServletRequest request, Model model) {

		Pagination<User> users = userService.search(getQueryForm(request));
		model.addAttribute("users", users);
		setBreadcrumb(request, "用户管理", null);
		return "admin/user/index";
	}

	// new是关键字，用new0代替。
	@RequestMapping(value = "new", method = RequestMethod.GET)
	public String new0(HttpServletRequest request, Model model) {
		User user = new User();
		model.addAttribute("user", user);
		setBreadcrumb(request, "用户管理", "/admin/user", "添加用户", null);
		return "admin/user/new";
	}

	@RequestMapping(value = "new", method = RequestMethod.POST)
	public String create(HttpServletRequest request,
						 @Valid User user, BindingResult result, Model model,
						 RedirectAttributes redirectAttrs) {
		validateUser(result, user);
		if (result.hasErrors()) {
			model.addAttribute("user", user);
			setBreadcrumb(request, "用户管理", "/admin/user", "添加用户", null);
			return "admin/user/new";
		}

		User cuUser = getCurrentUser();
		userService.create(user, cuUser.getId());
		redirectAttrs.addFlashAttribute("message", "创建成功!");
		return "redirect:/admin/user/" + user.getId();
	}

	@RequestMapping(value = "/{id:^\\d+$}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, Model model) {
		User user = userService.load(id);
		model.addAttribute("user", user);
		setBreadcrumb("用户管理", "/admin/user", user.getName(), null);
		return "admin/user/show";
	}

	@RequestMapping(value = "/{id:^\\d+$}/edit", method = RequestMethod.GET)
	public String edit(HttpServletRequest request, @PathVariable("id") Long id, Model model) {
		User user = userService.load(id);
		model.addAttribute("user", user);
		setBreadcrumb(request, "用户管理", "/admin/user", user.getName(), "/admin/user/" + user.getId(), "编辑", null);
		return "admin/user/edit";
	}

	@RequestMapping(value = "/{id:^\\d+$}/edit", method = RequestMethod.POST)
	public String update(HttpServletRequest request, @PathVariable("id") Long id, @Valid User user,
						 BindingResult result, Model model) {
		user.setId(id);
		validateUser(result, user);
		if (result.hasErrors()) {
			model.addAttribute("user", user);
			setBreadcrumb(request, "用户管理", "/admin/user", user.getName(), "/admin/user/" + user.getId(), "编辑", null);
			return "admin/user/edit";
		}
		User cuUser = getCurrentUser();

		User userOld = userService.find(id);
		user.setSalt(userOld.getSalt());
		user.setLoginPassword(userOld.getLoginPassword());
		user.setUpdateDate(DateTime.now());
		user.setModifierId(cuUser.getId());
		userService.update(user);
		return "redirect:/admin/user/" + id;
	}

	@RequestMapping(value = "/{id:^\\d+$}", method = RequestMethod.DELETE)
	public
	@ResponseBody
	JsonResponse destroy(@PathVariable("id") Long id,
						 RedirectAttributes redirectAttrs) {
		User cuUser = getCurrentUser();

		User user = userService.find(id);
		user.setStatus("0");
		user.setUpdateDate(DateTime.now());
		user.setModifierId(cuUser.getId());
		userService.update(user);
		String message = "删除用户" + user.getName() + "成功!";
		redirectAttrs.addFlashAttribute("message", "删除用户" + user.getName()
				+ "成功!");
		return new JsonResponse(true, message);
	}

	@Confirm.List({@Confirm(property = "password", confirmProperty = "confirmPassword")})
	public static class ResetPasswordForm {
		@NotBlank
		private String password;
		@NotBlank
		private String confirmPassword;

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getConfirmPassword() {
			return confirmPassword;
		}

		public void setConfirmPassword(String confirmPassword) {
			this.confirmPassword = confirmPassword;
		}

	}

	@RequestMapping(value = "/{id:^\\d+$}/reset_password", method = RequestMethod.GET)
	public String showResetPassword(HttpServletRequest request, @PathVariable("id") Long id, Model model) {
		User user = userService.find(id);
		model.addAttribute("user", user);
		model.addAttribute("resetPasswordForm", new ResetPasswordForm());

		setBreadcrumb(request, "用户管理", "/admin/user", user.getName(), "/admin/user/" + user.getId(), "重置密码", null);
		return "admin/user/reset_password";
	}

	@RequestMapping(value = "/{id:^\\d+$}/reset_password", method = RequestMethod.POST)
	public String resetPassword(@PathVariable("id") Long id,
								@Valid ResetPasswordForm resetPasswordForm, BindingResult result,
								Model model, RedirectAttributes redirectAttrs) {
		User user = userService.find(id);
		model.addAttribute("user", user);
		if (result.hasErrors()) {
			model.addAttribute("resetPasswordForm", resetPasswordForm);
			return "admin/user/reset_password";
		}
		User cuUser = getCurrentUser();
		userService.changePassword(id, resetPasswordForm.getPassword(), cuUser.getId());
		redirectAttrs.addFlashAttribute("message", "重置密码成功");
		return "redirect:/admin/user/" + id;
	}

	public static class ChangePasswordForm extends ResetPasswordForm {
		private String oldPassword;

		public String getOldPassword() {
			return oldPassword;
		}

		public void setOldPassword(String oldPassword) {
			this.oldPassword = oldPassword;
		}
	}

	@RequestMapping(value = "/change_password", method = RequestMethod.GET)
	public String showChangePassword(Model model) {
		model.addAttribute("changePasswordForm", new ChangePasswordForm());
		return "admin/user/change_password";
	}

	@RequestMapping(value = "/change_password", method = RequestMethod.POST)
	public String changePassword(HttpServletRequest request, @Valid ChangePasswordForm changePasswordForm,
								 BindingResult result, Model model, RedirectAttributes redirectAttrs) {
		User user = getCurrentUser(request);
		if (result.hasErrors()
				|| !checkOldPassword(user, changePasswordForm, result)) {
			model.addAttribute("changePasswordForm", changePasswordForm);
			return "admin/user/change_password";
		}
		User cuUser = getCurrentUser();
		userService.changePassword(user.getId(),
				changePasswordForm.getPassword(), cuUser.getId());
		redirectAttrs.addFlashAttribute("message", "修改密码成功");
		return "redirect:/admin";
	}

	private boolean checkOldPassword(User operator,
									 ChangePasswordForm changePasswordForm, BindingResult result) {
		if (userService.isValidPassword(operator,
				changePasswordForm.getOldPassword())) {
			return true;
		}
		result.rejectValue("oldPassword", "password.error", "原密码错误");
		return false;
	}

	private void validateUser(BindingResult result, User user){
		// 表单验证 新建用户

		User user1 = userService.findByLoginName(user.getLoginName());
		if(user1 != null && !user1.getId().equals(user.getId())){
			FieldError fieldError = new FieldError("user", "loginName", user.getLoginName(), false, null, null,  "登录名重复");
			result.addError(fieldError);
		}
		if(Utils.isBlankEmpty(user.getId())){
			// 新建
			if(Utils.isBlankEmpty(user.getLoginPassword())){
				FieldError fieldError = new FieldError("user", "loginPassword", "", false, null, null,  "密码不能为空");
				result.addError(fieldError);
			}else if(user.getLoginPassword().length() < 3 || user.getLoginPassword().length() > 40){
				FieldError fieldError = new FieldError("user", "loginPassword", "", false, null, null,  "长度为3到40位字符");
				result.addError(fieldError);
			}
			if(!Utils.isBlankEmpty(user.getLoginPassword())
					&& !user.getLoginPassword().equalsIgnoreCase(user.getConfirmPassword())){
				FieldError fieldError = new FieldError("user", "loginPassword", "", false, null, null,  "密码和确认密码不一致");
				result.addError(fieldError);
			}
		}
	}
}
