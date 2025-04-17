package com.studentGradeBook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import com.studentGradeBook.models.CollegeStudent;
import com.studentGradeBook.models.GradebookCollegeStudent;
import com.studentGradeBook.models.MathGrade;
import com.studentGradeBook.repository.MathGradesDao;
import com.studentGradeBook.repository.StudentDao;
import com.studentGradeBook.service.StudentAndGradeService;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class GradebookControllerTest {

	private static MockHttpServletRequest request;

	@Autowired
	private JdbcTemplate jdbc;

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private StudentAndGradeService studentAndGradeServiceMock;

	//ao inves de alterar o atributo acima, eu adicionei esse para trabalhar,
	//qualquer erro alterar o atributo acima para o nome abaixo
	@Mock 
	private StudentAndGradeService studentCreateServiceMock;

	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	private StudentAndGradeService studentService;
	
	@Autowired
	private MathGradesDao mathGradesDao;
	
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
	
	@BeforeAll
	public static void setup() {
		request = new MockHttpServletRequest();
		request.setParameter("firstname", "Chad");
		request.setParameter("lastname", "Darby");
		request.setParameter("emailAddress", "chad@gmail.com");
	}

	@BeforeEach
	public void beforeEach() {
		jdbc.execute(sqlAddStudent);
		jdbc.execute(sqlAddMathGrade);
		jdbc.execute(sqlAddHistoryGrade);
		jdbc.execute(sqlAddScienceGrade);
	}

	@Test
	public void getStudentsHttpRequest() throws Exception {
		CollegeStudent studentOne = new GradebookCollegeStudent("Eric", "Roby", "eric@gmail.com");
		CollegeStudent studentTwo = new GradebookCollegeStudent("Chad", "Darby", "chad@gmail.com");

		List<CollegeStudent> collegeStudentList = new ArrayList<>();
		collegeStudentList.add(studentOne);
		collegeStudentList.add(studentTwo);

		when(studentAndGradeServiceMock.getGradebook()).thenReturn(collegeStudentList);
		// tendo certeza de que a lista será retornada de forma certa como mock
		assertIterableEquals(collegeStudentList, studentAndGradeServiceMock.getGradebook());

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/")).andExpect(status().isOk()).andReturn();

		ModelAndView mav = mvcResult.getModelAndView();

		ModelAndViewAssert.assertViewName(mav, "index");
	}

	@Test
	public void creatstudentHttpRequest() throws Exception {

		CollegeStudent studentOne = new CollegeStudent("Eric", "Roby", "eric@gmail.com");

		List<CollegeStudent> collegeStudentList = new ArrayList<>();
		collegeStudentList.add(studentOne);

		when(studentCreateServiceMock.getGradebook()).thenReturn(collegeStudentList);

		assertIterableEquals(collegeStudentList, studentCreateServiceMock.getGradebook());

		// Fazendo um post e passando parametros para o post
		MvcResult mvcResult = this.mockMvc
				.perform(post("/").contentType(MediaType.APPLICATION_JSON)
						.param("firstname", request.getParameterValues("firstname"))
						.param("lastname", request.getParameterValues("lastname"))
						.param("emailAddress", request.getParameterValues("emailAddress")))
				.andExpect(status().isOk()).andReturn();

		ModelAndView mav = mvcResult.getModelAndView();

		ModelAndViewAssert.assertViewName(mav, "index");

		CollegeStudent verifyStudent = studentDao.findByEmailAddress("chad@gmail.com");

		assertNotNull(verifyStudent, "Student should be found");

	}
	
	@Test
	public void deleteStudentHttpRequest() throws Exception{
		assertTrue(studentDao.findById(1).isPresent());
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get("/delete/student/{id}",1)).andExpect(status().isOk()).andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName(mav, "index");
		
		assertFalse(studentDao.findById(1).isPresent());
	}
	
	@Test
	public void deleteStudentHttpRequestErrorPage() throws Exception{
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get("/delete/student/{id}",0)).andExpect(status().isOk()).andReturn();
	
		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName(mav, "error");
	}
	
	@Test
	public void studentInformationHttpRequest() throws  Exception{
		assertTrue(studentDao.findById(1).isPresent());
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}", 1))
				.andExpect(status().isOk()).andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName(mav, "studentInformation");
		
	}
	
	@Test
	public void studentInformationHttpStudentDoesNotExistRequest() throws Exception{
		
		assertFalse(studentDao.findById(0).isPresent());
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}",0))
				.andExpect(status().isOk()).andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName(mav, "error");
		
	}
	
	@Test
	public void createValidGradeHttpRequest() throws Exception{
		assertTrue(studentDao.findById(1).isPresent());
		
		GradebookCollegeStudent student = studentService.studentInformation(1);
		
		assertEquals(1, student.getStudentGrades().getMathGradeResults().size());
		
		MvcResult mvcResult = this.mockMvc.perform(post("/grades")
				.contentType(MediaType.APPLICATION_JSON)
				.param("grade", "85.00")
				.param("gradeType", "math")
				.param("studentId", "1")).andExpect(status().isOk()).andReturn();
		
		ModelAndView modelAndView = mvcResult.getModelAndView(); 
		
		ModelAndViewAssert.assertViewName(modelAndView,	"studentInformation");
			
		student = studentService.studentInformation(1);
		
		assertEquals(2, student.getStudentGrades().getMathGradeResults().size());
	}
	
	@Test
	public void createAValidGradeHttpRequestStudentDoesNotExistsEmptyResponse() throws Exception{
		MvcResult mvcResult = this.mockMvc.perform(post("/grades")
				.contentType(MediaType.APPLICATION_JSON)
				.param("grade", "85.00")
				.param("gradeType", "history")
				.param("studentId", "0")).andExpect(status().isOk()).andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName(mav, "error");
		}
	
	@Test
	public void createANonValidGradeHttpRequestGradeTypeDoesNotExistEmptyResponse()throws Exception{
		MvcResult mvcResult = this.mockMvc.perform(post("/grades")
				.contentType(MediaType.APPLICATION_JSON)
				.param("grade", "85.00")
				.param("gradeType", "literature")
				.param("studentId", "0")).andExpect(status().isOk()).andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName(mav, "error");
	}
	
	@Test
	public void deleteAValidGradeHttpRequest()throws Exception{
		Optional<MathGrade> mathGrade = mathGradesDao.findById(1);
		
		assertTrue(mathGrade.isPresent());
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get("/grades/{id}/{gradeType}", 1, "math"))
				.andExpect(status().isOk()).andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName(mav, "studentInformation");
		
		mathGrade = mathGradesDao.findById(1);
		
		assertFalse(mathGrade.isPresent());
	}
	
	@Test
	public void deleteAValidGradeHttpRequestStudentIdDoesNotExistEmptyResponse()throws Exception{
		Optional<MathGrade> mathGrade = mathGradesDao.findById(2);
		
		assertFalse(mathGrade.isPresent());
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get("/grades/{id}/{gradeType}", 2, "math"))
				.andExpect(status().isOk()).andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName(mav, "error");
		
	}
	
	@Test
	public void deleteANonValidGradeHttpRequest()throws Exception{
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get("/grades/{id}/{gradeType}", 1, "literature"))
				.andExpect(status().isOk()).andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName(mav, "error");
		
	}

	@AfterEach
	public void afterEach() {
		jdbc.execute(sqlDeleteStudent);
		jdbc.execute(sqlDeleteHistoryGrade);
		jdbc.execute(sqlDeleteMathGrade);
		jdbc.execute(sqlDeleteScienceGrade);
	}

}
