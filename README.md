# Android Clean MVI Architecture with JWT Authentication


An example application with Android Jetpack Components and JWT authentication based on mvi architecture.

## Description


A simple twitter-like application which can comment and like posts.

- Android clean architecture
- single activity
- jwt token authentication
- mvi architecture with Kotlin flow
- Jetpack Compose for ui
  - use compose navigation component
  - bottom paging loading/error indicator
- offline first(using Room as local data source)
- **global errors handling** for coroutine network authentication errors(401, 403)
- use hilt for DI
- material design 3 styling

## Libraries and tools used


- Kotlin Flow
- Hilt
- Jetpack compose
- Compose navigation
- Coil
- Paging 3(both PagingSource and RemoteMediator)
- Room
- Datastore
- Retrofit