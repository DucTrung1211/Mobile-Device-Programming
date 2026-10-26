# TUẦN 04 — Navigation, OOP và Data Flow

Project Android: [Tuan04_Project](./Tuan04_Project) (package `com.example.tuan04project`).

## Công nghệ

Kotlin, Jetpack Compose, Material 3, Navigation Compose, ViewModel.

## Cách chạy

1. Android Studio → Open → chọn thư mục `Tuan04/Tuan04_Project`.
2. Chờ Gradle Sync, chọn emulator hoặc điện thoại Android 8.0 (API 26) trở lên.
3. Run module `app`. Màn hình đầu tiên là **Bài tập Tuần 04** để chọn bài.

Build bằng dòng lệnh tại `Tuan04_Project`:

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug
```

## Các bài tập

| Bài | Nội dung | Chức năng chính |
|-----|----------|-----------------|
| 1 | Navigation + OOP — UTH Todo V2 | Splash, 3 trang onboarding tạo từ `OnboardingPage`, Tiếp tục / Bỏ qua / Bắt đầu ngay, chỉ báo trang |
| 2 | Hệ thống Quản lý Thư viện | Danh sách sách, sinh viên, mượn nhiều sách, lịch sử mượn, trả sách; áp dụng đóng gói, kế thừa, đa hình, trừu tượng |
| 3 | Data Flow Navigation — SmartTasks | Forgot Password → Verify Code → Reset Password → Confirm; Submit quay về màn hình đầu và hiển thị dữ liệu nhận được |
| TH | Thực hành trên lớp | Root → LazyColumn 1.000.000 mục → Detail |

Dữ liệu Bài 2 và Bài 3 được lưu trong bộ nhớ (ViewModel), không giữ lại sau khi đóng ứng dụng.
