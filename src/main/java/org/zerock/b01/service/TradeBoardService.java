package org.zerock.b01.service;

import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.Reply.TradeBoardReplyCountDTO;
import org.zerock.b01.dto.TradeDTO;

public interface TradeBoardService {


    Long register(TradeDTO tradeDTO);

    PageResponseDTO<TradeDTO> list (PageRequestDTO pageRequestDTO);

    TradeDTO readOne(Long bno);
    void modify(TradeDTO tradeDTO);
    void delete(Long bno);
    PageResponseDTO<TradeBoardReplyCountDTO> listWithTradeReplyCount(PageRequestDTO pageRequestDTO);

}

