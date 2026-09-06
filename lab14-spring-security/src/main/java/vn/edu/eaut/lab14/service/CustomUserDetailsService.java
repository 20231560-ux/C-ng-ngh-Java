package vn.edu.eaut.lab14.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import vn.edu.eaut.lab14.entity.AppUser;
import vn.edu.eaut.lab14.repository.AppUserRepository;

/**
 * Bai 10: Authentication doc tai khoan tu bang app_user trong CSDL.
 * Spring Security se tu dong dung bean UserDetailsService nay.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public CustomUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Khong tim thay tai khoan: " + username));

        return User.builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword())   // mat khau da ma hoa BCrypt
                .roles(appUser.getRole())          // "ADMIN" -> quyen ROLE_ADMIN
                .disabled(!appUser.isEnabled())
                .build();
    }
}
