package com.example.demo.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.department.DepartmentService;
import com.example.demo.department.DepartmentSummary;
import com.example.demo.employee.EmployeeService;

@Controller
public class HomeController {

	private final EmployeeService employees;

	private final DepartmentService departments;

	public HomeController(EmployeeService employees, DepartmentService departments) {
		this.employees = employees;
		this.departments = departments;
	}

	@GetMapping("/")
	public String dashboard(Model model) {
		List<DepartmentSummary> summaries = departments.findSummaries();
		long maxHeadcount = summaries.stream().mapToLong(DepartmentSummary::employeeCount).max().orElse(0);
		model.addAttribute("employeeCount", employees.count());
		model.addAttribute("departmentCount", summaries.size());
		model.addAttribute("summaries", summaries);
		model.addAttribute("maxHeadcount", Math.max(maxHeadcount, 1));
		model.addAttribute("activePage", "dashboard");
		return "index";
	}

}
