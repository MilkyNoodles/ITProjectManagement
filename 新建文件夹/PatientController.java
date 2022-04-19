package com.sdutwork.database.controller;

import java.util.List;

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
import com.sdutwork.database.entity.Patient;
import com.sdutwork.database.service.DoctorService;
import com.sdutwork.database.service.PatientService;

@Controller
@RequestMapping("/patient")
public class PatientController 
{
	@Autowired //这个注释每个要请求的都要写 我服了 找半天找不出错
	private DoctorService doctorService;
	@Autowired
	private PatientService patientService;
	
	@GetMapping("/patientlist")
	public String patientlist(Model model) 
	{
		List<Doctor> doctor = doctorService.getALL();
		model.addAttribute("doctor",doctor);
		return "patient/patientlist";
	}
	@PostMapping("/patientlist")//分页查询
	@ResponseBody           //返回json数据
	public APIresult patientlist(int page, int limit, String key, Integer doctor) 
	{
		PageInfo pageInfo = patientService.getPage(page, limit, key, doctor);
		APIresult result = new APIresult();
		result.setCode(0);
		result.setData(pageInfo);
		return result;
	}
	
	@GetMapping("/newpatient")
	public String newpatient(Model model) 
	{
		List<Doctor> doctor = doctorService.getALL();
		model.addAttribute("doctor",doctor);
		return "patient/newpatient";
	}
	
	@PostMapping("/newpatient")
	@ResponseBody
	public APIresult insert(Patient patient)
	{
		int res = patientService.insert(patient);
		patientService.insertill(patient);
		if(res>0)
		{
			return APIresult.success(null);
		}
		else 
		{
			return APIresult.error("添加患者失败");
		}
	}
	/**
	 * 编辑患者表
	 */
	@PostMapping("/editpatient")
	@ResponseBody
	public APIresult edit(Patient patient) {
		int res = patientService.edit(patient);
		return APIresult.success(null);
	}
	/**
	 * 删除患者表
	 */
	@PostMapping("/delete")
	@ResponseBody
	public APIresult delete(int id) 
	{
		int res = patientService.delete(id);
		patientService.deletecases(id);
		if(res>0) 
		{
			return APIresult.success(null);
		}
		else
		{
			return APIresult.error("删除失败");
		}
	}
	/**
	 * 
	 * 查看详情
	 */
	@GetMapping("/lookpatient")
	public String look(int id, Model model) {
		Patient patient = patientService.selectById(id);
		model.addAttribute("patient",patient);
		//model 自动在各层之间（其实是不是各层不知道）传递，无须用return传值
		return "patient/lookpatient";
	}
}
