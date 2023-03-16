package com.example.myapplicationg.usecase

import androidx.paging.cachedIn
import androidx.paging.map
import com.example.myapplicationg.data.repository.PublishRepository
import com.example.myapplicationg.data.repository.UserInfoRepository
import com.example.myapplicationg.ui.page.profile.ProfileState
import com.example.myapplicationg.usecase.PublishItemConverter
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ViewModelScoped
class ProfileUseCase @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val publishRepository: PublishRepository,
    private val publishItemConverter: PublishItemConverter
) {
    suspend fun getProfile(viewModelScope: CoroutineScope): ProfileState {
        val userInfo = userInfoRepository.getSelfInfo()
        val posts = publishRepository.getProfilePager("post", userInfo.id)
            .map { pagingData ->
                pagingData.map {
                    publishItemConverter.toPostData(it.toDbItem())
                }
            }
            .cachedIn(viewModelScope)
        val comments = publishRepository.getProfilePager("comment", userInfo.id)
            .map { pagingData ->
                pagingData.map {
                    publishItemConverter.toCommentsData(it.toDbItem())
                }
            }
            .cachedIn(viewModelScope)
        return ProfileState(
            userInfo, posts, comments
        )
    }
}