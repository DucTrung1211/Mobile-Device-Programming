package com.example.tuan05project.features.todo.ui.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan05project.R
import com.example.tuan05project.features.todo.ui.common.TodoInfoDialog
import com.example.tuan05project.features.todo.ui.theme.TodoTheme
import androidx.compose.ui.graphics.Color

private val TodoBlue = Color(0xFF2F5BEA)

@Composable
fun LoginScreen(loading: Boolean, onBack: () -> Unit, onGoogleLogin: () -> Unit, onGuestLogin: () -> Unit) {
    val colors = TodoTheme.colors
    var policyDialogTitle by remember { mutableStateOf<String?>(null) }

    BoxWithConstraints(Modifier.fillMaxSize().background(colors.card).safeDrawingPadding()) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .heightIn(min = maxHeight)
            .padding(start = 28.dp, end = 28.dp, top = 12.dp, bottom = 32.dp)
    ) {
        Box(
            modifier = Modifier
                .offset(x = (-10).dp)
                .size(44.dp)
                .clip(CircleShape)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_todo_back),
                contentDescription = "Quay lại",
                tint = colors.textPrimary,
                modifier = Modifier.size(24.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f).padding(vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp, Alignment.CenterVertically)
        ) {
            LoginLogo()
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Chào mừng đến với\nUTH Todo",
                    color = colors.textPrimary,
                    fontSize = 30.sp,
                    lineHeight = 38.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.4).sp
                )
                Text(
                    text = "Đăng nhập để lưu công việc và đồng bộ trên mọi thiết bị của bạn.",
                    color = colors.textLabel,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                FeatureRow(iconRes = R.drawable.ic_todo_refresh, text = "Tự động sao lưu, không lo mất dữ liệu")
                FeatureRow(iconRes = R.drawable.ic_todo_phone, text = "Dùng trên điện thoại và máy tính bảng")
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            GoogleButton(loading = loading, onClick = onGoogleLogin)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = colors.navBorder)
                Text(text = "hoặc", color = colors.textSecondary, fontSize = 13.sp)
                HorizontalDivider(modifier = Modifier.weight(1f), color = colors.navBorder)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(enabled = !loading, onClick = onGuestLogin),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Dùng thử không cần tài khoản",
                    color = colors.accentText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            PolicyText(onOpenPolicy = { title -> policyDialogTitle = title })
        }
    }

    }
    val dialogTitle = policyDialogTitle
    if (dialogTitle != null) {
        TodoInfoDialog(
            title = dialogTitle,
            text = "Đây là ứng dụng bài tập. Bài 01 dùng Google và Firebase Authentication. Bài 02 lưu công việc, ảnh và token thông báo theo UID lên Firebase; Analytics không nhận email, nội dung việc hoặc token. Phiên khách không đồng bộ.",
            onDismiss = { policyDialogTitle = null }
        )
    }
}

@Composable
private fun LoginLogo() {
    val logoShape = RoundedCornerShape(26.dp)
    Image(
        painter = painterResource(R.drawable.ic_todo_logo_small),
        contentDescription = "Logo UTH Todo",
        modifier = Modifier
            .size(88.dp)
            .dropShadow(
                shape = logoShape,
                shadow = Shadow(
                    radius = 15.dp,
                    color = TodoBlue,
                    offset = DpOffset(x = 0.dp, y = 12.dp),
                    alpha = 0.3f
                )
            )
            .background(TodoBlue, logoShape)
    )
}

@Composable
private fun FeatureRow(iconRes: Int, text: String) {
    val colors = TodoTheme.colors
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = TodoBlue,
            modifier = Modifier.size(18.dp)
        )
        Text(text = text, color = colors.textBody, fontSize = 14.sp)
    }
}

@Composable
private fun GoogleButton(loading: Boolean, onClick: () -> Unit) {
    val colors = TodoTheme.colors
    val buttonShape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(buttonShape)
            .background(colors.card)
            .border(1.dp, colors.outline, buttonShape)
            .clickable(enabled = !loading, onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .border(1.dp, colors.circleBorder, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "G", color = colors.textLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Text(text = if (loading) "Đang đăng nhập…" else "Tiếp tục với Google", color = colors.textPrimary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun PolicyText(onOpenPolicy: (String) -> Unit) {
    val colors = TodoTheme.colors
    val linkStyles = TextLinkStyles(style = SpanStyle(color = colors.textBody, textDecoration = TextDecoration.Underline))
    val policyText = buildAnnotatedString {
        append("Khi tiếp tục, bạn đồng ý với ")
        withLink(LinkAnnotation.Clickable(tag = "terms", styles = linkStyles) { onOpenPolicy("Điều khoản sử dụng") }) {
            append("Điều khoản sử dụng")
        }
        append(" và ")
        withLink(LinkAnnotation.Clickable(tag = "privacy", styles = linkStyles) { onOpenPolicy("Chính sách quyền riêng tư") }) {
            append("Chính sách quyền riêng tư")
        }
        append(" của UTH Todo.")
    }
    Text(
        text = policyText,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
        color = colors.textSecondary,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        textAlign = TextAlign.Center
    )
}
