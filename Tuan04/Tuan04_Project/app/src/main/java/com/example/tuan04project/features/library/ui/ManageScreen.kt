package com.example.tuan04project.features.library.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan04project.features.library.model.Book
import com.example.tuan04project.features.library.model.BorrowRecord
import com.example.tuan04project.features.library.model.Student
import com.example.tuan04project.features.library.viewmodel.LibraryViewModel
import com.example.tuan04project.ui.theme.BorderGray
import com.example.tuan04project.ui.theme.LibraryBlue
import com.example.tuan04project.ui.theme.LibraryBookArea
import com.example.tuan04project.ui.theme.LibraryCheckRed
import com.example.tuan04project.ui.theme.TextDark
import com.example.tuan04project.ui.theme.TextGray

@Composable
fun ManageScreen(viewModel: LibraryViewModel) {
    var isStudentPickerVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(28.dp))
        LibraryScreenTitle(text = "Hệ thống\nQuản lý Thư viện")
        Spacer(Modifier.height(40.dp))

        LibrarySectionTitle(text = "Sinh viên")
        Spacer(Modifier.height(8.dp))
        SelectedStudentRow(
            studentName = viewModel.selectedStudent?.name,
            onChangeClick = { isStudentPickerVisible = true }
        )
        Spacer(Modifier.height(24.dp))

        LibrarySectionTitle(text = "Danh sách sách")
        Spacer(Modifier.height(10.dp))
        BookSelectionArea(viewModel = viewModel)
        Spacer(Modifier.height(20.dp))

        LibraryButton(
            text = "Thêm",
            onClick = { viewModel.borrowSelectedBooks() },
            modifier = Modifier
                .width(150.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(28.dp))

        LibrarySectionTitle(text = "Lịch sử mượn")
        Spacer(Modifier.height(10.dp))
        BorrowHistory(viewModel = viewModel)
        Spacer(Modifier.height(24.dp))
    }

    if (isStudentPickerVisible) {
        StudentPickerDialog(
            students = viewModel.students,
            selectedStudentId = viewModel.selectedStudentId,
            onStudentSelected = { studentId ->
                viewModel.selectStudent(studentId)
                isStudentPickerVisible = false
            },
            onDismiss = { isStudentPickerVisible = false }
        )
    }
}

@Composable
private fun SelectedStudentRow(studentName: String?, onChangeClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(46.dp)
                .border(1.dp, Color(0xFFBDBDBD), RoundedCornerShape(8.dp))
                .padding(horizontal = 14.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = studentName ?: "Chưa chọn sinh viên",
                color = if (studentName == null) TextGray else TextDark,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(Modifier.width(12.dp))
        LibraryButton(text = "Thay đổi", onClick = onChangeClick)
    }
}

@Composable
private fun BookSelectionArea(viewModel: LibraryViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 180.dp)
            .background(LibraryBookArea, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (viewModel.books.isEmpty()) {
            Text(text = "Chưa có sách. Hãy thêm sách ở mục DS Sách.", color = TextGray, fontSize = 14.sp)
        }
        viewModel.books.forEach { book ->
            BookCheckItem(
                book = book,
                isChecked = viewModel.isBookSelected(book.id),
                borrowerName = viewModel.getBorrowerName(book.id),
                onToggle = { viewModel.toggleBookSelection(book.id) }
            )
        }
    }
}

@Composable
private fun BookCheckItem(book: Book, isChecked: Boolean, borrowerName: String?, onToggle: () -> Unit) {
    val isAvailable = borrowerName == null
    val itemShape = RoundedCornerShape(18.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, itemShape)
            .clip(itemShape)
            .background(Color.White)
            .clickable(enabled = isAvailable, onClick = onToggle)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { onToggle() },
            enabled = isAvailable,
            colors = CheckboxDefaults.colors(checkedColor = LibraryCheckRed)
        )
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                text = book.title,
                color = if (isAvailable) TextDark else TextGray,
                fontSize = 15.sp
            )
            if (!isAvailable) {
                Text(text = "Đang được mượn bởi $borrowerName", color = LibraryCheckRed, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun BorrowHistory(viewModel: LibraryViewModel) {
    if (viewModel.borrowRecords.isEmpty()) {
        Text(text = "Chưa có lượt mượn nào.", color = TextGray, fontSize = 14.sp)
        return
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        viewModel.borrowRecords.reversed().forEach { record ->
            BorrowRecordItem(
                studentName = viewModel.getStudentName(record),
                bookTitle = viewModel.getBookTitle(record),
                record = record,
                onReturnClick = { viewModel.returnBook(record) }
            )
        }
    }
}

@Composable
private fun BorrowRecordItem(
    studentName: String,
    bookTitle: String,
    record: BorrowRecord,
    onReturnClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderGray, RoundedCornerShape(12.dp))
            .padding(start = 14.dp, top = 10.dp, bottom = 10.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = studentName, color = TextDark, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Text(text = "Mượn: $bookTitle", color = TextDark, fontSize = 14.sp)
            Text(text = "Ngày mượn: ${record.borrowDate}", color = TextGray, fontSize = 12.sp)
        }
        TextButton(onClick = onReturnClick) {
            Text(text = "Trả sách", color = LibraryBlue)
        }
    }
}

@Composable
private fun StudentPickerDialog(
    students: List<Student>,
    selectedStudentId: Int?,
    onStudentSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = { Text(text = "Chọn sinh viên", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .heightIn(max = 360.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                students.forEach { student ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onStudentSelected(student.id) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = student.id == selectedStudentId,
                            onClick = { onStudentSelected(student.id) },
                            colors = RadioButtonDefaults.colors(selectedColor = LibraryBlue)
                        )
                        Column {
                            Text(text = student.name, color = TextDark, fontSize = 15.sp)
                            Text(text = student.studentCode, color = TextGray, fontSize = 12.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Đóng", color = LibraryBlue)
            }
        }
    )
}
