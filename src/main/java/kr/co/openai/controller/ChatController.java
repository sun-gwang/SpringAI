package kr.co.openai.controller;

import kr.co.openai.entity.Answer;
import kr.co.openai.service.ChatService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/*
* 클라이언트의 요청을 받아서 JSON 형식으로 응답하는 컨트롤러
* */
@RestController
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping(value = "/chat")
    public String chat(@RequestParam("message") String message){
        return chatService.chat(message);
    }

    @GetMapping("/chatMessage")
    public String chatMessage(@RequestParam("message") String message){
        return chatService.chatMessage(message);
    }

    @GetMapping("/chatPlace")
    public String chatPlace(String subject, String tone, String message){ //DTO로 받을 거임
        return chatService.chatPlace(subject, tone, message);
    }

    @GetMapping("/chatJson")
    public ChatResponse chatJson(@RequestParam("message") String message){
        return chatService.chatJson(message);
    }

    @GetMapping("/chatObject")
    public Answer chatObject(String message){
        return chatService.ChatObject(message);
    }

    @GetMapping("/recipe")
    public Answer recipe(String foodName, String question){
        return chatService.recipe(foodName, question);
    }

    @GetMapping("/chatList")
    public List<String> chatList(String message){
        return chatService.chatList(message);
    }

    @GetMapping("/chatMap")
    public Map<String, String> chatMap(String message){
        return chatService.chatMap(message);
    }
}
