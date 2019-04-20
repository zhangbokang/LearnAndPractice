package com.zbk.wsboot.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * The type Web socket config.
 *
 * @author 张卜亢
 * @date 2019.04.21 01:24:12
 */
@Configuration
public class WebSocketConfig {

    /**
     * websocket服务中心
     * @return websocket服务中心
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        //定义websocket服务中心
        return new ServerEndpointExporter();
    }
}
