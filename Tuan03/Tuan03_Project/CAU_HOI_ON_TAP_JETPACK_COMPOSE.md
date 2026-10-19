# Câu hỏi ôn tập Jetpack Compose

68 câu gắn với source Tuan03_Project. Đoạn trích và số dòng lấy từ bản cuối ngày 19/10/2026. Phần “Nói trước thầy” là cách diễn đạt ngắn bằng lời, không cần học thuộc thuật ngữ dài.


## Kotlin cơ bản

### Câu 1: Khi nào dùng val, khi nào dùng var?

**Trả lời:** val không gán lại tên biến; var được gán giá trị mới. Dữ liệu thay đổi khi người dùng thao tác cần var state. Màu cố định dùng val.

**Ví dụ trong bài:** [ComponentsApp.kt, dòng 12](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsApp.kt#L12).

```kotlin
val TitleBlue = Color(0xFF2196D2)
```

**Nói trước thầy:** “Em dùng val cho màu chung, var cho trạng thái màn hình.”

### Câu 2: String, Boolean, Int, Float trong bài lưu gì?

**Trả lời:** String lưu chữ; Boolean lưu đúng/sai; Int đếm lần nhấn; Float lưu giá trị kéo có phần lẻ. Hậu tố f tạo Float.

**Ví dụ trong bài:** [examples/SelectionExamples.kt, dòng 56](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L56).

```kotlin
    var giaTriSlider by rememberSaveable { mutableFloatStateOf(50f) }
```

**Nói trước thầy:** “Slider cần Float vì có thể kéo đến giá trị lẻ; Text hiển thị toInt cho dễ đọc.”

### Câu 3: Dấu by ở state có nghĩa gì?

**Trả lời:** by ủy quyền thao tác đọc/ghi cho đối tượng State. Nhờ getValue/setValue của Compose, có thể dùng daChon như Boolean thay vì viết daChon.value.

**Ví dụ trong bài:** [examples/SelectionExamples.kt, dòng 22](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L22).

```kotlin
    var daChon by rememberSaveable { mutableStateOf(false) }
```

**Nói trước thầy:** “Em đọc/ghi biến trực tiếp, Compose vẫn theo dõi thay đổi.”

### Câu 4: it là gì?

**Trả lời:** it là tên mặc định của tham số duy nhất trong lambda. Với onCheckedChange, it là trạng thái Boolean mới; có thể đổi thành tên rõ hơn.

**Ví dụ trong bài:** [examples/SelectionExamples.kt, dòng 24](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L24).

```kotlin
        Checkbox(checked = daChon, onCheckedChange = { daChon = it })
```

**Nói trước thầy:** “it ở đây là giá trị chọn mới do Checkbox gửi về.”

### Câu 5: () -> Unit là gì?

**Trả lời:** Đây là kiểu hàm không nhận tham số và không trả về dữ liệu có ý nghĩa. Dùng cho callback như onReady, onBack hoặc onClick.

**Ví dụ trong bài:** [IntroScreen.kt, dòng 22](app/src/main/java/com/example/tuan03project/bai01uicomponents/IntroScreen.kt#L22).

```kotlin
fun IntroScreen(onReady: () -> Unit) {
```

**Nói trước thầy:** “Màn hình Intro nhận một hành động từ cha; nhấn nút sẽ gọi hành động đó.”

### Câu 6: $ trong chuỗi dùng làm gì?

**Trả lời:** Đưa giá trị biến hoặc biểu thức vào nội dung String. Biểu thức phức tạp đặt trong ${...}.

**Ví dụ trong bài:** [examples/ActionExamples.kt, dòng 29](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/ActionExamples.kt#L29).

```kotlin
        Text("Đã nhấn $soLanNhan lần")
```

**Nói trước thầy:** “Mỗi lần đếm thay đổi, Text tạo nội dung có số mới.”

### Câu 7: listOf và data class có vai trò gì?

**Trả lời:** listOf tạo dữ liệu danh sách. data class gom các thuộc tính liên quan của một mục để truyền cho giao diện dễ hơn.

**Ví dụ trong bài:** [ComponentsData.kt, dòng 4](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsData.kt#L4).

```kotlin
data class ComponentInfo(
    val id: String,
    val name: String,
    val description: String,
    val group: String
)
```

**Nói trước thầy:** “Mỗi ComponentInfo có khóa, tên, mô tả và nhóm; giao diện đọc dữ liệu này.”

### Câu 8: when khác if thế nào trong bài?

**Trả lời:** if kiểm tra một điều kiện; when tiện khi chọn nhiều trường hợp. Điều hướng dùng when với các chuỗi intro/list/detail.

**Ví dụ trong bài:** [ComponentsApp.kt, dòng 24](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsApp.kt#L24).

```kotlin
        when (manHinh) {
            "detail" -> manHinh = "list"
            "list" -> manHinh = "intro"
            else -> onExit()
        }
    }
```

**Nói trước thầy:** “Em dùng when để thấy rõ mỗi trạng thái tương ứng màn hình nào.”

### Câu 9: repeat và for khác nhau thế nào?

**Trả lời:** repeat(n) lặp n lần; for duyệt từng phần tử dữ liệu. RowExample lặp bốn hàng rồi duyệt ba màu cho từng hàng.

**Ví dụ trong bài:** [examples/LayoutExamples.kt, dòng 20](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L20).

```kotlin
        repeat(4) {
            Row(
                Modifier.fillMaxWidth().background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)).padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (mau in listOf(LightTileBlue, DarkTileBlue, LightTileBlue)) {
                    Box(Modifier.weight(1f).height(64.dp).background(mau, RoundedCornerShape(12.dp)))
```

**Nói trước thầy:** “Em tạo bốn Row bằng repeat và ba ô bằng for theo danh sách màu.”

### Câu 10: Long? và null nghĩa là gì?

**Trả lời:** Dấu ? cho phép giá trị không có dữ liệu. Ngày chưa chọn là null nên cần kiểm tra trước khi định dạng.

**Ví dụ trong bài:** [examples/SelectionExamples.kt, dòng 113](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L113).

```kotlin
fun formatDate(millis: Long?): String {
    if (millis == null) return "Chưa chọn ngày"
    val dinhDang = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    dinhDang.timeZone = TimeZone.getTimeZone("UTC")
```

**Nói trước thầy:** “Chưa chọn ngày thì em hiện lời nhắc, không cố chuyển null thành ngày.”


## Compose cơ bản

### Câu 11: @Composable là gì?

**Trả lời:** Đánh dấu hàm có thể tham gia mô tả giao diện Compose. Hàm được gọi trong setContent hoặc trong composable khác.

**Ví dụ trong bài:** [IntroScreen.kt, dòng 21](app/src/main/java/com/example/tuan03project/bai01uicomponents/IntroScreen.kt#L21).

```kotlin
@Composable
fun IntroScreen(onReady: () -> Unit) {
```

**Nói trước thầy:** “Đây là hàm mô tả UI; em không gọi nó như hàm xử lý dữ liệu ở bất kỳ chỗ nào.”

### Câu 12: ComponentActivity và onCreate để làm gì?

**Trả lời:** ComponentActivity cung cấp vòng đời Android. onCreate là điểm khởi tạo Activity, gọi super rồi thiết lập UI.

**Ví dụ trong bài:** [MainActivity.kt, dòng 10](app/src/main/java/com/example/tuan03project/MainActivity.kt#L10).

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lightSystemBars = SystemBarStyle.light(
```

**Nói trước thầy:** “Android tạo MainActivity; onCreate đặt theme và cây Compose.”

### Câu 13: setContent để làm gì?

**Trả lời:** Đặt nội dung Compose của Activity. Các composable bên trong tạo cây UI theo state.

**Ví dụ trong bài:** [MainActivity.kt, dòng 21](app/src/main/java/com/example/tuan03project/MainActivity.kt#L21).

```kotlin
        setContent {
            Tuan03ProjectTheme {
                CourseworkApp()
            }
        }
```

**Nói trước thầy:** “Em dùng setContent để dựng Compose thay cho setContentView XML.”

### Câu 14: Tại sao chia nhiều file?

**Trả lời:** Để tìm đúng trách nhiệm: Activity khởi tạo, Data chứa mục, List hiển thị, Detail chọn ví dụ, examples chứa demo theo nhóm. Không chia mỗi Text thành một file.

**Ví dụ trong bài:** [ComponentDetailScreen.kt, dòng 56](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentDetailScreen.kt#L56).

```kotlin
                "textfield" -> TextFieldExample()
```

**Nói trước thầy:** “Khi sửa ô nhập, em mở InputExamples chứ không tìm trong MainActivity.”

### Câu 15: Composable là mô tả UI hay vẽ lại toàn Activity?

**Trả lời:** Composable mô tả UI theo dữ liệu hiện tại. Khi state đổi, Compose cập nhật các vùng cần thiết; không restart Activity.

**Ví dụ trong bài:** [examples/SelectionExamples.kt, dòng 25](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L25).

```kotlin
        Text(if (daChon) "Đã chọn" else "Chưa chọn")
```

**Nói trước thầy:** “Text đọc state nên đổi nội dung khi state đổi.”


## UI Components

### Câu 16: Text và BasicText khác nhau thế nào?

**Trả lời:** Text của Material lấy các kiểu mặc định theo theme. BasicText của Foundation là nền tảng hiển thị chữ, thích hợp học thành phần thấp hơn.

**Ví dụ trong bài:** [examples/DisplayExamples.kt, dòng 74](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/DisplayExamples.kt#L74).

```kotlin
        "basictext" -> BasicText("BasicText: văn bản Foundation, không áp dụng kiểu chữ Material tự động.")
```

**Nói trước thầy:** “Em dùng Text cho giao diện chính và BasicText trong màn hình minh họa.”

### Câu 17: PasswordField có phải API Compose riêng không?

**Trả lời:** Trong bài, PasswordField là tên chức năng. API thật là OutlinedTextField kết hợp PasswordVisualTransformation.

**Ví dụ trong bài:** [examples/InputExamples.kt, dòng 47](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/InputExamples.kt#L47).

```kotlin
        visualTransformation = if (hienMatKhau) VisualTransformation.None else PasswordVisualTransformation(),
```

**Nói trước thầy:** “Không có composable PasswordField do em tự giả định; em ghép ô nhập và cơ chế che chữ.”

### Câu 18: TextField đầu danh sách sao dùng OutlinedTextField?

**Trả lời:** Ảnh mẫu có viền và nền trắng nên dùng OutlinedTextField. Mục Filled TextField riêng dùng TextField để học đúng biến thể nền.

**Ví dụ trong bài:** [examples/InputExamples.kt, dòng 23](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/InputExamples.kt#L23).

```kotlin
        OutlinedTextField(
            value = noiDungNhap,
            onValueChange = { noiDungNhap = it },
            placeholder = { Text("Thông tin nhập", fontSize = 14.sp) },
            modifier = Modifier.fillMaxWidth(),
```

**Nói trước thầy:** “Tên mục theo slide, API em chọn để tái hiện viền trong ảnh.”

### Câu 19: Card và Surface khác nhau thế nào?

**Trả lời:** Surface là bề mặt có màu, hình dạng, độ nổi. Card tổ chức nội dung liên quan theo kiểu thẻ Material, có biến thể elevated/outlined.

**Ví dụ trong bài:** [examples/ContentExamples.kt, dòng 23](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/ContentExamples.kt#L23).

```kotlin
            "card" -> Card(colors = CardDefaults.cardColors(containerColor = ItemBlue)) {
                Text("Card · Bài học Compose", Modifier.padding(24.dp))
            }
```

**Nói trước thầy:** “Em dùng Card để gom nội dung một bài học; Surface cũng dùng cho nền và dialog.”

### Câu 20: Slot trong Material component là gì?

**Trả lời:** Là tham số nhận composable để đặt nội dung ở vị trí do component quy định, như title, actions, leadingContent, trailingContent.

**Ví dụ trong bài:** [examples/ContentExamples.kt, dòng 30](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/ContentExamples.kt#L30).

```kotlin
            "listitem" -> ListItem(
                headlineContent = { Text("Kotlin") }, supportingContent = { Text("Ngôn ngữ dùng trong bài") },
                leadingContent = { Text("K") }, trailingContent = { Text("01") })
```

**Nói trước thầy:** “Em đưa Text vào từng slot, Material lo cách sắp xếp.”


## Layout

### Câu 21: Column khác Row thế nào?

**Trả lời:** Column xếp con dọc; Row xếp con ngang. Dùng verticalArrangement cho Column và horizontalArrangement cho Row.

**Ví dụ trong bài:** [examples/LayoutExamples.kt, dòng 18](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L18).

```kotlin
fun RowExample() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        repeat(4) {
            Row(
                Modifier.fillMaxWidth().background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)).padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (mau in listOf(LightTileBlue, DarkTileBlue, LightTileBlue)) {
```

**Nói trước thầy:** “Bốn hàng đặt trong Column; ba ô mỗi hàng đặt trong Row.”

### Câu 22: Box dùng làm gì?

**Trả lời:** Đặt các con trong cùng vùng, có thể chồng nhau và căn từng con. Con vẽ sau nằm phía trên.

**Ví dụ trong bài:** [examples/LayoutExamples.kt, dòng 47](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L47).

```kotlin
fun BoxExample() {
    Box(Modifier.size(240.dp), contentAlignment = Alignment.Center) {
        Box(Modifier.offset(x = (-55).dp, y = (-55).dp).size(90.dp)
            .background(Color(0xFFFFB5A2), RoundedCornerShape(10.dp)))
        Box(Modifier.offset(x = 55.dp, y = 55.dp).size(90.dp)
            .background(Color(0xFFFFB5A2), RoundedCornerShape(10.dp)))
        Box(Modifier.size(120.dp).background(Color(0xFFFF653B), RoundedCornerShape(10.dp)))
    }
}
```

**Nói trước thầy:** “Em dùng Box cho ba ô cam chồng một phần, dùng offset để lộ các ô.”

### Câu 23: Spacer làm gì?

**Trả lời:** Tạo vùng trống trong layout. Height cho khoảng trống dọc, width cho khoảng trống ngang.

**Ví dụ trong bài:** [IntroScreen.kt, dòng 37](app/src/main/java/com/example/tuan03project/bai01uicomponents/IntroScreen.kt#L37).

```kotlin
            Spacer(Modifier.height(80.dp))
```

**Nói trước thầy:** “Khoảng giữa logo và title là Spacer 80.dp.”

### Câu 24: Arrangement và Alignment khác nhau thế nào?

**Trả lời:** Arrangement phân bố con theo trục chính, như khoảng cách ngang của Row. Alignment căn theo trục còn lại hoặc vị trí trong Box.

**Ví dụ trong bài:** [examples/LayoutExamples.kt, dòng 110](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L110).

```kotlin
                    horizontalArrangement = if (gianDeu) Arrangement.SpaceBetween else Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically) {
```

**Nói trước thầy:** “Row này phân bố ngang bằng Arrangement, căn giữa chiều cao bằng verticalAlignment.”

### Câu 25: weight(1f) để làm gì?

**Trả lời:** Phân chia phần chiều rộng/chiều cao còn lại giữa các con theo tỉ lệ. Trong Row, ba ô cùng weight(1f) sẽ rộng bằng nhau.

**Ví dụ trong bài:** [examples/LayoutExamples.kt, dòng 26](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L26).

```kotlin
                    Box(Modifier.weight(1f).height(64.dp).background(mau, RoundedCornerShape(12.dp)))
```

**Nói trước thầy:** “Em không đặt ba ô rộng pixel cố định; weight giúp phù hợp máy nhỏ.”

### Câu 26: FlowRow khác LazyRow thế nào?

**Trả lời:** FlowRow xuống dòng khi thiếu ngang. LazyRow giữ một hàng và cuộn ngang; nó tạo các mục cần hiển thị theo cơ chế lazy.

**Ví dụ trong bài:** [examples/LayoutExamples.kt, dòng 88](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L88).

```kotlin
        "flowrow" -> FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(9) { NumberTile(it + 1) }
        }
```

**Nói trước thầy:** “Nhiều nhãn cần xuống dòng thì FlowRow; một dải ô kéo ngang thì LazyRow.”

### Câu 27: BoxWithConstraints đọc kích thước nào?

**Trả lời:** Đọc constraints do cha cấp, như maxWidth; không phải kích thước vật lý hoặc số pixel thiết bị.

**Ví dụ trong bài:** [examples/LayoutExamples.kt, dòng 97](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L97).

```kotlin
        "constraints" -> BoxWithConstraints(Modifier.fillMaxWidth()) {
            val chieuRong = maxWidth
            Column {
                Text("Chiều rộng khả dụng: $chieuRong")
                if (chieuRong < 360.dp) ColumnExample() else RowExample()
```

**Nói trước thầy:** “Em chọn bố cục dựa chiều rộng khả dụng của vùng nội dung.”


## Modifier

### Câu 28: Modifier là gì?

**Trả lời:** Chuỗi cấu hình bố trí, vẽ, tương tác và ngữ nghĩa của một composable. Thứ tự các hàm có thể thay đổi kết quả.

**Ví dụ trong bài:** [ComponentsListScreen.kt, dòng 39](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsListScreen.kt#L39).

```kotlin
        Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
            .background(ItemBlue).clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 12.dp)
```

**Nói trước thầy:** “Thẻ này chiếm ngang, cắt góc, vẽ nền, nhận click rồi đệm chữ.”

### Câu 29: fillMaxSize khác fillMaxWidth thế nào?

**Trả lời:** fillMaxSize lấy cả rộng và cao tối đa cha cho phép. fillMaxWidth chỉ lấy chiều ngang.

**Ví dụ trong bài:** [IntroScreen.kt, dòng 24](app/src/main/java/com/example/tuan03project/bai01uicomponents/IntroScreen.kt#L24).

```kotlin
        Modifier.fillMaxSize().padding(horizontal = 24.dp),
```

**Nói trước thầy:** “Màn hình dùng fillMaxSize; nút chỉ cần fillMaxWidth.”

### Câu 30: dp khác sp thế nào?

**Trả lời:** dp là đơn vị kích thước bố cục độc lập mật độ; sp dùng cho chữ và chịu ảnh hưởng cài đặt cỡ chữ của người dùng.

**Ví dụ trong bài:** [IntroScreen.kt, dòng 38](app/src/main/java/com/example/tuan03project/bai01uicomponents/IntroScreen.kt#L38).

```kotlin
            Text("Jetpack Compose", fontSize = 16.sp, fontWeight = FontWeight.Medium)
```

**Nói trước thầy:** “Kích thước nút em dùng dp, chữ dùng sp.”

### Câu 31: padding và margin trong Compose dùng thế nào?

**Trả lời:** Compose thường dùng padding ở vị trí thích hợp trong chuỗi Modifier để tạo khoảng đệm trong hoặc ngoài phần vẽ. Không có tham số margin chung như CSS.

**Ví dụ trong bài:** [examples/LayoutExamples.kt, dòng 121](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L121).

```kotlin
                Box(Modifier.fillMaxWidth().border(2.dp, DarkTileBlue).padding(khoangCach.dp)
                    .background(ItemBlue).height(100.dp), contentAlignment = Alignment.Center) { Text("Nội dung") }
```

**Nói trước thầy:** “Viền nằm ngoài padding, nền xanh nằm bên trong nên em nhìn rõ khoảng đệm.”

### Câu 32: offset có thay đổi chỗ dành cho anh em không?

**Trả lời:** offset dịch con khỏi vị trí được bố trí, không tự làm các phần tử khác chừa thêm vùng bằng phần dịch. Cần khung cha đủ rộng/cao.

**Ví dụ trong bài:** [examples/LayoutExamples.kt, dòng 49](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L49).

```kotlin
        Box(Modifier.offset(x = (-55).dp, y = (-55).dp).size(90.dp)
            .background(Color(0xFFFFB5A2), RoundedCornerShape(10.dp)))
```

**Nói trước thầy:** “Khung Box cha 240.dp giúp các hình dịch vẫn còn nằm trong vùng demo.”


## State

### Câu 33: remember là gì?

**Trả lời:** Giữ một giá trị qua các lần recomposition khi composable còn ở cùng vị trí trong cây. Nó không phải lưu vĩnh viễn.

**Ví dụ trong bài:** [examples/SelectionExamples.kt, dòng 69](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L69).

```kotlin
    var khoang by remember { mutableStateOf(20f..80f) }
```

**Nói trước thầy:** “RangeSlider không trở về 20..80 ở mỗi lần Compose cập nhật.”

### Câu 34: mutableStateOf là gì?

**Trả lời:** Tạo đối tượng chứa giá trị có thể thay đổi và cho Compose theo dõi các lần đọc/ghi. Có thể dùng với Boolean hoặc String trong bài.

**Ví dụ trong bài:** [examples/SelectionExamples.kt, dòng 22](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L22).

```kotlin
    var daChon by rememberSaveable { mutableStateOf(false) }
```

**Nói trước thầy:** “Em dùng mutableStateOf để khi biến chọn đổi, UI biết cần cập nhật.”

### Câu 35: rememberSaveable khác remember ra sao?

**Trả lời:** Ngoài giữ qua recomposition, rememberSaveable lưu/khôi phục dữ liệu phù hợp qua cấu hình hoặc tái tạo theo SavedState. Không thay thế database và không tự giữ mọi demo sau khi rời màn hình.

**Ví dụ trong bài:** [ComponentsApp.kt, dòng 20](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsApp.kt#L20).

```kotlin
    var manHinh by rememberSaveable { mutableStateOf("intro") }
    var componentId by rememberSaveable { mutableStateOf("text") }
    val trangThaiDanhSach = rememberLazyListState()
```

**Nói trước thầy:** “Em lưu tên màn hình bằng rememberSaveable, nhưng dữ liệu demo rời composition có thể khởi tạo lại.”

### Câu 36: Tại sao nhập TextField thì Text cập nhật?

**Trả lời:** Cả value của ô và nội dung Text cùng đọc noiDungNhap. onValueChange cập nhật state, các phần đọc state được recomposition.

**Ví dụ trong bài:** [examples/InputExamples.kt, dòng 25](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/InputExamples.kt#L25).

```kotlin
            onValueChange = { noiDungNhap = it },
```

**Nói trước thầy:** “Em không sao chép dữ liệu sang Text thủ công; cả hai cùng đọc một state.”


## Event handling

### Câu 37: onClick hoạt động thế nào?

**Trả lời:** Nhận một lambda được chạy khi người dùng nhấn. Bên trong callback thay đổi state hoặc gọi hành động cha.

**Ví dụ trong bài:** [IntroScreen.kt, dòng 47](app/src/main/java/com/example/tuan03project/bai01uicomponents/IntroScreen.kt#L47).

```kotlin
            onClick = onReady,
```

**Nói trước thầy:** “Nhấn I'm ready gọi onReady do ComponentsApp truyền xuống.”

### Câu 38: onValueChange có tác dụng gì?

**Trả lời:** Nhận nội dung mới sau thao tác nhập/xóa. Với kiểu value-based trong bài, phải gán nội dung này lại state để giữ giá trị mới.

**Ví dụ trong bài:** [examples/InputExamples.kt, dòng 24](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/InputExamples.kt#L24).

```kotlin
            value = noiDungNhap,
            onValueChange = { noiDungNhap = it },
```

**Nói trước thầy:** “value đưa state ra UI, onValueChange đưa dữ liệu mới về state.”

### Câu 39: Tại sao Checkbox thay đổi khi nhấn?

**Trả lời:** Checkbox gọi onCheckedChange với Boolean mới; callback gán daChon. checked đọc lại state nên dấu chọn đổi.

**Ví dụ trong bài:** [examples/SelectionExamples.kt, dòng 24](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L24).

```kotlin
        Checkbox(checked = daChon, onCheckedChange = { daChon = it })
```

**Nói trước thầy:** “Nhấn đổi state, Compose phản ánh state mới ở dấu tích và dòng chữ.”

### Câu 40: Tại sao Slider kéo là số thay đổi?

**Trả lời:** onValueChange gửi giá trị Float liên tục trong lúc kéo. State được cập nhật và dòng số đọc lại.

**Ví dụ trong bài:** [examples/SelectionExamples.kt, dòng 58](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L58).

```kotlin
        Slider(value = giaTriSlider, onValueChange = { giaTriSlider = it }, valueRange = 0f..100f)
        Text("Giá trị: ${giaTriSlider.toInt()}")
```

**Nói trước thầy:** “Em không chờ thả tay hoặc Submit, số cập nhật trong lúc kéo.”

### Câu 41: Tại sao không đổi state trực tiếp trong thân composable?

**Trả lời:** Thân composable có thể chạy nhiều lần. Gán state vô điều kiện ở đó có thể gây vòng cập nhật; thay đổi có chủ ý nên đặt trong callback.

**Ví dụ trong bài:** [examples/ActionExamples.kt, dòng 14](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/ActionExamples.kt#L14).

```kotlin
    val khiNhan = { soLanNhan++; Unit }
```

**Nói trước thầy:** “Việc tăng số đặt trong khiNhan, được onClick gọi, không tự chạy khi vẽ UI.”


## Navigation

### Câu 42: Ứng dụng có dùng NavHost không?

**Trả lời:** Không. Ba màn hình chính đủ đơn giản để dùng state String và when. Không tạo Activity cho từng component.

**Ví dụ trong bài:** [ComponentsApp.kt, dòng 44](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsApp.kt#L44).

```kotlin
                    "detail" -> ComponentDetailScreen(componentId, onBack = { quayLai() })
```

**Nói trước thầy:** “Em chỉ thay composable đang hiển thị theo manHinh.”

### Câu 43: Back trên màn hình và Back Android liên quan gì?

**Trả lời:** Cả hai gọi quayLai, nên detail về list, list về intro, intro thoát ra trang chọn bài. Khi bàn phím mở, Back có thể đóng IME trước.

**Ví dụ trong bài:** [ComponentsApp.kt, dòng 30](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsApp.kt#L30).

```kotlin
    BackHandler { quayLai() }
```

**Nói trước thầy:** “Em gom logic quay lại vào một hàm để hai cách Back nhất quán.”

### Câu 44: Làm sao danh sách giữ vị trí cuộn?

**Trả lời:** LazyListState được tạo ở ComponentsApp, nằm ngoài nhánh list/detail và truyền xuống LazyColumn. Mở detail không xóa state đó.

**Ví dụ trong bài:** [ComponentsApp.kt, dòng 22](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsApp.kt#L22).

```kotlin
    val trangThaiDanhSach = rememberLazyListState()
```

**Nói trước thầy:** “Em Back từ component thì danh sách vẫn ở vùng đã chọn.”

### Câu 45: NavigationBar có tự chuyển màn hình không?

**Trả lời:** Không. UI component cung cấp nút chọn và selected; lập trình viên quyết định nội dung hoặc luồng điều hướng. Demo chỉ đổi chỉ số và chữ.

**Ví dụ trong bài:** [examples/NavigationExamples.kt, dòng 32](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/NavigationExamples.kt#L32).

```kotlin
                    NavigationBarItem(selected = mucDangChon == viTri,
                        onClick = { mucDangChon = viTri },
                        icon = { Text("${viTri + 1}") }, label = { Text(ten) })
```

**Nói trước thầy:** “Em không nhầm NavigationBar với thư viện Navigation Compose.”


## Theme

### Câu 46: Theme đặt màu và font ở đâu?

**Trả lời:** Theme cung cấp mặc định, nhưng từng Text/component có thể ghi đè. Bài cũ giữ Theme.kt; bài mới có lightColorScheme lồng trong ComponentsApp.

**Ví dụ trong bài:** [ComponentsApp.kt, dòng 32](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsApp.kt#L32).

```kotlin
    MaterialTheme(colorScheme = lightColorScheme(
        primary = ButtonBlue, background = Color.White, surface = Color.White,
        onBackground = Color.Black, onSurface = Color.Black
    )) {
```

**Nói trước thầy:** “Em đổi màu mới trong ComponentsApp để không ảnh hưởng bài cũ.”


## Text styling

### Câu 47: Làm sao đổi màu chỉ một từ?

**Trả lời:** Dùng AnnotatedString và withStyle(SpanStyle(...)) quanh phần append của từ đó. Text ngoài vẫn giữ kiểu cho phần còn lại.

**Ví dụ trong bài:** [examples/DisplayExamples.kt, dòng 32](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/DisplayExamples.kt#L32).

```kotlin
        withStyle(SpanStyle(color = Color(0xFFB87818), fontSize = 38.sp)) { append("Brown") }
```

**Nói trước thầy:** “Brown có màu nâu và 38.sp, các từ khác không bị đổi.”

### Câu 48: Gạch chân, gạch ngang, đậm, nghiêng ở đâu?

**Trả lời:** Trong SpanStyle: Underline, LineThrough, FontWeight.Bold và FontStyle.Italic. Cũng có thể đặt trên toàn Text.

**Ví dụ trong bài:** [examples/DisplayExamples.kt, dòng 30](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/DisplayExamples.kt#L30).

```kotlin
        withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) { append("quick") }
```

**Nói trước thầy:** “quick bị gạch ngang; các SpanStyle khác dùng tương tự cho over, the và lazy.”

### Câu 49: TextOverflow.Ellipsis có tác dụng gì?

**Trả lời:** Hiện dấu ba chấm khi văn bản vượt số dòng/khung cho phép. Cần giới hạn maxLines hoặc chiều rộng để thấy tác dụng.

**Ví dụ trong bài:** [examples/DisplayExamples.kt, dòng 84](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/DisplayExamples.kt#L84).

```kotlin
            Text("Văn bản rất dài để thấy dấu ba chấm khi nội dung vượt chiều rộng.",
                Modifier.width(180.dp), maxLines = 1, overflow = TextOverflow.Ellipsis)
```

**Nói trước thầy:** “Ví dụ dùng width 180.dp và maxLines 1, nên chữ dài kết thúc bằng ba chấm.”


## Image resources

### Câu 50: painterResource đọc ảnh ở đâu?

**Trả lời:** Đọc tài nguyên đã đóng gói trong res/drawable qua R.drawable. Ảnh hiển thị offline, không cần Coil hoặc quyền Internet.

**Ví dụ trong bài:** [examples/DisplayExamples.kt, dòng 52](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/DisplayExamples.kt#L52).

```kotlin
            painterResource(R.drawable.uth_campus_one), "Mặt trước trường UTH",
            Modifier.fillMaxWidth().aspectRatio(1.78f).clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
```

**Nói trước thầy:** “Em thay file drawable và tên resource trong Image để đổi ảnh.”

### Câu 51: Crop, Fit và clip khác nhau thế nào?

**Trả lời:** Crop phủ đầy khung và có thể cắt rìa; Fit giữ toàn ảnh nhưng có khoảng trống. clip cắt hình theo đường bao như góc tròn.

**Ví dụ trong bài:** [examples/DisplayExamples.kt, dòng 54](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/DisplayExamples.kt#L54).

```kotlin
            contentScale = ContentScale.Crop
```

**Nói trước thầy:** “Crop điều khiển ảnh nằm trong khung, clip điều khiển hình khung nhìn thấy.”

### Câu 52: contentDescription dùng làm gì?

**Trả lời:** Cung cấp mô tả ý nghĩa cho trợ năng. Ảnh thông tin cần mô tả phù hợp; icon hành động cần mô tả thao tác.

**Ví dụ trong bài:** [IntroScreen.kt, dòng 34](app/src/main/java/com/example/tuan03project/bai01uicomponents/IntroScreen.kt#L34).

```kotlin
                contentDescription = "Logo Jetpack Compose",
```

**Nói trước thầy:** “Người dùng trình đọc màn hình vẫn hiểu đó là logo Compose.”


## Lists

### Câu 53: LazyColumn khác Column cuộn thế nào?

**Trả lời:** Column cùng verticalScroll thích hợp nội dung ngắn/đa dạng. LazyColumn tạo các item cần hiện cho danh sách dài, phù hợp 79 mục.

**Ví dụ trong bài:** [ComponentsListScreen.kt, dòng 19](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsListScreen.kt#L19).

```kotlin
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(start = 14.dp, end = 14.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
```

**Nói trước thầy:** “Danh sách nhiều component dùng lazy; chi tiết ngắn dùng Column cuộn.”

### Câu 54: key trong items để làm gì?

**Trả lời:** Giúp Compose nhận diện item ổn định qua thay đổi vị trí/dữ liệu. Khóa phải duy nhất.

**Ví dụ trong bài:** [ComponentsListScreen.kt, dòng 28](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsListScreen.kt#L28).

```kotlin
                items(components.filter { it.group == nhom }, key = { it.id }) { component ->
```

**Nói trước thầy:** “Em dùng component.id làm key, không dùng tên có thể trùng.”

### Câu 55: LazyVerticalGrid và LazyHorizontalGrid khác nhau thế nào?

**Trả lời:** Lưới dọc Fixed(3) có ba cột và cuộn dọc. Lưới ngang Fixed(2) có hai hàng và cuộn ngang.

**Ví dụ trong bài:** [examples/LayoutExamples.kt, dòng 80](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L80).

```kotlin
        "vgrid" -> LazyVerticalGrid(GridCells.Fixed(3), Modifier.height(340.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(36) { NumberTile(it + 1) }
        }
```

**Nói trước thầy:** “Fixed mang ý nghĩa số cột ở lưới dọc, số hàng ở lưới ngang.”


## Selection controls

### Câu 56: RadioButton làm sao chỉ chọn một?

**Trả lời:** Các nút so sánh với cùng một biến luaChon. Nhấn hàng gán String mới; biến chỉ có một giá trị tại một thời điểm.

**Ví dụ trong bài:** [examples/SelectionExamples.kt, dòng 35](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L35).

```kotlin
                selected = luaChon == ten, onClick = { luaChon = ten }, role = Role.RadioButton
```

**Nói trước thầy:** “Không dùng ba Boolean độc lập vì sẽ chọn được nhiều mục.”

### Câu 57: Switch khác Checkbox thế nào?

**Trả lời:** Switch thường bật/tắt một cài đặt. Checkbox chọn/bỏ một mục, có thể nằm trong tập nhiều lựa chọn. Cả hai nhận checked/onCheckedChange.

**Ví dụ trong bài:** [examples/SelectionExamples.kt, dòng 49](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L49).

```kotlin
        Switch(checked = daBat, onCheckedChange = { daBat = it })
```

**Nói trước thầy:** “Demo Switch hiển thị ON/OFF, Checkbox hiển thị đã chọn/chưa chọn.”

### Câu 58: RangeSlider khác Slider thế nào?

**Trả lời:** Slider chọn một giá trị. RangeSlider giữ khoảng bắt đầu/kết thúc, có hai tay kéo.

**Ví dụ trong bài:** [examples/SelectionExamples.kt, dòng 73](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L73).

```kotlin
                RangeSlider(value = khoang, onValueChange = { khoang = it }, valueRange = 0f..100f)
```

**Nói trước thầy:** “Em dùng khoang.start và endInclusive để in hai đầu khoảng.”

### Câu 59: DatePicker và DatePickerDialog có phải một thứ không?

**Trả lời:** DatePicker là nội dung lịch. DatePickerDialog là container dialog nhận nội dung lịch và nút OK/Hủy. State giữ ngày được chọn.

**Ví dụ trong bài:** [examples/SelectionExamples.kt, dòng 133](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L133).

```kotlin
        if (moHopThoai) DatePickerDialog(
            onDismissRequest = { moHopThoai = false },
            confirmButton = {
                TextButton(enabled = ngay.selectedDateMillis != null, onClick = {
                    ngayXacNhan = ngay.selectedDateMillis
```

**Nói trước thầy:** “Em có thể đặt lịch trực tiếp hoặc đặt trong dialog; ngày xác nhận chỉ đổi khi OK.”


## Input controls

### Câu 60: Che password có mã hóa dữ liệu không?

**Trả lời:** Không. VisualTransformation chỉ che cách hiển thị; String matKhau vẫn là dữ liệu nhập trong bộ nhớ. Demo không lưu hoặc gửi ra ngoài.

**Ví dụ trong bài:** [examples/InputExamples.kt, dòng 41](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/InputExamples.kt#L41).

```kotlin
    var matKhau by rememberSaveable { mutableStateOf("") }
```

**Nói trước thầy:** “Che ký tự giúp nhìn kín đáo, không phải chức năng mã hóa.”

### Câu 61: KeyboardOptions có xác thực dữ liệu không?

**Trả lời:** Không, nó yêu cầu kiểu bàn phím/IME. Dữ liệu vẫn cần kiểm tra riêng, nhất là khi nhập từ bàn phím vật lý hoặc dán.

**Ví dụ trong bài:** [examples/InputExamples.kt, dòng 79](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/InputExamples.kt#L79).

```kotlin
                label = { Text("Số lượng") }, keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth())
```

**Nói trước thầy:** “Bàn phím số là gợi ý nhập; em không gọi đó là kiểm tra số hợp lệ.”

### Câu 62: Input validation trong bài kiểm tra đến đâu?

**Trả lời:** Chỉ kiểm tra chuỗi không rỗng và có @, dấu chấm. isError hiển thị lỗi, không kiểm tra tài khoản/email thật.

**Ví dụ trong bài:** [examples/InputExamples.kt, dòng 83](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/InputExamples.kt#L83).

```kotlin
                val coLoi = noiDung.isNotEmpty() && (!noiDung.contains("@") || !noiDung.contains("."))
```

**Nói trước thầy:** “Em nói rõ đây là validation cơ bản để học UI.”


## Feedback

### Câu 63: Snackbar cần coroutine vì sao?

**Trả lời:** showSnackbar là suspend function, có thể chờ vòng đời thông báo. rememberCoroutineScope gắn tác vụ với composable; không chặn UI.

**Ví dụ trong bài:** [examples/FeedbackExamples.kt, dòng 28](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/FeedbackExamples.kt#L28).

```kotlin
                    phamVi.launch { snackbar.showSnackbar("Đã nhấn nút", actionLabel = "Đóng") }
```

**Nói trước thầy:** “Em chỉ dùng coroutine tại API bắt buộc, không thêm tầng xử lý nền.”

### Câu 64: Loading demo có tải dữ liệu thật không?

**Trả lời:** Không. Button đảo Boolean để học cách hiển thị vòng quay và trạng thái sẵn sàng. Tiến trình xác định có slider riêng.

**Ví dụ trong bài:** [examples/FeedbackExamples.kt, dòng 45](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/FeedbackExamples.kt#L45).

```kotlin
                if (dangTai) {
                    CircularProgressIndicator()
                    Text("Đang tải · trạng thái minh họa")
                } else Text("Sẵn sàng")
```

**Nói trước thầy:** “Em minh họa trạng thái loading, không giả có server.”


## Build và kiểm tra

### Câu 65: assembleDebug khác testDebugUnitTest thế nào?

**Trả lời:** assembleDebug biên dịch/đóng gói APK debug. testDebugUnitTest chạy unit test JVM nếu có. Không có test thì NO-SOURCE không chứng minh UI đã đúng.

**Ví dụ trong bài:** [MainActivity.kt, dòng 23](app/src/main/java/com/example/tuan03project/MainActivity.kt#L23).

```kotlin
                CourseworkApp()
```

**Nói trước thầy:** “Build xác nhận source/resource đóng gói được; thao tác emulator mới kiểm tra tương tác.”

### Câu 66: Tại sao không nâng dependency khi thêm mỗi component?

**Trả lời:** Dự án đã có API cần dùng. Giữ BOM và kiểm tra compile giúp tránh thay đổi ngoài phạm vi; API web mới có thể không giống bản đang cài.

**Ví dụ trong bài:** [examples/FeedbackExamples.kt, dòng 35](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/FeedbackExamples.kt#L35).

```kotlin
                    positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
```

**Nói trước thầy:** “Em dùng API phù hợp Material 3 1.4.0 được Gradle xác nhận.”

### Câu 67: Làm sao bảo vệ bài cũ?

**Trả lời:** Giữ MainScreen nguyên file và chỉ thay lời gọi Activity sang trang chọn bài. Không đổi package/project/launcher hoặc chạm Tuần 1, Tuần 2.

**Ví dụ trong bài:** [CourseworkApp.kt, dòng 20](app/src/main/java/com/example/tuan03project/CourseworkApp.kt#L20).

```kotlin
        "cu" -> MainScreen()
```

**Nói trước thầy:** “Bài cũ có lối mở riêng và vẫn chạy cùng Activity.”

### Câu 68: Preview có thay thế kiểm thử thiết bị không?

**Trả lời:** Không. Preview giúp xem hình ở 390×844 hoặc 320×640, còn bàn phím, Back, dialog và cử chỉ cần thử trên thiết bị.

**Ví dụ trong bài:** [IntroScreen.kt, dòng 59](app/src/main/java/com/example/tuan03project/bai01uicomponents/IntroScreen.kt#L59).

```kotlin
@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
private fun IntroSmallPreview() { MaterialTheme { IntroScreen {} } }
```

**Nói trước thầy:** “Em dùng Preview cho bố cục nhỏ và emulator cho tương tác.”

## Cách tự ôn

1. Mở đúng file ở liên kết và tìm biến/hàm được hỏi.
2. Nói chức năng bằng một câu, chỉ vào tham số quyết định hành vi.
3. Sửa thử một giá trị nhỏ: padding, màu, text hoặc state ban đầu.
4. Chạy lại để giải thích kết quả nhìn thấy.
5. Với một component mới, chỉ ra ba chỗ: dữ liệu danh sách, nhánh chọn demo, hàm demo.
6. Nếu chưa chạy kiểm tra một trạng thái, nói “chưa kiểm chứng trạng thái đó”, không nói PASS theo suy đoán.

Xem HUONG_DAN_CHINH_SUA_GIAO_DIEN.md để thực hành các câu hỏi đổi màu, đổi bố cục và xử lý sự kiện.
