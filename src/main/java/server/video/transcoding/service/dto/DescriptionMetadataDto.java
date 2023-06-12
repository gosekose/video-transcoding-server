package server.video.transcoding.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DescriptionMetadataDto implements Serializable {
    private Long descriptionId;
}
