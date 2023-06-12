package server.video.transcoding.service.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.video.transcoding.service.dto.InfoMetadata;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class TransMetadataToTranscodingHandlerServer {

    private InfoMetadata infoMetadata;
    private List<TransVideoMetadata> transVideoMetadataList = new ArrayList<>();
    private String duration; // 지속 시간

    public TransMetadataToTranscodingHandlerServer(InfoMetadata infoMetadata, String duration) {
        this.infoMetadata = infoMetadata;
        this.duration = duration;
    }

    public void addMetadata(String transVideoFilePath, String bitrate, String format) {
        transVideoMetadataList.add(new TransVideoMetadata(transVideoFilePath, bitrate, format));
    }

    public String getTransVideoFilePath(int index) {
        return transVideoMetadataList.get(index).getTransVideoFilePath();
    }

    public String getBitrate(int index) {
        return transVideoMetadataList.get(index).getBitrate();
    }

    public String getFormat(int index) {
        return transVideoMetadataList.get(index).getFormat();
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    static class TransVideoMetadata {
        private String transVideoFilePath; // 저장소 위치
        private String bitrate; // 비트레이트
        private String format; // 확장자
    }
}
