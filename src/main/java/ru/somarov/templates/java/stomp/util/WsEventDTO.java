package ru.somarov.templates.java.stomp.util;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.somarov.dwd.backend.json.views.*;

/**
 * DTO для пересылки сообщений от сервера в очередь
 * @see WebSocketSender#getSender(ObjectType, Class)
 */

@Data
@AllArgsConstructor
@JsonView(Views.Def.class)
class WsEventDTO {


    /**
     * Тип изменяемого обьекта
     */
    private ObjectType objectType;
    /**
     * Вид изменения
     */
    private EventType eventType;
    /**
     * Полученное значение
     */
    @JsonRawValue
    private String body;

}
