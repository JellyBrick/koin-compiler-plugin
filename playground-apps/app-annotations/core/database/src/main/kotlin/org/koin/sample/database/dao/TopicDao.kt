package org.koin.sample.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.koin.sample.database.entity.TopicEntity

@Dao
interface TopicDao {
    @Query("SELECT * FROM topics")
    fun getAll(): Flow<List<TopicEntity>>

    @Query("SELECT * FROM topics WHERE id = :id")
    fun getById(id: String): Flow<TopicEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(topics: List<TopicEntity>)

    @Query("DELETE FROM topics")
    suspend fun deleteAll()
}
