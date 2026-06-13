package com.divyanshu.youtube.LearningRESTAPIs.controller;

import com.divyanshu.youtube.LearningRESTAPIs.dto.AddStudentRequetDto;
import com.divyanshu.youtube.LearningRESTAPIs.dto.StudentDto;

import com.divyanshu.youtube.LearningRESTAPIs.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PostMapping
    public ResponseEntity<StudentDto> createNewStudent(@Valid @RequestBody AddStudentRequetDto addStudentRequetDto) {
        StudentDto savedStudent = studentService.createStudent(addStudentRequetDto);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id, @Valid @RequestBody AddStudentRequetDto addStudentRequetDto) {
        StudentDto updatedStudent = studentService.updateStudent(id, addStudentRequetDto);
        return ResponseEntity.ok(updatedStudent);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StudentDto> patchStudent(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        // We cannot use @Valid on Map, so partial validation must be done manually if needed.
        // I will not apply @Valid here so it doesn't break.
        StudentDto patchedStudent = studentService.patchStudent(id, updates);
        return ResponseEntity.ok(patchedStudent);
    }

}