package com.majid2851.blog_kmm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.majid2851.blog_kmm.navigation.SetupNavGraph
import com.majid2851.blog_kmm.ui.theme.Blog_kmmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Blog_kmmTheme {
                val navController= rememberNavController()
                SetupNavGraph(navController = navController)

            }
        }
    }
}
