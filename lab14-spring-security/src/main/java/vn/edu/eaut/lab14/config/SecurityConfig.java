package vn.edu.eaut.lab14.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Bai 2 + Bai 6 + Bai 7 + Bai 9: cau hinh Authentication va Authorization.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // cho phep dung @PreAuthorize tren controller (Bai 9)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // URL cong khai
                .requestMatchers("/", "/about", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/login", "/403").permitAll()
                .requestMatchers("/h2-console/**").permitAll()

                // Bai 6: /courses/** chi danh cho ADMIN
                .requestMatchers("/courses/**").hasRole("ADMIN")

                // Bai 9: them / sua / xoa sinh vien chi danh cho ADMIN
                .requestMatchers("/students/create", "/students/edit/**", "/students/delete/**").hasRole("ADMIN")

                // Xem danh sach sinh vien: ca ADMIN va USER
                .requestMatchers("/students/**").hasAnyRole("ADMIN", "USER")

                .anyRequest().authenticated()
            )
            // Bai 4: form dang nhap tuy chinh
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/students", true)
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            // Bai 7: trang bao loi 403 khi khong du quyen
            .exceptionHandling(ex -> ex.accessDeniedPage("/403"));

        // Chi de mo H2 console khi hoc/kiem thu
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * Bai 3 - User trong bo nho.
     * Du an nay da chuyen sang doc user tu CSDL (Bai 10) bang CustomUserDetailsService,
     * nen doan duoi de dang chu thich. Neu muon chay theo Bai 3, hay xoa @Service o
     * CustomUserDetailsService roi bo chu thich doan nay.
     *
     * @Bean
     * public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
     *     UserDetails admin = User.builder()
     *             .username("admin")
     *             .password(passwordEncoder.encode("123456"))
     *             .roles("ADMIN")
     *             .build();
     *     UserDetails user = User.builder()
     *             .username("user")
     *             .password(passwordEncoder.encode("123456"))
     *             .roles("USER")
     *             .build();
     *     return new InMemoryUserDetailsManager(admin, user);
     * }
     */
}
