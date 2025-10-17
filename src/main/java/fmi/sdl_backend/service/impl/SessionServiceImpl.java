package fmi.sdl_backend.service.impl;

import fmi.sdl_backend.presistance.model.Session;
import fmi.sdl_backend.presistance.model.Subsection;
import fmi.sdl_backend.presistance.model.enums.Status;
import fmi.sdl_backend.rest.request.session.SessionCreateRequest;
import fmi.sdl_backend.service.SessionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SessionServiceImpl implements SessionService {
    @Override
    public void createSession(SessionCreateRequest request) {
        List<Subsection> subsections = request.getSubsectionIds().stream().map(Subsection::new).toList();
        Session session = new Session();


    }

    @Override
    public Session findSessionByIdOrThrow(UUID sessionId) {
        return null;
    }

    @Override
    public List<Session> getAllUserSessions() {
        return List.of();
    }

    @Override
    public void updateSessionStatus(UUID sessionId, Status status) {

    }

    @Override
    public void endSession(UUID sessionId) {

    }

    @Override
    public double getSessionProgress(UUID sessionId) {
        return 0;
    }
}
