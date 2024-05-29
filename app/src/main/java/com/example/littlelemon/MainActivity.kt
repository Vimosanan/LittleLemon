package com.example.littlelemon

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.littlelemon.composable.Home
import com.example.littlelemon.composable.Onboarding
import com.example.littlelemon.composable.Profile
import com.example.littlelemon.ui.theme.LittleLemonTheme
import io.ktor.http.ContentType
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val database by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database").build()
    }

    private val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(contentType = ContentType("text", "plain"))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LittleLemonTheme {
                val sharedPreferences = getSharedPreferences("LITTLE_LEMON", Context.MODE_PRIVATE)

                val onBoarded = sharedPreferences.getBoolean(KEY_ON_BOARDED, false)

                val menuItemsRoom =
                    database.menuItemDao().getAll().observeAsState(initial = emptyList())

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = if (onBoarded) Home.route else Onboarding.route
                ) {
                    composable(Onboarding.route) {
                        Onboarding(navController)
                    }
                    composable(Home.route) {
                        Home(navController, menuItemsRoom.value)
                    }
                    composable(Profile.route) {
                        val firstName = sharedPreferences.getString(KEY_FIRST_NAME, null)
                        val lastName = sharedPreferences.getString(KEY_LAST_NAME, null)
                        val email = sharedPreferences.getString(KEY_EMAIL, null)

                        var user: User? = null
                        if (firstName != null && lastName != null && email != null) {
                            user = User(firstName, lastName, email)
                        }

                        Profile(navController, user!!)
                    }
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            if (database.menuItemDao().isEmpty()) {
                val menuItems = fetchMenu()
                val menuItemsRoom = menuItems.map {
                    it.toMenuItem()
                }.toTypedArray()
                database.menuItemDao().insertAll(menuItems = menuItemsRoom)
            }
        }
    }

    private suspend fun fetchMenu(): List<MenuItemNetwork> {
        return httpClient.get(urlString = "https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json")
            .body<MenuNetwork>().menu
    }

    companion object {
        const val KEY_ON_BOARDED: String = "ON_BOARDED"
        const val KEY_FIRST_NAME: String = "first_name"
        const val KEY_LAST_NAME: String = "last_name"
        const val KEY_EMAIL: String = "email"
    }
}
