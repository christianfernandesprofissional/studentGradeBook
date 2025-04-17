package com.studentGradeBook.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.studentGradeBook.models.CollegeStudent;
import com.studentGradeBook.models.MathGrade;


@Repository
public interface StudentDao extends CrudRepository<CollegeStudent, Integer>{

	
	 CollegeStudent findByEmailAddress(String emailAddress);
	
}
