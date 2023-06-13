package student.app.studentapp.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import student.app.studentapp.models.Student;
import student.app.studentapp.models.StudentRepository;

@Controller
public class studentController {
    @Autowired
    private StudentRepository studentRepo;

    @GetMapping("/students/view")
    public String getAllStudents(Model model) {
        System.out.println("Getting all students");
        // Get all students from database
        List<Student> students = studentRepo.findAll();
        // End of call from the database

        // Model for the endpoint
        model.addAttribute("studs", students);
        return "students/showAll";
    }

    @PostMapping("/students/add")
    public String addStudent(@RequestParam Map<String, String> newStudent,
            HttpServletResponse response) {

        System.out.println("Adding a student");

        String name = newStudent.get("name");
        double weight = Double.parseDouble(newStudent.get("weight"));
        double height = Double.parseDouble(newStudent.get("height"));
        String hairColor = newStudent.get("hairColor");
        double gpa = Double.parseDouble(newStudent.get("gpa"));
        String nationality = newStudent.get("nationality");

        studentRepo.save(new Student(name, weight, height, hairColor, gpa,
                nationality));
        response.setStatus(201);
        return "redirect:/students/view";
    }

    @GetMapping("/students/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        List<Student> studentToBeEdited = studentRepo.findBySid(id);
        if (!studentToBeEdited.isEmpty()) {
            Student student = studentToBeEdited.get(0);
            model.addAttribute("student", student);
            return "/students/updateStudent";
        } else {
            System.out.println("Student Not found");
            return "error";
        }
    }

    @PostMapping("/students/edit/{id}")
    public String editStudent(@RequestParam("id") int id, @RequestParam Map<String, String> updatedStudent,
            HttpServletResponse response) {

        List<Student> editS = studentRepo.findBySid(id);
        System.out.println("Student is now updated whos ID is:" + id);
        if (!editS.isEmpty()) {
            Student student = editS.get(0);
            System.out.println(student.getSid());
            student.setName(updatedStudent.get("name"));
            student.setWeight(Double.parseDouble(updatedStudent.get("weight")));
            student.setHeight(Double.parseDouble(updatedStudent.get("height")));
            student.setHairColor(updatedStudent.get("hairColor"));
            student.setGpa(Double.parseDouble(updatedStudent.get("gpa")));
            student.setNationality(updatedStudent.get("nationality"));

            studentRepo.save(student);
            response.setStatus(201);

            return "redirect:/students/view";

        } else {
            response.setStatus(404); // Set HTTP status code as "Not Found" if student not found
            return "error";
        }
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable("id") int id, HttpServletResponse response) {
        System.out.println("Deleting student with ID: " + id);
        List<Student> studentToBeDeleted = studentRepo.findBySid(id);
        if (studentToBeDeleted.isEmpty()) {
            response.setStatus(404); // Set HTTP status code as "Not Found" if student not found

        } else {
            // Delete the student from the database
            studentRepo.deleteById(id);
            response.setStatus(204); // Set HTTP status code as "No Content"
        }
        return "redirect:/students/view";
    }

    @GetMapping("/students/display")
    public String displayStudents(Model model) {
        List<Student> dispStuds = studentRepo.findAll();
        model.addAttribute("DispStudents", dispStuds);
        return "/students/displayStudents";
    }

}
