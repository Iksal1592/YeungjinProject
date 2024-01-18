package org.zerock.b01.repository.reply;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b01.domain.TradeBoardReply;


public interface TradeBoardReplyRepository extends JpaRepository<TradeBoardReply, Long> {

    @Query("select t from TradeBoardReply t where t.trade.bno = :bno")
    Page<TradeBoardReply> listOfTradeBoard(Long bno, Pageable pageable);
}
