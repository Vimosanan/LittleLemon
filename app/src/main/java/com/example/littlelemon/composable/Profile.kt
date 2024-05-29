package com.example.littlelemon.composable

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.example.littlelemon.Onboarding
import com.example.littlelemon.Home
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.littlelemon.CustomTextField
import com.example.littlelemon.R
import com.example.littlelemon.User

@Composable
fun Profile(navController: NavHostController? = null, user: User) {
    val firstName = remember { mutableStateOf(user.firstName) }
    val lastname = remember { mutableStateOf(user.lastName) }
    val email = remember { mutableStateOf(user.email) }

    val context = LocalContext.current
    return Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()

    ) {
        Column(
            verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(id = R.string.logo_description),
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .padding(24.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = stringResource(id = R.string.onboarding_title),
                color = Color(0xFF000000),
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 32.dp)
            )
            CustomTextField(
                firstName.value,
                { text -> firstName.value = text },
                stringResource(id = R.string.first_name),
                enabled = false
            )
            CustomTextField(
                lastname.value,
                { text -> lastname.value = text },
                stringResource(id = R.string.last_name),
                enabled = false
            )
            CustomTextField(
                email.value,
                { text -> email.value = text },
                stringResource(id = R.string.email),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                enabled = false
            )
        }
        Button(
            onClick = {
                val sharedPref = context.getSharedPreferences("LITTLE_LEMON", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    clear()
                    apply()
                }

                navController?.navigate(Onboarding.route) {
                    popUpTo(Home.route) { inclusive = false }
                }
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF4CE14), contentColor = Color.White
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .fillMaxWidth()
                .height(42.dp)

        ) {
            Text(
                text = stringResource(id = R.string.btn_logout), color = Color(0xFF000000)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    Profile(user = User("Vimo", "Ahil", "vimo@gmail.com"))
}