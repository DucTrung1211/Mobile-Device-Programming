package com.example.tuan05project.features.todo.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan05project.R
import com.example.tuan05project.features.todo.model.TodoTab
import com.example.tuan05project.features.todo.ui.common.TodoSnackbarHost
import com.example.tuan05project.features.todo.ui.theme.TodoTheme


@Composable
fun TodoBottomBar(selectedTab: TodoTab, onTabSelected: (TodoTab) -> Unit, onAddTask: () -> Unit) {
    val colors = TodoTheme.colors
    Column(modifier = Modifier.background(colors.card)) {
        HorizontalDivider(color = colors.navBorder)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(start = 8.dp, end = 8.dp, top = 6.dp, bottom = 8.dp),
            verticalAlignment = Alignment.Top
        ) {
            BottomBarItem(TodoTab.HOME, selectedTab, onTabSelected)
            BottomBarItem(TodoTab.LIST, selectedTab, onTabSelected)
            AddTaskButton(onClick = onAddTask)
            BottomBarItem(TodoTab.NOTIFICATIONS, selectedTab, onTabSelected)
            BottomBarItem(TodoTab.SETTINGS, selectedTab, onTabSelected)
        }
    }
}

@Composable
private fun AddTaskButton(onClick: () -> Unit) {
    val colors = TodoTheme.colors
    Box(
        modifier = Modifier
            .width(56.dp)
            .height(50.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .offset(y = (-20).dp)
                .requiredSize(56.dp)
                .dropShadow(
                    shape = CircleShape,
                    shadow = Shadow(
                        radius = 10.dp,
                        color = colors.accent,
                        offset = DpOffset(x = 0.dp, y = 8.dp),
                        alpha = 0.35f
                    )
                )
                .clip(CircleShape)
                .background(colors.accent)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_todo_plus),
                contentDescription = "Thêm việc mới",
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Composable
private fun RowScope.BottomBarItem(
    tab: TodoTab,
    selectedTab: TodoTab,
    onTabSelected: (TodoTab) -> Unit
) {
    val colors = TodoTheme.colors
    val isSelected = tab == selectedTab
    val itemColor = if (isSelected) colors.accentText else colors.textSecondary
    Column(
        modifier = Modifier
            .weight(1f)
            .height(50.dp)
            .clip(CircleShape)
            .clickable { onTabSelected(tab) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(tabIcon(tab)),
            contentDescription = null,
            tint = itemColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.height(3.dp))
        Text(
            text = tab.label,
            color = itemColor,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
        )
    }
}

private fun tabIcon(tab: TodoTab): Int {
    return when (tab) {
        TodoTab.HOME -> R.drawable.ic_todo_home
        TodoTab.LIST -> R.drawable.ic_todo_list
        TodoTab.NOTIFICATIONS -> R.drawable.ic_bell
        TodoTab.SETTINGS -> R.drawable.ic_todo_settings
    }
}
