package com.example.myapplicationg.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplicationg.data.db.entity.RemoteKey

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: RemoteKey)

    @Query("SELECT * FROM remote_keys WHERE belongs == :belongs")
    suspend fun remoteKey(belongs: Int): RemoteKey

    @Query("DELETE FROM remote_keys")
    suspend fun delete()
}