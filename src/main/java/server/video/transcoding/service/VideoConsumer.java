package server.video.transcoding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import server.video.transcoding.service.message.VideoFileMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoConsumer {

    private final VideoTranscodingService videoTranscodingService;

    @RabbitListener(queues = "#{transcodeVideoQueue.name}")
    public void transcodeVideoFile(VideoFileMessage videoFileMessage) {
        try {
            videoTranscodingService.transcode(videoFileMessage);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
