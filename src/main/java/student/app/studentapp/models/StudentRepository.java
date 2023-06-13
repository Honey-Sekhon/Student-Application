package student.app.studentapp.models;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    List<Student> findBySid(int sid);

    List<Student> findByHeightAndWeight(float height, double weight);

    List<Student> findByGpa(double gpa);

}
