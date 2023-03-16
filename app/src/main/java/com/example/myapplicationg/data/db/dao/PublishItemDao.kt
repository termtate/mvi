package com.example.myapplicationg.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplicationg.data.db.entity.PublishItem


@Dao
interface PublishItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<PublishItem>)

    @Query(
        "SELECT * FROM publish_items " +
                "WHERE type == 'post' " +
                "ORDER BY publishTime DESC "
    )
    fun postsPagingSource(): PagingSource<Int, PublishItem>

    @Query(
        "SELECT * FROM publish_items " +
                "WHERE type == 'comment' AND belongs == :belongs " +
                "ORDER BY publishTime DESC "
    )
    fun commentsPagingSource(belongs: Int): PagingSource<Int, PublishItem>

    @Query("DELETE FROM publish_items WHERE type == :type AND belongs == :belongs")
    suspend fun clearAll(type: String, belongs: Int)

    @Query("SELECT * FROM publish_items WHERE itemId == :itemId")
    suspend fun getItem(itemId: Int): PublishItem

    @Update
    suspend fun updateItems(vararg item: PublishItem)

    @Insert
    suspend fun addItems(vararg items: PublishItem)
}