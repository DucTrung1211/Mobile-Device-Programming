# TUẦN 03 — Tuan03_Project

- Ngôn ngữ: Kotlin.
- UI: Jetpack Compose.
- Bài tập: bố trí Text và Button theo ảnh mẫu, xử lý sự kiện nhấn nút.
- Ban đầu: `My First App`, `Hello`, nút `Say Hi!`.
- Sau khi nhấn: thay `Hello` bằng hai dòng `I'm` và `Nguyen Van A`.
- Tiêu đề và nút giữ nguyên vị trí; trạng thái được lưu bằng `rememberSaveable`.

## Project và cách chạy

Mở [Tuan03_Project](./Tuan03_Project) bằng Android Studio, chờ Gradle sync, chọn thiết bị rồi Run module `app`.

Từ thư mục project trên Windows:

```powershell
.\gradlew.bat assembleDebug
```

Project độc lập theo cách tổ chức Tuần 01–02, gồm `app/src/main`, cấu hình Gradle và Gradle Wrapper riêng.
Xem [README của project](./Tuan03_Project/README.md) để biết cấu trúc chi tiết.

## Kiểm tra đã thực hiện

- `assembleDebug`: BUILD SUCCESSFUL.
- `testDebugUnitTest`: NO-SOURCE (chưa có unit test).
- Đã cài và chạy trên emulator `Medium_Phone_API_36.1`.
- Đã kiểm tra trạng thái Hello, bấm nút đổi thông báo, vị trí nút không đổi.
- Đã kiểm tra bố cục và giữ trạng thái khi đổi kích thước emulator; khôi phục về kích thước gốc.
