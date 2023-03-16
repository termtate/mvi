package com.example.myapplicationg.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey val nextKey: Int,
    val belongs: Int,
)