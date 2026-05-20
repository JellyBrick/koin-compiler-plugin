package org.koin.sample.data.repository

import kotlinx.coroutines.flow.Flow
import org.koin.sample.model.UserNewsResource

interface UserNewsResourceRepository {
    fun observeAll(): Flow<List<UserNewsResource>>
}
