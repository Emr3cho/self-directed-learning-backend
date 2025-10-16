package fmi.sdl_backend.security;

import fmi.sdl_backend.presistance.model.User;
import fmi.sdl_backend.presistance.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SdlUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public SdlUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // TODO: 1. Search for that user by email
        // TODO: 2. If not found create new user and seve
        // TODO: 3. Return that object as UserDetails
        User user = userRepository.findByEmail(email);
        if (user == null) {
            String message = "Email " + email + " not found";
            throw new UsernameNotFoundException(message);
        } else if (user.isDeleted()) {
            String message = "Email '" + email + "' was found but account is deleted!";
            throw new UsernameNotFoundException(message);
        }

        return new SdlUserDetails(user);
    }
}
