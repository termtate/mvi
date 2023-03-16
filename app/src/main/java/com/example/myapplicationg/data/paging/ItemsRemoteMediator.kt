package com.example.myapplicationg.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.myapplicationg.data.db.DbDataSource
import com.example.myapplicationg.data.db.entity.PublishItem
import com.example.myapplicationg.data.db.entity.RemoteKey
import com.example.myapplicationg.data.repository.ItemType
import com.example.myapplicationg.data.repository.LoginRepository
import com.example.myapplicationg.data.repository.PublishRepository
import com.example.myapplicationg.data.repository.PublishRepository.Companion.PAGE_SIZE
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ItemsRemoteMediator(
    private val query: ItemType,
    private val db: DbDataSource,
    private val publishRepository: PublishRepository,
    private val loginRepository: LoginRepository
) : RemoteMediator<Int, PublishItem>() {
    private val itemsDao = db.publishItemDao()
    private val remoteKeyDao = db.remoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PublishItem>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = db.withTransaction {
                        remoteKeyDao.remoteKey(query.belongs)
                    }

                    remoteKey.nextKey
                }
            }
            val response = when (query) {
                is ItemType.Post -> publishRepository.readPosts(loadKey ?: 1, PAGE_SIZE)
                is ItemType.Comment -> publishRepository.readComments(query.postId, loadKey ?: 1, PAGE_SIZE)
            }
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.delete()
                    itemsDao.clearAll(query.name, query.belongs)
                }

                remoteKeyDao.insertOrReplace(RemoteKey(response.page + 1, query.belongs))

                itemsDao.insertAll(response.items.map { it.toDbItem() })
            }

            MediatorResult.Success(endOfPaginationReached = response.items.isEmpty())
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            when (e.code()) {
                401, 403 -> loginRepository.setLoginState(false)
            }
            MediatorResult.Error(e)
        }
    }
}