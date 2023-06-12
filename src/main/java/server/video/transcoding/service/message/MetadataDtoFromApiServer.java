package server.video.transcoding.service.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.video.transcoding.service.dto.InfoMetadata;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MetadataDtoFromApiServer implements Serializable {
    private InfoMetadata infoMetadata; // infoMetadata의 pk
    private VideoMetadata videoMetadata;

    public MetadataDtoFromApiServer(InfoMetadata infoMetadata, String uploadFilePath, String transcodingFilePath) {
        this.infoMetadata = infoMetadata;
        this.videoMetadata = new VideoMetadata(uploadFilePath, transcodingFilePath);
    }

    public String getUploadFilePathInVideoMetadata() {
        return videoMetadata.getUploadFilePath();
    }

    public String getTranscodingFilePathInVideoMetadata() {
        return videoMetadata.getTranscodingFilePath();
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    static class VideoMetadata implements Serializable {
        private String uploadFilePath;
        private String transcodingFilePath;
    }
}
