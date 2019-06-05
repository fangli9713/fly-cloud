package com.fly.finance.dao;

import com.fly.finance.dto.Ashare;
import com.fly.finance.dto.AshareTransaction;
import com.fly.finance.dto.HistoryPrice;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Fangln on 2018/11/7.
 */
@Repository
public interface AshareDao {

    List<Ashare> selectAshareList();

    List<HistoryPrice> selectAshareHistoryPriceList(HistoryPrice historyPrice);

    int insertAshareHistoryPrice(HistoryPrice historyPrice);

    List<AshareTransaction> selectAshareTransactionList(AshareTransaction ashareTransaction);

    int insertAshareTransaction(AshareTransaction ashareTransaction);

    int updateAshareTransaction(AshareTransaction ashareTransaction);

}
