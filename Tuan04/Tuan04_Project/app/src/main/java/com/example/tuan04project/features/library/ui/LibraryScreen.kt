package com.example.tuan04project.features.library.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuan04project.R
import com.example.tuan04project.features.library.viewmodel.LibraryViewModel
import com.example.tuan04project.ui.theme.BorderGray
import com.example.tuan04project.ui.theme.LibraryInactive
import com.example.tuan04project.ui.theme.LibraryNavBlue

enum class LibraryTab(val label: String, val iconRes: Int) {
    MANAGE("Quản lý", R.drawable.ic_home),
    BOOKS("DS Sách", R.drawable.ic_book),
    STUDENTS("Sinh viên", R.drawable.ic_person)
}

@Composable
fun LibraryScreen() {
    val libraryViewModel: LibraryViewModel = viewModel()
    var selectedTab by rememberSaveable { mutableStateOf(LibraryTab.MANAGE) }
    val snackbarHostState = remember { SnackbarHostState() }
    val message = libraryViewModel.message

    LaunchedEffect(message) {
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            libraryViewModel.clearMessage()
        }
    }

    BackHandler(enabled = selectedTab != LibraryTab.MANAGE) {
        selectedTab = LibraryTab.MANAGE
    }

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            LibraryBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { tab -> selectedTab = tab }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .imePadding()
        ) {
            when (selectedTab) {
                LibraryTab.MANAGE -> ManageScreen(libraryViewModel)
                LibraryTab.BOOKS -> BookListScreen(libraryViewModel)
                LibraryTab.STUDENTS -> StudentListScreen(libraryViewModel)
            }
        }
    }
}

@Composable
private fun LibraryBottomBar(selectedTab: LibraryTab, onTabSelected: (LibraryTab) -> Unit) {
    Column {
        HorizontalDivider(color = BorderGray)
        NavigationBar(containerColor = Color.White) {
            LibraryTab.entries.forEach { tab ->
                NavigationBarItem(
                    selected = tab == selectedTab,
                    onClick = { onTabSelected(tab) },
                    icon = { Icon(painter = painterResource(tab.iconRes), contentDescription = null) },
                    label = { Text(tab.label) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = LibraryNavBlue,
                        selectedTextColor = LibraryNavBlue,
                        unselectedIconColor = LibraryInactive,
                        unselectedTextColor = LibraryInactive,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}
