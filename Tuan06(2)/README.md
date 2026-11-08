# Tuần 06 (2) — Android Permissions

Ứng dụng riêng cho ảnh 4; applicationId `com.uth.week6.permissions`.

## Thiết kế và chức năng

Nguồn: [Figma 11245:1654](https://www.figma.com/design/ivYBomXm4kJqddjjzvqhl1/UTH-Mobile--Copy-?node-id=11245-1654).
Đã đọc design context/screenshot. Ảnh bếp, nền hình minh họa, ghim vị trí, chuông và camera
được tải về assets; không dùng URL Figma tạm khi chạy.

Ba bước Location → Notification → Camera có card trắng bo góc, lớp phủ tối, mô tả tiếng Anh,
nút gradient và Skip for now. Màn giải thích do Compose dựng, hộp thoại xin quyền do Android hiển thị.
Font hiện dùng sans-serif Android thay cho Inter trong Figma; chưa khẳng định giống từng pixel.

- Activity Result API RequestMultiplePermissions yêu cầu quyền thật.
- Location yêu cầu FINE và COARSE cùng lúc; chấp nhận quyền gần đúng.
- Notification chỉ yêu cầu POST_NOTIFICATIONS từ API 33, kiểm tra trạng thái thông báo trên bản cũ.
- Camera khai báo manifest và xin quyền runtime, camera hardware không bắt buộc.
- Skip chỉ chuyển bước. Quyền đã cấp hiện Continue; từ chối vẫn tiếp tục dùng app.
- Kiểm tra rationale; trường hợp hệ thống không hỏi lại có hướng dẫn Settings.
- Kiểm tra lại quyền khi ON_RESUME và sau khi quay về Settings.
- Lưu bước/đã từng hỏi, dùng SavedStateHandle cho request đang chờ; không lưu boolean để giả quyền.
- Màn trạng thái có Manage từng quyền, mở Settings và gửi notification thử thật.

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


## Cấu hình

Không cần token hoặc tài khoản. Android 13+ cần người dùng cấp quyền thông báo.
Notification channel “Permission test” được tạo bằng NotificationManager;
nếu người dùng tắt channel, app báo rõ và không nói đã gửi thành công.

Các chữ Login/Continue without account phía sau modal chỉ là nền theo thiết kế.
Bài này không triển khai đăng nhập, bản đồ, đọc vị trí thực tế, chụp ảnh hoặc QR scanner.

## Cấu trúc và luồng

- model/PermissionStep.kt: thứ tự, tiêu đề/mô tả và tập quyền theo SDK.
- data/PermissionRepository.kt: kiểm tra quyền từ hệ thống, lưu bước, channel, notification.
- viewmodel/PermissionViewModel.kt: StateFlow, bước hiện tại, request/response và refresh.
- ui/PermissionsApp.kt: modal Figma, launcher, rationale/Settings, status.
- MainActivity: khởi tạo Activity và Compose.

Nhấn Allow/Turn on → kiểm tra quyền/rationale → launcher hệ thống → callback kiểm tra lại
→ chuyển bước. Settings không tự cấp quyền; quay về app đọc lại trạng thái thật.

## Kiểm chứng và giới hạn

- testDebugUnitTest, assembleDebug, lintDebug đều thành công.
- 4 unit test kiểm tra thứ tự và tập quyền, đặc biệt ngưỡng API 32/33.
- Đã cài/chạy trên Samsung SM-F741N, Android 17/API 37.
  Quan sát modal Location và hộp thoại Location thật; lần mở lại đọc đúng Precise location,
  Notification Allowed và Camera Allowed do người dùng đã cấp.
- Đã nhấn Send test notification; Android ghi nhận notification id 1001 thuộc
  com.uth.week6.permissions, không chỉ có Snackbar.
- Chưa hoàn tất kiểm tra runtime trên API <33, quyền gần đúng, từ chối nhiều lần,
  không hỏi lại, Settings revoke/restore và xoay khi hộp thoại đang mở.
- Chỉ có thiết bị API 37 ở lượt kiểm chứng; emulator khởi động chưa kết nối được.
