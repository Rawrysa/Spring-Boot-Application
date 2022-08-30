package demo.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import java.util.List;

import javax.annotation.security.RolesAllowed;

import demo.demo.model.*;
import demo.demo.repository.StudentRepository;
import demo.demo.service.StudentService;

@Controller // This means that this class is a Controller
@RequestMapping(path="/students") // This means URL's start with /demo (after Application path)
public class StudentController {
  @Autowired // This means to get the bean called userRepository
         // Which is auto-generated by Spring, we will use it to handle the data
  private StudentRepository Repository;
  private StudentService studentService;

  public StudentController(StudentService studentService) {
		super();
		this.studentService = studentService;
	}
// build create employee REST API
@PostMapping()
public ResponseEntity<Student> saveStudent(@RequestBody Student student){
  return new ResponseEntity<Student>(studentService.saveStudent(student), HttpStatus.CREATED);
}


	// build get all student REST API
	@GetMapping
	public String getAllStudent(Model model){
    model.addAttribute("students", studentService.getAllStudent());
    return "admin/AdminPage";
	}

  // build get student by id REST API
	@GetMapping("{id}")
	public ResponseEntity<Student> getStudentById(@PathVariable("id") Integer studentId){
		return new ResponseEntity<Student>(studentService.getStudentById(studentId), HttpStatus.OK);
	}

  // build update student REST API
	@PutMapping("/edit/{id}")
	public ResponseEntity<Student> updateStudent(@PathVariable("id") Integer id
												  ,@RequestBody Student student){
		return new ResponseEntity<Student>(studentService.updateStudent(student, id), HttpStatus.OK);
	}
	
	// build delete student REST API
	@RequestMapping("/delete/{id}")
	public ResponseEntity<String> deleteStudent(@PathVariable("id") Integer id){
		
		// delete Student from DB
	 studentService.deleteStudent(id);
		
		return new ResponseEntity<String>("Student deleted successfully!.", HttpStatus.OK);
	}

  @RolesAllowed("ADMIN")
  @PostMapping(path="/add") // Map ONLY POST Requests
  public @ResponseBody String StudentSubmit (@ModelAttribute Student student, Model model) {
    model.addAttribute("student", student);
    Repository.save(student);
    return "Submitted";
  }

  @RolesAllowed("ADMIN")
  @GetMapping(path="/add")
  public @ResponseBody String StudentForm(Model model) {
    model.addAttribute("student", new Student());
    return "Student";
  }

  @RolesAllowed("ADMIN")
  @GetMapping(path="/view")
  public @ResponseBody String getAllUsers(Model model) {
    model.addAttribute("students", Repository.findAll());
    return "StudentsView";
  }
}