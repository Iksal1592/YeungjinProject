package org.zerock.b01.domain;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")
public class LocalBoardImage implements Comparable<LocalBoardImage>{
    @Id
    private String uuid;
    private String fileName;
    private int ord;

    @ManyToOne
    private Board board;

    @Override
    public int compareTo(LocalBoardImage other){return this.ord - other.ord;}
    public void changeBoard(Board board) {this.board = board;}
}
