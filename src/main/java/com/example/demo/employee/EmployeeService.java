package com.example.demo.employee;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.demo.NotFoundException;
import com.example.demo.department.DepartmentService;

@Service
@Transactional(readOnly = true)
public class EmployeeService {

	private final EmployeeRepository employees;

	private final DepartmentService departments;

	public EmployeeService(EmployeeRepository employees, DepartmentService departments) {
		this.employees = employees;
		this.departments = departments;
	}

	public List<Employee> search(String query, Long departmentId) {
		String q = query == null ? "" : query.trim().toLowerCase();
		String escaped = q.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
		return employees.search("%" + escaped + "%", departmentId);
	}

	public long count() {
		return employees.count();
	}

	public Employee get(Long id) {
		return employees.findWithDepartment(id)
			.orElseThrow(() -> new NotFoundException("Employee " + id + " not found"));
	}

	public boolean emailTaken(String email, Long excludeId) {
		String trimmed = email == null ? "" : email.trim();
		return excludeId == null ? employees.existsByEmailIgnoreCase(trimmed)
				: employees.existsByEmailIgnoreCaseAndIdNot(trimmed, excludeId);
	}

	@Transactional
	public Employee save(EmployeeForm form) {
		Employee e = form.getId() == null ? new Employee() : get(form.getId());
		e.setFirstName(form.getFirstName().trim());
		e.setLastName(form.getLastName().trim());
		e.setEmail(form.getEmail().trim().toLowerCase());
		e.setPhone(StringUtils.hasText(form.getPhone()) ? form.getPhone().trim() : null);
		e.setJobTitle(StringUtils.hasText(form.getJobTitle()) ? form.getJobTitle().trim() : null);
		e.setSalary(form.getSalary());
		e.setHireDate(form.getHireDate());
		e.setDepartment(departments.get(form.getDepartmentId()));
		return employees.save(e);
	}

	@Transactional
	public void delete(Long id) {
		employees.delete(get(id));
	}

}
