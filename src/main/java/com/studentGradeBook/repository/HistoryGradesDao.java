package com.studentGradeBook.repository;

import org.springframework.data.repository.CrudRepository;

import com.studentGradeBook.models.HistoryGrade;

public interface HistoryGradesDao extends CrudRepository<HistoryGrade, Integer>{
	
	public Iterable<HistoryGrade> findGradeByStudentId(int id);
	public void deleteByStudentId(int i);
}
