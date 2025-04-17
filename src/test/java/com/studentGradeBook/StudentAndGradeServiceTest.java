package com.studentGradeBook;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import com.studentGradeBook.models.CollegeStudent;
import com.studentGradeBook.models.GradebookCollegeStudent;
import com.studentGradeBook.models.HistoryGrade;
import com.studentGradeBook.models.MathGrade;
import com.studentGradeBook.models.ScienceGrade;
import com.studentGradeBook.repository.HistoryGradesDao;
import com.studentGradeBook.repository.MathGradesDao;
import com.studentGradeBook.repository.ScienceGradesDao;
import com.studentGradeBook.repository.StudentDao;
import com.studentGradeBook.service.StudentAndGradeService;

@TestPropertySource("/application-test.properties")
@SpringBootTest(classes=StudentGradesControllerApplication.class)
class StudentAndGradeServiceTest {
	
	//A classe JdbcTemplate é uma classe fornecida pelo Spring
	//que os permite realizar operações JDBC
	@Autowired
	private JdbcTemplate jdbc;
	
	@Autowired
	private StudentAndGradeService studentService;
	
	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	private MathGradesDao mathGradeDao;
	
	@Autowired
	private ScienceGradesDao scienceGradeDao;
	
	@Autowired
	private HistoryGradesDao historyGradeDao;
	//add
	@Value("${sql.scripts.create.student}")
	private String sqlAddStudent;
	
	@Value("${sql.scripts.create.math.grade}")
	private String sqlAddMathGrade;
	
	@Value("${sql.scripts.create.history.grade}")
	private String sqlAddHistoryGrade;
	
	@Value("${sql.scripts.create.science.grade}")
	private String sqlAddScienceGrade;
	
	//delete
	@Value("${sql.scripts.delete.student}")
	private String sqlDeleteStudent;
	
	@Value("${sql.scripts.delete.math.grade}")
	private String sqlDeleteMathGrade;
	
	@Value("${sql.scripts.delete.history.grade}")
	private String sqlDeleteHistoryGrade;
	
	@Value("${sql.scripts.delete.science.grade}")
	private String sqlDeleteScienceGrade;
	
	
	
	
	@BeforeEach
	public void setupDatabase(){
		jdbc.execute(sqlAddStudent);
		jdbc.execute(sqlAddMathGrade);
		jdbc.execute(sqlAddHistoryGrade);
		jdbc.execute(sqlAddScienceGrade);
		
	}

	@Test
	public void createStudentService() {
		
		studentService.createStudent("Chad", "Darby", "chad.darby@gmail.com");
		
		CollegeStudent student = studentDao.findByEmailAddress("chad.darby@gmail.com");
		
		assertEquals("chad.darby@gmail.com", student.getEmailAddress());
	}
	
	
	@Test
	public void isStudentNullCheck() {
		assertTrue(studentService.checkIfStudentIsNull(1));
		assertFalse(studentService.checkIfStudentIsNull(0));
	
	}
	
	@Test
	public void deleteStudentService() {
		Optional<CollegeStudent> deletedCollegeStudent = studentDao.findById(1);
		Optional<MathGrade> deletedMathGrade = mathGradeDao.findById(1);
		Optional<HistoryGrade> deletedHistoryGrade = historyGradeDao.findById(1);
		Optional<ScienceGrade> deletedScienceGrade = scienceGradeDao.findById(1);
		
		assertTrue(deletedCollegeStudent.isPresent(), "Return true");
		assertTrue(deletedMathGrade.isPresent());
		assertTrue(deletedHistoryGrade.isPresent());
		assertTrue(deletedScienceGrade.isPresent());
		
		studentService.deleteStudent(1);
		
		deletedCollegeStudent = studentDao.findById(1);
		deletedMathGrade = mathGradeDao.findById(1);
		deletedHistoryGrade = historyGradeDao.findById(1);
		deletedScienceGrade = scienceGradeDao.findById(1);
		
		assertFalse(deletedCollegeStudent.isPresent(), "Return false");
		assertFalse(deletedMathGrade.isPresent());
		assertFalse(deletedHistoryGrade.isPresent());
		assertFalse(deletedScienceGrade.isPresent());
	}
	
	@Sql("/insertData.sql")
	@Test
	public void getGradebookService() {
		Iterable<CollegeStudent> iterableCollegeStudents = studentService.getGradebook();
		List<CollegeStudent> collegeStudents = new ArrayList<>();
		
		for(CollegeStudent cs : iterableCollegeStudents) {
			collegeStudents.add(cs);
		}
		
		assertEquals(5,collegeStudents.size());
		//esse teste passa porque temos um CollegeStudent no banco cadastrado no @BeforeEach
		//e 4 cadastrados no arquivo sql na pasta Resouces, que é executado pelo @Sql
	}
	
	@Test
	public void createGradeService() {
		
		//Create grade
		assertTrue(studentService.createGrade(80.50,1,"math"));
		assertTrue(studentService.createGrade(80.50, 1, "science"));
		assertTrue(studentService.createGrade(80.50, 1, "history"));
		//Get all grades with studentId
		Iterable<MathGrade> mathGrades = mathGradeDao.findGradeByStudentId(1);
		Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findGradeByStudentId(1);
		Iterable<HistoryGrade>	historyGrades = historyGradeDao.findGradeByStudentId(1);
		//Verify there is grades
		//essa verificação só é possível pq no @BeforeEach eu adicionei um item ao banco
		assertTrue(((Collection<MathGrade>)mathGrades).size() == 2, "Student has math grades"); 
		assertTrue(((Collection<ScienceGrade>)scienceGrades).size() == 2,"Student has science grades");
		assertTrue(((Collection<HistoryGrade>)historyGrades).size() == 2,"Student has history grades");
		
		//A maneira abaixo fica de exemplo, mas ela só verifica se a nota foi adicionada
		//assertTrue(historyGrades.iterator().hasNext(),"Student has science grades");
		
		
	}
	
	@Test
	public void createGradeServiceReturnFalse() {
		assertFalse(studentService.createGrade(105, 1, "math"));
		assertFalse(studentService.createGrade(-5, 1, "math"));
		assertFalse(studentService.createGrade(80.50, 2, "math"));
		assertFalse(studentService.createGrade(80.50, 1, "literature"));
	}
	
	@Test
	public void deleteGradeService() {
		assertEquals(1, studentService.deleteGrade(1,"math"),"Returns student Id after delete");
		assertEquals(1, studentService.deleteGrade(1,"history"),"Returns student Id after delete");
		assertEquals(1, studentService.deleteGrade(1,"science"),"Returns student Id after delete");
		
		
	}
	
	@Test
	public void deleteGradeServiceReturnStudentIdOfZero() {
		assertEquals(0, studentService.deleteGrade(0,"math"),"NO student should have 0 id");
		assertEquals(0, studentService.deleteGrade(0,"history"),"NO student should have 0 id");
		assertEquals(0, studentService.deleteGrade(0,"science"),"NO student should have 0 id");
	}
	
	@Test
	public void studentInformation() {
		GradebookCollegeStudent gradebookCollegeStudent = studentService.studentInformation(1);
		
		assertNotNull(gradebookCollegeStudent);
		assertEquals(1, gradebookCollegeStudent.getId());
		assertEquals("Eric", gradebookCollegeStudent.getFirstname());
		assertEquals("Roby", gradebookCollegeStudent.getLastname());
		assertEquals("eric@gmail.com", gradebookCollegeStudent.getEmailAddress());
		assertTrue(gradebookCollegeStudent.getStudentGrades().getMathGradeResults().size() == 1);
		assertTrue(gradebookCollegeStudent.getStudentGrades().getScienceGradeResults().size() == 1);
		assertTrue(gradebookCollegeStudent.getStudentGrades().getHistoryGradeResults().size() == 1);
	
	}
	
	@Test
	public void studentInformationServiceReturnNull() {
		GradebookCollegeStudent gradebookCollegeStudent = studentService.studentInformation(0);
		
		assertNull(gradebookCollegeStudent);
	}
	
	@AfterEach
	public void setupAfterTransaction() {
		jdbc.execute(sqlDeleteStudent);
		jdbc.execute(sqlDeleteHistoryGrade);
		jdbc.execute(sqlDeleteMathGrade);
		jdbc.execute(sqlDeleteScienceGrade);
		
		
		
		
	}

}
