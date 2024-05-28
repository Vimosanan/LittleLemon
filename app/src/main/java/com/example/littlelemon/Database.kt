package com.example.littlelemon

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.serialization.Serializable

@Serializable
enum class Category(val title: String) {
    starters("starters"),
    desserts("desserts"),
    mains("mains")
}

@Entity
data class MenuItem(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val image: String,
    val category: Category
)

@Dao
interface MenuItemDao {
    @Query("SELECT * FROM MenuItem")
    fun getAll(): LiveData<List<MenuItem>>

    @Insert
    fun insertAll(vararg menuItems: MenuItem)

    @Query("SELECT (SELECT COUNT(*) FROM MenuItem) == 0")
    fun isEmpty(): Boolean
}

@Database(entities = [MenuItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun menuItemDao(): MenuItemDao
}

// list.filter { it.startsWith("a") }