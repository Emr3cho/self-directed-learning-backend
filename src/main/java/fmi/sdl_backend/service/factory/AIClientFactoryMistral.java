//package fmi.sdl_backend.service.factory;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.mistralai.MistralAiChatModel;
//import org.springframework.ai.mistralai.MistralAiChatOptions;
//import org.springframework.ai.mistralai.api.MistralAiApi;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.Resource;
//import org.springframework.stereotype.Component;
//
//@Component("mistral_client_factory")
//@RequiredArgsConstructor
//public class AIClientFactoryMistral extends AIClientFactoryBase {
//
//    private final MistralAiChatOptions baseMistralOptions;
//
//    @Value("${ai.api-key}")
//    private String mistralAiApiKey;
//
//    @Override
//    protected ChatClient createClient(String model, Resource promptName) {
//        String cacheKey = "mistral:" + model + ":" + promptName.getFilename();
//
//        return clientCache.computeIfAbsent(cacheKey, key -> {
//            MistralAiChatOptions options = baseMistralOptions.copy();
//            options.setModel(model);
//
//            MistralAiChatModel chatModel = MistralAiChatModel.builder().defaultOptions(options)
//                    .mistralAiApi(new MistralAiApi(mistralAiApiKey)).build();
//
//            return ChatClient.builder(chatModel).defaultSystem(promptName).build();
//        });
//    }
//
//}