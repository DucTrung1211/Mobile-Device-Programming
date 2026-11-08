package com.uth.week6.permissions.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import com.uth.week6.permissions.model.PermissionStep
import com.uth.week6.permissions.viewmodel.PermissionViewModel

private val Orange = Color(0xFFF58229)
private val Ink = Color(0xFF182035)

@Composable
fun PermissionsApp(vm: PermissionViewModel) {
    val state by vm.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val imageLoader = remember(context) { ImageLoader.Builder(context).components { add(SvgDecoder.Factory()) }.build() }
    var dialog by rememberSaveable { mutableStateOf<String?>(null) }
    val snackbar = remember { SnackbarHostState() }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { vm.result() }
    val settingsLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        vm.refresh()
    }
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event -> if (event == Lifecycle.Event.ON_RESUME) vm.refresh() }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }
    LaunchedEffect(state.message) {
        state.message?.let {
            snackbar.showSnackbar(it)
            vm.messageShown()
        }
    }
    val openSettings = {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.packageName))
        settingsLauncher.launch(intent)
    }
    val launchRequest = {
        if (!state.requesting && state.step < 3) {
            val step = PermissionStep.entries[state.step]
            vm.requesting(state.step)
            launcher.launch(step.permissions())
        }
    }
    val request = {
        if (state.step < 3 && !state.requesting) {
            val step = PermissionStep.entries[state.step]
            val permissions = step.permissions()
            when {
                vm.alreadyGranted(step) -> vm.next()
                permissions.isEmpty() -> dialog = "settings"
                permissions.any { ActivityCompat.shouldShowRequestPermissionRationale(activity, it) } -> dialog = "rationale"
                vm.asked(step) -> dialog = "settings"
                else -> launchRequest()
            }
        }
    }
    BackHandler(state.step in 1..2) { vm.back() }
    MaterialTheme(colorScheme = lightColorScheme(primary = Orange, onPrimary = Color.White)) {
        Box(Modifier.fillMaxSize().background(Color.White)) {
            if (state.step < 3) {
                PermissionBackground(imageLoader)
                Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.56f)))
                Box(Modifier.fillMaxSize().safeDrawingPadding().padding(horizontal = 29.dp), contentAlignment = Alignment.Center) {
                    val step = PermissionStep.entries[state.step]
                    Column(
                        Modifier.widthIn(max = 380.dp).fillMaxWidth().clip(RoundedCornerShape(24.dp))
                            .background(Color.White).verticalScroll(rememberScrollState()).padding(horizontal = 32.dp, vertical = 30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(Modifier.size(154.dp, 120.dp), contentAlignment = Alignment.Center) {
                            AsyncImage("file:///android_asset/blob.svg", null, modifier = Modifier.size(148.dp, 112.dp), imageLoader = imageLoader)
                            AsyncImage(
                                "file:///android_asset/" + step.asset + ".svg", null,
                                modifier = Modifier.size(if (step == PermissionStep.LOCATION) 87.dp else if (step == PermissionStep.NOTIFICATION) 77.dp else 48.dp)
                                    .rotate(if (step == PermissionStep.NOTIFICATION) -10f else 0f),
                                imageLoader = imageLoader
                            )
                        }
                        Spacer(Modifier.height(28.dp))
                        Text(step.title, color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
                        Spacer(Modifier.height(22.dp))
                        Text(step.description, color = Color(0xFF606268), fontSize = 16.sp, lineHeight = 21.sp, textAlign = TextAlign.Center)
                        Spacer(Modifier.height(44.dp))
                        GradientButton(
                            if (vm.alreadyGranted(step)) "Continue" else if (step == PermissionStep.LOCATION) "Allow" else "Turn on",
                            enabled = !state.requesting,
                            onClick = request
                        )
                        Spacer(Modifier.height(15.dp))
                        OutlinedButton(onClick = vm::next, enabled = !state.requesting, modifier = Modifier.fillMaxWidth().height(44.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Orange), shape = RoundedCornerShape(50)) {
                            Text("Skip for now", color = Orange, fontSize = 16.sp)
                        }
                    }
                }
            } else {
                Column(Modifier.fillMaxSize().safeDrawingPadding().verticalScroll(rememberScrollState()).padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    Text("Permission status", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Ink)
                    Text("Android " + Build.VERSION.RELEASE + " · API " + Build.VERSION.SDK_INT, color = Color.Gray)
                    PermissionStep.entries.forEachIndexed { index, step ->
                        Card(Modifier.fillMaxWidth()) {
                            Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text(step.title, fontWeight = FontWeight.SemiBold)
                                    Text(state.labels[index], fontSize = 14.sp, color = Color(0xFF606268))
                                }
                                TextButton(onClick = { vm.review(index) }) { Text("Manage") }
                            }
                        }
                    }
                    GradientButton("Send test notification", state.granted[1], vm::testNotification)
                    OutlinedButton(onClick = openSettings, modifier = Modifier.fillMaxWidth()) { Text("Open Android Settings") }
                    Text("This exercise requests permissions and sends a test notification. Map and QR scanner features are not included.",
                        fontSize = 14.sp, color = Color(0xFF606268))
                }
            }
            SnackbarHost(snackbar, Modifier.align(Alignment.BottomCenter).navigationBarsPadding())
        }
        if (dialog != null) AlertDialog(
            onDismissRequest = { dialog = null },
            title = { Text(if (dialog == "settings") "Enable in Settings" else "Why this permission?") },
            text = { Text(if (dialog == "settings") "Android is not showing another request. You can change this permission in the app's Settings, or continue without it."
                else "This step demonstrates the Android permission request. Denying access does not prevent using the rest of this app.") },
            confirmButton = {
                TextButton(onClick = {
                    val mode = dialog
                    dialog = null
                    if (mode == "settings") openSettings() else launchRequest()
                }) { Text(if (dialog == "settings") "Open Settings" else "Continue") }
            },
            dismissButton = { TextButton(onClick = { dialog = null }) { Text("Not now") } }
        )
    }
}

@Composable
private fun PermissionBackground(imageLoader: ImageLoader) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        AsyncImage("file:///android_asset/background.png", null, modifier = Modifier.fillMaxWidth().height(maxWidth), imageLoader = imageLoader, contentScale = ContentScale.Crop)
        Box(Modifier.fillMaxWidth().height(maxWidth).background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent, Color.White))))
        Column(Modifier.align(Alignment.BottomCenter).fillMaxWidth().navigationBarsPadding().padding(30.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Safe for your family", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Text("Completely safe and cool to the touch", color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(64.dp))
            Box(Modifier.fillMaxWidth().height(48.dp).background(Brush.verticalGradient(listOf(Color(0xFFF54A48), Orange)), RoundedCornerShape(5.dp)), contentAlignment = Alignment.Center) { Text("Login", color = Color.White) }
            Box(Modifier.fillMaxWidth().height(48.dp).border(1.dp, Orange, RoundedCornerShape(5.dp)), contentAlignment = Alignment.Center) { Text("Continue without account", color = Orange) }
        }
    }
}

@Composable
private fun GradientButton(label: String, enabled: Boolean, onClick: () -> Unit) {
    Box(Modifier.fillMaxWidth().height(44.dp).clip(RoundedCornerShape(50))
        .background(Brush.verticalGradient(listOf(Color(0xFFF54A48), Orange)))
        .clickable(enabled = enabled, onClick = onClick), contentAlignment = Alignment.Center) {
        Text(label, color = Color.White.copy(alpha = if (enabled) 1f else 0.5f), fontSize = 16.sp)
    }
}
