package server.video.transcoding.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import server.video.transcoding.service.message.VideoFileMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
@Service
public class VideoTranscodingService {

    /**
     * ffmpeg 소프트웨어 실행 파일 위치
     */
    @Value("${location.ffmpeg}")
    private String ffmpeg;


    private static final String[] formatList = {
            "mp4", "mkv"
    };

    private static final String[] bitrateList = {
            "1000k",
            "240k"
    };

    public void transcode(VideoFileMessage videoFileMessage) {

        log.info("filePath = {}, savePath = {}", videoFileMessage.getUploadFilePath(), videoFileMessage.getTranscodingFilePath());
        for (String format : formatList) {
            for (String bitrate : bitrateList) {

                // ffmpeg -i 파일명 -vf 변환 비트레이트 변환 위치.포멧
                String command = String.format("%s -i %s -b:v %s %s.%s",
                        ffmpeg,
                        videoFileMessage.getUploadFilePath(),
                        bitrate,
                        videoFileMessage.getTranscodingFilePath() + "_" + bitrate,
                        format
                );

                try {
                    log.info("command = {}", command);
                    Process process = Runtime.getRuntime().exec(command);

                    try (InputStreamReader inputStreamReader = new InputStreamReader(process.getErrorStream());
                         BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            log.info("stream = {}", line);
                        }

                        if (process.waitFor() != 0) { // 0이 아니라면 잘못된 변환
                            log.error("error = {}", process.exitValue());
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
    }


}
