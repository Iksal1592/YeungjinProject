package org.zerock.b01.repository.reply;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b01.domain.LocalBoardReply;

public interface LocalBoardReplyRepository extends JpaRepository<LocalBoardReply, Long> {

    @Query("select r from LocalBoardReply r where r.board.bno = :bno")
    Page<LocalBoardReply> listOfBoard(Long bno, Pageable pageable);
}
