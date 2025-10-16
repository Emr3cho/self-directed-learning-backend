//package fmi.sdl_backend.config;
//
//import fmi.sdl_backend.service.factory.AIClientFactory;
//import lombok.RequiredArgsConstructor;
//import org.springframework.ai.mistralai.MistralAiChatOptions;
//import org.springframework.ai.openai.OpenAiChatOptions;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//@Configuration
//@RequiredArgsConstructor
//public class ChatClientConfig {
//
//	private final ApplicationContext context;
//
//	@Bean
//	@Primary
//	public AIClientFactory aiClientFactory(@Value("${ai.client_factory}") String qualifier) {
//		return (AIClientFactory) context.getBean(qualifier);
//	}
//
//	@Bean
//	public OpenAiChatOptions baseOpenAiOptions() {
//		return OpenAiChatOptions.builder().temperature(0.7).topP(0.9).frequencyPenalty(0.5).presencePenalty(0.5)
//				.build();
//	}
//
//	@Bean
//	public MistralAiChatOptions baseMistralOptions() {
//		return MistralAiChatOptions.builder().temperature(0.7).frequencyPenalty(0.5).presencePenalty(0.5).topP(0.9)
//				.build();
//	}
//}