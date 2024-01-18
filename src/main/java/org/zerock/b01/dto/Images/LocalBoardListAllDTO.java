package org.zerock.b01.dto.Images;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class LocalBoardListAllDTO {
    private Long bno;
    private String title;
    private String writer;
    private String local;
    private LocalDateTime regDate;
    private Long replyCount;

    private List<LocalBoardImageDTO> boardImages;
}
