package com.example.tuan04project.features.library.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan04project.features.library.model.LibraryUser
import com.example.tuan04project.ui.theme.BorderGray
import com.example.tuan04project.ui.theme.LibraryBlue
import com.example.tuan04project.ui.theme.TextDark
import com.example.tuan04project.ui.theme.TextGray

@Composable
fun LibraryScreenTitle(text: String) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        color = TextDark,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}

@Composable
fun LibrarySectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        color = TextDark,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun LibraryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.height(46.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = LibraryBlue)
    ) {
        Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
fun LibraryTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = LibraryBlue,
            focusedLabelColor = LibraryBlue,
            unfocusedBorderColor = BorderGray
        )
    )
}

@Composable
fun LibraryUserCard(user: LibraryUser, extraInfo: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderGray, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = user.name, color = TextDark, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Text(text = user.getDescription(), color = TextGray, fontSize = 13.sp)
            Text(text = extraInfo, color = LibraryBlue, fontSize = 13.sp)
        }
    }
}
