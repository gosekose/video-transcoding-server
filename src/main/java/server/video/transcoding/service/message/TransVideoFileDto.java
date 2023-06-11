package server.video.transcoding.service.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransVideoFileDto {
    private String uploadFilePath;
    private String transcodingFilePath;
}
