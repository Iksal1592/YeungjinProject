package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b01.domain.Trade;
import org.zerock.b01.repository.search.BoardSearch;

public interface TradeRepository extends JpaRepository<Trade, Long>, BoardSearch {


    @Query(value="select now()", nativeQuery = true)
    String getTime();
}
