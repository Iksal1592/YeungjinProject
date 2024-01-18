package org.zerock.b01.domain;


import lombok.*;
import javax.persistence.*;


// trade에 들어가는 것 :
// 번호, 작성자, 제목,내용, 등록시간,

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Trade extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long bno;

    @Column(length = 500, nullable = false)
    private String local;
    @Column(length = 500, nullable = false)
    private String title;
    @Column(length = 2000, nullable = false)
    private String content;
    @Column(length = 50, nullable = false)
    private String writer;


    public void tradechange(String title, String content){
        this.title = title;
        this.content = content;
    }




}
