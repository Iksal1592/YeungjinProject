package org.zerock.b01.service.reply;


import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.Reply.LocalBoardReplyDTO;


public interface LocalBoardReplyService {

    Long register(LocalBoardReplyDTO localBoardReplyDTO);

    LocalBoardReplyDTO read(Long rno);

    void modify(LocalBoardReplyDTO localBoardReplyDTO);

    void remove(Long rno);


    //댓글 목록을 페이징함
    PageResponseDTO<LocalBoardReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);
    /* 특정 'bno'에 해당하는 'LocalBoardReplyDTO'를 페이지네이션하여 반환합니다.
     * 'pageRequestDTO'에 의해 페이지 정보를 제공받아, 'PageResponseDTO'의 형태로 페이징된 'LocalBoardReplyDTO' 데이터를 반환합니다.
     * 반환된 'PageResponseDTO'는 페이지에 속한 'LocalBoardReplyDTO' 값을 포함합니다.
     */



}
