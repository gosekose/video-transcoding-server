package server.video.transcoding.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.video.transcoding.service.dto.InfoMetadataDto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * From: Transcoding-Server
 * To: Api-Server

 * infoMetadataDto: 동영상에 관한 제목 설명을 저장한 pk
 * status: 성공 실패 여부
 * finishTime: 종료 시간
 */
@Getter
@NoArgsConstructor
public class TransStatusDto implements Serializable {
    private InfoMetadataDto infoMetadataDto;
    private boolean status;
    private LocalDateTime finishTime;

    @Builder
    public TransStatusDto(InfoMetadataDto infoMetadataDto, boolean status, LocalDateTime finishTime) {
        this.infoMetadataDto = infoMetadataDto;
        this.status = status;
        this.finishTime = finishTime;
    }
}
