package server.video.transcoding.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import server.video.transcoding.service.message.TransMetadataDto;
import server.video.transcoding.service.message.MetadataDto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public TransMetadataDto transcode(MetadataDto metadataDto) {

        String originalFilePath = metadataDto.getUploadFilePathInVideoMetadata();
        String originalDuration = null;
        String originalBitrate = null;

        // 원본 파일의 경로, bitrate, format 저장
        String command = String.format("%s -i %s", ffmpeg, originalFilePath);
        try {
            Process process = Runtime.getRuntime().exec(command);
            try (
                    InputStreamReader inputStreamReader = new InputStreamReader(process.getErrorStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
            ) {
                    Pattern durationPattern = Pattern.compile("Duration: ([^,]*),");
                    Pattern bitratePattern = Pattern.compile("bitrate: ([^ ]*) kb/s");

                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                         Matcher durationMatcher = durationPattern.matcher(line);
                         Matcher bitrateMatcher = bitratePattern.matcher(line);

                         if (durationMatcher.find()) originalDuration = durationMatcher.group(1);
                         if (bitrateMatcher.find()) originalBitrate = bitrateMatcher.group(1);
                    }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        TransMetadataDto transMetadataDto = new TransMetadataDto(
                metadataDto.getInfoMetadata(), originalDuration);

        String[] formats = originalFilePath.split("\\.");
        transMetadataDto.addMetadata(originalFilePath, originalBitrate, formats[formats.length - 1]);

        for (String format : formatList) {
            for (String bitrate : bitrateList) {
                String transPath = metadataDto.getTranscodingFilePathInVideoMetadata() + "_" + bitrate;

                // ffmpeg -i 파일명 -vf 변환 비트레이트 변환 위치.포멧
                command = String.format("%s -i %s -b:v %s %s.%s",
                        ffmpeg, metadataDto.getUploadFilePathInVideoMetadata(),
                        bitrate, transPath, format
                );

                try {
                    log.info("command = {}", command);
                    Process process = Runtime.getRuntime().exec(command);

                    try (InputStreamReader inputStreamReader = new InputStreamReader(process.getErrorStream());
                         BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            log.info(line);
                        }

                        if (process.waitFor() != 0) log.error("error = {}", process.exitValue());
                        transMetadataDto.addMetadata(transPath + "." + format, bitrate, format);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }

        return transMetadataDto;
    }


}
