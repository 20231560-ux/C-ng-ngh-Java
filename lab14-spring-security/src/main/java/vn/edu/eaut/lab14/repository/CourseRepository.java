package vn.edu.eaut.lab14.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.eaut.lab14.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
