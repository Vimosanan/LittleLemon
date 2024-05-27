package com.example.littlelemon

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun Onboarding() {
    return Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()

    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
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
            Box(

                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFF485E57)) // Change to your desired background color
                    .padding(8.dp), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Let's get to know you",
                    color = Color(0xFFFFFFFF),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.W400
                )
            }
            Text(
                text = "Personal information",
                color = Color(0xFF000000),
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 32.dp)
            )
            CustomTextField("First name")
            CustomTextField("Last name")
            CustomTextField("Email")
        }
        Button(
            onClick = {}, shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF4CE15),
                contentColor = Color.White
            ), modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .fillMaxWidth()
                .height(42.dp)
        ) {
            Text(
                text = "Register", color = Color(0xFF000000)
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun OnboardingPreview() {
    Onboarding()
}