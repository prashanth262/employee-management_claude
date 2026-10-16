package com.example.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.department.DepartmentService;
import com.example.demo.employee.Employee;
import com.example.demo.employee.EmployeeForm;
import com.example.demo.employee.EmployeeService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

	private final EmployeeService employees;

	private final DepartmentService departments;

	public EmployeeController(EmployeeService employees, DepartmentService departments) {
		this.employees = employees;
		this.departments = departments;
	}

	@ModelAttribute("activePage")
	String activePage() {
		return "employees";
	}

	@GetMapping
	public String list(@RequestParam(required = false) String q, @RequestParam(required = false) Long departmentId,
			Model model) {
		model.addAttribute("employees", employees.search(q, departmentId));
		model.addAttribute("departments", departments.findAll());
		model.addAttribute("q", q);
		model.addAttribute("departmentId", departmentId);
		return "employees/list";
	}

	@GetMapping("/new")
	public String createForm(@RequestParam(required = false) Long departmentId, Model model) {
		EmployeeForm form = new EmployeeForm();
		form.setDepartmentId(departmentId);
		return showForm(form, model);
	}

	@GetMapping("/{id}/edit")
	public String editForm(@PathVariable Long id, Model model) {
		return showForm(EmployeeForm.from(employees.get(id)), model);
	}

	@PostMapping("/save")
	public String save(@Valid @ModelAttribute("employee") EmployeeForm form, BindingResult result, Model model,
			RedirectAttributes redirect) {
		if (!result.hasFieldErrors("email") && employees.emailTaken(form.getEmail(), form.getId())) {
			result.rejectValue("email", "duplicate", "Another employee already uses this email");
		}
		if (result.hasErrors()) {
			return showForm(form, model);
		}
		boolean isNew = form.getId() == null;
		Employee saved = employees.save(form);
		redirect.addFlashAttribute("success",
				saved.getFullName() + (isNew ? " was added." : " was updated."));
		return "redirect:/employees";
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id, RedirectAttributes redirect) {
		String name = employees.get(id).getFullName();
		employees.delete(id);
		redirect.addFlashAttribute("success", name + " was deleted.");
		return "redirect:/employees";
	}

	private String showForm(EmployeeForm form, Model model) {
		model.addAttribute("employee", form);
		model.addAttribute("departments", departments.findAll());
		return "employees/form";
	}

}
