package server.video.transcoding.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * From: Api-Server
 * To: Transcoding-Server

 * descriptionMetadataDto: 동영상에 관한 제목 설명을 저장한 pk
 * videoMetadata: 동영상 관련 경로 등의 정보를 담은 data
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MetadataDto implements Serializable {
    private DescriptionMetadataDto descriptionMetadataDto; // infoMetadata의 pk
    private VideoMetadata videoMetadata;

    public MetadataDto(DescriptionMetadataDto descriptionMetadataDto, String uploadFilePath, String transcodingFilePath) {
        this.descriptionMetadataDto = descriptionMetadataDto;
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
