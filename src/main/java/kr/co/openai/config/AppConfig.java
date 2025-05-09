package kr.co.openai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class AppConfig {

    /*
    * ChatClient <--------API KEY--------> LLM(OpenAI)
    * */
    @Value("classpath:prompt.txt")
    private Resource prompt;

    PromptTemplate promptTemplate = new PromptTemplate("""
            You are an AI assistant that specialized in {subject}.
            You respond in a {tone} voice with detailed explanation.
            """);

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        // SystemMessage(LLM에 역할을 부여)
        return chatClientBuilder
                //.defaultSystem("당신은 교육 튜터입니다. 개념을 명확하고 간단하게 설명하세요")
                //.defaultSystem(prompt)
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory())).build();
    }

}
