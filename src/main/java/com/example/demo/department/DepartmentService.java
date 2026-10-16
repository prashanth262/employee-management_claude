package com.example.demo.department;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.NotFoundException;
import com.example.demo.employee.EmployeeRepository;

@Service
@Transactional(readOnly = true)
public class DepartmentService {

	private final DepartmentRepository departments;

	private final EmployeeRepository employees;

	public DepartmentService(DepartmentRepository departments, EmployeeRepository employees) {
		this.departments = departments;
		this.employees = employees;
	}

	public List<Department> findAll() {
		return departments.findAllByOrderByNameAsc();
	}

	public List<DepartmentSummary> findSummaries() {
		return departments.findSummaries();
	}

	public Department get(Long id) {
		return departments.findById(id).orElseThrow(() -> new NotFoundException("Department " + id + " not found"));
	}

	public boolean nameTaken(String name, Long excludeId) {
		String trimmed = name == null ? "" : name.trim();
		return excludeId == null ? departments.existsByNameIgnoreCase(trimmed)
				: departments.existsByNameIgnoreCaseAndIdNot(trimmed, excludeId);
	}

	@Transactional
	public Department save(Department department) {
		department.setName(department.getName().trim());
		return departments.save(department);
	}

	@Transactional
	public void delete(Long id) {
		Department department = get(id);
		long count = employees.countByDepartmentId(id);
		if (count > 0) {
			throw new IllegalStateException("Cannot delete \"" + department.getName() + "\": it still has " + count
					+ " employee(s). Move or delete them first.");
		}
		departments.delete(department);
	}

}
