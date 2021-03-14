package ru.somarov.templates.java.stomp;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Конфигурация STOMP.
 *
 *  @author Alex Omarov
 *  @version 1.0.0
 *  @since 1.0.0
 */

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Включаем MessageBroker c отправкой сообщений в очередь на адрес /fight
     * @param config обьект, содержащий параметры брокера
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/fight");
    }

    /**
     * Регистрируем точку доступа STOMP. К этойточкебудут присоединяться пользователи. Через эту точку будетидти подписка на очереди и передача сообщений от клиента к указанному контроллеру.
     * @param registry Контракт для регистрации STOMP - точек доступа
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stompEndpoint").setAllowedOrigins("localhost:8080","uni.somarov.ru").withSockJS();
    }

    /**
     * Подключение Spring Security к рассылке сообщений
     * @param messages Класс для подключения Security к STOMP. Позволяет определить ограничения на пересылку сообщений по определенным адресам
     */
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages.simpDestMatchers("/stomp/message").authenticated().simpDestMatchers("/app/hello1").authenticated();//permitAll();

    }
}
