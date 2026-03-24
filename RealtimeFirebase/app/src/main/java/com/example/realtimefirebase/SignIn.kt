package com.example.realtimefirebase

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignIn(navController: NavController) {
    val context = LocalContext.current
    val firebaseAuth = FirebaseAuth.getInstance()

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            placeholder = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            placeholder = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (email.value.isNotEmpty() && password.value.isNotEmpty()) {
                    firebaseAuth.signInWithEmailAndPassword(
                        email.value,
                        password.value
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Đăng nhập thành công",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.navigate("home")
                        } else {
                            Toast.makeText(
                                context,
                                it.exception?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Không được để trống",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign In")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                navController.navigate("signup")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Sign Up")
        }
    }
}