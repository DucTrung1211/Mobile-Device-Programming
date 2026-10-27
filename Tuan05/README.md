# TUẦN 05 — UTH Auth (Base on InClassProject)

Project Android: [Tuan05_Project](./Tuan05_Project) (package `com.example.tuan05project`).

## Công nghệ

Kotlin, Jetpack Compose, Material 3, Firebase Authentication, Credential Manager (Sign in with Google).

## Chức năng

- Nút `Login by Gmail` mở hộp chọn tài khoản Google bằng Credential Manager.
- ID token của Google được dùng để đăng nhập Firebase Authentication.
- Thành công: khung xanh hiển thị email thật của tài khoản Firebase.
- Thất bại hoặc hủy: khung hồng hiển thị lý do.

## Cấu hình Firebase

1. Tạo project trên [Firebase Console](https://console.firebase.google.com/).
2. Thêm Android app với package `com.example.tuan05project` và SHA-1 debug
   (lấy bằng `.\gradlew.bat :app:signingReport`).
3. Authentication → Sign-in method → bật **Google**.
4. Tải `google-services.json` (sau khi bật Google) và đặt vào `Tuan05_Project/app/`.

Nếu thiếu `google-services.json`, build sẽ báo lỗi `File google-services.json is missing`.

## Cách chạy

1. Android Studio → Open → chọn thư mục `Tuan05/Tuan05_Project`.
2. Chờ Gradle Sync, chọn emulator có Google Play hoặc điện thoại thật đã đăng nhập Google.
3. Run module `app`.

Build bằng dòng lệnh tại `Tuan05_Project`:

```powershell
.\gradlew.bat :app:assembleDebug
```
