package com.sdutwork.database.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sdutwork.database.entity.User;
import com.sdutwork.database.service.UserService;
import com.sdutwork.database.util.VerCodeUtil;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@RequestMapping("/list")
	public ModelAndView getAll()
	{
		List<User> users = userService.getAll();
		ModelAndView mv = new ModelAndView();
		mv.addObject("users",users);
		mv.setViewName("/user/list");
		return mv;
	}
	@GetMapping("/add")
	public String add() {
		return "/user/add";
	}
	@PostMapping("/add")
	public String add(User user,Model model) {
		int res = userService.addUser(user);
		if(res>0) {
			return "redirect:/user/list";
		}
		else if(res == -1){
			model.addAttribute("error","用户名不可以");
		}
		
		else {
			model.addAttribute("error","未知错误");
		}
		return "/user/add";
		
	}
	@GetMapping("/delete")
	public String delete(int id) {
		
		int res = userService.delete(id);
		return "redirect:/user/list";
	}
	@GetMapping("/update")
	public String update(int id, Model model) {
		
		User user = userService.getID(id);
		model.addAttribute("user",user);
		return "/user/update";
	}
	@PostMapping("/update")
	public String update(User user)
	{
		int res = userService.update(user);
		return "redirect:/user/list";
	}
	
	//用户登录
	@GetMapping("/login")
	public String login() {
		return "/user/login";
	}
	@PostMapping("/login")
	public String login(User user, String vercode, HttpSession session,Model model) 
	{
		if(!vercode.toUpperCase().equals(session.getAttribute("vercode")))
		{
			model.addAttribute("error","验证码错误！！！");
			return "user/login";
		}
		try {
			User loginUser = userService.login(user);
			session.setAttribute("user", loginUser);
			return "redirect:/";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			model.addAttribute("error",e.getMessage());
		}
		return "user/login";
	}
	

	@GetMapping("/vercode")
	public void vercode(HttpServletResponse response, HttpSession session) {
		String vercode = VerCodeUtil.createVerCode(response);
		session.setAttribute("vercode", vercode);
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		//session.removeAttribute("user");
		session.invalidate();
		return "redirect:/user/login";
	}
	
	
}
