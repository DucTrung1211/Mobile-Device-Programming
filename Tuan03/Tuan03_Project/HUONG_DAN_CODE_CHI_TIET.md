# Hướng dẫn code chi tiết — Bài 01 UI Components

Tài liệu tiếng Việt dành cho sinh viên đang học Kotlin/Compose. Số dòng được lấy từ các file cuối cùng trong dự án ngày 19/10/2026, không phải số ước lượng. Khi tự thêm/xóa code, số dòng sẽ dịch chuyển: hãy ưu tiên tìm đúng tên hàm và đoạn trích.

## 1. Mở ứng dụng và cấu trúc dự án

Run module app trong Android Studio. Trang chọn bài có hai nút:

- **Tuần 03 · Bài 01 — UI Components**: Intro → I'm ready → danh sách → nhấn component → chi tiết.
- **Tuần 03 — My First App**: mở nguyên MainScreen cũ, nhấn Say Hi! để hiện tên.
- Back ở chi tiết → danh sách; Back ở danh sách → Intro; Back ở Intro → chọn bài.
- Back ở bài cũ → chọn bài. Không thêm nút chọn bài lên những màn hình tham chiếu.

Trước khi làm, Kotlin chỉ có MainActivity.kt, MainScreen.kt và ui/theme/Theme.kt. Một module :app, một launcher. Bài cũ chỉ gồm My First App / Hello / Say Hi!. README cũ nói rememberSaveable nhưng source thực tế là remember; source được giữ nguyên, tài liệu này mô tả đúng source.

Sau khi làm:

```text
com/example/tuan03project/
  MainActivity.kt                  # Activity duy nhất, sửa một lời gọi
  MainScreen.kt                    # nguyên bài cũ
  CourseworkApp.kt                 # chọn bài
  ui/theme/Theme.kt                # theme cũ, giữ nguyên
  bai01uicomponents/
    ComponentsApp.kt               # state điều hướng, màu riêng bài mới
    IntroScreen.kt                 # logo, mô tả, I'm ready
    ComponentsData.kt              # dữ liệu 79 mục
    ComponentsListScreen.kt        # LazyColumn và ComponentItem chung
    ComponentDetailScreen.kt       # header và chọn demo bằng when
    examples/
      DisplayExamples.kt           # chữ, ảnh, icon, surface
      InputExamples.kt             # nhập liệu, mật khẩu, tìm kiếm
      SelectionExamples.kt         # checkbox, radio, switch, slider, chips, ngày giờ
      LayoutExamples.kt            # Row, Column, Box và các danh sách/lưới
      ActionExamples.kt            # tám kiểu nút
      ContentExamples.kt           # card, divider, menu, dialog, sheet, scaffold
      NavigationExamples.kt        # bar, rail, drawer, tabs
      FeedbackExamples.kt          # progress, snackbar, tooltip, loading
```

Ảnh nằm ở app/src/main/res/drawable/compose_logo.png, uth_campus_one.jpg, uth_campus_two.jpg. ic_launcher.xml là vector resource cũ, không phải XML layout. Không có thư mục assets mới; bài không cần assets. Manifest chỉ khai báo MainActivity như cũ. Không có Color.kt và Type.kt: dự án ban đầu không có, không tạo file rỗng cho đủ tên.

Nếu dự án khác có Color.kt thì thường để màu; Type.kt thường chứa Typography. Trong dự án này màu mới đặt đầu ComponentsApp.kt và font đặt trực tiếp tại Text hoặc lấy MaterialTheme.typography.

## 2. Cách đọc Kotlin và import

Dòng package cho biết không gian tên, không phải tên màn hình. Package gốc giữ com.example.tuan03project; bài mới dùng com.example.tuan03project.bai01uicomponents; ví dụ dùng thêm .examples.

- import androidx.compose.foundation...: Image, background, clickable, cuộn, layout, BasicText/BasicTextField.
- import androidx.compose.material3...: Text, Button, Card, DatePicker... theo Material 3.
- import androidx.compose.runtime...: Composable và các hàm quản lý state; getValue/setValue hỗ trợ cú pháp by.
- import androidx.compose.runtime.saveable.rememberSaveable: giữ dữ liệu nhỏ khi Activity được tái tạo.
- import androidx.compose.ui...: Modifier, Alignment, Color, văn bản, đơn vị và resource.
- import ...R: lấy mã tài nguyên do Android tự sinh; không tự tạo class R.
- import java.text/Date/Locale/TimeZone: định dạng ngày trong SelectionExamples.
- import kotlinx.coroutines.launch: chỉ ở Drawer/Snackbar vì các API đó yêu cầu gọi hàm suspend.

Dấu * cuối import là nhập các tên trong package, không sao chép toàn bộ thư viện vào source. Dấu @Composable đánh dấu hàm mô tả UI; chỉ gọi từ ngữ cảnh composable. Hàm formatDate xử lý dữ liệu, không vẽ UI nên không có @Composable.

val là tên không gán lại; var có thể gán lại. String là chữ, Boolean true/false, Int là số nguyên, Float là số thực có hậu tố f. Dấu $ đưa biến vào chuỗi, ${...} đưa biểu thức vào chuỗi. listOf tạo danh sách. when chọn nhánh. for/repeat sinh nhiều thành phần lặp. it là tham số duy nhất của lambda; trong onValueChange, it chính là nội dung mới.

## 3. MainActivity.kt — từ Android đến Compose

[MainActivity.kt, dòng 10](app/src/main/java/com/example/tuan03project/MainActivity.kt#L10):

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lightSystemBars = SystemBarStyle.light(
            android.graphics.Color.TRANSPARENT,
            android.graphics.Color.TRANSPARENT
        )
        enableEdgeToEdge(
            statusBarStyle = lightSystemBars,
            navigationBarStyle = lightSystemBars
        )
        setContent {
            Tuan03ProjectTheme {
                CourseworkApp()
            }
        }
```

Đọc từ trên xuống:

1. MainActivity kế thừa ComponentActivity để có vòng đời Android và setContent của Compose.
2. override fun onCreate ghi đè hàm được Android gọi khi Activity được tạo. savedInstanceState có thể null, vì vậy kiểu Bundle?.
3. super.onCreate gọi xử lý khởi tạo của lớp cha. Không bỏ dòng này.
4. lightSystemBars đặt system bar trong suốt, biểu tượng tối. enableEdgeToEdge đã có từ bài cũ; bài mới dùng safeDrawingPadding để nội dung tránh thanh hệ thống.
5. setContent đặt cây giao diện Compose thay cho setContentView XML.
6. Tuan03ProjectTheme bọc ứng dụng bằng theme hiện tại.
7. Thay đổi duy nhất tại Activity là MainScreen() → CourseworkApp(). Mọi demo nằm ở file riêng.

Không cần Activity cho từng component. Nhấn Button thay state chứ không restart Activity.

## 4. Theme.kt và màu bài mới

[ui/theme/Theme.kt, dòng 8](app/src/main/java/com/example/tuan03project/ui/theme/Theme.kt#L8):

```kotlin
fun Tuan03ProjectTheme(content: @Composable () -> Unit) {
    // Giữ cấu hình theme sáng như giao diện ban đầu
    MaterialTheme(colorScheme = lightColorScheme()) {
        content()
    }
}

```

content là một hàm composable được truyền vào. MaterialTheme cung cấp màu/kiểu chữ/hình dạng mặc định. Theme cũ giữ sáng. Bài mới có MaterialTheme lồng bên trong ComponentsApp; điều này chỉ ảnh hưởng các phần tử con của bài mới.

[ComponentsApp.kt, dòng 12](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsApp.kt#L12):

```kotlin
val TitleBlue = Color(0xFF2196D2)
val ItemBlue = Color(0xFFBBDFFC)
val ButtonBlue = Color(0xFF1598EF)
val LightTileBlue = Color(0xFFA1BEF1)
val DarkTileBlue = Color(0xFF5086EF)
```

TitleBlue là màu tiêu đề, ItemBlue là nền thẻ, ButtonBlue là màu hành động, LightTileBlue/DarkTileBlue là các ô layout. 0xFF là alpha 100%, sáu ký tự sau là RGB. Ví dụ đổi ItemBlue thành Color(0xFFC8E6C9) thì tất cả thẻ danh sách đổi xanh lá vì cùng đọc một biến màu.

Màu chuyên biệt như chữ đỏ của TextField hoặc cam của Box nằm ngay demo để dễ chỉ ra khi học.

## 5. CourseworkApp.kt — giữ bài cũ chạy được

[CourseworkApp.kt, dòng 16](app/src/main/java/com/example/tuan03project/CourseworkApp.kt#L16):

```kotlin
fun CourseworkApp() {
    var baiDangMo by rememberSaveable { mutableStateOf("menu") }
    BackHandler(enabled = baiDangMo == "cu") { baiDangMo = "menu" }
    when (baiDangMo) {
        "cu" -> MainScreen()
        "moi" -> ComponentsApp(onExit = { baiDangMo = "menu" })
        else -> Column(
            modifier = Modifier.fillMaxSize().background(Color.White)
                .safeDrawingPadding().padding(24.dp),
```

baiDangMo kiểu String khởi tạo "menu". Nút bài cũ gán "cu"; nút bài mới gán "moi". when chỉ đặt một cây màn hình vào UI. BackHandler chỉ bật khi bài cũ đang mở; quay lại đổi về "menu".

Ở nhánh "moi", ComponentsApp nhận onExit. Đó là hàm không có tham số, không trả giá trị đáng kể: () -> Unit. ComponentsApp gọi onExit khi Back tại Intro; bố mẹ mới đổi baiDangMo về menu.

MainScreen cũ không bị sửa. Biến daNhanNut của bài cũ dùng remember; khi rời bài và mở lại, nó trở về false. Không khẳng định bài cũ giữ trạng thái qua xoay màn hình.

## 6. ComponentsApp.kt — điều hướng bằng state

[ComponentsApp.kt, dòng 20](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsApp.kt#L20):

```kotlin
    var manHinh by rememberSaveable { mutableStateOf("intro") }
    var componentId by rememberSaveable { mutableStateOf("text") }
    val trangThaiDanhSach = rememberLazyListState()
    fun quayLai() {
        when (manHinh) {
            "detail" -> manHinh = "list"
            "list" -> manHinh = "intro"
            else -> onExit()
        }
    }
    BackHandler { quayLai() }
    // Chỉ áp dụng bảng màu này cho bài mới.
```

- manHinh là String: "intro", "list" hoặc "detail".
- componentId là String khóa của mục, ban đầu "text".
- trangThaiDanhSach là LazyListState; giữ vị trí cuộn khi mở detail rồi Back.
- quayLai là hàm thường nằm trong composable để dùng chung cho nút Back và BackHandler.
- BackHandler đăng ký xử lý Back hệ thống khi bài mới đang ở trong composition.

[ComponentsApp.kt, dòng 39](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsApp.kt#L39):

```kotlin
                    "intro" -> IntroScreen(onReady = { manHinh = "list" })
                    "list" -> ComponentsListScreen(trangThaiDanhSach) {
                        componentId = it
                        manHinh = "detail"
                    }
                    "detail" -> ComponentDetailScreen(componentId, onBack = { quayLai() })
                }
            }
```

onReady gán manHinh = "list". Khi chọn item, **gán componentId trước**, sau đó gán manHinh = "detail". Detail đọc khóa để chọn đúng ví dụ.

Surface vẽ nền trắng; Box bao ngoài dùng fillMaxSize, safeDrawingPadding, imePadding. safeDrawingPadding tránh status/navigation bar thật; imePadding dành thêm vùng khi bàn phím xuất hiện. Không vẽ status bar giả.

rememberSaveable lưu các String nhỏ qua cấu hình, không phải database. Nó không có nghĩa mọi state của mọi demo đều sống sau khi rời màn hình. State của demo nằm bên trong detail; rời detail rồi mở lại thường khởi tạo lại.

## 7. IntroScreen.kt — bố cục theo ảnh

[IntroScreen.kt, dòng 22](app/src/main/java/com/example/tuan03project/bai01uicomponents/IntroScreen.kt#L22):

```kotlin
fun IntroScreen(onReady: () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier.weight(1f).fillMaxWidth().padding(bottom = 44.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painterResource(R.drawable.compose_logo),
                contentDescription = "Logo Jetpack Compose",
                modifier = Modifier.size(200.dp)
            )
            Spacer(Modifier.height(80.dp))
            Text("Jetpack Compose", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(24.dp))
            Text(
                "Jetpack Compose is a modern UI toolkit for building native Android applications using a declarative programming approach.",
                Modifier.fillMaxWidth(), fontSize = 14.sp, lineHeight = 19.sp,
                textAlign = TextAlign.Center
            )
        }
```

Column ngoài xếp nội dung và Button theo chiều dọc. Column trong có weight(1f), nhận phần chiều cao còn lại. verticalScroll cho vùng logo/mô tả cuộn nếu màn hình thấp. Arrangement.Center căn cả nhóm theo chiều dọc; Alignment.CenterHorizontally căn giữa theo chiều ngang.

Vùng nội dung có padding đáy 44.dp để cân lại vị trí nhóm logo/chữ; logo gốc có khoảng trắng quanh hình nên khung Image lớn hơn phần logo nhìn thấy. Image đọc compose_logo từ drawable, contentDescription mô tả cho trình đọc màn hình. size(200.dp) quy định vùng ảnh; logo gốc được giữ tỉ lệ. Spacer 80.dp ngăn logo và tiêu đề. Text mô tả có textAlign Center, lineHeight 19.sp.

[IntroScreen.kt, dòng 46](app/src/main/java/com/example/tuan03project/bai01uicomponents/IntroScreen.kt#L46):

```kotlin
        Button(
            onClick = onReady,
            modifier = Modifier.fillMaxWidth().padding(bottom = 30.dp).height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue, contentColor = Color.White)
        ) { Text("I'm ready", fontSize = 16.sp) }
    }
}

```

onClick = onReady truyền sự kiện lên ComponentsApp. Không mở web, Toast hay dialog. fillMaxWidth làm nút gần hết vùng nội dung sau lề 24.dp. padding bottom 30.dp chừa khoảng cách đáy; height 50.dp là chiều cao nút; RoundedCornerShape 25.dp tạo bo tròn. Các @Preview cuối file dùng 390×844 và 320×640 để xem trước, không tạo màn hình runtime mới.

## 8. ComponentsData.kt — dữ liệu không lẫn UI

[ComponentsData.kt, dòng 4](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsData.kt#L4):

```kotlin
data class ComponentInfo(
    val id: String,
    val name: String,
    val description: String,
    val group: String
)

val components = listOf(
    ComponentInfo("text", "Text", "Displays text", "Display"),
    ComponentInfo("image", "Image", "Displays an image", "Display"),
```

Mỗi mục chỉ có id, name, description, group, đều String. id để khi nhấn biết mở demo nào; name để hiển thị; description để giải thích ngắn; group để chia nhóm.

listOf theo thứ tự Text, Image, TextField, PasswordField, Column, Row rồi phần mở rộng. Group "Display · More" và "Input · More" đặt sau Layout để sáu mục đầu giữ thứ tự slide. Không có mục “Tự tìm hiểu”.

## 9. ComponentsListScreen.kt — tái sử dụng item

[ComponentsListScreen.kt, dòng 16](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsListScreen.kt#L16):

```kotlin
fun ComponentsListScreen(listState: LazyListState, onComponentClick: (String) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        DetailHeader("UI Components List")
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(start = 14.dp, end = 14.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            for (nhom in components.map { it.group }.distinct()) {
                item(key = "group_$nhom") {
                    Text(nhom, Modifier.padding(top = 12.dp), fontWeight = FontWeight.Medium)
                }
                items(components.filter { it.group == nhom }, key = { it.id }) { component ->
                    ComponentItem(component) { onComponentClick(component.id) }
                }
            }
        }
    }
}
```

Column chứa header cố định và LazyColumn cuộn. contentPadding là khoảng đệm danh sách; spacedBy(12.dp) tạo khoảng cách giữa các phần tử. map lấy tên nhóm, distinct loại nhóm trùng, filter lấy các mục thuộc nhóm. Đây là thao tác trên danh sách Kotlin bình thường, không phải hệ thống registry.

items(... key = { it.id }) giúp Compose nhận dạng mục ổn định. listState do màn hình cha giữ. Nhấn item gọi onComponentClick(component.id).

[ComponentsListScreen.kt, dòng 37](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsListScreen.kt#L37):

```kotlin
fun ComponentItem(component: ComponentInfo, onClick: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
            .background(ItemBlue).clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 12.dp)
    ) {
        Text(component.name, fontWeight = FontWeight.Bold)
        Text(component.description, style = MaterialTheme.typography.bodyMedium)
    }
}
```

Một composable dùng cho 79 thẻ. Thứ tự modifier: chiếm chiều ngang → cắt góc → vẽ nền → nhận click → đệm nội dung. Tên in đậm, mô tả bodyMedium. Nếu đổi background ở đây thì mọi thẻ cùng đổi.

## 10. ComponentDetailScreen.kt — header và chọn ví dụ

[ComponentDetailScreen.kt, dòng 19](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentDetailScreen.kt#L19):

```kotlin
fun DetailHeader(title: String, onBack: (() -> Unit)? = null) {
    Box(Modifier.fillMaxWidth().height(56.dp), contentAlignment = Alignment.Center) {
        if (onBack != null) {
            TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)
                .semantics { contentDescription = "Quay lại" }) {
                Text("‹", color = TitleBlue, fontSize = 30.sp)
            }
        }
        Text(title, Modifier.padding(horizontal = if (onBack == null) 12.dp else 56.dp),
            color = TitleBlue, fontSize = 20.sp, fontWeight = FontWeight.Bold,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center)
    }
}

```

Box chồng nút Back bên trái và title ở giữa. onBack nullable: null thì không có nút Back, như danh sách; truyền hàm thì có nút Back, như detail. Modifier.align(CenterStart) áp dụng trong BoxScope. semantics contentDescription = "Quay lại" giúp nhận biết ý nghĩa nút, thay vì chỉ đọc ký tự ‹.

[ComponentDetailScreen.kt, dòng 35](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentDetailScreen.kt#L35):

```kotlin
    val component = components.first { it.id == componentId }
    val tieuDe = when (componentId) {
        "text" -> "Text Detail"
        "image" -> "Images"
        "row" -> "Row Layout"
        "column" -> "Column Layout"
        "box" -> "Box Layout"
        else -> component.name
    }
    Column(Modifier.fillMaxSize()) {
        DetailHeader(tieuDe, onBack)
        val canGiua = componentId in listOf("text", "textfield", "password", "column", "box")
        Column(
            Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
```

first tìm đúng dữ liệu theo id; tất cả id được điều hướng từ danh sách cố định. when đổi tên Text → Text Detail, Image → Images, Row → Row Layout... Tên còn lại dùng component.name.

canGiua là Boolean xác định demo cần đặt giữa vùng nội dung. Column detail cuộn dọc, padding ngang 24.dp/dọc 20.dp. Các demo dài vẫn xem được khi máy nhỏ; header ở ngoài vùng cuộn.

[ComponentDetailScreen.kt, dòng 54](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentDetailScreen.kt#L54):

```kotlin
                "text" -> TextExample()
                "image" -> ImageExample()
                "textfield" -> TextFieldExample()
                "password" -> PasswordExample()
                "row" -> RowExample()
                "column" -> ColumnExample()
                "box" -> BoxExample()
                "checkbox" -> CheckboxExample()
                "radio" -> RadioExample()
                "switch" -> SwitchExample()
                "slider" -> SliderExample()
                else -> when (component.group) {
                    "Layout" -> LayoutExample(componentId)
                    "Display · More" -> DisplayExample(componentId)
                    "Input · More" -> InputExample(componentId)
                    "Selection" -> SelectionExample(componentId)
                    "Actions" -> ActionExample(componentId)
                    "Content" -> ContentExample(componentId)
                    "Navigation" -> NavigationExample(componentId)
                    "Feedback" -> if (componentId == "error") InputExample("error") else FeedbackExample(componentId)
                }
            }
```

Các bài bắt buộc có nhánh rõ ràng; phần khác chọn nhóm rồi gọi hàm examples. Hàm nhóm dùng when(id) để chọn API thật. Không dùng reflection, ViewModel, generic registry hoặc NavHost.

## 11. DisplayExamples.kt — chữ và ảnh

### TextExample: từng từ được định dạng ở đâu?

[examples/DisplayExamples.kt, dòng 28](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/DisplayExamples.kt#L28):

```kotlin
    val cauVan = buildAnnotatedString {
        append("The ")
        withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) { append("quick") }
        append(" ")
        withStyle(SpanStyle(color = Color(0xFFB87818), fontSize = 38.sp)) { append("Brown") }
        append("\nfox j u m p s  ")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("over") }
        append("\n")
        withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) { append("the") }
        append(" ")
        withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append("lazy") }
        append(" dog.")
    }
    Column(Modifier.fillMaxWidth()) {
        Text(cauVan, Modifier.padding(horizontal = 12.dp), fontSize = 30.sp, lineHeight = 56.sp)
        Spacer(Modifier.height(18.dp))
        HorizontalDivider(color = Color(0xFFE6E6E6))
    }
```

cauVan là AnnotatedString, chứa nội dung kèm thông tin định dạng:

| Đoạn | Kiểu | Tham số |
|---|---|---|
| The, fox, dog | kiểu thường | lấy từ Text bên ngoài |
| quick | gạch ngang | textDecoration = LineThrough |
| Brown | nâu/cam, lớn hơn | color = 0xFFB87818, fontSize = 38.sp |
| over | đậm | FontWeight.Bold |
| the | gạch chân | TextDecoration.Underline |
| lazy | nghiêng | FontStyle.Italic |

withStyle chỉ tác động đoạn append trong cặp ngoặc của nó. append("\\n") xuống dòng chủ động. “j u m p s” tạo khoảng trắng bằng nội dung chuỗi để sát ảnh. Text ngoài đặt fontSize 30.sp, lineHeight 56.sp. HorizontalDivider màu xám nhạt nằm bên dưới. Không có HTML hoặc WebView.

### ImageExample: ảnh local, hoạt động offline

[examples/DisplayExamples.kt, dòng 52](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/DisplayExamples.kt#L52):

```kotlin
            painterResource(R.drawable.uth_campus_one), "Mặt trước trường UTH",
            Modifier.fillMaxWidth().aspectRatio(1.78f).clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(16.dp))
```

painterResource đọc ảnh đã đóng gói. fillMaxWidth theo chiều ngang nội dung; aspectRatio(1.78f) khiến cao ≈ rộng/1.78; clip RoundedCornerShape(10.dp) cắt góc; ContentScale.Crop phủ kín khung và có thể cắt một phần rìa.

Ảnh một có nguồn ngắn bên dưới; ảnh hai có “In app”. Hai ảnh đều local: nguồn chữ không có nghĩa ảnh được tải mạng. Ảnh không đúng hoàn toàn góc chụp slide vì URL gốc 404; xem DANH_SACH_UI_COMPONENTS.md để biết URL, tác giả và giới hạn quyền sử dụng.

Thay ảnh: chép ảnh hợp lệ vào drawable, tên chữ thường và dấu gạch dưới; ví dụ uth_new.jpg → R.drawable.uth_new. Đổi painterResource tại Image muốn thay. Không cần quyền Internet hoặc Coil. Muốn kích thước cố định, thay aspectRatio bằng height(200.dp). Muốn không cắt ảnh, đổi Crop thành Fit; khi đó có thể thấy khoảng trống.

### DisplayExample: các nhánh khác

- basictext: BasicText của Foundation, không tự lấy kiểu Material như Text.
- styled: gọi lại TextExample, tránh sao chép đoạn định dạng.
- icon: Icon đọc vector launcher cũ, tint Unspecified để giữ màu.
- surface: shape, color, tonalElevation; Text bên trong padding 24.dp.
- imagestyle: size 180.dp, bo 90.dp tạo khung tròn, Crop để phủ.
- textstyles: maxLines = 1, overflow Ellipsis tạo dấu ba chấm; TextAlign Center/End đổi căn chữ; TextDecoration tạo gạch.

## 12. InputExamples.kt — nhập liệu và cập nhật trực tiếp

[examples/InputExamples.kt, dòng 21](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/InputExamples.kt#L21):

```kotlin
    var noiDungNhap by rememberSaveable { mutableStateOf("") }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = noiDungNhap,
            onValueChange = { noiDungNhap = it },
            placeholder = { Text("Thông tin nhập", fontSize = 14.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp), singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Gray, focusedBorderColor = Color.Gray)
        )
        Text(
            if (noiDungNhap.isEmpty()) "Tự động cập nhật dữ liệu theo textfield" else noiDungNhap,
            color = Color(0xFFB52A2A), fontSize = 14.sp
        )
```

Đọc một vòng sự kiện:

1. Ban đầu noiDungNhap là String rỗng.
2. value = noiDungNhap đưa dữ liệu hiện tại vào ô nhập.
3. Người dùng gõ, Compose gọi onValueChange với chuỗi mới.
4. noiDungNhap = it ghi chuỗi mới vào state.
5. Các vùng đọc state được Compose chạy lại khi cần, gọi là recomposition.
6. Text phía dưới đọc cùng noiDungNhap nên thay đổi ngay, không cần Submit hoặc restart Activity.

Nếu viết onValueChange = {} mà vẫn truyền value cũ thì ô không cập nhật dữ liệu. Placeholder chỉ hiện khi rỗng. singleLine = true dùng một dòng. shape bo 14.dp; colors đặt viền xám cả khi focus. Text đỏ hiển thị lời nhắc khi rỗng và chính nội dung khi có dữ liệu.

### PasswordExample

[examples/InputExamples.kt, dòng 41](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/InputExamples.kt#L41):

```kotlin
    var matKhau by rememberSaveable { mutableStateOf("") }
    var hienMatKhau by rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        value = matKhau, onValueChange = { matKhau = it },
        label = { Text("Mật khẩu") }, modifier = Modifier.fillMaxWidth(), singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (hienMatKhau) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            TextButton(onClick = { hienMatKhau = !hienMatKhau }) {
                Text(if (hienMatKhau) "Ẩn" else "Hiện")
            }
        }
    )
```

matKhau giữ String thật, hienMatKhau là Boolean. Khi false dùng PasswordVisualTransformation; true dùng VisualTransformation.None. Nút trailingIcon đảo Boolean bằng !. KeyboardType.Password chỉ gợi ý bàn phím dành cho password; việc che chữ do visualTransformation. Ví dụ không lưu hoặc gửi mật khẩu ra ngoài, không in mật khẩu vào log.

### InputExample và tham số cần hiểu

- noiDung là String dùng trong đúng demo đang mở; moTimKiem Boolean chỉ điều khiển gợi ý SearchBar.
- filled dùng TextField nền Material; outlined dùng OutlinedTextField viền.
- basicinput dùng BasicTextField, border và decorationBox. oNhap là hàm vẽ vùng nhập thật, luôn phải gọi oNhap().
- multiline đặt minLines 3, maxLines 6; Enter tạo dòng mới.
- keyboard dùng KeyboardType.Number và ImeAction.Done; không phải bộ kiểm tra số hợp lệ.
- validation/error tính coLoi từ dữ liệu hiện tại. isError đổi giao diện lỗi; supportingText nói điều kiện. Đây là kiểm tra định dạng sơ lược.
- search dùng filter + contains(ignoreCase = true) để tìm tên; take(6) giới hạn phần kết quả; none hiện thông báo rỗng.
- searchbar dùng SearchBarDefaults.InputField, query/onQueryChange cho dữ liệu; expanded/onExpandedChange cho mở gợi ý; chọn kết quả cập nhật chữ và đóng danh sách.

SearchBar cần chiều cao hữu hạn khi mở gợi ý vì màn hình detail đã cuộn dọc. InputExamples.kt dòng 107 đặt giới hạn:

```kotlin
                modifier = Modifier.fillMaxWidth().heightIn(max = 480.dp),
```

heightIn chỉ đặt trần 480.dp, không ép thanh tìm kiếm luôn cao 480.dp khi đang thu gọn. Không truyền chiều cao vô hạn vào SearchBarLayout. Đây là ví dụ vì sao phải thử cả trạng thái mở rộng, không chỉ mở màn hình và nhìn thanh tìm kiếm thu gọn.
- TextButton “Xóa dữ liệu” gán noiDung = ""; ô nhập và dòng kết quả đều trở về rỗng.

## 13. SelectionExamples.kt — chọn, kéo, chọn ngày giờ

[examples/SelectionExamples.kt, dòng 21](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L21):

```kotlin
fun CheckboxExample() {
    var daChon by rememberSaveable { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = daChon, onCheckedChange = { daChon = it })
        Text(if (daChon) "Đã chọn" else "Chưa chọn")
    }
}
```

daChon: Boolean, false = chưa chọn. var cho phép đổi giá trị. rememberSaveable giữ state nhỏ khi Activity tái tạo; mutableStateOf tạo đối tượng state được Compose theo dõi; by cho phép đọc/ghi trực tiếp như Boolean. Đổi false thành true thì checkbox được chọn ngay khi mở. onCheckedChange nhận giá trị mới, gán vào daChon; Text đọc daChon nên đổi theo.

remember giữ qua recomposition; rememberSaveable còn hỗ trợ lưu/khôi phục qua cấu hình với kiểu lưu được. Không hiểu nhầm remember là lưu vĩnh viễn.

### RadioExample

luaChon là một String ("Kotlin"). Ba RadioButton so sánh cùng biến, vì vậy chỉ một mục true. Row dùng selectable để cả nhãn cùng nhấn được; selectableGroup báo đây là một nhóm. RadioButton onClick = null vì Row đã xử lý click, tránh xử lý hai lần.

### SwitchExample

daBat false → OFF; callback nhận Boolean mới. Không dùng hai biến cho chữ và switch, vì hai nguồn dữ liệu dễ lệch nhau.

### SliderExample

[examples/SelectionExamples.kt, dòng 56](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L56):

```kotlin
    var giaTriSlider by rememberSaveable { mutableFloatStateOf(50f) }
    Column {
        Slider(value = giaTriSlider, onValueChange = { giaTriSlider = it }, valueRange = 0f..100f)
        Text("Giá trị: ${giaTriSlider.toInt()}")
    }
```

Float 50f nằm trong 0f..100f. value là vị trí nút kéo; onValueChange cập nhật khi kéo; toInt chỉ dùng khi hiển thị, không làm mất phần lẻ trong state.

### SelectionExample và DateExample

- khoang kiểu ClosedFloatingPointRange<Float>, khởi tạo 20f..80f. RangeSlider có hai tay kéo, Text dùng start/endInclusive.
- FilterChip đọc selected = daChon và đảo !daChon. AssistChip/SuggestionChip gán thongBao. InputChip nhấn để xóa, khi đã xóa thì có nút thêm lại.
- luaChon Int 0 là chỉ số phân đoạn. cacMuc giữ Ngày/Tuần/Tháng; SegmentedButtonDefaults.itemShape tạo góc hợp vị trí.
- gio là TimePickerState khởi tạo 09:30, 24 giờ; hour/minute thay đổi theo TimePicker; %02d hiển thị đủ hai chữ số.
- ngay là DatePickerState; selectedDateMillis là Long? có thể null trước khi chọn.
- moHopThoai false ẩn DatePickerDialog; nút chọn ngày đổi true.
- ngayXacNhan Long? ban đầu null; chỉ nhận giá trị trong nút OK. Hủy chỉ đóng hộp thoại.
- formatDate kiểm tra null trước; SimpleDateFormat dd/MM/yyyy và UTC chuyển thời điểm sang ngày. UTC phù hợp quy ước selectedDateMillis của DatePicker.
- horizontalScroll cho lịch/đồng hồ trong máy hẹp. Không tăng minSdk hoặc thêm thư viện ngày giờ mới.

DatePicker có vùng cuộn ngang nội bộ. Phải đặt chiều rộng hữu hạn ở phía trong modifier cuộn ngoài để tránh lỗi đo vô hạn. Source tại SelectionExamples.kt dòng 127–128:

```kotlin
        DatePicker(state = ngay, showModeToggle = true,
            modifier = Modifier.horizontalScroll(rememberScrollState()).width(360.dp))
```

360.dp là chiều rộng khung lịch, không phải chiều rộng màn hình cố định. Vùng cuộn ngoài vẫn theo chiều rộng khả dụng; máy nhỏ có thể kéo ngang để xem phần lịch còn lại. Thứ tự horizontalScroll rồi width rất quan trọng: width giới hạn con chứa lịch. TimePicker ở dòng 106 cũng dùng width(300.dp) bên trong vùng cuộn. Lỗi runtime về chiều rộng vô hạn đã được phát hiện khi chạy thử, không thể chỉ dựa vào build.

## 14. LayoutExamples.kt — Row, Column, Box

### RowExample

[examples/LayoutExamples.kt, dòng 18](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L18):

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

Column ngoài chứa repeat(4) để tạo bốn hàng. Row trong xếp các Box theo chiều ngang. listOf có ba màu, phần tử thứ hai dùng DarkTileBlue. weight(1f) chia đều chiều rộng còn lại cho ba ô sau khoảng cách và padding. Không gán pixel cố định nên máy hẹp không tràn ngang. height 64.dp quy định chiều cao ô, RoundedCornerShape 12.dp bo góc.

### ColumnExample

[examples/LayoutExamples.kt, dòng 34](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L34):

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

Column xếp ba Box dọc, spacedBy(12.dp), CenterHorizontally căn giữa. width 150.dp và height 64.dp là kích thước từng ô. Muốn căn trái dùng Alignment.Start; căn phải Alignment.End. Thêm màu vào listOf sẽ thêm một ô mà không cần tạo thêm hàm.

### BoxExample

[examples/LayoutExamples.kt, dòng 47](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L47):

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

Box cha 240.dp giữ vùng bố trí; Center đặt các hình tại tâm. Hai ô 90.dp offset (-55,-55) và (55,55), ô giữa 120.dp vẽ sau nên nằm trên. Offset dịch chỗ vẽ/bố trí con nhưng không làm phần tử khác trong Column chừa thêm chỗ; vì vậy Box cha đã đủ lớn. Đây là hình minh họa màu cam, không phải thẻ tự học.

### LayoutExample

- spacer: khoảng trống 64.dp giữa hai Text.
- lazycolumn/lazyrow: items(30/20) vẽ số ô; chỉ tạo các mục cần thiết để cuộn hiệu quả.
- vgrid: GridCells.Fixed(3) là ba cột; hgrid Fixed(2) là hai hàng. Lưới có height hữu hạn; tránh lỗi đo chiều cao vô hạn trong màn hình cuộn dọc.
- flowrow: thiếu chiều ngang thì tự xuống hàng, không phải Row cuộn ngang.
- flowcolumn: giới hạn height 230.dp để con tràn sang cột tiếp theo.
- constraints: maxWidth là chiều rộng cha cấp, không phải độ phân giải thiết bị. Dưới 360.dp chọn ColumnExample, đủ rộng chọn RowExample.
- arrangement: gianDeu false dùng Center, true dùng SpaceBetween. verticalAlignment CenterVertically căn con giữa chiều cao Row.
- modifiers: khoangCach Float 16f, Slider 0..40 thay padding. Viền đặt trước padding nên thấy rõ khoảng trống từ viền đến nền.
- NumberTile chỉ dùng chung cho các danh sách/lưới, không tạo kiến trúc thành phần phức tạp.

## 15. ActionExamples.kt — một biến đếm cho từng demo nút

[examples/ActionExamples.kt, dòng 12](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/ActionExamples.kt#L12):

```kotlin
fun ActionExample(id: String) {
    var soLanNhan by rememberSaveable { mutableIntStateOf(0) }
    val khiNhan = { soLanNhan++; Unit }
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        when (id) {
            "button" -> Button(onClick = khiNhan) { Text("Nhấn Button") }
            "outlinedbutton" -> OutlinedButton(onClick = khiNhan) { Text("Nhấn OutlinedButton") }
            "textbutton" -> TextButton(onClick = khiNhan) { Text("Nhấn TextButton") }
```

soLanNhan Int 0; khiNhan là lambda tăng biến và trả Unit. Các nút cùng được chọn bằng when(id), nhưng mỗi lần mở chi tiết chỉ có một kiểu. Khi nhấn, Text dưới hiển thị số lần đã nhấn.

Button là nút nền đậm; OutlinedButton có viền; TextButton chỉ chữ; ElevatedButton có độ nổi; FilledTonalButton nền tonal. IconButton dùng icon và contentDescription. FAB dùng dấu +, Extended FAB thêm nhãn “Thêm”. Chỉ xuất hiện trong màn hình demo tương ứng, không gắn lên mọi màn hình.

## 16. ContentExamples.kt — container và cửa sổ nổi

[examples/ContentExamples.kt, dòng 16](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/ContentExamples.kt#L16):

```kotlin
fun ContentExample(id: String) {
    var dangMo by rememberSaveable { mutableStateOf(false) }
    var luaChon by rememberSaveable { mutableStateOf("Kotlin") }
    var soThongBao by rememberSaveable { mutableIntStateOf(3) }
    var thongBao by rememberSaveable { mutableStateOf("Chưa xác nhận") }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
```

dangMo Boolean false dùng mở/đóng overlay của demo hiện tại. luaChon String Kotlin dùng menu/sheet. soThongBao Int 3 dùng Badge/Scaffold. thongBao String “Chưa xác nhận” dùng kết quả AlertDialog.

- Card có containerColor ItemBlue; ElevatedCard có elevation 8.dp; OutlinedCard có viền. Nội dung Text padding 24.dp.
- ListItem có headline, supporting, leading, trailing slots: các hàm composable đặt vào đúng vị trí.
- HorizontalDivider tách trên/dưới; VerticalDivider có Row cha height 80.dp để đường thẳng có chiều cao đo được.
- BadgedBox đặt Badge lên nội dung hộp thư; chỉ hiển thị badge khi số > 0. Hai nút tăng và xóa số.
- DropdownMenu nằm trong Box cùng nút neo; expanded đọc dangMo. onDismissRequest đóng khi nhấn ra ngoài/Back; chọn mục cập nhật luaChon rồi đóng.
- ExposedDropdownMenuBox phối hợp expanded/onExpandedChange; menuAnchor có loại PrimaryNotEditable vì TextField readOnly. Không cho gõ vào ô chọn.
- AlertDialog: confirmButton cập nhật thongBao, dismissButton giữ kết quả; cả hai đóng hộp thoại.
- Dialog: cửa sổ tùy chỉnh, tự bọc Surface và nút Đóng.
- ModalBottomSheet: mở từ nút, ListItem chọn ngôn ngữ, onDismissRequest xử lý vuốt/Back/nhấn ngoài. Không giả một Box cố định thành bottom sheet.
- Scaffold: demo cao 300.dp, có topBar và FAB. Nội dung nhận PaddingValues trong lambda và dùng padding(khoangTrong), tránh đè top bar. WindowInsets zero chỉ trong khung demo vì màn hình ngoài đã xử lý safe drawing.

## 17. NavigationExamples.kt — UI điều hướng khác thư viện điều hướng

[examples/NavigationExamples.kt, dòng 13](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/NavigationExamples.kt#L13):

```kotlin
fun NavigationExample(id: String) {
    var mucDangChon by rememberSaveable { mutableIntStateOf(0) }
    var soLanNhan by rememberSaveable { mutableIntStateOf(0) }
    val cacMuc = listOf("Trang chủ", "Bài học", "Đã lưu")
    val khongInsets = WindowInsets(0, 0, 0, 0)
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
```

mucDangChon Int 0 = mục đầu. soLanNhan Int dùng hành động app bar. cacMuc có ba nhãn. khongInsets zero dùng cho ví dụ bar nằm bên trong màn hình, không phải cấu hình toàn ứng dụng.

- TopAppBar/CenterAlignedTopAppBar đặt title và actions. Lưu/+ tăng biến đếm.
- BottomAppBar chứa Thêm/Đặt lại.
- NavigationBarItem, NavigationRailItem: selected so sánh chỉ số; onClick đổi chỉ số; dòng nội dung bên dưới dùng cùng state.
- ModalNavigationDrawer dùng DrawerState. open/close là hàm suspend, vì vậy rememberCoroutineScope().launch gọi ngắn trong callback. Coroutine scope gắn với composable, không tạo GlobalScope.
- ModalDrawerSheet chỉ rộng 250.dp, khung demo cao 340.dp. Chọn mục đổi state và đóng drawer.
- PrimaryTabRow dùng ba Tab. PrimaryScrollableTabRow dùng tám Tab nên có thể cuộn ngang.
- Các demo này không thực sự điều hướng sang Activity khác. Chúng minh họa trạng thái lựa chọn và nội dung thay đổi tại chỗ. Luồng ứng dụng thật đã ở ComponentsApp.

## 18. FeedbackExamples.kt — tiến trình và thông báo

[examples/FeedbackExamples.kt, dòng 13](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/FeedbackExamples.kt#L13):

```kotlin
fun FeedbackExample(id: String) {
    var tienTrinh by rememberSaveable { mutableFloatStateOf(0.5f) }
    var dangTai by rememberSaveable { mutableStateOf(false) }
    val snackbar = remember { SnackbarHostState() }
    val phamVi = rememberCoroutineScope()
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        when (id) {
            "circular", "linear" -> {
                if (id == "circular") CircularProgressIndicator(progress = { tienTrinh })
                else LinearProgressIndicator(progress = { tienTrinh }, modifier = Modifier.fillMaxWidth())
                Slider(value = tienTrinh, onValueChange = { tienTrinh = it })
```

tienTrinh Float 0.5f tương ứng 50%; Slider mặc định 0..1, progress nhận lambda đọc state. LinearProgressIndicator và CircularProgressIndicator có bản xác định tiến độ; bản không tham số trong loading là vòng quay vô định.

SnackbarHostState giữ hàng chờ thông báo. Nút dùng phamVi.launch { snackbar.showSnackbar(...) } vì hàm có thể chờ đến khi thông báo kết thúc; không chặn luồng UI. SnackbarHost vẽ thông báo thật. actionLabel “Đóng” cho phép kết thúc.

TooltipBox đặt PlainTooltip và state. rememberTooltipPositionProvider(TooltipAnchorPosition.Above) là overload mới phù hợp dependency; nhấn giữ icon để xem, nhấn thường đổi trạng thái. Loading đổi dangTai thủ công; nút Hoàn tất kết thúc trạng thái mẫu, không có tải API.

## 19. Bảng state quan trọng để ôn nhanh

| Biến | Kiểu / khởi tạo | File/hàm | Khi đổi | UI đọc |
|---|---|---|---|---|
| baiDangMo | String / menu | CourseworkApp | chọn bài, thoát | when chọn cây bài |
| manHinh | String / intro | ComponentsApp | Ready, click item, Back | Intro/List/Detail |
| componentId | String / text | ComponentsApp | chọn item | demo đang mở |
| trangThaiDanhSach | LazyListState | ComponentsApp | vuốt danh sách | vị trí cuộn |
| noiDungNhap | String / rỗng | TextFieldExample | gõ/xóa | value và chữ đỏ |
| matKhau | String / rỗng | PasswordExample | gõ/xóa | value mật khẩu |
| hienMatKhau | Boolean / false | PasswordExample | Hiện/Ẩn | visualTransformation |
| noiDung | String / rỗng | InputExample | nhập, chọn gợi ý, xóa | input/kết quả/validation |
| moTimKiem | Boolean / false | InputExample | mở/đóng SearchBar | expanded |
| coLoi | Boolean / tính lại | InputExample | noiDung đổi | isError/supportingText |
| daChon | Boolean / false | CheckboxExample | click checkbox | checked và nhãn |
| luaChon | String / Kotlin | RadioExample | chọn Row | selected và kết quả |
| daBat | Boolean / false | SwitchExample | nhấn switch | checked và ON/OFF |
| giaTriSlider | Float / 50f | SliderExample | kéo | vị trí và số |
| daChon | Boolean / false | SelectionExample | click chip | trạng thái/xóa chip |
| luaChon | Int / 0 | SelectionExample | chọn phân đoạn | selected và tên |
| thongBao | String / Chưa nhấn | SelectionExample | click chip | thông báo |
| khoang | Range<Float> / 20f..80f | SelectionExample | kéo hai tay | RangeSlider và hai số |
| gio | TimePickerState / 09:30 | SelectionExample | chọn giờ/phút | đồng hồ và giờ in |
| ngay | DatePickerState | DateExample | chọn ngày | lịch/ngày đang chọn |
| moHopThoai | Boolean / false | DateExample | mở, OK/Hủy | dialog có tồn tại |
| ngayXacNhan | Long? / null | DateExample | chỉ OK | ngày xác nhận |
| gianDeu | Boolean / false | LayoutExample | Switch | Arrangement |
| khoangCach | Float / 16f | LayoutExample | Slider | padding |
| chieuRong | Dp / maxWidth | LayoutExample | cha đổi kích thước | chọn Column/Row |
| soLanNhan | Int / 0 | ActionExample | click nút | số lần |
| dangMo | Boolean / false | ContentExample | mở/đóng | menu/dialog/sheet |
| luaChon | String / Kotlin | ContentExample | chọn menu/sheet | ô chọn và kết quả |
| soThongBao | Int / 3 | ContentExample | tăng/xóa | badge/nội dung scaffold |
| thongBao | String / Chưa xác nhận | ContentExample | Đồng ý | kết quả dialog |
| mucDangChon | Int / 0 | NavigationExample | chọn tab/destination | selected/nội dung |
| soLanNhan | Int / 0 | NavigationExample | app bar actions | số lần |
| nganKeo | DrawerState / Closed | NavigationExample | open/close/cử chỉ | drawer |
| tienTrinh | Float / 0.5f | FeedbackExample | Slider | progress/phần trăm |
| dangTai | Boolean / false | FeedbackExample | nút loading/icon | vòng quay/nội dung |
| snackbar | SnackbarHostState | FeedbackExample | showSnackbar | thông báo đang hiện |
| phamVi | CoroutineScope | Navigation/Feedback | không gán lại | gọi suspend API an toàn theo composition |

Tên biến lặp ở các hàm độc lập không dùng chung dữ liệu. Khi muốn state sống sau khi rời detail, cần đưa state lên màn hình cha hoặc cơ chế lưu riêng; bài này cố ý giữ demo cục bộ cho dễ học.

## 20. Modifier: tác động thực tế và thứ tự

| Modifier | Dùng trong bài | Nếu sửa thì sao? |
|---|---|---|
| fillMaxSize() | Surface/Box gốc | Lấy hết kích thước cha cho phép; bỏ đi có thể chỉ vừa nội dung |
| fillMaxWidth() | nút Intro, thẻ, input | Chiếm hết ngang sau padding; dùng fillMaxWidth(0.8f) còn 80% |
| padding(16.dp) | menu/dialog/nội dung | Đổi 16 thành 30 thì nội dung cách biên thêm 14.dp mỗi cạnh, vùng còn lại hẹp hơn |
| width(150.dp) | ColumnExample | Đổi 200 thì ô rộng hơn; vẫn chịu giới hạn cha |
| height(64.dp) | RowExample | Đổi 90 thì từng ô và cả hàng cao hơn |
| size(200.dp) | logo | Đặt cả rộng và cao; đổi 240 thì logo lớn hơn |
| background(color, shape) | ô layout/thẻ | Đổi màu chỉ nền, không đổi màu chữ; shape quyết định nét nền |
| border(1.dp, color, shape) | BasicTextField | 2.dp làm viền dày hơn; shape nên trùng góc nền |
| clickable { } | ComponentItem | Nhận click; gán id/màn hình mới trong callback |
| weight(1f) | ba ô Row, nội dung Intro | Chia phần còn lại theo tỉ lệ; 2f nhận gấp đôi 1f khi các điều kiện khác giống nhau |
| offset(x, y) | BoxExample | x dương sang phải, y dương xuống dưới; không dành thêm chỗ cho phần bị dịch |
| clip(shape) | ảnh/thẻ | Cắt nội dung theo hình; background có shape chưa chắc cắt ảnh con |
| aspectRatio(1.78f) | ảnh UTH | Giữ tỉ lệ khung; 1f tạo khung vuông |
| verticalScroll(rememberScrollState()) | detail | Cho Column cuộn, không dùng trực tiếp thay LazyColumn khi danh sách rất dài |
| safeDrawingPadding() | gốc bài | Tránh thanh hệ thống/cutout |
| imePadding() | gốc bài | Tránh bàn phím đang mở che nội dung |

Compose không có tham số margin như CSS. Đặt padding bên ngoài background/clickable tạo khoảng đệm ngoài phần vẽ đó; đặt sau background thì nền phủ vùng padding. Modifier được nối theo thứ tự nên đổi thứ tự có thể đổi kết quả.

Ví dụ thực tế LayoutExample dùng border → padding → background: viền ngoài, khoảng đệm, rồi nền xanh bên trong. Đổi background lên trước padding thì nền xanh cũng phủ vùng đệm.

## 21. Sự kiện, state và recomposition

- onClick: callback không có tham số cho nút; chỉ chạy khi có thao tác nhấn.
- onValueChange: callback nhận chuỗi/giá trị mới; cần ghi state.
- onCheckedChange: callback nhận Boolean mới, không phải tên nút.
- Nút Back gọi onBack → quayLai → đổi manHinh; không tạo Activity mới.
- Dữ liệu trong mutableStateOf/mutableIntStateOf/mutableFloatStateOf được Compose theo dõi. Khi thay đổi, những vùng đã đọc state có thể được chạy lại để phản ánh giá trị mới.
- Không gán state vô điều kiện trong phần thân composable, vì có thể tạo vòng lặp recomposition; đặt thay đổi trong callback.
- State là nguồn dữ liệu duy nhất. Không tạo một biến cho ô nhập và biến khác cho dòng đỏ rồi cố đồng bộ thủ công.
- Các nút vẫn dùng vùng nhấn Material; Radio dùng selectable cho cả hàng. Ảnh có contentDescription; Back có nhãn ngữ nghĩa.

## 22. Build và kiểm tra sau khi sửa

Tại thư mục dự án trên PowerShell:

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug
```

testDebugUnitTest được chạy, nhưng dự án không có source unit test; NO-SOURCE không phải một bộ test logic đã PASS. Kiểm tra tương tác thực tế và kết quả mở từng demo được ghi ở README. Journey lưu tại app/src/journeysTest/reference_screens.journey.xml; công cụ tự động trong phiên làm việc bị lỗi Model query failed: UNKNOWN nên không ghi PASS cho Journey.

APK ở app/build/outputs/apk/debug/app-debug.apk. Không đổi namespace/applicationId, Gradle hoặc Manifest khi chỉ sửa màu, chữ, spacing. Nếu lỗi Unresolved reference, kiểm tra import/API của phiên bản đang cài, không nâng cấp toàn bộ thư viện.

## 23. Chỉ mục hàm và vị trí trong source cuối

Các dòng dưới đây được đọc trực tiếp từ file source; bấm tên file để đối chiếu. Preview chỉ dùng trong IDE.

| File | Hàm/lớp | Dòng bắt đầu |
|---|---|---|
| [ComponentsApp.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsApp.kt#L19) | ComponentsApp | 19 |
| [IntroScreen.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/IntroScreen.kt#L22) | IntroScreen | 22 |
| [IntroScreen.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/IntroScreen.kt#L57) | IntroPreview | 57 |
| [IntroScreen.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/IntroScreen.kt#L61) | IntroSmallPreview | 61 |
| [ComponentsListScreen.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsListScreen.kt#L16) | ComponentsListScreen | 16 |
| [ComponentsListScreen.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsListScreen.kt#L37) | ComponentItem | 37 |
| [ComponentsData.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentsData.kt#L4) | ComponentInfo | 4 |
| [ComponentDetailScreen.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentDetailScreen.kt#L19) | DetailHeader | 19 |
| [ComponentDetailScreen.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentDetailScreen.kt#L34) | ComponentDetailScreen | 34 |
| [ComponentDetailScreen.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentDetailScreen.kt#L82) | TextDetailPreview | 82 |
| [ComponentDetailScreen.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentDetailScreen.kt#L86) | RowSmallPreview | 86 |
| [ComponentDetailScreen.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/ComponentDetailScreen.kt#L90) | ImagesPreview | 90 |
| [examples/DisplayExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/DisplayExamples.kt#L27) | TextExample | 27 |
| [examples/DisplayExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/DisplayExamples.kt#L49) | ImageExample | 49 |
| [examples/DisplayExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/DisplayExamples.kt#L72) | DisplayExample | 72 |
| [examples/InputExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/InputExamples.kt#L19) | TextFieldExample | 19 |
| [examples/InputExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/InputExamples.kt#L40) | PasswordExample | 40 |
| [examples/InputExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/InputExamples.kt#L58) | InputExample | 58 |
| [examples/SelectionExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L21) | CheckboxExample | 21 |
| [examples/SelectionExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L30) | RadioExample | 30 |
| [examples/SelectionExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L46) | SwitchExample | 46 |
| [examples/SelectionExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L55) | SliderExample | 55 |
| [examples/SelectionExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L65) | SelectionExample | 65 |
| [examples/SelectionExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L113) | formatDate | 113 |
| [examples/SelectionExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/SelectionExamples.kt#L122) | DateExample | 122 |
| [examples/LayoutExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L18) | RowExample | 18 |
| [examples/LayoutExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L34) | ColumnExample | 34 |
| [examples/LayoutExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L47) | BoxExample | 47 |
| [examples/LayoutExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L58) | NumberTile | 58 |
| [examples/LayoutExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/LayoutExamples.kt#L65) | LayoutExample | 65 |
| [examples/ActionExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/ActionExamples.kt#L12) | ActionExample | 12 |
| [examples/ContentExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/ContentExamples.kt#L16) | ContentExample | 16 |
| [examples/NavigationExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/NavigationExamples.kt#L13) | NavigationExample | 13 |
| [examples/FeedbackExamples.kt](app/src/main/java/com/example/tuan03project/bai01uicomponents/examples/FeedbackExamples.kt#L13) | FeedbackExample | 13 |
| [CourseworkApp.kt](app/src/main/java/com/example/tuan03project/CourseworkApp.kt#L16) | CourseworkApp | 16 |
| [MainActivity.kt](app/src/main/java/com/example/tuan03project/MainActivity.kt#L10) | MainActivity | 10 |
| [MainScreen.kt](app/src/main/java/com/example/tuan03project/MainScreen.kt#L30) | MainScreen | 30 |
| [MainScreen.kt](app/src/main/java/com/example/tuan03project/MainScreen.kt#L109) | MainScreenPreview | 109 |
| [ui/theme/Theme.kt](app/src/main/java/com/example/tuan03project/ui/theme/Theme.kt#L8) | Tuan03ProjectTheme | 8 |
