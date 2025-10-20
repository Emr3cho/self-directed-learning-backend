package fmi.sdl_backend.service.impl;

import fmi.sdl_backend.presistance.model.Cycle;
import fmi.sdl_backend.presistance.model.Session;
import fmi.sdl_backend.presistance.repository.CycleRepository;
import fmi.sdl_backend.presistance.repository.SessionRepository;
import fmi.sdl_backend.rest.response.QuestionSectionResponse;
import fmi.sdl_backend.rest.response.QuestionToAsk;
import fmi.sdl_backend.rest.response.cycle.CreateCycleResponse;
import fmi.sdl_backend.service.CycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CycleServiceImpl implements CycleService {

    private final SessionRepository sessionRepository;
    private final CycleRepository cycleRepository;

    private final AiSectionSelectionServiceImpl aiSectionSelectionServiceImpl;

    @Override
    public CreateCycleResponse createCycleBySessionId(UUID sessionId) {
        Session session = sessionRepository.findById(sessionId).orElse(null);
        Cycle cycle = new Cycle();
        cycle.setSession(session);
        int cycleCountInSession = session.getCycles().size();
        cycle.setTitle(session.getName() + " - " + (cycleCountInSession + 1));
        cycle.setCycleOrder(cycleCountInSession + 1);
        cycle = cycleRepository.save(cycle);
        return new CreateCycleResponse(cycle.getId(), cycle.getTitle());
    }

    @Override
    public QuestionToAsk getNextQuestionForCycle(UUID cycleId) {
        Session session = sessionRepository.findById(cycleId).orElse(null);
        StringBuilder text = new StringBuilder();
        session.getSubsections().forEach(x -> text.append(x.getSubsectionInfo()));
        //QuestionSectionResponse questionSectionResponse = aiSectionSelectionServiceImpl.selectSectionForNextQuestion()
        return null;
    }
}
