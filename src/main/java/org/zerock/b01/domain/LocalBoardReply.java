package org.zerock.b01.domain;



import lombok.*;
import org.w3c.dom.Text;
import org.zerock.b01.domain.BaseEntity;
import org.zerock.b01.domain.Board;



import javax.persistence.*;

@Entity
@Table(name="LocalBoardReply", indexes = {
        @Index(name = "local_board_reply_board_bno", columnList = "board_bno")
}) //==> 하는 기능


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "board")
public class LocalBoardReply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;
    private String replyer;
    private String replytext;

    @ManyToOne(fetch = FetchType.LAZY) //하나의 게시물에 여러개의 댓글
    private Board board; //부모엔티티(하나)에 자식엔티티(여러개)를 관계 짓는것.


    //값을 바꾸는 메서드, changereply에 값이 들어가면 reply의 값이 변경됨
    //댓글 수정
    public void changereply(String text){this.replytext=text;}
}
