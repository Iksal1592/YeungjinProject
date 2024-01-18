package org.zerock.b01.dto.Reply;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeBoardReplyDTO {


        private Long rno;
        @NotNull
        private Long bno;

        @NotEmpty
        private String replyer;
        @NotEmpty
        private String replytext;
        private LocalDateTime regDate;
        private LocalDateTime modDate;

    }




