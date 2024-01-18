package org.zerock.b01.repository;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import org.zerock.b01.domain.Trade;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class TradeRepositoryTest {

    @Autowired
    private TradeRepository tradeRepository;

    @Test
    public void testTradeInsert() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Trade trade = Trade.builder()
                    .title("중고물품" + i)
                    .content("이 물건은 아주 좋습니다!!" + i)
                    .writer("user" + (i % 10))
                    .build();
            Trade result = tradeRepository.save(trade);
            log.info("BNO:" + result.getBno());

        });
    }

}


