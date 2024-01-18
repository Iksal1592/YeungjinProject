package org.zerock.b01.service.reply;

import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.Reply.TradeBoardReplyDTO;

public interface TradeBoardReplyService {

    Long register(TradeBoardReplyDTO tradeBoardReplyDTO);

    TradeBoardReplyDTO read(Long rno);

    void modify(TradeBoardReplyDTO tradeBoardReplyDTO);

    void remove(Long rno);


    PageResponseDTO<TradeBoardReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);
}
