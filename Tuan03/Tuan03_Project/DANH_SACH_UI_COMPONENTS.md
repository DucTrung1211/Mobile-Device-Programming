# Danh sách UI Components — Tuan03_Project

Ngày rà soát: 19/10/2026. Đây là danh sách có phạm vi, không phải tuyên bố bao phủ mọi API Jetpack Compose.

## Phạm vi và cách đếm

- 91 mục nghiên cứu: **79 mục đã có ví dụ trong ứng dụng**, 12 mục chưa triển khai.
- Trong 79 mục: **58 ví dụ có tương tác** (gồm cuộn danh sách), **21 ví dụ hiển thị**.
- “Mục” gồm composable, biến thể và kỹ thuật kết hợp. Styled Text, PasswordField, loading/error, Arrangement/Alignment và padding/sizing không phải các composable Material độc lập.
- TextField đầu danh sách dùng **OutlinedTextField** để giống viền trong ảnh. Mục Filled TextField minh họa đúng API **TextField**.
- Badge chỉ đếm một lần trong Content. Error dùng lại InputExample("error"), Styled Text dùng lại TextExample, không tạo bản sao.
- Đã xác nhận dependency thực tế bằng Gradle: BOM 2026.02.01; Material 3 **1.4.0**; Foundation/UI **1.10.4**. Không nâng cấp dependency.
- Không có mục nào được kết luận “không tương thích” chỉ vì chưa làm. 12 mục hoãn là lựa chọn phạm vi, chưa thử biên dịch từng mục.
- “Đã triển khai” nghĩa là có source thật và biên dịch thành công. Phạm vi chạy thử thực tế xem README; không đồng nghĩa mọi cử chỉ đã được kiểm thử tự động.

## Danh mục đã triển khai

| # | Thành phần / ví dụ | Nhóm | Chức năng | API thực tế | Trạng thái / mức demo | Nguồn |
|---|---|---|---|---|---|---|
| 1 | Text | Display | Displays text | `Text` | Đã triển khai · hiển thị | [Android Developers](https://developer.android.com/develop/ui/compose/text/style-text) |
| 2 | Image | Display | Displays an image | `Image` | Đã triển khai · hiển thị | [Android Developers](https://developer.android.com/develop/ui/compose/graphics/images/customize) |
| 3 | TextField | Input | Input field for text | `OutlinedTextField` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/text/user-input) |
| 4 | PasswordField | Input | Input field for passwords | `OutlinedTextField + PasswordVisualTransformation` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/text/user-input) |
| 5 | Column | Layout | Arranges elements vertically | `Column` | Đã triển khai · hiển thị | [Android Developers](https://developer.android.com/develop/ui/compose/layouts/basics) |
| 6 | Row | Layout | Arranges elements horizontally | `Row` | Đã triển khai · hiển thị | [Android Developers](https://developer.android.com/develop/ui/compose/layouts/basics) |
| 7 | Box | Layout | Stacks elements | `Box` | Đã triển khai · hiển thị | [Android Developers](https://developer.android.com/develop/ui/compose/layouts/basics) |
| 8 | Spacer | Layout | Creates empty space | `Spacer` | Đã triển khai · hiển thị | [Android Developers](https://developer.android.com/develop/ui/compose/layouts/basics) |
| 9 | LazyColumn | Layout | Scrolls a vertical list | `LazyColumn` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/lists) |
| 10 | LazyRow | Layout | Scrolls a horizontal list | `LazyRow` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/lists) |
| 11 | LazyVerticalGrid | Layout | Scrolls a vertical grid | `LazyVerticalGrid` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/lists) |
| 12 | LazyHorizontalGrid | Layout | Scrolls a horizontal grid | `LazyHorizontalGrid` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/lists) |
| 13 | FlowRow | Layout | Wraps items onto new rows | `FlowRow` | Đã triển khai · hiển thị | [Android Developers](https://developer.android.com/develop/ui/compose/layouts/flow) |
| 14 | FlowColumn | Layout | Wraps items onto new columns | `FlowColumn` | Đã triển khai · hiển thị | [Android Developers](https://developer.android.com/develop/ui/compose/layouts/flow) |
| 15 | BoxWithConstraints | Layout | Reads available space | `BoxWithConstraints` | Đã triển khai · hiển thị | [Android Developers](https://developer.android.com/develop/ui/compose/layouts/basics) |
| 16 | Arrangement & Alignment | Layout | Arranges and aligns children | `Arrangement + Alignment` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/layouts/basics) |
| 17 | Padding & sizing | Layout | Changes size, border and spacing | `Modifier` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/modifiers) |
| 18 | BasicText | Display · More | Shows Foundation text | `BasicText` | Đã triển khai · hiển thị | [Android Developers](https://developer.android.com/develop/ui/compose/text/style-text) |
| 19 | Styled Text | Display · More | Colors and formats individual words | `buildAnnotatedString + SpanStyle` | Đã triển khai · hiển thị | [Android Developers](https://developer.android.com/develop/ui/compose/text/style-text) |
| 20 | Icon | Display · More | Displays a symbol | `Icon` | Đã triển khai · hiển thị | [Android Developers](https://developer.android.com/develop/ui/compose/resources) |
| 21 | Surface | Display · More | Provides a colored container | `Surface` | Đã triển khai · hiển thị | [Android Developers](https://developer.android.com/develop/ui/compose/designsystems/material3) |
| 22 | Image styling | Display · More | Clips and crops an image | `Image + ContentScale + clip` | Đã triển khai · hiển thị | [Android Developers](https://developer.android.com/develop/ui/compose/graphics/images/customize) |
| 23 | Overflow, alignment & decoration | Display · More | Changes text appearance | `Text + TextOverflow + TextAlign + TextDecoration` | Đã triển khai · hiển thị | [Android Developers](https://developer.android.com/develop/ui/compose/text/style-text) |
| 24 | OutlinedTextField | Input · More | Shows an outlined input | `OutlinedTextField` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/text/user-input) |
| 25 | Filled TextField | Input · More | Shows a filled Material input | `TextField` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/text/user-input) |
| 26 | BasicTextField | Input · More | Shows a minimal input | `BasicTextField` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/text/user-input) |
| 27 | Multiline TextField | Input · More | Enters multiple lines | `OutlinedTextField` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/text/user-input) |
| 28 | Search input | Input · More | Filters local component names | `OutlinedTextField + filter` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/text/user-input) |
| 29 | SearchBar | Input · More | Expands search suggestions | `SearchBar + SearchBarDefaults.InputField` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/search-bar) |
| 30 | Input validation | Input · More | Checks a simple email address | `OutlinedTextField + isError` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/text/user-input) |
| 31 | Keyboard options | Input · More | Requests a number keyboard | `KeyboardOptions` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/text/user-input) |
| 32 | Checkbox | Selection | Selects or clears an option | `Checkbox` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/checkbox) |
| 33 | RadioButton | Selection | Selects one of three options | `RadioButton` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/radio-button) |
| 34 | Switch | Selection | Turns a setting on or off | `Switch` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/switch) |
| 35 | Slider | Selection | Selects a value from 0 to 100 | `Slider` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/slider) |
| 36 | RangeSlider | Selection | Selects a value range | `RangeSlider` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/slider) |
| 37 | FilterChip | Selection | Toggles a filter | `FilterChip` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/chip) |
| 38 | AssistChip | Selection | Performs a small action | `AssistChip` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/chip) |
| 39 | SuggestionChip | Selection | Applies a suggestion | `SuggestionChip` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/chip) |
| 40 | InputChip | Selection | Removes a selected entry | `InputChip` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/chip) |
| 41 | SegmentedButton | Selection | Selects one segment | `SingleChoiceSegmentedButtonRow + SegmentedButton` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/segmented-button) |
| 42 | DatePicker | Selection | Selects a calendar date | `DatePicker` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/datepickers) |
| 43 | DatePickerDialog | Selection | Confirms a date in a dialog | `DatePickerDialog + DatePicker` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/datepickers) |
| 44 | TimePicker | Selection | Selects hours and minutes | `TimePicker` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/time-pickers) |
| 45 | Button | Actions | Triggers an action | `Button` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/button) |
| 46 | OutlinedButton | Actions | Shows an outlined action | `OutlinedButton` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/button) |
| 47 | TextButton | Actions | Shows a text action | `TextButton` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/button) |
| 48 | ElevatedButton | Actions | Shows an elevated action | `ElevatedButton` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/button) |
| 49 | FilledTonalButton | Actions | Shows a tonal action | `FilledTonalButton` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/button) |
| 50 | IconButton | Actions | Triggers an icon action | `IconButton` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/icon-button) |
| 51 | FloatingActionButton | Actions | Shows a primary floating action | `FloatingActionButton` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/fab) |
| 52 | ExtendedFloatingActionButton | Actions | Combines icon and action text | `ExtendedFloatingActionButton` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/fab) |
| 53 | Card | Content | Groups related content | `Card` | Đã triển khai · hiển thị | [Android Developers](https://developer.android.com/develop/ui/compose/components/card) |
| 54 | ElevatedCard | Content | Shows a raised card | `ElevatedCard` | Đã triển khai · hiển thị | [Android Developers](https://developer.android.com/develop/ui/compose/components/card) |
| 55 | OutlinedCard | Content | Shows a bordered card | `OutlinedCard` | Đã triển khai · hiển thị | [Android Developers](https://developer.android.com/develop/ui/compose/components/card) |
| 56 | ListItem | Content | Shows a standard content row | `ListItem` | Đã triển khai · hiển thị | [Android Developers](https://developer.android.com/develop/ui/compose/lists) |
| 57 | HorizontalDivider | Content | Separates content horizontally | `HorizontalDivider` | Đã triển khai · hiển thị | [Android Developers](https://developer.android.com/develop/ui/compose/components/divider) |
| 58 | VerticalDivider | Content | Separates content vertically | `VerticalDivider` | Đã triển khai · hiển thị | [Android Developers](https://developer.android.com/develop/ui/compose/components/divider) |
| 59 | Badge | Content | Displays a notification count | `BadgedBox + Badge` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/badges) |
| 60 | DropdownMenu | Content | Selects an item from a menu | `DropdownMenu` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/menu) |
| 61 | ExposedDropdownMenu | Content | Selects a value for an input | `ExposedDropdownMenuBox` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/menu) |
| 62 | AlertDialog | Content | Requests confirmation | `AlertDialog` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/dialog) |
| 63 | Dialog | Content | Shows a custom dialog | `Dialog` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/dialog) |
| 64 | ModalBottomSheet | Content | Shows additional content | `ModalBottomSheet` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/bottom-sheets) |
| 65 | Scaffold | Content | Combines bars and content | `Scaffold` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/scaffold) |
| 66 | TopAppBar | Navigation | Shows a top app bar | `TopAppBar` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/app-bars) |
| 67 | CenterAlignedTopAppBar | Navigation | Centers an app bar title | `CenterAlignedTopAppBar` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/app-bars) |
| 68 | BottomAppBar | Navigation | Shows bottom actions | `BottomAppBar` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/app-bars) |
| 69 | NavigationBar | Navigation | Selects a destination | `NavigationBar + NavigationBarItem` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/navigation-bar) |
| 70 | NavigationRail | Navigation | Shows vertical destinations | `NavigationRail + NavigationRailItem` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/navigation-rail) |
| 71 | NavigationDrawer | Navigation | Opens a destination drawer | `ModalNavigationDrawer + ModalDrawerSheet` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/drawer) |
| 72 | TabRow | Navigation | Switches between tabs | `PrimaryTabRow + Tab` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/tabs) |
| 73 | ScrollableTabRow | Navigation | Scrolls a long row of tabs | `PrimaryScrollableTabRow + Tab` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/tabs) |
| 74 | CircularProgressIndicator | Feedback | Displays adjustable progress | `CircularProgressIndicator` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/progress) |
| 75 | LinearProgressIndicator | Feedback | Displays adjustable progress | `LinearProgressIndicator` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/progress) |
| 76 | Snackbar | Feedback | Shows a temporary message | `SnackbarHost + SnackbarHostState` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/snackbar) |
| 77 | Tooltip | Feedback | Explains an action on long press | `TooltipBox + PlainTooltip` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/tooltip) |
| 78 | Loading states | Feedback | Switches loading and ready states | `CircularProgressIndicator + if` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/components/progress) |
| 79 | Error messages | Feedback | Shows a validation message | `Text + isError` | Đã triển khai · tương tác | [Android Developers](https://developer.android.com/develop/ui/compose/text/user-input) |

## Thành phần bổ sung đã rà soát nhưng chưa triển khai

| # | Tên | Nhóm | Chức năng | API | Trạng thái và lý do | Nguồn |
|---|---|---|---|---|---|---|
| 80 | TriStateCheckbox | Selection | Chọn tất cả/một phần | `TriStateCheckbox` | Chưa triển khai. Chưa cần trạng thái cha–con cho demo cơ bản. | [Tài liệu](https://developer.android.com/develop/ui/compose/components/checkbox) |
| 81 | FilledIconButton | Actions | Nút icon nền đậm | `FilledIconButton` | Chưa triển khai. Đã có IconButton; giữ ví dụ gọn. | [Tài liệu](https://developer.android.com/develop/ui/compose/components/icon-button) |
| 82 | FilledTonalIconButton | Actions | Nút icon nền tonal | `FilledTonalIconButton` | Chưa triển khai. Biến thể mở rộng, chưa thêm màn hình riêng. | [Tài liệu](https://developer.android.com/develop/ui/compose/components/icon-button) |
| 83 | OutlinedIconButton | Actions | Nút icon có viền | `OutlinedIconButton` | Chưa triển khai. Biến thể mở rộng, chưa thêm màn hình riêng. | [Tài liệu](https://developer.android.com/develop/ui/compose/components/icon-button) |
| 84 | DateRangePicker | Selection | Chọn ngày bắt đầu/kết thúc | `DateRangePicker` | Chưa triển khai. Đã có chọn một ngày; range cần thêm giải thích. | [Tài liệu](https://developer.android.com/develop/ui/compose/components/datepickers) |
| 85 | TimeInput | Selection | Nhập giờ bằng chữ số | `TimeInput` | Chưa triển khai. Đã có TimePicker trực quan. | [Tài liệu](https://developer.android.com/develop/ui/compose/components/time-pickers) |
| 86 | HorizontalPager | Layout | Vuốt giữa các trang ngang | `HorizontalPager` | Chưa triển khai. Không cần cho luồng Intro/List/Detail. | [Tài liệu](https://developer.android.com/develop/ui/compose/layouts/pager) |
| 87 | VerticalPager | Layout | Vuốt giữa các trang dọc | `VerticalPager` | Chưa triển khai. Không cần cho luồng bài tập. | [Tài liệu](https://developer.android.com/develop/ui/compose/layouts/pager) |
| 88 | HorizontalMultiBrowseCarousel | Content | Cuộn các mục có kích thước nổi bật | `HorizontalMultiBrowseCarousel` | Chưa triển khai. API nâng cao; ưu tiên LazyRow đơn giản. | [Tài liệu](https://developer.android.com/develop/ui/compose/components/carousel) |
| 89 | HorizontalUncontainedCarousel | Content | Carousel ngang không giới hạn khung mục | `HorizontalUncontainedCarousel` | Chưa triển khai. Chưa triển khai; tránh tăng độ phức tạp đo và cắt ảnh. | [Tài liệu](https://developer.android.com/develop/ui/compose/components/carousel) |
| 90 | ElevatedAssistChip | Selection | AssistChip có độ nổi | `ElevatedAssistChip` | Chưa triển khai. Đã có AssistChip; chưa tạo biến thể riêng. | [Tài liệu](https://developer.android.com/develop/ui/compose/components/chip) |
| 91 | MultiChoiceSegmentedButtonRow | Selection | Cho phép nhiều phân đoạn được chọn | `MultiChoiceSegmentedButtonRow` | Chưa triển khai. Đã có SingleChoiceSegmentedButtonRow; chưa thêm đa chọn. | [Tài liệu](https://developer.android.com/develop/ui/compose/components/segmented-button) |

## Các tên trong slide cần hiểu đúng

- PasswordField: tên ví dụ chức năng. Source dùng OutlinedTextField, PasswordVisualTransformation và visualTransformation; nút Hiện/Ẩn đổi Boolean.
- Toggle: dùng Switch trong Material 3.
- Picker: khái niệm chung; bài có DropdownMenu, DatePicker, DatePickerDialog và TimePicker cụ thể.
- NavigationView, TabBar: không dùng nguyên tên từ slide làm API Compose. Bài dùng header tự ghép, NavigationBar, NavigationRail, ModalNavigationDrawer, PrimaryTabRow và PrimaryScrollableTabRow.
- TabRow/ScrollableTabRow: bài chọn PrimaryTabRow/PrimaryScrollableTabRow phù hợp phiên bản đã cài, tránh overload TabRow cũ.
- Back navigation: cơ chế BackHandler + state, không phải một UI component, không tính thêm một mục.
- Basic image styling: Image + clip + ContentScale. TextOverflow, TextAlign, TextDecoration: tham số/kiểu dữ liệu, gộp trong một demo.
- Arrangement, Alignment và Modifier không tự vẽ UI: chúng điều khiển cách bố trí composable.
- NavigationBar/Drawer demo chỉ đổi nội dung và lựa chọn ngay tại màn hình ví dụ. Không thêm NavHost hoặc thư viện điều hướng cho ba màn hình chính.

## Tương tác và giới hạn

- TextField, password, multiline, BasicTextField, search và validation đều dùng state trong composable. Search lọc danh sách cục bộ, không gọi mạng. SearchBar giới hạn chiều cao tối đa 480.dp để mở gợi ý an toàn trong màn hình cuộn dọc.
- Validation chỉ kiểm tra có @ và dấu chấm, **không chứng minh email tồn tại**.
- KeyboardOptions yêu cầu kiểu bàn phím, không tự chặn mọi ký tự nhập bằng bàn phím vật lý.
- Chips thay đổi state/thông báo; AssistChip không mở tài liệu hay gọi dịch vụ bên ngoài.
- InputChip có thao tác xóa và thêm lại. Slider/RangeSlider hiển thị giá trị ngay khi kéo.
- DatePickerDialog chỉ cập nhật ngày xác nhận khi nhấn OK; Hủy giữ ngày đã xác nhận. Ngày được định dạng theo UTC để tránh lệch lịch.
- Progress cho kéo slider; Loading states đổi thủ công để minh họa, không có tiến trình tải mạng giả.
- Snackbar và Drawer dùng coroutine ngắn vì showSnackbar/open/close là suspend API; không có Flow, Repository hay tác vụ nền.
- SearchBar, Date/TimePicker, Tooltip và một số API Material nằm trong hàm có @OptIn(ExperimentalMaterial3Api::class) theo bộ thư viện hiện tại. Không thêm thư viện alpha mới.
- DatePicker và TimePicker trực tiếp cho cuộn ngang khi màn hình hẹp, với chiều rộng con hữu hạn 360.dp/300.dp để tránh lỗi đo vô hạn; màn hình chi tiết cuộn dọc. LazyColumn/Grid con có chiều cao hữu hạn để không lồng cuộn vô hạn.
- Không thêm thẻ hồng “Tự tìm hiểu” vào source UI. Box dùng ô cam để minh họa chồng lớp theo ảnh tổng kết.

## Nguồn đã sử dụng và cách xác minh

1. [Material components](https://developer.android.com/develop/ui/compose/components): rà soát Actions, Communication, Containment, Navigation, Selection và Text inputs.
2. [Layout basics](https://developer.android.com/develop/ui/compose/layouts/basics), [Flow layouts](https://developer.android.com/develop/ui/compose/layouts/flow), [Lists and grids](https://developer.android.com/develop/ui/compose/lists), [Pager](https://developer.android.com/develop/ui/compose/layouts/pager).
3. [Text](https://developer.android.com/develop/ui/compose/text), [Style text](https://developer.android.com/develop/ui/compose/text/style-text), [User input](https://developer.android.com/develop/ui/compose/text/user-input).
4. [Material 3](https://developer.android.com/develop/ui/compose/designsystems/material3), [release notes](https://developer.android.com/jetpack/androidx/releases/compose-material3).
5. Các trang theo từng dòng trong hai bảng là nguồn chức năng/API. Một số mẫu chính thức nằm ở [android/snippets](https://github.com/android/snippets/tree/main/compose/snippets/src/main/java/com/example/compose/snippets/components).
6. [Material Design 3 guidelines](https://m3.material.io/components): được liên kết từ tài liệu Android, dùng tham khảo ý nghĩa nhóm, không dùng để thay đổi giao diện bài thầy giao.

Trang package-summary Material 3 và TooltipDefaults vượt giới hạn tải 4 MB của công cụ web. Một số URL KB như “chips” không có kết quả; đã chuyển sang trang **chip** chính thức. Đối với API triển khai, kiểm tra quan trọng cuối cùng là biên dịch với dependency thực tế, không sao chép API mới nhất của web một cách máy móc.

## Nguồn ảnh

- Logo: lấy logo icon_RGB từ [bài Compose Alpha của Android Developers](https://android-developers.googleblog.com/2020/08/announcing-jetpack-compose-alpha.html), giữ nguyên hình, lưu compose_logo.png.
- Ảnh 1: [Ảnh Mặt Trước UTH — Wikimedia Commons](https://commons.wikimedia.org/wiki/File:%E1%BA%A2nh_M%E1%BA%B7t_Tr%C6%B0%E1%BB%9Bc_UTH.jpg), tác giả ghi UTH-TV, nguồn ut.edu.vn, trang mô tả ghi CC0. Lưu uth_campus_one.jpg, không chỉnh nội dung.
- Ảnh 2: [tham quan chính thức UTH](https://vr.uth.edu.vn/), [ảnh gốc](https://vr.uth.edu.vn/assets/gallery/gallery_260630_024241_b54e.jpg). Ảnh trường và đoàn khách tại khuôn viên UTH, lưu uth_campus_two.jpg. Dùng trong bài tập với nguồn ghi ở đây; trang không công bố giấy phép mở riêng, không suy diễn quyền tái cấp phép thương mại.
- URL trong slide https://giaothongvantaitphcm.edu.vn/wp-content/uploads/2025/01/Logo-GTVT.png trả 404 khi kiểm tra. Một số trang ut.edu.vn trả 403. Vì vậy hai góc ảnh khác slide; không dùng ảnh ngẫu nhiên, không cắt toàn bộ slide vào UI.
