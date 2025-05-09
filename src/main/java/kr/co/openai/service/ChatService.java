package kr.co.openai.service;

import kr.co.openai.entity.Answer;
import kr.co.openai.entity.Movie;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Service
public class ChatService {
    private final ChatClient chatClient;

    public ChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /*
     * 클라이언트 -> 서버로 보낼 때 프롬프트를 사용해야함
     * */
    public String chat(String message) {
        return chatClient.prompt() // 프롬프트 생성
                .user(message) // 사용자 메세지
                .call() // 호출
                .content(); // chatResponse에서 요청 정보를 받는 부분(문자열로만 받음)
    }

    public String chatMessage(String message) {
        return chatClient
                .prompt()
                .user(message)
                .call()
                .chatResponse() // chatResponse타입으로 받음
                .getResult()
                .getOutput()
                .getText();
    }

    public String chatPlace(String subject, String tone, String message) {
        return chatClient
                .prompt()
                .user(message)
                .system(promptSystemSpec -> promptSystemSpec
                        .param("subject", subject)
                        .param("tone", tone)
                )
                .call()
                .chatResponse()
                .getResult()
                .getOutput()
                .getText();
    }

    public ChatResponse chatJson(String message) {
        return chatClient
                .prompt()
                .user(message)
                .call()
                .chatResponse();//ChatResponse((?)--> JSON)
    }

    public Answer ChatObject(String message) {
        return chatClient
                .prompt()
                .user(message)
                .call()
                .entity(Answer.class);
    }

    private final String recipeTemplate = """
            Answer for {foodName} for{question}?
            """;

    public Answer recipe(String foodName, String question) {
        return chatClient
                .prompt()
                .user(promptUserSpec -> promptUserSpec.text(recipeTemplate)
                        .param("foodName", foodName)
                        .param("question", question)
                )
                .call()
                .entity(Answer.class);
    }

    public List<String> chatList(String message) {
        return chatClient
                .prompt()
                .user(message)
                .call()
                .entity(new ListOutputConverter(new DefaultConversionService()));
    }

    public Map<String, String> chatMap(String message) {
        return chatClient
                .prompt()
                .user(message)
                .call()
                .entity(new ParameterizedTypeReference<Map<String, String>>() {
                });
    }

    public List<Movie> chatMovie(String directorName) {

        String template = """
                    "Generate a list of movies directed by {directorName}. If the directorName is unknown, return null.
                    한국영화는 한글로 표기해줘.
                    Each movie should include a title and release year. {format}
                    "
                """;

        List<Movie> movieList = chatClient.prompt()
                .user(promptUserSpec -> promptUserSpec.text(template)
                        .param("directorName", directorName)
                        .param("format", "json")
                )
                .call()
                .entity(new ParameterizedTypeReference<List<Movie>>() {});
        return movieList;
    }

    public String getResponse(String message){
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    public void startChat(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("메세지를 입력하세요");

        while(true){
            String message = scanner.nextLine();

            if(message.equals("exit")){
                System.out.println("채팅이 끝났습니다.");
                break;
            }

            String response = getResponse(message);
            System.out.println("Bot : " + response);
        }
        scanner.close();
    }
}
