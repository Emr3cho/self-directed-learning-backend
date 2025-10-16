//package fmi.sdl_backend.service.factory;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.openai.OpenAiChatModel;
//import org.springframework.ai.openai.OpenAiChatOptions;
//import org.springframework.ai.openai.api.OpenAiApi;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.Resource;
//import org.springframework.stereotype.Component;
//
//@Component("openai_client_factory")
//@RequiredArgsConstructor
//public class AIClientFactoryOpenAI extends AIClientFactoryBase {
//
//	private final OpenAiChatOptions baseOpenAiOptions;
//
//	@Value("${ai.api-key}")
//	private String apiKey;
//
//	@Value("${ai.base-url}")
//	private String baseUrl;
//
//	@Value("${spring.ai.openai.chat.completions-path}")
//	private String completionsPath;
//
//	@Override
//	protected ChatClient createClient(String model, Resource promptName) {
//		String cacheKey = "openai:" + model + ":" + promptName.getFilename();
//
//		return clientCache.computeIfAbsent(cacheKey, key -> {
//			OpenAiChatOptions options = baseOpenAiOptions.copy();
//			options.setModel(model);
//
//			OpenAiChatModel chatModel = OpenAiChatModel.builder().defaultOptions(options).openAiApi(
//					OpenAiApi.builder().apiKey(apiKey).baseUrl(baseUrl).completionsPath(completionsPath).build())
//					.build();
//
//			return ChatClient.builder(chatModel).defaultSystem(promptName).build();
//		});
//	}
//}