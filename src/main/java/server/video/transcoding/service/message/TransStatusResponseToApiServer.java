package server.video.transcoding.service.message;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.video.transcoding.service.dto.InfoMetadata;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TransStatusResponseToApiServer implements Serializable {
    private InfoMetadata infoMetadata;
    private boolean status;
    private LocalDateTime finishTime;

    @Builder
    public TransStatusResponseToApiServer(InfoMetadata infoMetadata, boolean status, LocalDateTime finishTime) {
        this.infoMetadata = infoMetadata;
        this.status = status;
        this.finishTime = finishTime;
    }
}
