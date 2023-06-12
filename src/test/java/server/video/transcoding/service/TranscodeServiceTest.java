package server.video.transcoding.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import server.video.transcoding.service.dto.DescriptionMetadataDto;
import server.video.transcoding.service.dto.MetadataDto;
import server.video.transcoding.service.dto.TransMetadataDto;

import java.io.IOException;
import java.nio.file.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TranscodeServiceTest {

    @Autowired
    TranscodeService transcodeService;
    @Autowired ResourceLoader resourceLoader; // 테스트 파일 경로를 가져오기

    @Value("${location.video.uploadPath}")
    private String uploadPath;

    private String videoFilePath;
    private String transFilePath;
    private final String SRC_PATH = "/home/koseyun/projects/spring/transcoding/src/test/resources/static/video/test-01.MP4";

    @BeforeEach
    void init() {
        Resource videoFileResource = resourceLoader.getResource(uploadPath);
        try {
            videoFilePath = videoFileResource.getFile().getAbsolutePath();
            String[] paths = videoFilePath.split("/");
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < paths.length - 1; i++) sb.append(paths[i]).append("/");
            sb.append("trans/test_trans");
            transFilePath = sb.toString();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteFile(String path) {
        Path fileToDeletePath = Paths.get(path);
        try {
            if (Files.exists(fileToDeletePath)) { //파일이 존재하는지 확인
                Files.delete(fileToDeletePath); //파일 삭제
                System.out.println("File deleted successfully");
            } else {
                System.out.println("File doesn't exist");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("포맷팅 test")
    public void video_test() throws Exception {
        //given
        DescriptionMetadataDto descriptionMetadataDto = new DescriptionMetadataDto(3L);
        if (videoFilePath == null) {
            videoFilePath = SRC_PATH;
            transFilePath = "/home/koseyun/projects/spring/transcoding/build/resources/test/static";
        }

        MetadataDto metadataDto =
                new MetadataDto(descriptionMetadataDto, videoFilePath, transFilePath);

        //when
        TransMetadataDto metaDataDto = transcodeService.transcode(metadataDto);

        //then
        assertThat(metaDataDto.getTransVideoMetadataList().isEmpty()).isFalse();
        for (int i = 1; i < metaDataDto.getTransVideoMetadataList().size(); i++) {
            System.out.printf("Path = %s, bitrate = %s, format = %s%n",
                    metaDataDto.getTransVideoFilePath(i),
                    metaDataDto.getBitrate(i),
                    metaDataDto.getFormat(i));

            deleteFile(metaDataDto.getTransVideoFilePath(i));
        }
        System.out.println("duration = " + metaDataDto.getDuration());
    }
}