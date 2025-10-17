package fmi.sdl_backend.utils;

import fmi.sdl_backend.presistance.model.User;
import fmi.sdl_backend.security.SdlUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    private SecurityUtils() {
    }

    private static Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }

        return authentication;
    }

    private static SdlUserDetails getUserDetails() {
        Object principal = getAuthentication().getPrincipal();

        if (principal instanceof SdlUserDetails) {
            return (SdlUserDetails) principal;
        } else {
            throw new IllegalStateException("Principal is not of expected type");
        }
    }

    public static User getCurrentUser() {
        return getUserDetails().user();
    }
}