package org.zerock.b01.dto.Reply;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LocalBoardReplyCountDTO {
  private Long bno;
  private String title;
  private String writer;
  private String local;

  private LocalDateTime regDate;
  private Long replyCount;
    }

