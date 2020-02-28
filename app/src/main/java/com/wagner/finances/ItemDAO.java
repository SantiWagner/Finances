package com.wagner.finances;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;

/**
 * Created by Santiago on 4/22/2019.
 */

@Dao
public interface ItemDAO {

    @Query("SELECT * FROM items")
    List<Item> getItems();

    @Query("SELECT * FROM items WHERE currency= :c")
    List<Item> getItemsByCurrency(String c);

    @Query("SELECT SUM(amount) FROM items WHERE currency= :c")
    double getTotalByCurrency(String c);

    @Query("SELECT * FROM items WHERE id=:id")
    Item getItem(int id);

    @Insert
    void insertItem(Item item);

    @Delete
    void deleteItem(Item item);

    @Update
    void updateItem(Item item);
}