package com.studentGradeBook.repository;

import org.springframework.data.repository.CrudRepository;

import com.studentGradeBook.models.ScienceGrade;

public interface ScienceGradesDao extends CrudRepository<ScienceGrade, Integer>{

	public Iterable<ScienceGrade> findGradeByStudentId(int id);
	public void deleteByStudentId(int id);
}
