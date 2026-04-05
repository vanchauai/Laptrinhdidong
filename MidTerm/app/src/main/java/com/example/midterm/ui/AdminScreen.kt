package com.example.midterm.ui

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.midterm.model.Product
import com.example.midterm.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(viewModel: ProductViewModel, onLogout: () -> Unit) {
    val products by viewModel.products.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedProductId by remember { mutableStateOf<String?>(null) }
    var editingImageUrl by remember { mutableStateOf("") }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri = uri }

    fun clearForm() {
        name = ""
        type = ""
        price = ""
        imageUri = null
        selectedProductId = null
        editingImageUrl = ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quản lý sản phẩm") },
                actions = {
                    IconButton(onClick = {
                        viewModel.logout()
                        onLogout()
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Dữ liệu sản phẩm", style = MaterialTheme.typography.titleLarge, modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Tên sản phẩm") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = type,
                onValueChange = { type = it },
                label = { Text("Loại sản phẩm") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Giá") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                    .clickable { imagePickerLauncher.launch("image/*") }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = imageUri?.lastPathSegment ?: if(editingImageUrl.isNotEmpty()) "Có sẵn một ảnh" else "Chọn hình ảnh...",
                    color = if (imageUri == null && editingImageUrl.isEmpty()) Color.Gray else Color.Black
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val pPrice = price.toLongOrNull() ?: 0L
                    val product = Product(
                        id = selectedProductId ?: "",
                        name = name,
                        type = type,
                        price = pPrice,
                        imageUrl = editingImageUrl
                    )
                    viewModel.addOrUpdateProduct(context, product, imageUri, selectedProductId != null) {
                        clearForm()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                enabled = !isLoading
            ) {
                Text(if (selectedProductId == null) "THÊM SẢN PHẨM" else "CẬP NHẬT SẢN PHẨM")
            }

            Spacer(modifier = Modifier.height(8.dp))


            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error)
                LaunchedEffect(it) {
                    kotlinx.coroutines.delay(3000)
                    viewModel.clearError()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Danh sách sản phẩm:", style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(products) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            // Image View (Base64 hoặc URL)
                            if (item.imageUrl.startsWith("data:image")) {
                                val bitmap = remember(item.imageUrl) {
                                    try {
                                        val base64String = item.imageUrl.substringAfter(",")
                                        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
                                        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                                    } catch (e: Exception) {
                                        null
                                    }
                                }
                                if (bitmap != null) {
                                    Image(
                                        bitmap = bitmap.asImageBitmap(),
                                        contentDescription = "Product Image",
                                        modifier = Modifier
                                            .size(64.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color.LightGray),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(64.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color.LightGray)
                                    )
                                }
                            } else {
                                Image(
                                    painter = rememberAsyncImagePainter(model = item.imageUrl),
                                    contentDescription = "Product Image",
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.LightGray),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Tên sp: ${item.name}", style = MaterialTheme.typography.bodyMedium)
                                Text("Giá sp: ${item.price}", style = MaterialTheme.typography.bodyMedium)
                                Text("Loại sp: ${item.type}", style = MaterialTheme.typography.bodyMedium)
                            }
                            Column {
                                IconButton(onClick = {
                                    name = item.name
                                    type = item.type
                                    price = item.price.toString()
                                    selectedProductId = item.id
                                    editingImageUrl = item.imageUrl
                                    imageUri = null
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFFFFB300))
                                }
                                IconButton(onClick = { viewModel.deleteProduct(item.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
