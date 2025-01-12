package com.guardiancycle

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Database(
    entities = [EmergencyContact::class, SafeLocation::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun emergencyContactDao(): EmergencyContactDao

    class EmergencyContactDao {

    }

    abstract fun safeLocationDao(): SafeLocationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "guardian_circle_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

@Entity(tableName = "safe_locations")
data class SafeLocation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Float,
    val description: String
)

@Dao
interface SafeLocationDao {
    @Query("SELECT * FROM safe_locations")
    fun getAllLocations(): Flow<List<SafeLocation>>

    @Insert
    suspend fun insert(location: SafeLocation)
}