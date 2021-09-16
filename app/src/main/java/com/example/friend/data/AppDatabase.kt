package com.example.friend.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.friend.model.Friend
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//создаем таблицу по Dao
@Database(entities = [Friend::class], version = 1,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun friendDao(): FriendDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                instance
            }
        }

        suspend fun populateDatabase(friendDao: FriendDao) { }

        private class AppDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.friendDao())
                    }
                }
            }
        }
    }
}
