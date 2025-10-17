package fmi.sdl_backend.service;

import fmi.sdl_backend.presistance.model.Session;
import fmi.sdl_backend.presistance.model.enums.Status;
import fmi.sdl_backend.rest.request.session.SessionCreateRequest;

import java.util.List;
import java.util.UUID;

public interface SessionService {
    void createSession(SessionCreateRequest request);
    Session findSessionByIdOrThrow(UUID sessionId);
    List<Session> getAllUserSessions();
    void updateSessionStatus(UUID sessionId, Status status);
    void endSession(UUID sessionId);
    double getSessionProgress(UUID sessionId);
}