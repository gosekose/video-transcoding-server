package server.video.transcoding.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static server.video.transcoding.common.MessageRouter.*;

@Configuration
public class RabbitmqMessageConfig {

    /**
     * 트랜스코드 시작 알림
     */
    @Qualifier(TRANSCODE_READY_QUEUE)
    @Bean
    public Queue transcodeReadyQueue() {
        return new Queue(TRANSCODE_READY_QUEUE);
    }

    @Qualifier(TRANSCODE_READY_EXCHANGE)
    @Bean
    public FanoutExchange transcodeReadyExchange() {
        return new FanoutExchange(TRANSCODE_READY_EXCHANGE);
    }

    @Bean
    public Binding transcodeReadyBinding(
            @Qualifier(TRANSCODE_READY_EXCHANGE) FanoutExchange transcodeVideoExchange,
            @Qualifier(TRANSCODE_READY_QUEUE) Queue transcodeVideoQueue) {
        return BindingBuilder.bind(transcodeVideoQueue).to(transcodeVideoExchange);
    }

    /**
     * 트랜스코드 종료 알림
     */
    @Qualifier(TRANSCODE_FINISH_QUEUE)
    @Bean
    public Queue transcodeFinishQueue() {return new Queue(TRANSCODE_FINISH_QUEUE);}

    @Qualifier(TRANSCODE_FINISH_EXCHANGE)
    @Bean
    public FanoutExchange transcodeFinishExchange() { return new FanoutExchange(TRANSCODE_FINISH_EXCHANGE);}

    @Bean
    public Binding transcodeFinishBinding(
            @Qualifier(TRANSCODE_FINISH_EXCHANGE) FanoutExchange transcodeVideoExchange,
            @Qualifier(TRANSCODE_FINISH_QUEUE) Queue transcodeVideoQueue) {
        return BindingBuilder.bind(transcodeVideoQueue).to(transcodeVideoExchange);
    }
}
