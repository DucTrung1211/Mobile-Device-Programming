# CampusRoute — A* Pathfinding Visualizer

Ứng dụng Android minh họa thuật toán tìm đường A*, viết bằng **Kotlin, Jetpack Compose và Material 3**.

Ứng dụng phát triển từ Project 32: *Heuristic Searching with A\**, Chapter 7 trong sách *Kotlin from Scratch* của Faisal Islam. Ví dụ console được mở rộng thành giao diện Android để học và trình diễn thuật toán.

## Chức năng

- Chạy A* trên đồ thị ví dụ trong sách: tìm đường từ A đến J.
- Tìm đường trên bản đồ lưới 7 × 7; chạm để thêm hoặc xóa vật cản.
- Hiển thị đường đi, chi phí và số nút đã xét; thông báo khi không có đường đi.
- Hỗ trợ giao diện sáng/tối và khôi phục trạng thái bản đồ.
- Có bộ kiểm thử JVM cho thuật toán và bản đồ, cùng kiểm thử giao diện Android.

## Mở và chạy

1. Mở thư mục **CampusRoute** này trong Android Studio.
2. Cài Android SDK Platform 37 và dùng JDK 25 theo cấu hình Gradle hiện có; đồng bộ project.
3. Chọn module `app` và chạy trên thiết bị hoặc máy ảo Android API 26 trở lên.

Project gồm một module `:app`, package `com.example.campusroute`. Các phiên bản dependency và Gradle Wrapper được giữ nguyên từ source gốc.

## Kiểm tra và build

Chạy tại thư mục chứa `gradlew.bat`:

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug --console=plain
```

Kiểm thử giao diện cần thiết bị hoặc máy ảo đang chạy:

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest --console=plain
```

[Quay lại bài Tuần 08 và slide thuyết trình](../README.md).
