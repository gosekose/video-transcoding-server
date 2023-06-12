package server.video.transcoding.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import server.video.transcoding.service.dto.InfoMetadata;
import server.video.transcoding.service.message.TransStatusDto;
import server.video.transcoding.service.message.MetadataDto;
import server.video.transcoding.service.message.TransMetadataDto;

import java.time.LocalDateTime;

import static server.video.transcoding.common.MessageRouter.*;

@Slf4j
@Service
public class TranscodeConsumer {

    private final TranscodeService transcodeService;
    private final RabbitTemplate rabbitTemplate;

    private final Queue finishQueue;
    private final Queue successStatusQueue;

    public TranscodeConsumer(TranscodeService transcodeService,
                             RabbitTemplate rabbitTemplate,
                             @Qualifier(TRANSCODE_FINISH_QUEUE) Queue finishQueue,
                             @Qualifier(TRANSCODE_SUCCESS_STATUS_QUEUE) Queue successStatusQueue) {
        this.transcodeService = transcodeService;
        this.rabbitTemplate = rabbitTemplate;
        this.finishQueue = finishQueue;
        this.successStatusQueue = successStatusQueue;
    }

    @RabbitListener(queues = "#{" + TRANSCODE_READY_QUEUE + ".name}")
    public void transcodeAndSendMessage(MetadataDto metadataDto) {
        try {
            TransMetadataDto message = transcodeService.transcode(metadataDto);
            sendMessageWithTransVideoPaths(message);
            sendMessageWithSuccessStatus(metadataDto.getInfoMetadata(), true);

        } catch (Exception e) {
            log.error(e.getMessage());
            sendMessageWithSuccessStatus(metadataDto.getInfoMetadata(), false);
        }
    }


    /**
     * transcode를 요청한 api 서버로 성공 여부 메세지 전달
     */
    private void sendMessageWithSuccessStatus(InfoMetadata metadata, boolean status) {
        rabbitTemplate.convertAndSend(successStatusQueue.getName(),
                TransStatusDto.builder()
                        .infoMetadata(metadata)
                .status(status).finishTime(LocalDateTime.now()).build());
    }

    /**
     *transcode를 성공하면 트랜스코딩을 완료한 content 메타 데이터 전달
     */
    private void sendMessageWithTransVideoPaths(TransMetadataDto message) {
        rabbitTemplate.convertAndSend(finishQueue.getName(), message);
    }
}
