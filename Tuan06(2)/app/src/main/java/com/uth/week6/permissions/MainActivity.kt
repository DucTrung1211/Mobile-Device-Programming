package com.uth.week6.permissions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import com.uth.week6.permissions.ui.PermissionsApp

class MainActivity : ComponentActivity(), ImageLoaderFactory {
    override fun newImageLoader() = ImageLoader.Builder(this).components { add(SvgDecoder.Factory()) }.build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { PermissionsApp(viewModel()) }
    }
}
