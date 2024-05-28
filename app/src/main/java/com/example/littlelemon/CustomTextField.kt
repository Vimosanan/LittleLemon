package com.example.littlelemon

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    text: String,
    onValueChanged: (String) -> Unit,
    label: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = text,
            onValueChange = onValueChanged,
            keyboardOptions = keyboardOptions,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(8.dp)
                ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            placeholder = {
                Text(
                    text = stringResource(
                        id = R.string.text_field_placeholder,
                        label.lowercase(Locale.ROOT)
                    )
                )
            },
            textStyle = LocalTextStyle.current.copy(color = Color.Black)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomTextFieldPreview() {
    var email: String
    CustomTextField(text = "", {text  -> email = text } , label= "Email")
}