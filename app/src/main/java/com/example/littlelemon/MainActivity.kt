package com.example.littlelemon

import android.content.Context
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
                val isOnboarded = sharedPreferences.getBoolean(KEY_IS_ONBOARD, false)

                val menuItemsRoom =
                    database.menuItemDao().getAll().observeAsState(initial = emptyList())
                val orderMenuItems = remember { mutableStateOf(false) }
                val searchPhrase = remember { mutableStateOf("") }


                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = if (isOnboarded) Home.route else Onboarding.route
                ) {
                    composable(Onboarding.route) {
                        Onboarding(navController)
                    }
                    composable(Home.route) {
                        Home(navController, menuItemsRoom.value)
                    }
                    composable(Profile.route) {
                        Profile(navController)
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
        const val KEY_IS_ONBOARD: String = "is_onboarded"
    }
}
