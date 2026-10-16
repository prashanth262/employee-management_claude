package com.example.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.department.Department;
import com.example.demo.department.DepartmentService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

	private final DepartmentService departments;

	public DepartmentController(DepartmentService departments) {
		this.departments = departments;
	}

	@ModelAttribute("activePage")
	String activePage() {
		return "departments";
	}

	@GetMapping
	public String list(Model model) {
		model.addAttribute("departments", departments.findSummaries());
		return "departments/list";
	}

	@GetMapping("/new")
	public String createForm(Model model) {
		model.addAttribute("department", new Department());
		return "departments/form";
	}

	@GetMapping("/{id}/edit")
	public String editForm(@PathVariable Long id, Model model) {
		model.addAttribute("department", departments.get(id));
		return "departments/form";
	}

	@PostMapping("/save")
	public String save(@Valid @ModelAttribute("department") Department department, BindingResult result,
			RedirectAttributes redirect) {
		if (!result.hasFieldErrors("name") && departments.nameTaken(department.getName(), department.getId())) {
			result.rejectValue("name", "duplicate", "A department with this name already exists");
		}
		if (result.hasErrors()) {
			return "departments/form";
		}
		boolean isNew = department.getId() == null;
		Department saved = departments.save(department);
		redirect.addFlashAttribute("success",
				"Department \"" + saved.getName() + "\" was " + (isNew ? "created." : "updated."));
		return "redirect:/departments";
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id, RedirectAttributes redirect) {
		try {
			String name = departments.get(id).getName();
			departments.delete(id);
			redirect.addFlashAttribute("success", "Department \"" + name + "\" was deleted.");
		}
		catch (IllegalStateException ex) {
			redirect.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/departments";
	}

}
