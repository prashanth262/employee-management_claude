package com.example.demo.department;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

	List<Department> findAllByOrderByNameAsc();

	boolean existsByNameIgnoreCase(String name);

	boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

	@Query("""
			select new com.example.demo.department.DepartmentSummary(d.id, d.name, count(e.id))
			from Department d left join Employee e on e.department = d
			group by d.id, d.name
			order by d.name
			""")
	List<DepartmentSummary> findSummaries();

}
