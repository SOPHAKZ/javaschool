package com.javaschool.repository.course;

import com.javaschool.entity.course.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> , JpaSpecificationExecutor {
    @Query("select e from Enrollment e where e.user.id = ?1 and e.isCompleted = true")
    List<Enrollment> findByUser_IdAndIsCompletedTrue(Long id);


}
