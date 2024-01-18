package org.zerock.b01.domain;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="TradeBoardReply", indexes = {
        @Index(name = "trade_board_reply_board_bno", columnList = "trade_bno")
})

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "trade")

public class TradeBoardReply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;
    private String replyer;
    private String replytext;

    @ManyToOne(fetch = FetchType.LAZY)
    private Trade trade;


    public void changereply(String text){this.replytext=text;}
}
