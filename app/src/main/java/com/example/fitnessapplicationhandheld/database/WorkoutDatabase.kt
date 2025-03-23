package com.example.fitnessapplicationhandheld.database

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.fitnessapplicationhandheld.database.models.DailyHR
import com.example.fitnessapplicationhandheld.database.models.HRList
import com.example.fitnessapplicationhandheld.database.models.Location
import com.example.fitnessapplicationhandheld.database.models.Workout
import com.example.fitnessapplicationhandheld.database.models.WorkoutLabel
import com.example.fitnessapplicationhandheld.database.models.WorkoutType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Instant

@Database(entities = arrayOf(
    Workout::class,
    HRList::class,
    Location::class,
    WorkoutLabel::class,
    DailyHR::class),
    version = 1, exportSchema = false)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun workoutDao(): Dao

    companion object {
        private var INSTANCE: WorkoutDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): WorkoutDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutDatabase::class.java,
                    "workouts222"
                ).addCallback(WorkoutDatabaseCallback(scope)).build()
                INSTANCE = instance
                // return instance
                return@synchronized instance
            }
        }
    }

    private class WorkoutDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch { populateDatabase(database.workoutDao()) }
            }
        }

        suspend fun populateDatabase(workoutDao: Dao) {
            workoutDao.insert(Workout(timestamp = Instant.now().epochSecond,
                workoutType = WorkoutType.GYM, length = 9898, label = "Push", color = Color.Red.toArgb()))

            workoutDao.insert(Workout(timestamp = Instant.now().epochSecond - 10000,
                workoutType = WorkoutType.CARDIO, length = 7686, label = "Running", color = Color.Blue.toArgb()))

            workoutDao.insert(Workout(timestamp = Instant.now().epochSecond - 11111,
                workoutType = WorkoutType.CARDIO, length = 87, label = "Cycling", color = Color.Blue.toArgb(),
                distance = 1238, averageSpeed = 37.0))


            workoutDao.insert(WorkoutLabel(label = "Default Cardio Workout", color = Color.Black.toArgb()
                , workoutType = WorkoutType.CARDIO))

            workoutDao.insert(WorkoutLabel(label = "Default Gym Workout", color = Color.Black.toArgb()
                , workoutType = WorkoutType.GYM))

            workoutDao.insert(WorkoutLabel(label = "Push", color = Color.Red.toArgb()))
            workoutDao.insert(WorkoutLabel(label = "Running", color = Color.Red.toArgb(), workoutType = WorkoutType.CARDIO))


            for (i in 0 until 24){
                workoutDao.insert(DailyHR(hour = i, value = 0))
            }
        }
    }
}