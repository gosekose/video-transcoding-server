package server.video.transcoding.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    private static final String TRANSCODE_VIDEO_QUEUE = "transcodeVideoQueue";
    private static final String TRANSCODE_VIDEO_EXCHANGE = "transcodeVideoExchange";

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Qualifier("transcodeVideoQueue")
    @Bean
    public Queue transcodeVideoQueue() {
        return new Queue(TRANSCODE_VIDEO_QUEUE);
    }

    @Qualifier("transcodeVideoExchange")
    @Bean
    public FanoutExchange transcodeVideoExchange() {
        return new FanoutExchange(TRANSCODE_VIDEO_EXCHANGE);
    }

    @Bean
    public Binding payBinding(
            @Qualifier("transcodeVideoExchange") FanoutExchange transcodeVideoExchange,
            @Qualifier("transcodeVideoQueue") Queue transcodeVideoQueue) {
        return BindingBuilder.bind(transcodeVideoQueue).to(transcodeVideoExchange);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
