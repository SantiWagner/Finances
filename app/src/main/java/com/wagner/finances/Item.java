package com.wagner.finances;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "items")

public class Item implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    String name;

    @ColumnInfo(name = "amount")
    double amount;

    @ColumnInfo(name = "last_updated")
    long last_updated;

    @ColumnInfo(name = "currency")
    String currency;

    public Item( String name, double amount, long last_updated, String currency) {
        this.name = name;
        this.amount = amount;
        this.last_updated = last_updated;
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(long last_updated) {
        this.last_updated = last_updated;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
