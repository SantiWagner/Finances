package com.wagner.finances;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Santiago on 4/22/2019.
 */

@Dao
public interface HistoricalItemDAO {

    @Query("SELECT * FROM historical_data")
    List<HistoricalItem> getHistoricalData();

    @Insert
    void insertHistoricalItem(HistoricalItem item);
}
