package fmi.sdl_backend.security;

import fmi.sdl_backend.presistance.model.User;
import fmi.sdl_backend.presistance.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SdlUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public SdlUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String concatenatedEmailAndFullName) throws UsernameNotFoundException {
        String email = concatenatedEmailAndFullName.split("---")[0];
        String fullName = concatenatedEmailAndFullName.split("---")[1];
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) user = Optional.of(userRepository.save(new User(email, fullName)));
        return new SdlUserDetails(user.get());
    }
}
