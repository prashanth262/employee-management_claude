package com.example.demo.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.department.Department;
import com.example.demo.department.DepartmentRepository;
import com.example.demo.employee.EmployeeRepository;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeManagementWebTests {

	@Autowired
	MockMvc mvc;

	@Autowired
	DepartmentRepository departments;

	@Autowired
	EmployeeRepository employees;

	Department engineering;

	@BeforeEach
	void setUp() {
		employees.deleteAll();
		departments.deleteAll();
		Department d = new Department();
		d.setName("Engineering");
		engineering = departments.save(d);
	}

	@Test
	void pagesRender() throws Exception {
		mvc.perform(get("/")).andExpect(status().isOk()).andExpect(content().string(containsString("Engineering")));
		mvc.perform(get("/employees")).andExpect(status().isOk());
		mvc.perform(get("/employees/new")).andExpect(status().isOk());
		mvc.perform(get("/departments")).andExpect(status().isOk());
		mvc.perform(get("/departments/new")).andExpect(status().isOk());
	}

	@Test
	void addEditSearchAndDeleteEmployee() throws Exception {
		mvc.perform(post("/employees/save").param("firstName", "Asha")
			.param("lastName", "Rao")
			.param("email", "Asha.Rao@Example.com")
			.param("salary", "50000")
			.param("hireDate", "2024-01-15")
			.param("departmentId", engineering.getId().toString()))
			.andExpect(redirectedUrl("/employees"))
			.andExpect(flash().attribute("success", "Asha Rao was added."));

		var saved = employees.findAll().getFirst();
		assertThat(saved.getEmail()).isEqualTo("asha.rao@example.com");

		mvc.perform(get("/employees").param("q", "asha")).andExpect(content().string(containsString("Asha Rao")));
		mvc.perform(get("/employees/{id}/edit", saved.getId())).andExpect(status().isOk());

		mvc.perform(post("/employees/{id}/delete", saved.getId())).andExpect(redirectedUrl("/employees"));
		assertThat(employees.count()).isZero();
	}

	@Test
	void invalidEmployeeShowsFieldErrors() throws Exception {
		mvc.perform(post("/employees/save").param("firstName", "")
			.param("lastName", "")
			.param("email", "not-an-email")
			.param("salary", "-5")
			.param("hireDate", "2999-01-01"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrors("employee", "firstName", "lastName", "email", "salary",
					"hireDate", "departmentId"))
			.andExpect(content().string(containsString("Enter a valid email address")));
	}

	@Test
	void duplicateEmailIsRejected() throws Exception {
		var request = post("/employees/save").param("firstName", "A")
			.param("lastName", "B")
			.param("email", "dup@example.com")
			.param("departmentId", engineering.getId().toString());
		mvc.perform(request).andExpect(redirectedUrl("/employees"));
		mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrorCode("employee", "email", "duplicate"));
	}

	@Test
	void departmentWithEmployeesCannotBeDeleted() throws Exception {
		mvc.perform(post("/employees/save").param("firstName", "A")
			.param("lastName", "B")
			.param("email", "a@example.com")
			.param("departmentId", engineering.getId().toString()));

		mvc.perform(post("/departments/{id}/delete", engineering.getId()))
			.andExpect(redirectedUrl("/departments"))
			.andExpect(flash().attributeExists("error"));
		assertThat(departments.existsById(engineering.getId())).isTrue();
	}

	@Test
	void duplicateDepartmentNameIsRejected() throws Exception {
		mvc.perform(post("/departments/save").param("name", "engineering"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrorCode("department", "name", "duplicate"));
	}

}
