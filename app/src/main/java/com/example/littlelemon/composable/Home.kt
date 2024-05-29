package com.example.littlelemon.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.littlelemon.Category
import com.example.littlelemon.MenuItem
import com.example.littlelemon.R
import com.example.littlelemon.ui.theme.LittleLemonColor

@Composable
fun Home(navController: NavHostController? = null, dishes: List<MenuItem>) {
    val searchPhrase = remember { mutableStateOf("") }
    val selectedCategory: MutableState<Category?> = remember { mutableStateOf(null) }

    val searchItems: List<MenuItem> = if (searchPhrase.value.isEmpty()) {
        dishes
    } else {
        dishes.filter {
            it.title.contains(
                searchPhrase.value, ignoreCase = true
            )
        }
    }

    val filteredItems: List<MenuItem> = if (selectedCategory.value == null) {
        searchItems
    } else {
        searchItems.filter {
            it.category == selectedCategory.value
        }
    }

    return Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(horizontal = 12.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(2f))
            Box(
                modifier = Modifier
                    .weight(5f)
                    .fillMaxHeight()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = stringResource(id = R.string.logo_description),
                    modifier = Modifier
                        .height(112.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )
            }
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = stringResource(id = R.string.profile_photo),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .weight(2f)
                    .size(64.dp)
                    .clip(CircleShape)
                    .clickable(onClick = { navController?.navigate(com.example.littlelemon.Profile.route) })

            )
        }
        Column(
            modifier = Modifier
                .background(color = LittleLemonColor.green)
                .padding(horizontal = 12.dp, vertical = 18.dp)
        ) {
            Text(
                text = stringResource(id = R.string.app_title),
                fontSize = 64.sp,
                fontWeight = FontWeight.Medium,
                color = LittleLemonColor.yellow
            )
            Text(
                text = stringResource(id = R.string.city_chicago),
                fontSize = 40.sp,
                fontWeight = FontWeight.Normal,
                color = LittleLemonColor.white
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .padding(bottom = 14.dp, top = 12.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.home_description),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light,
                    color = LittleLemonColor.white,
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .fillMaxWidth(0.6f)
                )
                Image(
                    painter = painterResource(id = R.drawable.hero_image),
                    contentDescription = "Upper Panel Image",
                    modifier = Modifier
                        .height(120.dp)
                        .width(120.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            TextField(
                value = searchPhrase.value,
                onValueChange = { text -> searchPhrase.value = text },
                placeholder = {
                    Text(text = stringResource(id = R.string.home_placeholder))
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color = Color(0xFFEAEAEA)),
            )
        }
        CategoryView(
            onTap = { category ->
                if (selectedCategory.value == null || selectedCategory.value != category) {
                    selectedCategory.value = category
                } else {
                    selectedCategory.value = null
                }
            }, currentSelectedCategory = selectedCategory.value
        )
        LazyColumn {
            itemsIndexed(filteredItems) { _, dish ->
                MenuDish(dish)
            }
        }
    }
}

@Composable
fun CategoryView(onTap: (Category) -> Unit, currentSelectedCategory: Category?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Text(
            text = stringResource(id = R.string.home_subtitle),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            items(Category.entries.size) { index ->
                val category = Category.entries[index]

                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .height(48.dp)
                ) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (currentSelectedCategory == category) LittleLemonColor.yellow
                            else LittleLemonColor.grey
                        )
                        .clickable { onTap(category) }
                        .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center) {
                        Text(
                            text = category.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = LittleLemonColor.green
                        )
                    }
                }
            }
        }
        Divider(
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun MenuDish(dish: MenuItem) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column {
                Text(
                    text = dish.title, fontSize = 24.sp, fontWeight = FontWeight.Bold
                )
                Text(
                    text = dish.description,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .padding(top = 5.dp, bottom = 8.dp)
                )
                Text(
                    text = "\$${dish.price}", fontSize = 22.sp, fontWeight = FontWeight.W400
                )
            }
            Image(
                painter = rememberAsyncImagePainter(dish.image),
                contentDescription = null,
                modifier = Modifier.size(128.dp)
            )
        }
    }
    Divider(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
        thickness = .5.dp,
        color = Color(0xFFE9E7E6)
    )
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    MenuDish(
        dish = MenuItem(
            1,
            "Greek Salad",
            "he famous greek salad of crispy lettuce, peppers, olives, our Chicago.",
            10.0,
            "https://github.com/Meta-Mobile-Developer-PC/Working-With-Data-API/blob/main/images/greekSalad.jpg?raw=true",
            Category.mains
        )
    )
}