package fmi.sdl_backend.rest.response.interview.question;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerateQuestionDTO {
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctOption; // 'A', 'B', 'C', or 'D'
    private String explanation;
    private String explanationToGenerateThisQuestion;
}