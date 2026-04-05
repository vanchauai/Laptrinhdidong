package com.example.midterm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.midterm.ui.theme.MidTermTheme
import com.example.midterm.viewmodel.ProductViewModel

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<ProductViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MidTermTheme {
                AppNavigation(viewModel)
            }
        }
    }
}