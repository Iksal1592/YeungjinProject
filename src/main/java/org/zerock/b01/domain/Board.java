package org.zerock.b01.domain;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageSet")
public class Board extends BaseEntity{
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

    public void change(String title, String content){
        this.title = title;
        this.content = content;
    }

    @OneToMany(mappedBy = "board",
                cascade = {CascadeType.ALL}, //모든 작업이 자식 엔티티에 전파됨
                fetch = FetchType.LAZY,
                orphanRemoval = true)

    @Builder.Default
    @BatchSize(size=20)
    private Set<LocalBoardImage> imageSet = new HashSet<>();
    //LocalBoardImage entity를 중복과 순서없이 저장함

    public void addImage(String uuid, String fileName){
        LocalBoardImage localBoardImage = LocalBoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imageSet.size()) //imageSet으로 저장된 LocalBoardImage의 개수를 반환함
                .build();

        imageSet.add(localBoardImage);

    }

    public void clearImage(){
        imageSet.forEach(localBoardImage -> localBoardImage.changeBoard(null));
        this.imageSet.clear();
    }
}