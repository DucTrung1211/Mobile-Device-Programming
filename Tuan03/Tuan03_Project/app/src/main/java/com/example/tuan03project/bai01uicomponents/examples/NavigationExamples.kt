package com.example.tuan03project.bai01uicomponents.examples

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationExample(id: String) {
    var mucDangChon by rememberSaveable { mutableIntStateOf(0) }
    var soLanNhan by rememberSaveable { mutableIntStateOf(0) }
    val cacMuc = listOf("Trang chủ", "Bài học", "Đã lưu")
    val khongInsets = WindowInsets(0, 0, 0, 0)
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        when (id) {
            "topbar" -> TopAppBar(
                title = { Text("TopAppBar") }, windowInsets = khongInsets,
                actions = { TextButton(onClick = { soLanNhan++ }) { Text("Lưu") } })
            "centerbar" -> CenterAlignedTopAppBar(
                title = { Text("Centered title") }, windowInsets = khongInsets,
                actions = { TextButton(onClick = { soLanNhan++ }) { Text("+") } })
            "bottombar" -> BottomAppBar(windowInsets = khongInsets) {
                TextButton(onClick = { soLanNhan++ }) { Text("Thêm") }
                TextButton(onClick = { soLanNhan = 0 }) { Text("Đặt lại") }
            }
            "navbar" -> NavigationBar(windowInsets = khongInsets) {
                cacMuc.forEachIndexed { viTri, ten ->
                    NavigationBarItem(selected = mucDangChon == viTri,
                        onClick = { mucDangChon = viTri },
                        icon = { Text("${viTri + 1}") }, label = { Text(ten) })
                }
            }
            "rail" -> Row {
                NavigationRail(windowInsets = khongInsets) {
                    cacMuc.forEachIndexed { viTri, ten ->
                        NavigationRailItem(selected = mucDangChon == viTri,
                            onClick = { mucDangChon = viTri },
                            icon = { Text("${viTri + 1}") }, label = { Text(ten) })
                    }
                }
                Text(cacMuc[mucDangChon], Modifier.padding(16.dp))
            }
            "drawer" -> {
                val nganKeo = rememberDrawerState(DrawerValue.Closed)
                val phamVi = rememberCoroutineScope()
                ModalNavigationDrawer(
                    modifier = Modifier.fillMaxWidth().height(340.dp),
                    drawerState = nganKeo,
                    drawerContent = {
                        ModalDrawerSheet(windowInsets = khongInsets, modifier = Modifier.width(250.dp)) {
                            cacMuc.forEachIndexed { viTri, ten ->
                                NavigationDrawerItem(label = { Text(ten) }, selected = mucDangChon == viTri,
                                    onClick = {
                                        mucDangChon = viTri
                                        phamVi.launch { nganKeo.close() }
                                    })
                            }
                        }
                    }
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Button(onClick = { phamVi.launch { nganKeo.open() } }) { Text("Mở Drawer") }
                        Text("Đang xem: ${cacMuc[mucDangChon]}")
                    }
                }
            }
            "tabs" -> PrimaryTabRow(selectedTabIndex = mucDangChon) {
                cacMuc.forEachIndexed { viTri, ten ->
                    Tab(selected = mucDangChon == viTri, onClick = { mucDangChon = viTri }, text = { Text(ten) })
                }
            }
            "scrolltabs" -> PrimaryScrollableTabRow(selectedTabIndex = mucDangChon, edgePadding = 0.dp) {
                repeat(8) { viTri ->
                    Tab(selected = mucDangChon == viTri, onClick = { mucDangChon = viTri },
                        text = { Text("Bài ${viTri + 1}") })
                }
            }
        }
        if (id in listOf("topbar", "centerbar", "bottombar")) Text("Đã nhấn $soLanNhan lần")
        else Text(if (id == "scrolltabs") "Nội dung bài ${mucDangChon + 1}" else "Nội dung: ${cacMuc[mucDangChon]}")
    }
}
