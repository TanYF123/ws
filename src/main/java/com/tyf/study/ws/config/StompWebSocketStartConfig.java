package com.tyf.study.ws.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.ByteArrayMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class StompWebSocketStartConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 注册stomp端点，主要是起到连接作用
     * @param stompEndpointRegistry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry
                .addEndpoint("/webSocket")  //端点名称
//                .setHandshakeHandler()// 握手处理，主要是连接的时候认证获取其他数据验证等
                //.addInterceptors() 拦截处理，和http拦截类似
                .setAllowedOriginPatterns("*") //跨域
                .withSockJS(); //使用sockJS

    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        for (int i = 0; i < messageConverters.size(); i++) {
            String name = messageConverters.get(i).getClass().getSimpleName();
            System.out.println(name);
        }
        messageConverters.add(new ByteArrayMessageConverter());
        return true;
    }

    /**
     * 注册相关服务
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //这里使用的是内存模式，生产环境可以使用rabbitmq或者其他mq。
        //这里注册两个，主要是目的是将广播和队列分开。
        //registry.enableStompBrokerRelay().setRelayHost().setRelayPort() 其他方式
        registry.enableSimpleBroker("/topic", "/queue");
        //客户端发送消息时需要带上前缀/app
        registry.setApplicationDestinationPrefixes("/app");
        //用户名称前
        registry.setUserDestinationPrefix("/user");
    }
}
