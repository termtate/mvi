package com.example.myapplicationg.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myapplicationg.data.network.bean.PublishItem
import com.example.myapplicationg.data.repository.PublishRepository
import retrofit2.HttpException
import java.io.IOException


class ProfilePagingSource(
    private val type: String,
    private val userId: Int,
    private val publishRepository: PublishRepository,
) : PagingSource<Int, PublishItem>() {
    override fun getRefreshKey(state: PagingState<Int, PublishItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PublishItem> {
        return try {
            val nextPageNumber = params.key ?: 1
            val items = publishRepository.getUserItems(userId, type, nextPageNumber)
            LoadResult.Page(
                data = items.items,
                prevKey = null,
                nextKey = if (items.items.isNotEmpty()) nextPageNumber + 1 else null
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

}