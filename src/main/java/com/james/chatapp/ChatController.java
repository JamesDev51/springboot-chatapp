package com.james.chatapp;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController //데이터 리턴 서버
@CrossOrigin
public class ChatController {

      private final ChatRepository chatRepository;

      //귓속말 할 때 사용
      @GetMapping(value="/sender/{sender}/receiver/{receiver}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
      public Flux<Chat> getMessage(@PathVariable String sender, @PathVariable String receiver){
            return chatRepository.mFindBySender(sender,receiver)
                        .subscribeOn(Schedulers.boundedElastic());

      }

      //귓속말 할 때 사용
      @GetMapping(value="/chat/roomNum/{roomNum}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
      public Flux<Chat> findByRoomNum(@PathVariable Integer roomNum){
            return chatRepository.mFindByRoomNum(roomNum)
                        .subscribeOn(Schedulers.boundedElastic());

      }

      @PostMapping("/chat")
      public Mono<Chat> setMsg(@RequestBody Chat chat){
            chat.setCreatedAt(LocalDateTime.now());
            return chatRepository.save(chat);
      }
}
