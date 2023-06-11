package server.video.transcoding.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import server.video.transcoding.service.message.TransVideoMetaDataDto;
import server.video.transcoding.service.message.TransVideoFileDto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TranscodeService {

    /**
     * ffmpeg 소프트웨어 실행 파일 위치
     */
    @Value("${location.ffmpeg}")
    private String ffmpeg;


    private static final String[] formatList = {
            "mp4"
    };

    private static final String[] bitrateList = {
            "1000k",
            "240k"
    };

    public List<TransVideoMetaDataDto> transcode(TransVideoFileDto transVideoFileDto) {
        List<TransVideoMetaDataDto> metaDataDtos = new ArrayList<>();
        log.info("filePath = {}, savePath = {}", transVideoFileDto.getUploadFilePath(), transVideoFileDto.getTranscodingFilePath());

        // 원본 파일의 경로, bitrate, format 저장
//        metaDataDtos.add(transVideoFileDto.getUploadFilePath());

        for (String format : formatList) {
            for (String bitrate : bitrateList) {
                String transPath = transVideoFileDto.getTranscodingFilePath() + "_" + bitrate;

                // ffmpeg -i 파일명 -vf 변환 비트레이트 변환 위치.포멧
                String command = String.format("%s -i %s -b:v %s %s.%s",
                        ffmpeg,
                        transVideoFileDto.getUploadFilePath(),
                        bitrate,
                        transPath,
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

                        metaDataDtos.add(TransVideoMetaDataDto
                                .builder()
                                .transVideoFilePaths(transPath)
                                .bitrate(bitrate)
                                .format(format)
                                .build());
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }

        return metaDataDtos;
    }


}
