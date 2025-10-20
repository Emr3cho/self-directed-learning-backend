package fmi.sdl_backend.service;

import fmi.sdl_backend.presistance.model.Cycle;
import fmi.sdl_backend.rest.response.QuestionSectionResponse;

public interface AiSectionSelectionService {
    QuestionSectionResponse selectSectionForNextQuestion(Cycle cycle, String previousQuestionsAnalysisText);
}
