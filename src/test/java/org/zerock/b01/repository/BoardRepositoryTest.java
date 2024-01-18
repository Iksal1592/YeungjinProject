package org.zerock.b01.repository;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.zerock.b01.domain.Board;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void testInsert() {
        IntStream.rangeClosed(1, 2).forEach(i -> {
            Board board = Board.builder()
                    .title("시간 보려고..." + i)
                    .content("시간 test..." + i)
                    .writer("user" + (i % 10))
                    .build();
            Board result = boardRepository.save(board);
            log.info("BNO:" + result.getBno());

        });
    }

    @Test
    public void testSelect() {
        Long bno = 88L;
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();

        log.info(board);
    }

    @Test
    public void testUpdate(){
        Long bno = 19L;
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();

        board.change("update...19","update...19");
        boardRepository.save(board);

    }

    @Test
    public void testDelete(){
        Long bno =19L;
        boardRepository.deleteById(bno);
    }



    @Test
    public void testPaging(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.findAll(pageable);
        log.info(result.getTotalElements());
        log.info(result.getTotalPages());
        log.info(result.getNumber());
        log.info(result.getSize());
        List<Board> curList = result.getContent();
        curList.forEach(board->log.info(board));

    }

    @Test
    public void testSearch1(){
        Pageable pageable = PageRequest.of(1,10,Sort.by("bno").descending());
        boardRepository.search1(pageable);
    }

    @Test
    public void testSearchAll(){
        String[] types = {"t","c","w"};
        String keyword = "1";
        Pageable pageable = PageRequest.of(0,12,Sort.by("bno").descending());
        Page<Board> result = boardRepository.searchAll(types,keyword,pageable);
        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info(result.hasPrevious()+": "+result.hasNext());
        result.getContent().forEach(board->log.info(board));


    }

    @Test
    public void testInsertWithImages(){
        Board board = Board.builder()
                .title("Image TEST")
                .local("이미지 지역")
                .content("image content")
                .writer("tester")

                .build();

        for(int i=0;i<3;i++){
            board.addImage(UUID.randomUUID().toString(), "File"+i+".jpg");
        }

        boardRepository.save(board);
    }


    @Transactional
    @Commit
    @Test
    public void testModifyImages(){
        Optional<Board> result = boardRepository.findByIdWithImages(106L);
        Board board = result.orElseThrow();
        board.clearImage();

        for(int i=0;i<2;i++){
            board.addImage(UUID.randomUUID().toString(), "updatefile"+i+".jpg");
        }
        boardRepository.save(board);
    }

}


