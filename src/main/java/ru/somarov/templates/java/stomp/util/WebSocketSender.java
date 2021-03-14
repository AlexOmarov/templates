package ru.somarov.templates.java.stomp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;


/**
 * Класс для конфигурации sender-a сообщений.
 * Класс загружается вместе с контекстом и получает от Spring-a {@link SimpMessagingTemplate SimpMessagingTemplate} и {@link ObjectMapper ObjectMapper}.
 * {@link SimpMessagingTemplate SimpMessagingTemplate} - реализацию интерфейса обмена сообщениями по STOMP - протоколу.
 * Позволяет отправлять обьекты в виде JSON на указанный адрес.
 * {@link ObjectMapper ObjectMapper} - класс для преобразования обьектов JSON (часть Jackson). Позволяет получить {@link ObjectWriter ObjectWriter}.
 * {@link ObjectWriter ObjectWriter} - класс для конфигурирования преобразования обьектов в JSON. Позволяет указывать JSON View.
 *
 *
 * @see WebSocketSender#getSender(ObjectType, Class)
 */

@Component
public class WebSocketSender {

    private final SimpMessagingTemplate template;
    private final ObjectMapper mapper;


    public WebSocketSender(SimpMessagingTemplate template, ObjectMapper mapper) {
        this.template = template;
        this.mapper = mapper;
    }

    public <T> BiConsumer<EventType,T> getSender(ObjectType objectType, Class view){

        ObjectWriter writer = mapper.setConfig(mapper.getSerializationConfig()).writerWithView(view);

        return (EventType eventtype, T payload) -> {
            String value = null;
            try {
                value = writer.writeValueAsString(payload);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            template.convertAndSend("/fight", new WsEventDTO(objectType,eventtype,value));
        };
    }
}
