package fmi.sdl_backend.mapper;

import fmi.sdl_backend.presistance.model.Question;
import fmi.sdl_backend.rest.response.interview.question.GenerateQuestionDTO;
import fmi.sdl_backend.rest.response.interview.question.QuestionDetailsResponse;
import fmi.sdl_backend.rest.response.interview.question.QuestionToAsk;
import fmi.sdl_backend.rest.response.interview.question.SubmitAnswerResponse;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "questionOrder", ignore = true)
    @Mapping(target = "submittedAnswer", ignore = true)
    @Mapping(target = "answeredAt", ignore = true)
    Question toQuestion(GenerateQuestionDTO generateQuestionDTO);

    @Mapping(target = "questionId", source = "question.id")
    @Mapping(target = "title", source = "question.questionText")
    QuestionToAsk toQuestionToAsk(Question question);

    @Mapping(target = "questionId", source = "question.id")
    @Mapping(target = "correctAnswer", source = "question.correctOption")
    @Mapping(target = "correct", ignore = true)
    @Mapping(target = "currentProgress", ignore = true)
    SubmitAnswerResponse toSubmitAnswerResponse(Question question, String submittedAnswer);

    @Mapping(target = "isCorrect", expression = "java(getIsCorrect(question))")
    @Mapping(target = "answerTimeInMillis", expression = "java(getAnswerTimeInMillis(question))")
    @Named("toQuestionDetailsResponse")
    QuestionDetailsResponse toQuestionDetailsResponse(Question question);

    @IterableMapping(qualifiedByName = "toQuestionDetailsResponse")
    @Named("toQuestionDetailsResponses")    
    List<QuestionDetailsResponse> toQuestionDetailsResponses(List<Question> questions);

    // Helper methods for QuestionMapper
    default Boolean getIsCorrect(Question question) {
        if (!question.isAnswered()) return null;
        return question.isSubmittedAnswerCorrect();
    }

    default Long getAnswerTimeInMillis(Question question) {
        return question.getAnswerTimeInMillis();
    }
}
