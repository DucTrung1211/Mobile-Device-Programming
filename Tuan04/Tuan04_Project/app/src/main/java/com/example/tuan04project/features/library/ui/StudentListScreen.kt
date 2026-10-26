package com.example.tuan04project.features.library.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tuan04project.features.library.viewmodel.LibraryViewModel

@Composable
fun StudentListScreen(viewModel: LibraryViewModel) {
    var studentName by rememberSaveable { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            LibraryScreenTitle(text = "Danh sách sinh viên")
            Spacer(Modifier.height(16.dp))
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LibraryTextField(value = studentName, onValueChange = { studentName = it }, label = "Tên sinh viên")
                LibraryButton(
                    text = "Thêm sinh viên",
                    onClick = {
                        if (viewModel.addStudent(studentName)) {
                            studentName = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(Modifier.height(12.dp))
        }
        item {
            LibrarySectionTitle(text = "Tất cả sinh viên (${viewModel.students.size})")
        }
        items(viewModel.students, key = { student -> student.id }) { student ->
            LibraryUserCard(
                user = student,
                extraInfo = "Đang mượn: ${viewModel.countBorrowedBooks(student.id)} cuốn"
            )
        }
    }
}
