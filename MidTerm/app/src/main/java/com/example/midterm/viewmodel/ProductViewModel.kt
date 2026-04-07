package com.example.midterm.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.midterm.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID

class ProductViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _userState = MutableStateFlow(auth.currentUser != null)
    val userState: StateFlow<Boolean> = _userState.asStateFlow()

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            _userState.value = user != null
            if (user != null) {
                db.collection("users").document(user.uid).get()
                    .addOnSuccessListener { doc ->
                        _isAdmin.value = doc.getString("role") == "admin" || user.email?.contains("admin", ignoreCase = true) == true
                    }
                    .addOnFailureListener {
                        _isAdmin.value = user.email?.contains("admin", ignoreCase = true) == true
                    }
                retrieveProductList()
            } else {
                _userState.value = false
                _isAdmin.value = false
                _products.value = emptyList()
            }
        }
    }

    fun executeSignIn(email: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                auth.signInWithEmailAndPassword(email, pass).await()
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun executeSignUp(email: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = auth.createUserWithEmailAndPassword(email, pass).await()
                result.user?.let { user ->
                    val role = if (email.contains("admin", ignoreCase = true)) "admin" else "user"
                    val userData = hashMapOf(
                        "email" to email,
                        "role" to role
                    )
                    db.collection("users").document(user.uid).set(userData).await()
                }
                _errorMessage.value = "Register success!"
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun performSignOut() {
        auth.signOut()
    }

    private fun retrieveProductList() {
        db.collection("products")
            .orderBy("name", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _errorMessage.value = error.message
                    return@addSnapshotListener
                }
                if (value != null) {
                    val list = value.documents.mapNotNull { it.toObject(Product::class.java) }
                    _products.value = list
                }
            }
    }

    fun processProductStorage(context: Context, product: Product, uri: Uri?, isUpdate: Boolean, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                var finalUrl = product.imageUrl
                if (uri != null) {
                    // Chuyển sang Base64 thay vì upload Storage
                    finalUrl = convertMediaToBase64(context, uri) ?: product.imageUrl
                }

                val docRef = if (isUpdate) {
                    db.collection("products").document(product.id)
                } else {
                    db.collection("products").document() // auto-generate ID
                }

                val newProduct = product.copy(id = docRef.id, imageUrl = finalUrl)
                docRef.set(newProduct).await()
                _errorMessage.value = if (isUpdate) "Cập nhật thành công!" else "Thêm thành công!"
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun convertMediaToBase64(context: Context, uri: Uri): String? = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            if (originalBitmap == null) return@withContext null

            // Nén ảnh để dung lượng dưới 1MB
            // Giới hạn chiều rộng/cao tối đa khoảng 800px
            val scale = 800f / Math.max(originalBitmap.width, originalBitmap.height).coerceAtLeast(1)
            val resizedBitmap = if (scale < 1) {
                Bitmap.createScaledBitmap(
                    originalBitmap,
                    (originalBitmap.width * scale).toInt(),
                    (originalBitmap.height * scale).toInt(),
                    true
                )
            } else {
                originalBitmap
            }

            val outputStream = ByteArrayOutputStream()
            // Nén chất lượng 70% định dạng JPEG
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
            val bytes = outputStream.toByteArray()
            
            // Định dạng chuỗi để Coil/Image hiển thị được ngay. Sử dụng NO_WRAP để không có ký tự xuống dòng.
            "data:image/jpeg;base64," + Base64.encodeToString(bytes, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun removeProductRecord(productId: String) {
        viewModelScope.launch {
            try {
                db.collection("products").document(productId).delete().await()
                _errorMessage.value = "Xóa thành công!"
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun resetStatusMessage() {
        _errorMessage.value = null
    }

}
