# Tuần 06 (1) — UTH SmartTasks

Ứng dụng Kotlin + Jetpack Compose cho ảnh 1–3; applicationId `com.uth.week6.tasks`.
Giữ nguyên Tuan06 cũ. Bản sao component từ Tuần 05 nằm trong project này, không liên kết source ngoài.

## Thiết kế và chức năng

Đã đọc trực tiếp design context và screenshot từ file
[UTH Mobile](https://www.figma.com/design/ivYBomXm4kJqddjjzvqhl1/UTH-Mobile--Copy-?node-id=17870-741):
Trang chủ 17870:741, Danh sách 17870:878, Chi tiết 17873:2278,
form 17870:1000, onboarding 17870:605/651/698, Cài đặt 17870:1451, Hồ sơ 17870:1235.
Dùng UTH Todo V2, không dùng giao diện SmartTasks cũ.

Màu chính #2F5BEA, nền #F5F5F7, card trắng, nhóm study/life theo Figma.
Be Vietnam Pro được đóng gói ở res/font, kèm FONT-LICENSE.txt.
Ảnh onboarding xuất riêng từ cụm minh họa; không đưa khung điện thoại hoặc canvas Figma vào app.

- Trang chủ: ngày thiết bị, lời chào, tiến độ hôm nay, nhóm và số việc còn lại, mở danh sách có bộ lọc.
- Danh sách: dữ liệu API, tìm tên/ghi chú, lọc nhóm, chia Cần làm/Đã xong, ngày giờ và ưu tiên.
- Chi tiết: GET đúng id; ghi chú, hạn, remind, việc nhỏ và tiến độ. Thêm/sửa/xóa/đánh dấu việc nhỏ gọi PATCH.
- Form thêm/sửa: tên bắt buộc, ghi chú, nhóm, DatePicker/TimePicker, ưu tiên và remind.
- Đánh dấu công việc, POST/PATCH/DELETE đều có bước đọc lại; xóa công việc cần xác nhận.
- Onboarding, Back, tab Thông báo, cài đặt theme/màu/cỡ chữ/âm thanh và hồ sơ/thống kê.
- Loading, danh sách rỗng, lọc không có kết quả, lỗi HTTP/mạng và Thử lại.

## Mở và chạy

Mở chính thư mục project này trong Android Studio, chờ Gradle Sync, chọn module app và thiết bị, rồi Run.
Đây là project độc lập, có settings.gradle.kts và Gradle wrapper riêng; không phụ thuộc source các tuần trước hoặc hai bài còn lại.

Môi trường đã build ngày 08/11/2026: Windows, JDK 21.0.11, Gradle 9.6.0, AGP 9.5.0-alpha08,
built-in Kotlin, Compose compiler 2.4.21, Compose BOM 2026.02.01; minSdk 26, compile/target SDK 37.
Android Studio tự tạo local.properties theo SDK máy bạn. File đó không được commit.

Chạy từ thư mục project:

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug :app:lintDebug
```

APK: app/build/outputs/apk/debug/app-debug.apk.
Báo cáo test: app/build/reports/tests/testDebugUnitTest/index.html.
Báo cáo lint: app/build/reports/lint-results-debug.html.
Gradle configuration, dependency resolution, build và lint đã chạy bằng CLI; chưa quan sát riêng thao tác Sync trong ba cửa sổ Android Studio.


## Token và API

Sao chép secrets.properties.example thành secrets.properties và điền TASK_TOKEN được giảng viên cấp.
Bản local đã có cấu hình từ ảnh được cung cấp. secrets.properties được gitignore.
Không ghi token trong source, README, log hoặc commit. Gradle truyền cấu hình vào BuildConfig lúc build;
APK client vẫn chứa thông tin dùng để gọi API, không phải kho giữ bí mật phía máy chủ.

Base URL: https://share.tryapps.store/mock/teach/

| Method | Route | Kết quả thực tế 08/11/2026 |
|---|---|---|
| GET | tasks | 200, 6 công việc |
| GET | tasks/{id} | 200, đúng cả id 4, 1, 2, 3, 6, 5 |
| PATCH | tasks/4 | 200, nhưng đọc lại không lưu thay đổi |
| POST | tasks | 201, nhưng không xuất hiện bản ghi mới trong GET |
| DELETE | tasks/999999999 | 204 với id kiểm chứng không tồn tại |

Test live dùng id không tồn tại để kiểm tra DELETE khi POST không tạo được bản ghi riêng.
Chưa dùng DELETE để xóa một công việc thật trên máy chủ.

JSON dùng đúng id/title/note/category/date/time/priority/remind/done/subtasks.
Subtask dùng title/done; id subtask trên UI chỉ là chỉ số cục bộ, không gửi thêm trường id.
Không đổi ngày công việc API thành ngày hiện tại: hôm nay 08/11 nên Trang chủ 0/0 là đúng,
dù danh sách vẫn có 6 công việc với hạn tháng 10.

## Cấu trúc và luồng

- data/TaskApi.kt: DTO, payload, Retrofit suspend, OkHttp timeout; không bật logging header.
- data/TaskRepository.kt: status/body/mapping và xác minh ghi bằng GET chi tiết + danh sách.
- features/todo/model/: model công việc, nhóm, ưu tiên, tùy chọn.
- features/todo/viewmodel/: StateFlow, tìm/lọc, CRUD, chống thao tác ghi chồng.
- features/todo/ui/: component, home/list/form/theme được tái sử dụng bằng bản sao độc lập.
- ui/: root, chi tiết, onboarding, cài đặt, hồ sơ và danh sách remind.
- MainActivity chỉ khởi tạo Compose. UI thu thập StateFlow theo lifecycle; không gọi mạng trong Composable.

ViewModel giữ state chung cho Trang chủ/Danh sách/Chi tiết trong phiên Activity.
Chỉ nhận trạng thái mới sau khi máy chủ trả dữ liệu khớp. Nếu mock không lưu, báo rõ giới hạn
và tải lại dữ liệu thật; không chuyển sang dữ liệu giả hoặc giữ dấu hoàn thành như đã lưu.

## Kiểm chứng và giới hạn

- testDebugUnitTest, assembleDebug và lintDebug đã thành công.
- 7 unit test: mapping/schema/ngày, tìm/lọc/thống kê, id/header, PATCH cố định,
  PATCH được xác minh, DELETE/404, HTTP 401 và empty 204.
- 1 test live opt-in đã chạy thành công: `-PliveApi=true --tests "*LiveTaskApiTest"`.
  Test này thực sự POST/PATCH và chỉ dọn bản ghi do nó tạo nếu xác minh được quyền sở hữu;
  không dùng cho kiểm tra thường xuyên.
- Đã cài/chạy lại trên Samsung SM-F741N, API 37; quan sát Trang chủ, danh sách đủ 6 việc,
  mở đúng chi tiết “Ôn chương 3”, ghi chú và tiến độ việc nhỏ 1/2; đã đối chiếu screenshot với Figma.
- Chưa hoàn tất ma trận runtime: xoay/ngang/màn nhỏ/IME, toàn bộ picker,
  lỗi mạng → retry, thao tác lặp nhanh và mọi CRUD việc nhỏ.
- API mock hiện không lưu thay đổi. Không thể xác nhận CRUD bền vững khi máy chủ vẫn có hành vi này.
- remind chỉ lưu trường JSON; chưa có bộ lập lịch thông báo nền hoặc tổng kết 21:00.
- OAuth/Firebase của Tuần 05 đăng ký applicationId khác; chưa cấu hình đăng nhập cho app mới.
  Hồ sơ ghi rõ chưa đăng nhập, không mạo nhận xác thực Google.
- API không có lịch sử hoàn thành: không bịa chuỗi ngày, tỷ lệ đúng hạn hoặc biểu đồ tuần.
- Chưa khẳng định khớp từng pixel. Chi tiết dùng vùng nhập Material và ưu tiên đọc được trên Android;
  một số thông số/chi tiết bổ trợ vẫn cần tinh chỉnh so với Figma.
