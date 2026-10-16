package com.example.demo.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	@Query("""
			select e from Employee e join fetch e.department d
			where (lower(concat(concat(e.firstName, ' '), e.lastName)) like :pattern escape '\\'
			       or lower(e.email) like :pattern escape '\\'
			       or lower(e.jobTitle) like :pattern escape '\\')
			  and (:departmentId is null or d.id = :departmentId)
			order by e.firstName, e.lastName
			""")
	List<Employee> search(@Param("pattern") String pattern, @Param("departmentId") Long departmentId);

	@Query("select e from Employee e join fetch e.department where e.id = :id")
	Optional<Employee> findWithDepartment(@Param("id") Long id);

	boolean existsByEmailIgnoreCase(String email);

	boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

	long countByDepartmentId(Long departmentId);

}
