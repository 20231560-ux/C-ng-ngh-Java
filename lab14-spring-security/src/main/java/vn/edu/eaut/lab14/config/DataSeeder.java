package vn.edu.eaut.lab14.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import vn.edu.eaut.lab14.entity.AppUser;
import vn.edu.eaut.lab14.entity.Course;
import vn.edu.eaut.lab14.entity.Student;
import vn.edu.eaut.lab14.repository.AppUserRepository;
import vn.edu.eaut.lab14.repository.CourseRepository;
import vn.edu.eaut.lab14.repository.StudentRepository;

/** Tao san du lieu mau va hai tai khoan admin / user de kiem thu phan quyen. */
@Component
public class DataSeeder implements CommandLineRunner {

    private final AppUserRepository userRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(AppUserRepository userRepository,
                      StudentRepository studentRepository,
                      CourseRepository courseRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.save(new AppUser("admin", passwordEncoder.encode("123456"), "ADMIN"));
            userRepository.save(new AppUser("user", passwordEncoder.encode("123456"), "USER"));
        }

        if (studentRepository.count() == 0) {
            studentRepository.save(new Student("SV001", "Nguyen Van An", "an.nv@eaut.edu.vn", "CNTT K16"));
            studentRepository.save(new Student("SV002", "Tran Thi Binh", "binh.tt@eaut.edu.vn", "CNTT K16"));
            studentRepository.save(new Student("SV003", "Le Hoang Cuong", "cuong.lh@eaut.edu.vn", "CNTT K17"));
        }

        if (courseRepository.count() == 0) {
            courseRepository.save(new Course("IT3242", "Cong nghe Java", 3));
            courseRepository.save(new Course("IT3110", "Lap trinh Web", 3));
            courseRepository.save(new Course("IT3060", "Co so du lieu", 4));
        }
    }
}
