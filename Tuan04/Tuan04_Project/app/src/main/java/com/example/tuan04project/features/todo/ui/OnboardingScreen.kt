package com.example.tuan04project.features.todo.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuan04project.features.todo.model.OnboardingPage
import com.example.tuan04project.features.todo.model.TodoAppInfo
import com.example.tuan04project.features.todo.viewmodel.TodoViewModel
import com.example.tuan04project.ui.theme.TextDark
import com.example.tuan04project.ui.theme.TextGray
import com.example.tuan04project.ui.theme.TodoBlue

@Composable
fun OnboardingRoute(onFinish: () -> Unit) {
    val todoViewModel: TodoViewModel = viewModel()

    BackHandler(enabled = todoViewModel.currentPageIndex > 0) {
        todoViewModel.goToPreviousPage()
    }

    OnboardingScreen(
        appInfo = todoViewModel.appInfo,
        page = todoViewModel.currentPage,
        currentPageIndex = todoViewModel.currentPageIndex,
        pageCount = todoViewModel.pages.size,
        onSkip = onFinish,
        onMainButtonClick = {
            if (todoViewModel.isLastPage) {
                onFinish()
            } else {
                todoViewModel.goToNextPage()
            }
        }
    )
}

@Composable
fun OnboardingScreen(
    appInfo: TodoAppInfo,
    page: OnboardingPage,
    currentPageIndex: Int,
    pageCount: Int,
    onSkip: () -> Unit,
    onMainButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        TodoHeader(appInfo = appInfo, onSkip = onSkip)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            OnboardingIllustrationCard(
                illustration = page.illustration,
                modifier = Modifier.fillMaxWidth(0.56f)
            )
        }
        Column(modifier = Modifier.heightIn(min = 112.dp)) {
            Text(
                text = page.title,
                color = TextDark,
                fontSize = 21.sp,
                lineHeight = 27.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = page.description,
                color = TextGray,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )
        }
        Spacer(Modifier.height(28.dp))
        PageIndicator(pageCount = pageCount, currentPageIndex = currentPageIndex)
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = onMainButtonClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TodoBlue)
        ) {
            Text(text = page.buttonText, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(8.dp))
    }
}
