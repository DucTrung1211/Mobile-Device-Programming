package com.uth.week6.crypto.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import com.uth.week6.crypto.model.*
import com.uth.week6.crypto.viewmodel.CryptoViewModel
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val Background = Color(0xFF1D212D)
private val CardColor = Color(0xFF33373D)
private val Muted = Color(0xFF9197A9)
private val Blue = Color(0xFF4E7BE4)

fun priceLabel(quote: Quote?) = quote?.let { NumberFormat.getCurrencyInstance(Locale.US).format(it.price) } ?: "—"
fun changeLabel(quote: Quote?) = quote?.let { String.format(Locale.US, "%+.2f%%", it.changePercent) } ?: "—"
fun receivedLabel(time: Long) = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM").withZone(ZoneId.systemDefault()).format(Instant.ofEpochMilli(time))

@Composable
fun CryptoApp(vm: CryptoViewModel) {
    val state by vm.state.collectAsStateWithLifecycle()
    val owner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val imageLoader = remember(context) { ImageLoader.Builder(context).components { add(SvgDecoder.Factory()) }.build() }
    LaunchedEffect(owner) {
        owner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) { vm.pollWhileStarted() }
    }
    BackHandler(state.selected != null) { vm.close() }
    MaterialTheme(colorScheme = darkColorScheme(
        primary = Blue, background = Background, surface = CardColor, onSurface = Color.White
    )) {
        Surface(Modifier.fillMaxSize(), color = Background, contentColor = Color.White) {
        LazyColumn(
            Modifier.fillMaxSize().background(Background).safeDrawingPadding(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Box(Modifier.fillMaxWidth().height(40.dp), contentAlignment = Alignment.Center) {
                    Text("Home", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
                }
            }
            item { SectionTitle("New") }
            item {
                AsyncImage(
                    "file:///android_asset/banner.png", "Breaking news illustration",
                    modifier = Modifier.fillMaxWidth().aspectRatio(376f / 170).clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            item { SectionTitle("Favorites") }
            item {
                val favorites = Coin.entries.filter { it.name in state.favorites }
                if (favorites.isEmpty()) {
                    Text("Chưa có yêu thích. Nhấn ☆ bên cạnh coin để thêm.", color = Muted, fontSize = 13.sp)
                } else {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(favorites, key = { it.name }) { coin ->
                            FavoriteCard(coin, state, imageLoader, { vm.open(coin) }, { vm.favorite(coin) })
                        }
                    }
                }
            }
            item { SectionTitle("All Fluctuations") }
            if (state.loading) item {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CircularProgressIndicator(Modifier.size(22.dp), strokeWidth = 2.dp)
                    Text("Đang lấy giá từ 4 nguồn…", color = Muted, fontSize = 13.sp)
                }
            }
            items(Coin.entries, key = { it.name }) { coin ->
                CoinRow(coin, state, imageLoader, { vm.open(coin) }, { vm.favorite(coin) })
            }
            item {
                Text("Biểu đồ: các mẫu giá nhận trong phiên, cùng nguồn; tối đa 60 mẫu.", color = Muted, fontSize = 12.sp)
                Text(if (state.polling) "Đang cập nhật 4 nguồn…" else "Nghỉ 3 giây sau mỗi lượt · USD", color = Muted, fontSize = 12.sp)
            }
            items(state.sources, key = { it.name }) { source ->
                Column(Modifier.fillMaxWidth().background(CardColor, RoundedCornerShape(10.dp)).padding(12.dp)) {
                    Text(source.name + " · " + (source.error ?: "Đã cập nhật"), fontSize = 13.sp,
                        color = if (source.error == null) Color.White else Color(0xFFFFB4A9))
                    if (source.retryAt > state.now) {
                        Text("Chờ ít nhất " + ((source.retryAt - state.now + 999) / 1000) + " giây trước khi thử lại.", color = Muted, fontSize = 12.sp)
                    }
                }
            }
        }
        state.selected?.let { coin ->
            CoinDetail(coin, state, imageLoader, vm::close) { vm.favorite(coin) }
        }
        }
    }
}

@Composable
private fun SectionTitle(label: String) {
    Text(label, fontSize = 16.sp, fontWeight = FontWeight.Medium, letterSpacing = 1.sp)
}

@Composable
private fun CoinIcon(coin: Coin, loader: ImageLoader, modifier: Modifier = Modifier) {
    Box(modifier.size(44.dp).background(Color(0xFF2A2F37), RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
        AsyncImage(
            "file:///android_asset/" + coin.name.lowercase() + ".svg", null,
            imageLoader = loader, modifier = Modifier.size(if (coin == Coin.ETH || coin == Coin.XMR) 44.dp else 24.dp)
        )
    }
}

@Composable
private fun FavoriteCard(coin: Coin, state: MarketState, loader: ImageLoader, onOpen: () -> Unit, onFavorite: () -> Unit) {
    val selected = state.sourceQuote(coin)
    val quote = selected?.first
    Column(Modifier.width(182.dp).clip(RoundedCornerShape(10.dp)).background(CardColor).clickable(onClick = onOpen).padding(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CoinIcon(coin, loader, Modifier.size(38.dp))
            Text(coin.name, Modifier.weight(1f).padding(start = 10.dp), fontSize = 14.sp, letterSpacing = 1.sp)
            Text("★", Modifier.clickable(onClick = onFavorite).padding(4.dp), color = Muted)
        }
        Spacer(Modifier.height(12.dp))
        Text(priceLabel(quote), fontSize = 18.sp, maxLines = 1)
        Text(changeLabel(quote), fontSize = 12.sp, color = changeColor(quote))
        Spacer(Modifier.height(8.dp))
        PriceChart(quote?.let(state::samples).orEmpty(), Modifier.fillMaxWidth().height(52.dp))
        if (selected?.second == true) Text("Giá cũ", color = Color(0xFFFFB4A9), fontSize = 11.sp)
    }
}

@Composable
private fun CoinRow(coin: Coin, state: MarketState, loader: ImageLoader, onOpen: () -> Unit, onFavorite: () -> Unit) {
    val selected = state.sourceQuote(coin)
    val quote = selected?.first
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(CardColor).clickable(onClick = onOpen).padding(14.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CoinIcon(coin, loader)
        Column(Modifier.weight(1f)) {
            Text(coin.name, fontSize = 14.sp, letterSpacing = 1.sp)
            Text(coin.title, fontSize = 13.sp, color = Muted)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(priceLabel(quote), fontSize = 18.sp)
            Text(changeLabel(quote), fontSize = 14.sp, color = changeColor(quote))
            Text(quote?.period?.label ?: "Chưa có dữ liệu", fontSize = 10.sp, color = Muted)
            if (selected?.second == true) Text("Giá cũ · nguồn lỗi", fontSize = 11.sp, color = Color(0xFFFFB4A9))
        }
        TextButton(onClick = onFavorite, modifier = Modifier.width(32.dp), contentPadding = PaddingValues(0.dp)) {
            Text(if (coin.name in state.favorites) "★" else "☆", color = Muted, fontSize = 20.sp)
        }
    }
}

private fun changeColor(quote: Quote?) = when {
    quote == null -> Muted
    quote.changePercent >= 0 -> Color(0xFF2DAA20)
    else -> Color(0xFFF81506)
}

@Composable
fun PriceChart(samples: List<Sample>, modifier: Modifier = Modifier) {
    if (samples.size < 2) {
        Box(modifier, contentAlignment = Alignment.Center) { Text("Đang tích lũy mẫu giá", fontSize = 10.sp, color = Muted) }
        return
    }
    Canvas(modifier) {
        val prices = samples.map { it.price.toDouble() }
        val low = prices.min()
        val high = prices.max()
        val range = (high - low).takeIf { it > 0 } ?: 1.0
        val line = Path()
        prices.forEachIndexed { index, value ->
            val x = size.width * index / prices.lastIndex
            val y = size.height * (0.1f + 0.8f * (1f - ((value - low) / range).toFloat()))
            if (index == 0) line.moveTo(x, y) else line.lineTo(x, y)
        }
        val area = Path().apply {
            addPath(line)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        drawPath(area, Brush.verticalGradient(listOf(Blue.copy(alpha = 0.32f), Color.Transparent)))
        drawPath(line, Blue, style = Stroke(1.4.dp.toPx()))
    }
}

@Composable
private fun CoinDetail(coin: Coin, state: MarketState, loader: ImageLoader, onClose: () -> Unit, onFavorite: () -> Unit) {
    Dialog(onDismissRequest = onClose) {
        Column(Modifier.fillMaxWidth().heightIn(max = 640.dp).clip(RoundedCornerShape(20.dp)).background(Background)
            .verticalScroll(rememberScrollState()).padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CoinIcon(coin, loader)
                Text(coin.title + " · " + coin.name, Modifier.weight(1f), fontWeight = FontWeight.Bold)
            }
            val selected = state.sourceQuote(coin)
            selected?.first?.let { quote ->
                Text(priceLabel(quote), fontSize = 28.sp, fontWeight = FontWeight.Bold)
                PriceChart(state.samples(quote), Modifier.fillMaxWidth().height(100.dp))
            }
            state.sources.forEach { source ->
                source.quotes.firstOrNull { it.coin == coin }?.let { quote ->
                    Text(source.name + " · " + priceLabel(quote), fontWeight = FontWeight.SemiBold)
                    Text(changeLabel(quote) + " · " + quote.period.label, fontSize = 13.sp, color = changeColor(quote))
                    Text("Nhận lúc " + receivedLabel(quote.receivedAt), color = Muted, fontSize = 12.sp)
                    source.error?.let { Text("Giá cũ · " + it, color = Color(0xFFFFB4A9), fontSize = 12.sp) }
                    HorizontalDivider(color = CardColor)
                }
            }
            if (selected == null) Text("Chưa có giá hợp lệ. Các nguồn sẽ được thử lại khi hết thời gian chờ.")
            Text("Biểu đồ dùng mẫu giá nhận trong phiên, tách theo từng nguồn. Thời gian hiển thị là lúc app nhận phản hồi.",
                color = Muted, fontSize = 12.sp)
            OutlinedButton(onClick = onFavorite, modifier = Modifier.fillMaxWidth()) {
                Text(if (coin.name in state.favorites) "Bỏ yêu thích" else "Thêm yêu thích")
            }
            TextButton(onClick = onClose, modifier = Modifier.fillMaxWidth()) { Text("Đóng") }
        }
    }
}
