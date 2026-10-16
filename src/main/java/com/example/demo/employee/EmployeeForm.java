package com.example.demo.employee;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** Backing object for the add/edit employee form. */
public class EmployeeForm {

	private Long id;

	@NotBlank(message = "First name is required")
	@Size(max = 50, message = "First name must be at most 50 characters")
	private String firstName;

	@NotBlank(message = "Last name is required")
	@Size(max = 50, message = "Last name must be at most 50 characters")
	private String lastName;

	@NotBlank(message = "Email is required")
	@Email(message = "Enter a valid email address")
	@Size(max = 120, message = "Email must be at most 120 characters")
	private String email;

	@Pattern(regexp = "^$|^[+0-9][0-9 \\-]{6,19}$", message = "Enter a valid phone number")
	private String phone;

	@Size(max = 100, message = "Job title must be at most 100 characters")
	private String jobTitle;

	@Positive(message = "Salary must be greater than zero")
	@DecimalMax(value = "9999999999.99", message = "Salary is too large")
	private BigDecimal salary;

	@PastOrPresent(message = "Hire date cannot be in the future")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate hireDate;

	@NotNull(message = "Please select a department")
	private Long departmentId;

	public static EmployeeForm from(Employee e) {
		EmployeeForm form = new EmployeeForm();
		form.id = e.getId();
		form.firstName = e.getFirstName();
		form.lastName = e.getLastName();
		form.email = e.getEmail();
		form.phone = e.getPhone();
		form.jobTitle = e.getJobTitle();
		form.salary = e.getSalary();
		form.hireDate = e.getHireDate();
		form.departmentId = e.getDepartment().getId();
		return form;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	public LocalDate getHireDate() {
		return hireDate;
	}

	public void setHireDate(LocalDate hireDate) {
		this.hireDate = hireDate;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

}
