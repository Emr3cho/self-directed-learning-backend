package fmi.sdl_backend.service.impl;

import fmi.sdl_backend.mapper.SessionMapper;
import fmi.sdl_backend.presistance.model.Session;
import fmi.sdl_backend.presistance.model.Subsection;
import fmi.sdl_backend.presistance.model.enums.Status;
import fmi.sdl_backend.presistance.repository.SessionRepository;
import fmi.sdl_backend.rest.request.session.SessionCreateRequest;
import fmi.sdl_backend.rest.response.session.SessionResponse;
import fmi.sdl_backend.service.SessionService;
import fmi.sdl_backend.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionMapper sessionMapper;
    private final SessionRepository sessionRepository;

    @Override
    public SessionResponse createSession(SessionCreateRequest request) {
        List<Subsection> subsections = request.getSubsectionIds().stream().map(Subsection::new).toList();
        Session session = sessionMapper.toSession(request, subsections);
        session.setCreatedBy(SecurityUtils.getCurrentUser());
        session.setStartTime(OffsetDateTime.now());
        Session savedEntity = sessionRepository.save(session);
        return new SessionResponse(savedEntity.getId(), savedEntity.getName());
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
