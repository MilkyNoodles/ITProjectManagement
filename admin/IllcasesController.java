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
import com.sdutwork.database.entity.Illcases;
import com.sdutwork.database.entity.Kind;
import com.sdutwork.database.entity.Patient;
import com.sdutwork.database.service.DoctorService;
import com.sdutwork.database.service.IllcasesService;
import com.sdutwork.database.service.KindService;

@Controller
@RequestMapping("/illcases")
public class IllcasesController {
	
	@Autowired
	private IllcasesService illcasesService;
	@Autowired
	private KindService kindService;
	
	@GetMapping("/illcaseslist")
	public String doclist() 
	{
		return "illcases/illcaseslist";
	}
	@PostMapping("/illcaseslist")//分页查询
	@ResponseBody           //返回json数据
	public APIresult patientlist(int page, int limit, String key) 
	{
		PageInfo pageInfo = illcasesService.getPage(page, limit, key);
		APIresult result = new APIresult();
		result.setCode(0);
		result.setData(pageInfo);
		return result;
	}
	
	@PostMapping("/delete")
	@ResponseBody
	public APIresult delete(int id) 
	{
		int res = illcasesService.delete(id);
		if(res>0) 
		{
			return APIresult.success(null);
		}
		else
		{
			return APIresult.error("删除失败");
		}
	}
	@PostMapping("/editillcases")
	@ResponseBody
	public APIresult edit(Illcases illcases) {
		int res = illcasesService.edit(illcases);
		if(res>0) 
		{
			return APIresult.success(null);
		}
		else 
		{
			return APIresult.error("修改失败");
		}
	}
	
	@PostMapping("/newillcases")
	@ResponseBody
	public APIresult insert(Illcases illcases)
	{
		int res = illcasesService.insert(illcases);
		if(res>0)
		{
			return APIresult.success(null);
		}
		else 
		{
			return APIresult.error("添加患者失败");
		}
	}
	
	@GetMapping("/creatillcases")
	public String creatillcases(Model model) 
	{
		List<Kind> kind = kindService.getALL();
		model.addAttribute("kind",kind);
		return "illcases/creatillcases";
	}
}
