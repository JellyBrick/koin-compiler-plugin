package org.koin.sample.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.koin.sample.database.entity.NewsResourceEntity

@Dao
interface NewsResourceDao {
    @Query("SELECT * FROM news_resources")
    fun getAll(): Flow<List<NewsResourceEntity>>

    @Query("SELECT * FROM news_resources WHERE id = :id")
    fun getById(id: String): Flow<NewsResourceEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(resources: List<NewsResourceEntity>)

    @Query("DELETE FROM news_resources")
    suspend fun deleteAll()
}
