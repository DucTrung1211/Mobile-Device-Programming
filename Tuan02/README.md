# TUẦN 02

**Sinh viên:** Trần Đức Trung\
**MSSV:** 001206004489

## Nội dung

- Thực hành 02 - giao diện Profile đã thực hiện trước đó

1. Thực hành 03 - Nhập hai số và thực hiện phép tính
2. Bài 1 - Vòng lặp for và nullable
3. Bài 2 - Câu lệnh if / else
4. Bài 3 - Tổng hợp for + if + nullable

## Project

[Tuan02_Project](./Tuan02_Project)

Mở thư mục `Tuan02/Tuan02_Project` bằng Android Studio, chờ Gradle sync rồi chạy module `app`.
Đổi hằng số `START_SCREEN` trong `MainActivity.kt` rồi Run lại để xem bài khác:

| START_SCREEN | Màn hình |
|--------------|----------|
| 0 | Thực hành 02 - Profile |
| 1 | Thực hành 03 - Máy tính |
| 2 | Bài 1 - Nullable |
| 3 | Bài 2 - If / Else |
| 4 | Bài 3 - For + If + Nullable |

Giá trị hiện tại là `1` (Thực hành 03).

Build và chạy unit test trên Windows, từ thư mục project:

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug
```

Bài 3 dùng tên `StudentScore` để tránh trùng với `Student` của Bài 1.
Project Android của Tuần 02 được lưu trong thư mục `Tuan02_Project`.
