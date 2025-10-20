package fmi.sdl_backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.mistralai.MistralAiChatOptions;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ChatClientConfig {

    @Bean
	public OpenAiChatOptions baseOpenAiOptions() {
		return OpenAiChatOptions.builder().temperature(0.7).topP(0.9).frequencyPenalty(0.5).presencePenalty(0.5)
				.build();
	}

	@Bean
	public MistralAiChatOptions baseMistralOptions() {
		return MistralAiChatOptions.builder().temperature(0.7).frequencyPenalty(0.5).presencePenalty(0.5).topP(0.9)
				.build();
	}
}