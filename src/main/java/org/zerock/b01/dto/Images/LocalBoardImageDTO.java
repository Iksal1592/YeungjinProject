package org.zerock.b01.dto.Images;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class LocalBoardImageDTO {
    private String uuid;
    private String fileName;
    private int ord;
}
