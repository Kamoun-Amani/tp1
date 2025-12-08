package tn.m104.rh.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.m104.rh.entity.Student;
import tn.m104.rh.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    StudentRepository studentRepository;

    @InjectMocks
    StudentServiceImpl studentService;

    Student student = new Student(1, "name1", "adress1", 20.00);

    List<Student> listStudents = new ArrayList<Student>() {{
        add(new Student(2, "name2", "adress2", 30.00));
        add(new Student(3, "name3", "adress3", 10.00));
    }};

    @Test
    @Order(1)
    public void testGetStudents() {

        Mockito.when(studentRepository.findAll())
                .thenReturn(listStudents);

        List<Student> result = studentService.getStudents();

        Assertions.assertEquals(2, result.size());
    }

    @Test
    @Order(2)
    public void testAddStudent() {

        Mockito.when(studentRepository.save(Mockito.any(Student.class)))
                .thenReturn(student);

        Student saved = studentService.registerStudent(student);

        Assertions.assertNotNull(saved);
        Assertions.assertEquals("name1", saved.getName());
    }

    @Test
    @Order(3)
    public void testDeleteStudent() {

        // deleteById() est void → doAnswer() pour simuler la suppression
        Mockito.doAnswer(invocation -> {
            listStudents.remove(0);
            return null;
        }).when(studentRepository).deleteById(listStudents.get(0).getRollNumber());

        // Appel réel du service
        studentService.deleteStudent(listStudents.get(0).getRollNumber());

        // Après suppression, la liste doit contenir 1 élément
        Assertions.assertEquals(1, listStudents.size());
    }
}
