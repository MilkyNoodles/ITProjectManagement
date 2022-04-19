package com.sdutwork.database.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.sdutwork.database.entity.APIresult;
import com.sdutwork.database.entity.Doctor;
import com.sdutwork.database.service.DoctorService;

@Controller
@RequestMapping("/doctor")
public class DoctorController 
{
	@Autowired
	private DoctorService doctorService;
	
	@GetMapping("/doclist")
	public String doclist() 
	{
		return "doctor/doclist";
	}
	
	@PostMapping("/doclist")//分页查询
	@ResponseBody           //返回json数据
	public APIresult doclist(int page, int limit) 
	{
		PageInfo pageInfo = doctorService.getPage(page, limit);
		APIresult result = new APIresult();
		result.setCode(0);
		result.setData(pageInfo);
		return result;
	}
	
	@PostMapping("/delete")
	@ResponseBody
	public APIresult delete(int id) 
	{
		int res = doctorService.delete(id);
		if(res>0) 
		{
			return APIresult.success(null);
		}
		else
		{
			return APIresult.error("删除失败");
		}
	}
	
	@GetMapping("/newdoctor")
	public String newdoctor() 
	{
		return "doctor/newdoctor";
	}
	
	@PostMapping("/newdoctor")
	@ResponseBody
	public APIresult insert(Doctor doctor)
	{
		int res = doctorService.insert(doctor);
		if(res>0)
		{
			return APIresult.success(null);
		}
		else 
		{
			return APIresult.error("添加失败");
		}
	}
	
	@GetMapping("/editdoctor")
	public String edit(int id, Model model) {
		Doctor doctor = doctorService.selectById(id);
		model.addAttribute("doctor",doctor);
		//model 自动在各层之间（其实是不是各层不知道）传递，无须用return传值
		return "doctor/editdoctor";
	}
	
	@PostMapping("/editdoctor")
	@ResponseBody
	public APIresult edit(Doctor doctor) 
	{
		
		int res = doctorService.edit(doctor);
		if(res>0) 
		{
			return APIresult.success(null);
		}
		else 
		{
			return APIresult.error("修改失败");
		}
	}
	
	@GetMapping("/lookdoc")
	public String look(int id, Model model) {
		Doctor doctor = doctorService.selectById(id);
		model.addAttribute("doctor",doctor);
		//model 自动在各层之间（其实是不是各层不知道）传递，无须用return传值
		return "doctor/lookdoc";
	}
}
