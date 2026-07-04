package com.example.geomomentdiary.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GeoMomentDao {

    // CREATE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(moment: GeoMoment)

    // READ
    @Query("SELECT * FROM geo_moments ORDER BY id DESC")
    fun getAllMoments(): Flow<List<GeoMoment>>

    // UPDATE
    @Update
    suspend fun update(moment: GeoMoment)

    // DELETE
    @Delete
    suspend fun delete(moment: GeoMoment)

    // SEARCH
    @Query("""
        SELECT * FROM geo_moments
        WHERE title LIKE '%' || :keyword || '%'
        OR notes LIKE '%' || :keyword || '%'
    """)
    fun searchMoment(keyword: String): Flow<List<GeoMoment>>
}