package com.wagner.finances;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "historical_data" )
public class HistoricalItem {

    @ColumnInfo(name = "date")
    @PrimaryKey
    long date;

    @ColumnInfo(name = "amount")
    double amount;

    public HistoricalItem(long date, double amount) {
        this.date = date;
        this.amount = amount;
    }
}
