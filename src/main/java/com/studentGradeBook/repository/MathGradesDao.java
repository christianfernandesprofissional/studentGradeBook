package com.studentGradeBook.repository;

import org.springframework.data.repository.CrudRepository;

import com.studentGradeBook.models.MathGrade;

public interface MathGradesDao extends CrudRepository<MathGrade, Integer>{
	
	public Iterable<MathGrade> findGradeByStudentId(int id);
	public void deleteByStudentId(int id);
}
