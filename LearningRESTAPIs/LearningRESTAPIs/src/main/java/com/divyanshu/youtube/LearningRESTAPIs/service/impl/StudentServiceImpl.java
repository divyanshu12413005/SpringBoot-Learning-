package com.divyanshu.youtube.LearningRESTAPIs.service.impl;

import com.divyanshu.youtube.LearningRESTAPIs.dto.AddStudentRequetDto;
import com.divyanshu.youtube.LearningRESTAPIs.dto.StudentDto;
import com.divyanshu.youtube.LearningRESTAPIs.entity.Student;
import com.divyanshu.youtube.LearningRESTAPIs.exception.ResourceNotFoundException;
import com.divyanshu.youtube.LearningRESTAPIs.repository.StudentRepository;
import com.divyanshu.youtube.LearningRESTAPIs.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    @Override
    public StudentDto getStudentById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Student", "id", id)
        );
        return modelMapper.map(student, StudentDto.class);
    }

    @Override
    public List<StudentDto> getAllStudent() {
        return getAllStudents();
    }

    @Override
    public List<StudentDto> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(student -> modelMapper.map(student, StudentDto.class))
                .toList();
    }

    @Override
    public StudentDto createStudent(AddStudentRequetDto addStudentRequetDto) {
        // Map DTO to Entity
        Student student = modelMapper.map(addStudentRequetDto, Student.class);
        
        // Save the entity to the database
        Student savedStudent = studentRepository.save(student);
        
        // Map the saved entity back to DTO
        return modelMapper.map(savedStudent, StudentDto.class);
    }

    @Override
    public void deleteStudent(Long id) {
        // First check if the student exists, if not it will throw our ResourceNotFoundException
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Student", "id", id)
        );
        
        // If found, delete it
        studentRepository.delete(student);
    }

    @Override
    public StudentDto updateStudent(Long id, AddStudentRequetDto addStudentRequetDto) {
        // Find existing student or throw error if not found
        Student existingStudent = studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Student", "id", id)
        );

        // Update fields
        existingStudent.setName(addStudentRequetDto.getName());
        existingStudent.setEmail(addStudentRequetDto.getEmail());

        // Save updated entity
        Student updatedStudent = studentRepository.save(existingStudent);

        // Return updated DTO
        return modelMapper.map(updatedStudent, StudentDto.class);
    }

    @Override
    public StudentDto patchStudent(Long id, Map<String, Object> updates) {
        // Find existing student
        Student existingStudent = studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Student", "id", id)
        );

        // Dynamically update fields based on the map keys
        if (updates.containsKey("name")) {
            existingStudent.setName((String) updates.get("name"));
        }
        if (updates.containsKey("email")) {
            existingStudent.setEmail((String) updates.get("email"));
        }

        // Save updated entity
        Student updatedStudent = studentRepository.save(existingStudent);

        // Return updated DTO
        return modelMapper.map(updatedStudent, StudentDto.class);
    }
}