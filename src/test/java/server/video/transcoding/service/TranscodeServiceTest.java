package server.video.transcoding.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import server.video.transcoding.service.message.VideoFileMessage;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

@SpringBootTest
class TranscodeServiceTest {

    @Autowired
    TranscodeService transcodeService;
    @Autowired ResourceLoader resourceLoader; // 테스트 파일 경로를 가져오기

    @Value("${location.video.uploadPath}")
    private String uploadPath;

    private String videoFilePath;
    private String transFilePath;

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

    @AfterEach
    public void cleanup() throws Exception {
        String path = "";
        String[] filePaths = videoFilePath.split("/");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < filePaths.length - 1; i++) {
            if (i != filePaths.length - 2) sb.append(filePaths[i]).append("/");
            else sb.append(filePaths[i]);
        }
        path = sb.toString();
        System.out.println("delete Path = " + path);

        Path directory = Paths.get(path);
        if (Files.exists(directory)) {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    @Test
    @DisplayName("포맷팅 test")
    public void video_test() throws Exception {
        //given
        VideoFileMessage videoFileMessage = new VideoFileMessage(videoFilePath, transFilePath);

        //when
        transcodeService.transcode(videoFileMessage);
        //then
    }
}