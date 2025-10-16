//package fmi.sdl_backend.service.factory;
//
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.Resource;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//public abstract class AIClientFactoryBase implements AIClientFactory {
//	protected final Map<String, ChatClient> clientCache = new ConcurrentHashMap<>();
//
//	@Value("classpath:prompts/content_selection/sectionSelectionSystemPrompt.st")
//	private Resource contentSelectionSysPrompt;
//
//	@Value("classpath:prompts/question_generation/generateQuestionSystemPrompt.st")
//	private Resource generateQuestionSystemPrompt;
//
//	@Value("classpath:prompts/interview_review/interviewReviewSystemPrompt.st")
//	private Resource interviewReviewSystemPrompt;
//
//
//	public ChatClient createContentSelectionClient(String chatModel) {
//		return this.createClient(chatModel, contentSelectionSysPrompt);
//	}
//
//	public ChatClient createQuestionGenerationClient(String chatModel) {
//		return this.createClient(chatModel, generateQuestionSystemPrompt);
//	}
//
//	public ChatClient createInterviewReviewClient(String chatModel) {
//		return this.createClient(chatModel, interviewReviewSystemPrompt);
//	}
//
//	protected abstract ChatClient createClient(String model, Resource promptName);
//}
