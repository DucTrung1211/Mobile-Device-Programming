# Hướng dẫn chỉnh sửa giao diện khi thầy hỏi

Các ví dụ là **bài thực hành thay đổi**, không phải toàn bộ thay đổi đã áp dụng trong bản giao nộp. Mỗi tình huống độc lập: sửa, chạy thử, rồi hoàn tác trước khi làm tình huống khác. Đặc biệt các thay đổi nền đỏ, nút Xóa phụ hoặc đổi mặc định password không thuộc giao diện tham chiếu.

Số dòng dẫn tới bản source cuối cùng ngày 19/10/2026. Khi dòng dịch chuyển, tìm tên hàm và đoạn “Trước”. Đoạn “Trước” được lấy đúng từ source hiện tại. Đoạn “Sau” thay thế đoạn đó trong ngữ cảnh hàm; đừng dán cả hai cùng lúc. Một số đoạn xuất hiện nhiều lần: sửa đúng hàm được chỉ rõ.

Nếu thiếu import sau khi dán, đặt con trỏ vào tên đỏ và dùng Alt+Enter. Các ví dụ dùng tên đầy đủ cho Color/FontStyle khi file gốc chưa import. Giữ nguyên package, Manifest, MainScreen và Gradle khi chỉ sửa UI.


## A. Màu sắc

### Đổi nền trắng thành đỏ

Được. Mở [ComponentsApp.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsApp.kt#L36), tìm **ComponentsApp**, đoạn bắt đầu dòng 36.

Trước:

```kotlin
Surface(Modifier.fillMaxSize(), color = Color.White)
```

Sau:

```kotlin
Surface(Modifier.fillMaxSize(), color = Color.Red)
```

**Kết quả:** Nền cả bài mới đổi đỏ.

**Vì sao:** Surface gốc vẽ nền; theme cũ của bài trước không đổi.

### Đổi tiêu đề xanh thành đỏ

Được. Mở [ComponentsApp.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsApp.kt#L12), tìm **màu đầu file**, đoạn bắt đầu dòng 12.

Trước:

```kotlin
val TitleBlue = Color(0xFF2196D2)
```

Sau:

```kotlin
val TitleBlue = Color.Red
```

**Kết quả:** Tiêu đề danh sách/detail và chữ Back đổi đỏ.

**Vì sao:** Các vị trí cùng đọc TitleBlue.

### Đổi thẻ xanh nhạt thành xanh lá

Được. Mở [ComponentsApp.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsApp.kt#L13), tìm **màu đầu file**, đoạn bắt đầu dòng 13.

Trước:

```kotlin
val ItemBlue = Color(0xFFBBDFFC)
```

Sau:

```kotlin
val ItemBlue = Color(0xFFC8E6C9)
```

**Kết quả:** Các thẻ danh sách và demo dùng ItemBlue chuyển xanh lá.

**Vì sao:** Một biến màu dùng lại ở nhiều nơi.

### Đổi chữ đen thành trắng

Được. Mở [ComponentsListScreen.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsListScreen.kt#L43), tìm **ComponentItem**, đoạn bắt đầu dòng 43.

Trước:

```kotlin
Text(component.name, fontWeight = FontWeight.Bold)
```

Sau:

```kotlin
Text(component.name, fontWeight = FontWeight.Bold, color = androidx.compose.ui.graphics.Color.White)
```

**Kết quả:** Tên component thành trắng.

**Vì sao:** color của Text chỉ đổi chữ tên; muốn mô tả trắng thì thêm color tương tự ở Text bên dưới.

Nên dùng nền đậm để chữ trắng dễ đọc.

### Đổi màu Button

Được. Mở [IntroScreen.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/IntroScreen.kt#L50), tìm **IntroScreen**, đoạn bắt đầu dòng 50.

Trước:

```kotlin
containerColor = ButtonBlue, contentColor = Color.White
```

Sau:

```kotlin
containerColor = Color(0xFF1565C0), contentColor = Color.White
```

**Kết quả:** Nút I'm ready đổi xanh đậm.

**Vì sao:** ButtonDefaults.buttonColors tách màu nền và chữ.

### Đổi màu viền TextField

Được. Mở [examples/InputExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/InputExamples.kt#L30), tìm **TextFieldExample**, đoạn bắt đầu dòng 30.

Trước:

```kotlin
unfocusedBorderColor = Color.Gray, focusedBorderColor = Color.Gray
```

Sau:

```kotlin
unfocusedBorderColor = Color.Green, focusedBorderColor = Color.Blue
```

**Kết quả:** Viền xanh lá khi không focus, xanh dương khi đang nhập.

**Vì sao:** TextField có màu riêng theo trạng thái focus.

### Đổi màu Checkbox khi được chọn

Được. Mở [examples/SelectionExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L24), tìm **CheckboxExample**, đoạn bắt đầu dòng 24.

Trước:

```kotlin
Checkbox(checked = daChon, onCheckedChange = { daChon = it })
```

Sau:

```kotlin
Checkbox(checked = daChon, onCheckedChange = { daChon = it },
            colors = CheckboxDefaults.colors(checkedColor = androidx.compose.ui.graphics.Color.Green))
```

**Kết quả:** Dấu chọn có nền xanh lá.

**Vì sao:** checkedColor được dùng khi checked true.

### Đổi màu ô giữa Row

Được. Mở [ComponentsApp.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsApp.kt#L16), tìm **màu đầu file**, đoạn bắt đầu dòng 16.

Trước:

```kotlin
val DarkTileBlue = Color(0xFF5086EF)
```

Sau:

```kotlin
val DarkTileBlue = Color(0xFF1A4ED8)
```

**Kết quả:** Ô giữa Row/Column xanh đậm hơn.

**Vì sao:** Phần tử thứ hai trong listOf dùng DarkTileBlue.

Muốn chỉ Row đổi, thay DarkTileBlue bằng Color(...) trong listOf của RowExample.

### Đổi màu chữ dưới TextField

Được. Mở [examples/InputExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/InputExamples.kt#L34), tìm **TextFieldExample**, đoạn bắt đầu dòng 34.

Trước:

```kotlin
color = Color(0xFFB52A2A), fontSize = 14.sp
```

Sau:

```kotlin
color = Color.Blue, fontSize = 14.sp
```

**Kết quả:** Dòng phản hồi nhập liệu đổi xanh.

**Vì sao:** Tham số color đặt ngay Text phản hồi.

### Đổi màu Card

Được. Mở [examples/ContentExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/ContentExamples.kt#L23), tìm **ContentExample, nhánh card**, đoạn bắt đầu dòng 23.

Trước:

```kotlin
Card(colors = CardDefaults.cardColors(containerColor = ItemBlue))
```

Sau:

```kotlin
Card(colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.Yellow))
```

**Kết quả:** Card demo chuyển vàng.

**Vì sao:** Card có CardColors riêng; không đổi danh sách.


## B. Font chữ

### Tăng font size

Được. Mở [IntroScreen.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/IntroScreen.kt#L38), tìm **IntroScreen**, đoạn bắt đầu dòng 38.

Trước:

```kotlin
Text("Jetpack Compose", fontSize = 16.sp, fontWeight = FontWeight.Medium)
```

Sau:

```kotlin
Text("Jetpack Compose", fontSize = 22.sp, fontWeight = FontWeight.Medium)
```

**Kết quả:** Tiêu đề Intro lớn hơn.

**Vì sao:** fontSize dùng sp, tôn trọng cài đặt cỡ chữ.

### Giảm font size

Được. Mở [IntroScreen.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/IntroScreen.kt#L38), tìm **IntroScreen**, đoạn bắt đầu dòng 38.

Trước:

```kotlin
Text("Jetpack Compose", fontSize = 16.sp, fontWeight = FontWeight.Medium)
```

Sau:

```kotlin
Text("Jetpack Compose", fontSize = 14.sp, fontWeight = FontWeight.Medium)
```

**Kết quả:** Tiêu đề nhỏ hơn.

**Vì sao:** Giảm số sp giảm kích thước chữ.

### Đổi chữ thành bold

Được. Mở [IntroScreen.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/IntroScreen.kt#L38), tìm **IntroScreen**, đoạn bắt đầu dòng 38.

Trước:

```kotlin
fontWeight = FontWeight.Medium
```

Sau:

```kotlin
fontWeight = FontWeight.Bold
```

**Kết quả:** Tiêu đề Intro đậm.

**Vì sao:** FontWeight điều khiển độ đậm.

### Đổi thành italic

Được. Mở [ComponentsListScreen.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsListScreen.kt#L43), tìm **ComponentItem**, đoạn bắt đầu dòng 43.

Trước:

```kotlin
Text(component.name, fontWeight = FontWeight.Bold)
```

Sau:

```kotlin
Text(component.name, fontWeight = FontWeight.Bold,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
```

**Kết quả:** Tên component vừa đậm vừa nghiêng.

**Vì sao:** fontStyle độc lập với fontWeight.

### Thêm underline

Được. Mở [examples/DisplayExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/DisplayExamples.kt#L34), tìm **TextExample**, đoạn bắt đầu dòng 34.

Trước:

```kotlin
withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("over") }
```

Sau:

```kotlin
withStyle(SpanStyle(fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline)) { append("over") }
```

**Kết quả:** over có thêm gạch chân.

**Vì sao:** SpanStyle chỉ áp dụng nội dung trong khối.

### Thêm strikethrough

Được. Mở [examples/DisplayExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/DisplayExamples.kt#L38), tìm **TextExample**, đoạn bắt đầu dòng 38.

Trước:

```kotlin
withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append("lazy") }
```

Sau:

```kotlin
withStyle(SpanStyle(fontStyle = FontStyle.Italic, textDecoration = TextDecoration.LineThrough)) { append("lazy") }
```

**Kết quả:** lazy nghiêng và gạch ngang.

**Vì sao:** TextDecoration.LineThrough vẽ đường giữa chữ.

### Đổi màu một từ trong câu

Được. Mở [examples/DisplayExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/DisplayExamples.kt#L32), tìm **TextExample**, đoạn bắt đầu dòng 32.

Trước:

```kotlin
withStyle(SpanStyle(color = Color(0xFFB87818), fontSize = 38.sp)) { append("Brown") }
```

Sau:

```kotlin
withStyle(SpanStyle(color = Color.Red, fontSize = 38.sp)) { append("Brown") }
```

**Kết quả:** Chỉ Brown đổi đỏ.

**Vì sao:** Màu ở SpanStyle không ảnh hưởng các append ngoài khối.

### Căn giữa văn bản

Được. Mở [examples/InputExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/InputExamples.kt#L34), tìm **TextFieldExample**, đoạn bắt đầu dòng 34.

Trước:

```kotlin
color = Color(0xFFB52A2A), fontSize = 14.sp
```

Sau:

```kotlin
color = Color(0xFFB52A2A), fontSize = 14.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
```

**Kết quả:** Chữ đỏ căn giữa chiều rộng vùng nhập.

**Vì sao:** Text phải có vùng rộng thì textAlign mới thấy rõ.

### Căn trái hoặc căn phải

Được. Mở [examples/DisplayExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/DisplayExamples.kt#L86), tìm **DisplayExample, nhánh textstyles**, đoạn bắt đầu dòng 86.

Trước:

```kotlin
Text("Căn giữa", Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
```

Sau:

```kotlin
Text("Căn trái", Modifier.fillMaxWidth(), textAlign = TextAlign.Start)
            Text("Căn phải", Modifier.fillMaxWidth(), textAlign = TextAlign.End)
```

**Kết quả:** Có hai dòng căn trái/phải.

**Vì sao:** Start/End theo hướng ngôn ngữ; tiếng Việt là trái/phải.


## C. Kích thước và vị trí

### Tăng chiều rộng Button

Được. Mở [IntroScreen.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/IntroScreen.kt#L24), tìm **IntroScreen**, đoạn bắt đầu dòng 24.

Trước:

```kotlin
Modifier.fillMaxSize().padding(horizontal = 24.dp)
```

Sau:

```kotlin
Modifier.fillMaxSize().padding(horizontal = 12.dp)
```

**Kết quả:** Nút và vùng nội dung rộng thêm 24.dp tổng cộng.

**Vì sao:** Nút đã fillMaxWidth; giảm lề cha 12.dp mỗi bên làm vùng khả dụng rộng hơn.

Sửa Column ngoài của IntroScreen. Không cố dùng width lớn hơn chiều rộng màn hình.

### Tăng chiều cao Button

Được. Mở [IntroScreen.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/IntroScreen.kt#L48), tìm **IntroScreen**, đoạn bắt đầu dòng 48.

Trước:

```kotlin
padding(bottom = 30.dp).height(50.dp)
```

Sau:

```kotlin
padding(bottom = 30.dp).height(64.dp)
```

**Kết quả:** Nút I'm ready cao hơn 14.dp.

**Vì sao:** height đặt chiều cao vùng nút trong giới hạn cha.

### Tăng kích thước ảnh

Được. Mở [examples/DisplayExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/DisplayExamples.kt#L53), tìm **ImageExample**, đoạn bắt đầu dòng 53.

Trước:

```kotlin
Modifier.fillMaxWidth().aspectRatio(1.78f).clip(RoundedCornerShape(10.dp))
```

Sau:

```kotlin
Modifier.fillMaxWidth().height(240.dp).clip(RoundedCornerShape(10.dp))
```

**Kết quả:** Ảnh cao 240.dp thay vì tính theo tỉ lệ.

**Vì sao:** Đổi ràng buộc chiều cao; Crop vẫn phủ khung.

Đoạn này có hai lần; sửa ảnh muốn thay. Chiều rộng đã đầy vùng cha; muốn rộng thêm giảm padding ngang detail.

### Đổi góc bo 8.dp thành 16.dp

Được. Mở [ComponentsListScreen.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsListScreen.kt#L39), tìm **ComponentItem**, đoạn bắt đầu dòng 39.

Trước:

```kotlin
clip(RoundedCornerShape(8.dp))
```

Sau:

```kotlin
clip(RoundedCornerShape(16.dp))
```

**Kết quả:** Thẻ danh sách bo góc lớn hơn.

**Vì sao:** Bán kính bo tăng, màu/kích thước giữ nguyên.

### Đổi khoảng cách giữa item

Được. Mở [ComponentsListScreen.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsListScreen.kt#L22), tìm **ComponentsListScreen**, đoạn bắt đầu dòng 22.

Trước:

```kotlin
verticalArrangement = Arrangement.spacedBy(12.dp)
```

Sau:

```kotlin
verticalArrangement = Arrangement.spacedBy(20.dp)
```

**Kết quả:** Các thẻ cách nhau thêm 8.dp.

**Vì sao:** spacedBy đặt khoảng cách giữa các phần tử LazyColumn.

### Chuyển item về bên trái

Được. Mở [examples/LayoutExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L38), tìm **ColumnExample**, đoạn bắt đầu dòng 38.

Trước:

```kotlin
horizontalAlignment = Alignment.CenterHorizontally
```

Sau:

```kotlin
horizontalAlignment = Alignment.Start
```

**Kết quả:** Ba ô Column căn trái.

**Vì sao:** Alignment ngang trong Column điều khiển vị trí các con.

### Chuyển item về bên phải

Được. Mở [examples/LayoutExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L38), tìm **ColumnExample**, đoạn bắt đầu dòng 38.

Trước:

```kotlin
horizontalAlignment = Alignment.CenterHorizontally
```

Sau:

```kotlin
horizontalAlignment = Alignment.End
```

**Kết quả:** Ba ô Column căn phải.

**Vì sao:** End là cạnh kết thúc, bên phải với tiếng Việt.

### Căn nội dung giữa màn hình

Được. Mở [ComponentDetailScreen.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentDetailScreen.kt#L51), tìm **ComponentDetailScreen**, đoạn bắt đầu dòng 51.

Trước:

```kotlin
verticalArrangement = if (canGiua) Arrangement.Center else Arrangement.Top
```

Sau:

```kotlin
verticalArrangement = Arrangement.Center
```

**Kết quả:** Mọi demo ngắn căn giữa vùng dưới header.

**Vì sao:** Arrangement của Column quyết định vị trí nhóm con theo chiều dọc.

Không áp dụng nếu muốn Row giữ vị trí theo slide.

### Thêm margin/padding

Được. Mở [examples/DisplayExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/DisplayExamples.kt#L53), tìm **ImageExample**, đoạn bắt đầu dòng 53.

Trước:

```kotlin
Modifier.fillMaxWidth().aspectRatio(1.78f).clip(RoundedCornerShape(10.dp))
```

Sau:

```kotlin
Modifier.fillMaxWidth().padding(horizontal = 12.dp).aspectRatio(1.78f).clip(RoundedCornerShape(10.dp))
```

**Kết quả:** Ảnh cách biên nội dung thêm 12.dp mỗi bên.

**Vì sao:** padding trước khung ảnh đóng vai trò khoảng đệm ngoài ảnh.

### Làm nội dung cuộn được

Được. Mở [ComponentDetailScreen.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentDetailScreen.kt#L48), tìm **ComponentDetailScreen**, đoạn bắt đầu dòng 48.

Trước:

```kotlin
Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState())
```

Sau:

```kotlin
Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState(), enabled = true)
```

**Kết quả:** Detail hiện đã cuộn được khi nội dung dài.

**Vì sao:** verticalScroll kết hợp ScrollState lưu vị trí; giữ đoạn này thay vì thêm lần nữa.

Đoạn hiện tại đã cho phép cuộn. enabled = true viết rõ trạng thái bật. Để thử, thêm repeat(30) { Text("Dòng $it") } vào Column nội dung. Nếu đổi enabled thành false sẽ thấy phần cuối không kéo tới được. LazyColumn/Grid con vẫn phải có height hữu hạn.


## D. Row / Column / Box

### Đổi Row thành Column

Được. Mở [examples/LayoutExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L18), tìm **RowExample**, đoạn bắt đầu dòng 18.

Trước:

```kotlin
fun RowExample() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        repeat(4) {
            Row(
                Modifier.fillMaxWidth().background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)).padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (mau in listOf(LightTileBlue, DarkTileBlue, LightTileBlue)) {
                    Box(Modifier.weight(1f).height(64.dp).background(mau, RoundedCornerShape(12.dp)))
                }
            }
        }
    }
}
```

Sau:

```kotlin
fun RowExample() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        repeat(4) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                for (mau in listOf(LightTileBlue, DarkTileBlue, LightTileBlue)) {
                    Box(Modifier.width(100.dp).height(64.dp)
                        .background(mau, RoundedCornerShape(12.dp)))
                }
            }
        }
    }
}
```

**Kết quả:** Mỗi nhóm ba ô xếp dọc, có thể cuộn để xem đủ.

**Vì sao:** Không chỉ đổi tên Row: phải đổi horizontalArrangement sang verticalArrangement và thay weight bằng width/height phù hợp.

### Đổi Column thành Row

Được. Mở [examples/LayoutExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L34), tìm **ColumnExample**, đoạn bắt đầu dòng 34.

Trước:

```kotlin
fun ColumnExample() {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (mau in listOf(LightTileBlue, DarkTileBlue, LightTileBlue)) {
            Box(Modifier.width(150.dp).height(64.dp).background(mau, RoundedCornerShape(12.dp)))
        }
    }
}
```

Sau:

```kotlin
fun ColumnExample() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        for (mau in listOf(LightTileBlue, DarkTileBlue, LightTileBlue)) {
            Box(Modifier.weight(1f).height(64.dp)
                .background(mau, RoundedCornerShape(12.dp)))
        }
    }
}
```

**Kết quả:** Ba ô nằm ngang và chia đều chiều rộng.

**Vì sao:** Row dùng horizontalArrangement; weight tránh ba width 150.dp gây tràn.

### Thêm ô thứ tư vào Row

Được. Mở [examples/LayoutExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L25), tìm **RowExample**, đoạn bắt đầu dòng 25.

Trước:

```kotlin
listOf(LightTileBlue, DarkTileBlue, LightTileBlue)
```

Sau:

```kotlin
listOf(LightTileBlue, DarkTileBlue, LightTileBlue, DarkTileBlue)
```

**Kết quả:** Mỗi hàng có bốn ô nhỏ hơn.

**Vì sao:** for duyệt thêm một màu; weight chia phần rộng cho bốn ô.

Sửa trong RowExample; ColumnExample cũng có cùng đoạn listOf.

### Xóa một ô

Được. Mở [examples/LayoutExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L25), tìm **RowExample**, đoạn bắt đầu dòng 25.

Trước:

```kotlin
listOf(LightTileBlue, DarkTileBlue, LightTileBlue)
```

Sau:

```kotlin
listOf(LightTileBlue, DarkTileBlue)
```

**Kết quả:** Mỗi Row còn hai ô.

**Vì sao:** Bớt phần tử trong danh sách màu, vòng for chạy hai lần.

### Đổi số hàng

Được. Mở [examples/LayoutExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L20), tìm **RowExample**, đoạn bắt đầu dòng 20.

Trước:

```kotlin
repeat(4)
```

Sau:

```kotlin
repeat(5)
```

**Kết quả:** Có năm hàng.

**Vì sao:** repeat đếm số lần tạo Row; màn hình detail cuộn khi dài.

### Đổi thứ tự phần tử

Được. Mở [examples/LayoutExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L25), tìm **RowExample**, đoạn bắt đầu dòng 25.

Trước:

```kotlin
listOf(LightTileBlue, DarkTileBlue, LightTileBlue)
```

Sau:

```kotlin
listOf(DarkTileBlue, LightTileBlue, LightTileBlue)
```

**Kết quả:** Ô đậm chuyển từ giữa sang trái.

**Vì sao:** Thứ tự dữ liệu chính là thứ tự tạo Box.

### Căn giữa cả hàng

Được. Mở [examples/LayoutExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L110), tìm **LayoutExample, nhánh arrangement**, đoạn bắt đầu dòng 110.

Trước:

```kotlin
horizontalArrangement = if (gianDeu) Arrangement.SpaceBetween else Arrangement.Center
```

Sau:

```kotlin
horizontalArrangement = Arrangement.Center
```

**Kết quả:** Ba NumberTile tập trung giữa Row.

**Vì sao:** Các ô nhánh này width 76.dp nên còn không gian để căn.

RowExample dùng weight chiếm hết chiều ngang; đổi Arrangement.Center riêng nó sẽ ít khác biệt.

### Giãn đều phần tử

Được. Mở [examples/LayoutExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L110), tìm **LayoutExample, nhánh arrangement**, đoạn bắt đầu dòng 110.

Trước:

```kotlin
horizontalArrangement = if (gianDeu) Arrangement.SpaceBetween else Arrangement.Center
```

Sau:

```kotlin
horizontalArrangement = Arrangement.SpaceEvenly
```

**Kết quả:** Khoảng trống trước, giữa và sau các ô bằng nhau.

**Vì sao:** SpaceEvenly chia đều cả phần trống ở hai cạnh.

### Làm Box chồng các hình

Được. Mở [examples/LayoutExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L49), tìm **BoxExample**, đoạn bắt đầu dòng 49.

Trước:

```kotlin
Box(Modifier.offset(x = (-55).dp, y = (-55).dp).size(90.dp)
```

Sau:

```kotlin
Box(Modifier.offset(x = (-20).dp, y = (-20).dp).size(90.dp)
```

**Kết quả:** Ô đầu tiến gần tâm hơn và bị ô giữa phủ nhiều hơn.

**Vì sao:** Các con Box cùng vùng, offset dịch chỗ; con cuối vẽ trên.

Giữ Box cha. Thử chuyển Box giữa 120.dp từ cuối lên đầu để thay thứ tự chồng.

### Thay đổi vị trí hình trong Box

Được. Mở [examples/LayoutExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L49), tìm **BoxExample**, đoạn bắt đầu dòng 49.

Trước:

```kotlin
offset(x = (-55).dp, y = (-55).dp)
```

Sau:

```kotlin
offset(x = (-30).dp, y = (-70).dp)
```

**Kết quả:** Ô đầu sang phải 25.dp và lên thêm 15.dp.

**Vì sao:** x/y tính từ vị trí căn giữa ban đầu.


## E. Events

### Đổi thông báo sau khi nhấn Button

Được. Mở [examples/ActionExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/ActionExamples.kt#L29), tìm **ActionExample**, đoạn bắt đầu dòng 29.

Trước:

```kotlin
Text("Đã nhấn $soLanNhan lần")
```

Sau:

```kotlin
Text(if (soLanNhan == 0) "Chưa nhấn" else "Xin chào thầy!")
```

**Kết quả:** Sau lần nhấn đầu, dòng kết quả chào thầy.

**Vì sao:** Callback đã tăng soLanNhan, điều kiện dựa trên state đổi nhánh Text.

### Nhấn Button tăng biến đếm

Được. Mở [examples/ActionExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/ActionExamples.kt#L14), tìm **ActionExample**, đoạn bắt đầu dòng 14.

Trước:

```kotlin
val khiNhan = { soLanNhan++; Unit }
```

Sau:

```kotlin
val khiNhan: () -> Unit = { soLanNhan = soLanNhan + 1 }
```

**Kết quả:** Mỗi nhấn tăng một.

**Vì sao:** Hai cách viết tương đương; cách sau diễn đạt rõ gán số cũ + 1.

### Nhấn Button đổi màu nền

Được. Mở [examples/ActionExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/ActionExamples.kt#L13), tìm **ActionExample**, đoạn bắt đầu dòng 13.

Trước:

```kotlin
var soLanNhan by rememberSaveable { mutableIntStateOf(0) }
    val khiNhan = { soLanNhan++; Unit }
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
```

Sau:

```kotlin
var soLanNhan by rememberSaveable { mutableIntStateOf(0) }
    var nenXanh by rememberSaveable { mutableStateOf(false) }
    val khiNhan = { soLanNhan++; nenXanh = !nenXanh; Unit }
    Column(
        modifier = androidx.compose.ui.Modifier.background(
            if (nenXanh) androidx.compose.ui.graphics.Color.Cyan else androidx.compose.ui.graphics.Color.White
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
```

**Kết quả:** Mỗi nhấn đảo nền vùng demo trắng/xanh cyan.

**Vì sao:** onClick đổi Boolean; background đọc Boolean nên vẽ lại.

Thêm import androidx.compose.foundation.background. Muốn đổi cả nền màn hình cần đưa state lên ComponentsApp; ví dụ này đổi nền vùng demo.

### Nhấn Button đổi chữ trên chính nút

Được. Mở [examples/ActionExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/ActionExamples.kt#L17), tìm **ActionExample**, đoạn bắt đầu dòng 17.

Trước:

```kotlin
"button" -> Button(onClick = khiNhan) { Text("Nhấn Button") }
```

Sau:

```kotlin
"button" -> Button(onClick = khiNhan) { Text(if (soLanNhan == 0) "Nhấn Button" else "Đã nhấn") }
```

**Kết quả:** Nhãn Button đổi sau khi bấm.

**Vì sao:** Nội dung nút cũng là Text đọc state.

### Checkbox chọn và bỏ chọn

Được. Mở [examples/SelectionExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L24), tìm **CheckboxExample**, đoạn bắt đầu dòng 24.

Trước:

```kotlin
Checkbox(checked = daChon, onCheckedChange = { daChon = it })
```

Sau:

```kotlin
Checkbox(checked = daChon, onCheckedChange = { giaTriMoi -> daChon = giaTriMoi })
```

**Kết quả:** Nhấn lần một chọn, lần hai bỏ.

**Vì sao:** giaTriMoi là Boolean được truyền vào, giống it nhưng tên rõ hơn.

### Switch ON/OFF

Được. Mở [examples/SelectionExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L49), tìm **SwitchExample**, đoạn bắt đầu dòng 49.

Trước:

```kotlin
Switch(checked = daBat, onCheckedChange = { daBat = it })
```

Sau:

```kotlin
Switch(checked = daBat, onCheckedChange = { trangThaiMoi -> daBat = trangThaiMoi })
```

**Kết quả:** Switch và dòng ON/OFF đổi cùng nhau.

**Vì sao:** Cùng đọc daBat, không cần biến chữ thứ hai.

### RadioButton chỉ chọn một mục

Được. Mở [examples/SelectionExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L31), tìm **RadioExample**, đoạn bắt đầu dòng 31.

Trước:

```kotlin
var luaChon by rememberSaveable { mutableStateOf("Kotlin") }
```

Sau:

```kotlin
var luaChon by rememberSaveable { mutableStateOf("Java") }
```

**Kết quả:** Mặc định Java được chọn, sau đó vẫn chỉ chọn một.

**Vì sao:** Mọi selected so sánh với một String dùng chung; Row onClick gán lại luaChon.

### Slider tăng giảm giá trị

Được. Mở [examples/SelectionExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L58), tìm **SliderExample**, đoạn bắt đầu dòng 58.

Trước:

```kotlin
valueRange = 0f..100f
```

Sau:

```kotlin
valueRange = 0f..200f
```

**Kết quả:** Kéo để chọn 0 đến 200.

**Vì sao:** Thay giới hạn Slider; giá trị ban đầu 50f vẫn hợp lệ.

Sửa đúng SliderExample, không sửa RangeSlider trong SelectionExample nếu chỉ muốn đổi Slider.

### TextField hiển thị dữ liệu đang nhập

Được. Mở [examples/InputExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/InputExamples.kt#L25), tìm **TextFieldExample**, đoạn bắt đầu dòng 25.

Trước:

```kotlin
onValueChange = { noiDungNhap = it }
```

Sau:

```kotlin
onValueChange = { noiDungMoi -> noiDungNhap = noiDungMoi }
```

**Kết quả:** Dòng đỏ cập nhật từng lần gõ.

**Vì sao:** Tên tham số dài hơn cho dễ giải thích; cơ chế state giữ nguyên.

### TextField xóa dữ liệu

Được. Mở [examples/InputExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/InputExamples.kt#L32), tìm **TextFieldExample**, đoạn bắt đầu dòng 32.

Trước:

```kotlin
        Text(
            if (noiDungNhap.isEmpty()) "Tự động cập nhật dữ liệu theo textfield" else noiDungNhap,
            color = Color(0xFFB52A2A), fontSize = 14.sp
        )
```

Sau:

```kotlin
        Text(
            if (noiDungNhap.isEmpty()) "Tự động cập nhật dữ liệu theo textfield" else noiDungNhap,
            color = Color(0xFFB52A2A), fontSize = 14.sp
        )
        TextButton(onClick = { noiDungNhap = "" }) { Text("Xóa") }
```

**Kết quả:** Nhấn Xóa làm ô rỗng và dòng đỏ trở về lời nhắc.

**Vì sao:** value và Text đều đọc noiDungNhap.

Đây là thay đổi thực hành; bản tham chiếu giao nộp không có nút Xóa phụ trong TextField. Các Input · More đã có Xóa dữ liệu.

### Password hiện/ẩn

Được. Mở [examples/InputExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/InputExamples.kt#L42), tìm **PasswordExample**, đoạn bắt đầu dòng 42.

Trước:

```kotlin
var hienMatKhau by rememberSaveable { mutableStateOf(false) }
```

Sau:

```kotlin
var hienMatKhau by rememberSaveable { mutableStateOf(true) }
```

**Kết quả:** Lúc mở đã thấy chữ; nút vẫn bật/tắt được.

**Vì sao:** Giá trị mặc định điều khiển visualTransformation, không đổi dữ liệu mật khẩu.

Để giữ yêu cầu ban đầu của bài, dùng false trong bản giao nộp.

### Back quay lại màn hình trước

Được. Mở [ComponentsApp.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsApp.kt#L30), tìm **quayLai**, đoạn bắt đầu dòng 30.

Trước:

```kotlin
BackHandler { quayLai() }
```

Sau:

```kotlin
BackHandler(enabled = true, onBack = { quayLai() })
```

**Kết quả:** Back hệ thống vẫn quay detail → list → intro → trang chọn bài.

**Vì sao:** enabled true đăng ký handler; quayLai giữ nhánh "detail" -> manHinh = "list".

Nút Back trên header gọi cùng quayLai qua onBack. Không gán manHinh = intro trong nhánh detail nếu cần trở về danh sách.

## F. Thêm một ví dụ mới từ đầu đến cuối

Ví dụ thêm **Counter example**. Đây là tên ví dụ do mình đặt, dùng API Button/Text thật, không tuyên bố có composable Counter của thư viện.

### F1. Cần mở file nào?

- ComponentsData.kt: thêm dữ liệu mục.
- examples/ActionExamples.kt: viết CounterExample.
- ComponentDetailScreen.kt: gắn id mới với hàm.
- Không đổi MainActivity, không tạo Activity/launcher hoặc dependency mới.
- Màu dùng ButtonBlue/ItemBlue nếu muốn đồng bộ; font dùng tham số Text.

### F2. Thêm mục vào danh sách

Mở [ComponentsData.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsData.kt#L63), tìm dòng 63 ở nhóm Actions.

Trước:

```kotlin
    ComponentInfo("extendedfab", "ExtendedFloatingActionButton", "Combines icon and action text", "Actions"),
```

Sau:

```kotlin
    ComponentInfo("extendedfab", "ExtendedFloatingActionButton", "Combines icon and action text", "Actions"),
    ComponentInfo("counter", "Counter example", "Counts button taps", "Actions"),
```

Kết quả: nhóm Actions có thẻ xanh Counter example. id phải duy nhất; tên nhóm khớp nhóm đã dùng. ComponentItem chung tự áp dụng màu, góc bo và callback nên không sao chép giao diện item.

### F3. Tạo ví dụ component

Mở [examples/ActionExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/ActionExamples.kt#L29), tìm cuối ActionExample ở dòng 29. Thêm hàm sau khối đóng của hàm cũ, không đặt bên trong hàm cũ.

Trước:

```kotlin
        Text("Đã nhấn $soLanNhan lần")
    }
}
```

Sau:

```kotlin
        Text("Đã nhấn $soLanNhan lần")
    }
}

@Composable
fun CounterExample() {
    var soDem by rememberSaveable { mutableIntStateOf(0) }
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Số đếm: $soDem")
        Button(onClick = { soDem++ }) { Text("Tăng 1") }
        TextButton(onClick = { soDem = 0 }) { Text("Đặt lại") }
    }
}
```

Kết quả: hàm mới hiển thị số đếm, tăng và đặt lại. File đã import runtime/saveable/material3/layout/dp nên không cần dependency mới. @Composable cần đứng trước hàm mới, giữ @Composable của hàm cũ.

### F4. Xử lý nhấn và state

Trong **đoạn vừa thêm ở F3**, soDem Int khởi tạo 0. onClick tăng hoặc gán 0, Text đọc lại state. So sánh callback trước/sau nếu thầy muốn tăng 5:

Trước (code thực hành vừa thêm):

```kotlin
Button(onClick = { soDem++ }) { Text("Tăng 1") }
```

Sau:

```kotlin
Button(onClick = { soDem += 5 }) { Text("Tăng 5") }
```

Kết quả: mỗi lần nhấn tăng 5. Không sửa callback của thẻ danh sách: thẻ dùng để mở demo, nút bên trong demo dùng để tăng số.

### F5. Mở đúng màn hình chi tiết

Mở [ComponentDetailScreen.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentDetailScreen.kt#L61), trong when(componentId), chèn nhánh trước Checkbox tại dòng 61.

Trước:

```kotlin
                "checkbox" -> CheckboxExample()
```

Sau:

```kotlin
                "counter" -> CounterExample()
                "checkbox" -> CheckboxExample()
```

Kết quả: nhấn thẻ có id counter gọi CounterExample. Import examples.* đã có. Tiêu đề tự lấy component.name vì không cần tên đặc biệt trong when(tieuDe). Nút Back/header và cuộn được dùng lại từ màn hình chi tiết.

Luồng đầy đủ: ComponentItem.onClick → onComponentClick(id) → ComponentsApp gán componentId và manHinh → Detail chọn CounterExample. Nếu chỉ thêm data mà không thêm nhánh, chưa có ví dụ để xem.

### F6. Thêm màu và font cho ví dụ

Sửa **code đã thêm F3** trong CounterExample.

Trước:

```kotlin
Text("Số đếm: $soDem")
```

Sau:

```kotlin
Text(
    "Số đếm: $soDem",
    color = com.example.tuan03project.bai01uicomponents.TitleBlue,
    fontSize = 24.sp,
    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
)
```

Thêm import androidx.compose.ui.unit.sp vào ActionExamples.kt. Kết quả: số đếm lớn, xanh và đậm. Nếu dùng màu này nhiều nơi, thêm một val màu rõ tên đầu ComponentsApp.kt rồi import, không tạo theme mới chỉ cho một Text.

### F7. Kiểm tra sau khi sửa

1. Build bằng lệnh dưới; sửa lỗi import/id/hàm trước khi chạy.
2. Mở bài mới → I'm ready → cuộn Actions → Counter example.
3. Nhấn Tăng 1/Tăng 5, kiểm tra số đổi; nhấn Đặt lại thấy 0.
4. Back UI và Back hệ thống đều về danh sách đúng vị trí cuộn.
5. Thử màn hình nhỏ, không che thanh hệ thống.
6. Mở lại bài cũ, nhấn Say Hi! để bảo đảm không sửa nhầm.
7. Thêm dòng Counter example vào DANH_SACH_UI_COMPONENTS.md, cập nhật tổng đếm và tài liệu nếu giữ thay đổi.

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug
```

## G. Khi bị lỗi sau khi chỉnh

- Unresolved reference Color/FontWeight/sp: thiếu import đúng package.
- Unresolved reference drawable: tên ảnh không đúng hoặc chưa đặt trong app/src/main/res/drawable.
- Row đổi thành Column bị lỗi horizontalArrangement: đổi thành verticalArrangement, kiểm tra horizontalAlignment/verticalAlignment đúng loại layout.
- weight không được nhận: Modifier.weight chỉ có trong RowScope/ColumnScope phù hợp; không gọi tùy ý ngoài layout.
- List bị crash do chiều cao vô hạn: đặt height hữu hạn cho LazyColumn/LazyVerticalGrid con bên trong màn hình cuộn.
- Không nhập được TextField: value lấy state, onValueChange phải gán state mới.
- Nút bấm mà không đổi chữ: Text phải đọc biến state; var thông thường không làm Compose tự cập nhật.
- Màu mới không hiện: kiểm tra đang sửa màu của đúng màn hình, có SpanStyle hoặc colors của component ghi đè không.
- Sai màn hình khi nhấn: đối chiếu id trong ComponentsData, componentId trong ComponentsApp và nhánh when detail.

Không “sửa lỗi” bằng cách xóa MainScreen hoặc nâng toàn bộ dependency. Hoàn tác riêng đoạn vừa thay rồi so với bản source đang chạy được.
