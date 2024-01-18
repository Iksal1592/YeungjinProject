package org.zerock.b01.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.Trade;
import org.zerock.b01.dto.Images.LocalBoardListAllDTO;
import org.zerock.b01.dto.Reply.LocalBoardReplyCountDTO;
import org.zerock.b01.dto.Reply.TradeBoardReplyCountDTO;

public interface BoardSearch {
    Page<Board> search1(Pageable pageable);
    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);

    Page<Trade> tradesearchAll(String[] types, String keyword, Pageable pageable);



    Page<LocalBoardReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable);


    /*Trade Board 게시판 목록 옆 댓글 갯수*/
    Page<TradeBoardReplyCountDTO> searchWithTradeReplyCount(String[] types, String keyword, Pageable pageable);


    Page<LocalBoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable);

}
