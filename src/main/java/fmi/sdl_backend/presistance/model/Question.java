package fmi.sdl_backend.presistance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cycle_id", nullable = false)
    private Cycle cycle;

    @NotNull
    @Size(min = 10, message = "Question text must be at least 10 characters")
    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @NotNull
    @Column(name = "option_a", nullable = false, columnDefinition = "TEXT")
    private String optionA;

    @NotNull
    @Column(name = "option_b", nullable = false, columnDefinition = "TEXT")
    private String optionB;

    @NotNull
    @Column(name = "option_c", nullable = false, columnDefinition = "TEXT")
    private String optionC;

    @NotNull
    @Column(name = "option_d", nullable = false, columnDefinition = "TEXT")
    private String optionD;

    @NotNull
    @Pattern(regexp = "[ABCD]", message = "Correct option must be A, B, C, or D")
    @Column(name = "correct_option", nullable = false, length = 1)
    private String correctOption;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @Column(columnDefinition = "TEXT", name = "explanation_to_generate_this_question")
    private String explanationToGenerateThisQuestion;

    @NotNull
    @Min(value = 1, message = "Question order must be at least 1")
    @Column(name = "question_order", nullable = false)
    private Integer questionOrder = 1;

    @Pattern(regexp = "[ABCD]?", message = "Submitted answer must be A, B, C, D, or null")
    @Column(name = "submitted_answer", length = 1)
    private String submittedAnswer;

    @Column(name = "answered_at")
    private OffsetDateTime answeredAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public void submitAnswer(String answer) {
        if (answer == null || !answer.matches("[AaBbCcDd]")) {
            throw new IllegalArgumentException("Answer must be A, a, B, b, C, c, D or d");
        }

        if (submittedAnswer != null) {
            throw new  IllegalArgumentException("Answer already exists");
        }

        this.submittedAnswer = answer.toUpperCase();
        this.answeredAt = OffsetDateTime.now();
    }

    public boolean isAnswered() {
        return submittedAnswer != null && answeredAt != null;
    }

    public boolean isSubmittedAnswerCorrect() {
        if (!isAnswered()) {
            throw new IllegalStateException("Question has not been answered yet");
        }
        return this.correctOption.equalsIgnoreCase(submittedAnswer);
    }

    public Long getAnswerTimeInMillis() {
        if (!isAnswered()) {
            return null;
        }
        return java.time.Duration.between(createdAt, answeredAt).toMillis();
    }
}