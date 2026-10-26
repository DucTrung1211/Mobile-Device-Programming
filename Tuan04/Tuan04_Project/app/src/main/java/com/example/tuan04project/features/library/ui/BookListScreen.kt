package com.example.tuan04project.features.library.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan04project.features.library.model.Book
import com.example.tuan04project.features.library.viewmodel.LibraryViewModel
import com.example.tuan04project.ui.theme.BorderGray
import com.example.tuan04project.ui.theme.LibraryAvailableGreen
import com.example.tuan04project.ui.theme.LibraryCheckRed
import com.example.tuan04project.ui.theme.TextDark
import com.example.tuan04project.ui.theme.TextGray

@Composable
fun BookListScreen(viewModel: LibraryViewModel) {
    var bookTitle by rememberSaveable { mutableStateOf("") }
    var bookAuthor by rememberSaveable { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            LibraryScreenTitle(text = "Danh sách sách")
            Spacer(Modifier.height(16.dp))
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LibraryTextField(value = bookTitle, onValueChange = { bookTitle = it }, label = "Tên sách")
                LibraryTextField(value = bookAuthor, onValueChange = { bookAuthor = it }, label = "Tác giả")
                LibraryButton(
                    text = "Thêm sách",
                    onClick = {
                        if (viewModel.addBook(bookTitle, bookAuthor)) {
                            bookTitle = ""
                            bookAuthor = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(Modifier.height(12.dp))
        }
        item {
            LibrarySectionTitle(text = "Tất cả sách (${viewModel.books.size})")
        }
        items(viewModel.books, key = { book -> book.id }) { book ->
            BookInfoItem(book = book, borrowerName = viewModel.getBorrowerName(book.id))
        }
    }
}

@Composable
private fun BookInfoItem(book: Book, borrowerName: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderGray, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = book.title, color = TextDark, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Text(text = "Tác giả: ${book.author}", color = TextGray, fontSize = 13.sp)
            if (borrowerName != null) {
                Text(text = "Người mượn: $borrowerName", color = TextGray, fontSize = 13.sp)
            }
        }
        Spacer(Modifier.width(8.dp))
        BookStatusChip(isAvailable = borrowerName == null)
    }
}

@Composable
private fun BookStatusChip(isAvailable: Boolean) {
    val chipColor = if (isAvailable) LibraryAvailableGreen else LibraryCheckRed
    Text(
        text = if (isAvailable) "Sẵn sàng" else "Đang mượn",
        modifier = Modifier
            .background(chipColor.copy(alpha = 0.12f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        color = chipColor,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold
    )
}
