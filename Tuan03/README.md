# TUẦN 03 — Tuan03_Project

- Ngôn ngữ: Kotlin.
- UI: Jetpack Compose.
- **Tuần 03 — My First App:** bố trí Text và Button theo ảnh mẫu, xử lý sự kiện nhấn nút.
- Ban đầu: `My First App`, `Hello`, nút `Say Hi!`.
- Sau khi nhấn: thay `Hello` bằng hai dòng `I'm` và `Nguyen Van A`.
- Tiêu đề và nút giữ nguyên vị trí; trạng thái của bài My First App dùng `remember`.
- **Tuần 04 – Bài 01 — UI Components:** nằm trong chính `Tuan03_Project`, gồm Intro, danh sách và 79 ví dụ Compose thuộc các nhóm Display, Input, Layout, Selection, Actions, Content, Navigation và Feedback.
- Trang chọn bài cho phép mở cả hai bài; không có thư mục Tuan04 riêng.

## Project và cách chạy

Mở [Tuan03_Project](./Tuan03_Project) bằng Android Studio, chờ Gradle sync, chọn thiết bị rồi Run module `app`. Tại trang chọn bài, chọn **Tuần 03 — My First App** hoặc **Tuần 04 · Bài 01 — UI Components**; với bài UI Components, nhấn **I'm ready** để mở danh sách.

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
- Đã kiểm tra bố cục trên emulator và mở 79/79 ví dụ UI Components; kết quả chi tiết và giới hạn kiểm thử nằm trong [báo cáo project](./Tuan03_Project/README.md).
- Không khẳng định bài My First App giữ state qua việc tạo lại Activity vì source đang dùng `remember`.
