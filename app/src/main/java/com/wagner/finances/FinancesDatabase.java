package com.wagner.finances;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

/**
 * Created by Santiago on 4/22/2019.
 */

@Database(entities = {Item.class, HistoricalItem.class}, version = 1)
@TypeConverters({Converter.class})
public abstract class FinancesDatabase extends RoomDatabase {
    public abstract ItemDAO itemDao();
    public abstract HistoricalItemDAO historicalItemDao();
}

