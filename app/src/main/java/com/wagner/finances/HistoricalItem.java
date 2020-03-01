package com.wagner.finances;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "historical_data" )
public class HistoricalItem {

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "date")
    Date date;

    @ColumnInfo(name = "amount")
    double amount;

    public HistoricalItem(Date date, double amount) {
        this.date = date;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
