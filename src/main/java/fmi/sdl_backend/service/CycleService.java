package fmi.sdl_backend.service;

import fmi.sdl_backend.rest.response.QuestionToAsk;
import fmi.sdl_backend.rest.response.cycle.CreateCycleResponse;

import java.util.UUID;

public interface CycleService {
    CreateCycleResponse createCycleBySessionId(UUID sessionId);
    QuestionToAsk getNextQuestionForCycle(UUID cycleId);
}
