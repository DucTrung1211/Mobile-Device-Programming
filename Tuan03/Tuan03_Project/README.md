# Tuan03_Project — Báo cáo hoàn thiện bài UI Components

Cập nhật 19/10/2026. Bài “TUẦN 04 | Bài 01” được làm trong chính Tuan03_Project, không tạo project mới.

## 1. Cấu trúc trước khi làm

Gốc dự án: C:/Users/trung/AndroidStudioProjects/LapTrinhThietBiDiDong/Tuan03/Tuan03_Project.

Một module Android app :app, package com.example.tuan03project, một MainActivity launcher. Kotlin ban đầu gồm MainActivity.kt, MainScreen.kt và ui/theme/Theme.kt. MainScreen là bài My First App / Hello / Say Hi! → I'm / Nguyen Van A. Không có Color.kt, Type.kt hoặc ảnh UTH.

## 2. Cấu trúc sau khi làm

Giữ cấu trúc Gradle và theme cũ, thêm CourseworkApp.kt để chọn bài và package bai01uicomponents:

```text
MainActivity.kt → CourseworkApp()
  ├─ Tuần 03 → MainScreen() nguyên bản
  └─ Tuần 04 · Bài 01 → ComponentsApp()
       ├─ IntroScreen
       ├─ ComponentsListScreen ← ComponentsData
       └─ ComponentDetailScreen → examples theo nhóm
```

Không dùng ViewModel, Repository, DI, NavHost hoặc Activity phụ. Luồng ba màn hình dùng state và when.

## 3. Bài cũ được giữ lại

MainScreen.kt giữ nguyên toàn bộ nội dung và thao tác Say Hi!. Theme.kt, Manifest, tên project/package/applicationId, Gradle và resource cũ không sửa. Tuần 1/Tuần 2 không bị chỉnh sửa. Hai thay đổi Tuần 2 có sẵn trong Git từ trước nằm ngoài phạm vi công việc này.

Source cũ dùng remember cho daNhanNut, không phải rememberSaveable như README trước đây mô tả. Vì vậy không khẳng định bài cũ giữ trạng thái qua xoay màn hình.

## 4. File đã tạo

Đường dẫn Kotlin tính từ app/src/main/java/com/example/tuan03project/:

- CourseworkApp.kt
- bai01uicomponents/ComponentsApp.kt
- bai01uicomponents/IntroScreen.kt
- bai01uicomponents/ComponentsListScreen.kt
- bai01uicomponents/ComponentsData.kt
- bai01uicomponents/ComponentDetailScreen.kt
- bai01uicomponents/examples/DisplayExamples.kt
- bai01uicomponents/examples/InputExamples.kt
- bai01uicomponents/examples/SelectionExamples.kt
- bai01uicomponents/examples/LayoutExamples.kt
- bai01uicomponents/examples/ActionExamples.kt
- bai01uicomponents/examples/ContentExamples.kt
- bai01uicomponents/examples/NavigationExamples.kt
- bai01uicomponents/examples/FeedbackExamples.kt

Tài nguyên mới: app/src/main/res/drawable/compose_logo.png, uth_campus_one.jpg, uth_campus_two.jpg.

Kiểm thử mô tả: app/src/journeysTest/reference_screens.journey.xml. Đây là kịch bản kiểm thử, không phải XML layout.

Bốn tài liệu ở thư mục gốc: DANH_SACH_UI_COMPONENTS.md, HUONG_DAN_CODE_CHI_TIET.md, HUONG_DAN_CHINH_SUA_GIAO_DIEN.md, CAU_HOI_ON_TAP_JETPACK_COMPOSE.md.

## 5. File có sẵn đã sửa

- MainActivity.kt: chỉ thay MainScreen() bằng CourseworkApp().
- README.md: cập nhật cách chạy, cấu trúc, số lượng và kết quả kiểm tra.

## 6. Phạm vi nghiên cứu

91 mục được rà soát trong tài liệu chính thức: 79 mục có ví dụ, 12 mục chưa triển khai. Một mục có thể là composable, biến thể hoặc kỹ thuật kết hợp; không gọi đây là toàn bộ API Compose.

Material 3 1.4.0 và Foundation/UI 1.10.4 được xác nhận qua Gradle dependencyInsight, dưới BOM 2026.02.01. AGP 9.5.0-alpha08, Kotlin 2.2.10, Gradle 9.6.0, compile/target SDK 37, minSdk 24 được giữ nguyên.

## 7. Phạm vi triển khai

79 thẻ mở 79 ví dụ: 58 ví dụ có tương tác (kể cả cuộn danh sách/lưới), 21 ví dụ hiển thị. Không đếm Badge hai lần. Mục TextField theo slide dùng OutlinedTextField; mục Filled TextField dùng đúng API TextField.

12 mục hoãn và lý do ghi trong DANH_SACH_UI_COMPONENTS.md: TriStateCheckbox; FilledIconButton; FilledTonalIconButton; OutlinedIconButton; DateRangePicker; TimeInput; HorizontalPager; VerticalPager; hai carousel; ElevatedAssistChip; MultiChoiceSegmentedButtonRow. Không gắn nhãn “không tương thích” khi chưa thử biên dịch chúng.

## 8. Danh sách màn hình

Trang chọn bài nằm ngoài giao diện tham chiếu. Bài mới gồm Intro, UI Components List và một ComponentDetailScreen chọn nội dung theo id.

Chi tiết bắt buộc: Text Detail, Images, TextField, PasswordField, Row Layout, Column Layout, Box Layout. Các mục khác chia nhóm Layout, Display · More, Input · More, Selection, Actions, Content, Navigation, Feedback. Không tạo 79 Activity.

## 9. Thao tác có thể thực hiện

I'm ready mở danh sách; nhấn thẻ mở chi tiết; Back UI/hệ thống về danh sách và giữ vị trí cuộn. Back danh sách về Intro, Back Intro về chọn bài.

Demo có nhập/xóa chữ, ẩn/hiện password, tìm cục bộ, báo lỗi định dạng đơn giản, chọn/bỏ checkbox/chip, radio đơn lựa chọn, bật switch, kéo slider/range/progress, đếm lần nhấn nút, mở/chọn menu, chọn ngày/giờ, xác nhận/hủy dialog, bottom sheet, chọn tab/bar/rail/drawer, snackbar, tooltip và trạng thái loading minh họa.

Các ví dụ không gọi API mạng hoặc database; không có đăng nhập hay gửi mật khẩu. Coroutine chỉ dùng cho suspend API của Drawer và Snackbar.

## 10. Điểm bám theo ảnh

Nền trắng; logo Compose gốc; mô tả Intro và nút xanh I'm ready gần đáy. Header xanh căn giữa; sáu mục đầu giữ đúng thứ tự Display/Text/Image, Input/TextField/PasswordField, Layout/Column/Row. Thẻ xanh nhạt #BBDFFC dùng chung một ComponentItem.

Text Detail định dạng riêng quick/Brown/over/the/lazy, có divider; Images có hai ảnh chữ nhật bo góc và nguồn/In app; TextField có placeholder Việt và phản hồi đỏ; Row có bốn hàng, mỗi hàng ba ô xanh, ô giữa đậm. Column và Box minh họa bố trí dọc/chồng lớp.

Không có status bar giả, bottom navigation toàn ứng dụng, banner, hoặc thẻ hồng “Tự tìm hiểu”.

## 11. Khác biệt so với ảnh tham chiếu

Hai ảnh UTH khác góc chụp slide. URL ảnh ghi trong slide trả 404; dùng ảnh UTH-TV có mô tả CC0 trên Commons và ảnh từ website tham quan UTH. Nguồn/giấy phép và đường dẫn ghi rõ trong danh mục nghiên cứu.

Font hệ thống Android và kích thước thanh hệ thống khác thiết kế điện thoại trong slide. Không khẳng định khớp từng pixel hoặc tất cả mức font scale.

## 12. Kết quả build và kiểm tra thực tế

| Kiểm tra | Kết quả thực tế |
|---|---|
| Gradle cuối: :app:testDebugUnitTest :app:assembleDebug | **BUILD SUCCESSFUL**, exit 0; 39 tác vụ, 7 chạy và 32 up-to-date |
| :app:testDebugUnitTest | **NO-SOURCE**: chưa có source unit test; không gọi đây là bộ unit test đã PASS |
| APK debug | Đã xác nhận tồn tại tại app/build/outputs/apk/debug/app-debug.apk |
| Emulator | Medium Phone API 36.1, emulator-5554; đã cài và chạy app |
| Mở ví dụ | Đã mở **79/79** mục để kiểm tra màn hình chạy được; DatePicker và SearchBar được sửa rồi thử lại trạng thái gây crash |
| Luồng chính | Intro → List → Detail, Back UI/hệ thống, giữ vị trí cuộn; đã đi tới nhóm cuối danh sách |
| Bài cũ | Hello ban đầu; Say Hi! → I'm / Nguyen Van A; Back về chọn bài |
| Input | TextField nhập Xin chao cập nhật dòng kết quả; password che và bật hiện Compose123 |
| Selection | Checkbox chọn/bỏ, Radio chọn Java, Switch ON/OFF, Slider kéo tăng từ 50 |
| Ngày giờ | Chọn 20/10/2026 trực tiếp; dialog chọn và OK xác nhận cùng ngày; TimePicker đổi 09:30 → 10:30 |
| Actions/Content | Button tăng số lần; Dropdown mở/chọn Dart; AlertDialog Đồng ý; Bottom Sheet chọn Dart |
| Navigation/Feedback | NavigationBar chọn Bài học đổi nội dung; Snackbar hiện thông báo |
| SearchBar | Mở gợi ý và chọn Image thành công sau khi giới hạn chiều cao |
| Preview 390×844dp | Đã render/quan sát Intro, Text Detail, Images |
| Preview 320×640dp | Đã render/quan sát Intro và Row; nội dung chính hiển thị đủ trong các preview này |
| IDE/source/docs audit | Không lỗi/import dư/API deprecated được phát hiện; id duy nhất và nhánh demo đủ; trích dẫn source trong tài liệu đã đối chiếu |

**Phân biệt phạm vi:** 79/79 là kiểm tra mở màn hình. Các thao tác chi tiết đã thử được liệt kê trong bảng; chưa kiểm thử mọi cử chỉ/trạng thái của toàn bộ 58 ví dụ tương tác. Không ghi PASS cho phần chưa chạy.

## 13. Cách chạy bài mới

Mở đúng thư mục dự án bằng Android Studio, chờ sync, chọn emulator/thiết bị, Run module app. Ở trang chọn bài nhấn **Tuần 04 · Bài 01 — UI Components**, sau đó **I'm ready**.

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug
```

APK: app/build/outputs/apk/debug/app-debug.apk.

## 14. Cách mở lại bài cũ

Từ detail Back về danh sách, Back về Intro, Back về trang chọn bài. Chọn **Tuần 03 — My First App** rồi nhấn Say Hi!. Back hệ thống từ bài cũ trở lại chọn bài. Không cần sửa MainActivity để đổi qua lại.

## 15. Tài liệu hướng dẫn

- [Danh mục nghiên cứu và nguồn](DANH_SACH_UI_COMPONENTS.md): 91 mục, API thực tế, trạng thái và các hạn chế.
- [Giải thích code chi tiết](HUONG_DAN_CODE_CHI_TIET.md): theo từng file, state/Modifier/layout/sự kiện, đoạn trích và số dòng thực tế, chỉ mục hàm.
- [Thực hành chỉnh giao diện](HUONG_DAN_CHINH_SUA_GIAO_DIEN.md): 51 tình huống màu/font/bố cục/sự kiện và quy trình thêm ví dụ mới, code trước/sau.
- [68 câu ôn tập](CAU_HOI_ON_TAP_JETPACK_COMPOSE.md): câu trả lời, ví dụ source và cách nói trước thầy.

Các đoạn “Sau” trong tài liệu thực hành là bài tập tùy chọn, chưa áp dụng vào source giao nộp. Đừng dán toàn bộ cùng lúc.

## 16. Audit và giới hạn

Không có Java/XML layout mới, duplicate Activity/launcher, dependency mới hoặc kiến trúc nâng cao. Tìm kiếm source không có thẻ tự học bị cấm, ViewModel, WebView hoặc NavHost. Kiểm tra IDE không phát hiện lỗi/import dư/API deprecated trong mã bài mới.

DatePicker từng crash do cuộn ngang được đo vô hạn; đã sửa bằng width hữu hạn bên trong vùng cuộn và thử lại chọn ngày thành công. SearchBar cũng được giới hạn chiều cao khi mở gợi ý để tránh nhận chiều cao vô hạn từ vùng cuộn dọc. Không xóa demo để che lỗi.

Journey được lưu nhưng công cụ chạy tự động trả Model query failed: UNKNOWN; không ghi PASS cho Journey. Kết quả emulator trực tiếp khác với kiểm thử tự động toàn bộ mọi cử chỉ. Không có bộ unit test logic riêng trong dự án và chưa kiểm tra toàn bộ thiết bị, font scale hay TalkBack.
