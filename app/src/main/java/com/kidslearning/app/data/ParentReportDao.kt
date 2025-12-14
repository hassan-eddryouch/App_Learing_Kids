package com.kidslearning.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ParentReportDao {
    @Query("SELECT * FROM parent_reports WHERE kidProfileId = :profileId ORDER BY generatedDate DESC")
    fun getReports(profileId: Int): Flow<List<ParentReport>>

    @Insert
    suspend fun insertReport(report: ParentReport)
}