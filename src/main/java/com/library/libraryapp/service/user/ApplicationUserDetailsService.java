package com.library.libraryapp.service.user;

import com.library.libraryapp.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.List;

import static com.library.libraryapp.util.StringUtils.isNullOrEmpty;

@Component
@AllArgsConstructor
public class ApplicationUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (isNullOrEmpty(username)) throw new IllegalArgumentException();

        var foundUser = userRepository.findByName(username);

        if (foundUser.isEmpty()) foundUser = userRepository.findByEmail(username);

        var user = foundUser.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        var role = new SimpleGrantedAuthority(user.getRole());

        return new User(username, user.getPassword(), List.of(role));
    }
}
