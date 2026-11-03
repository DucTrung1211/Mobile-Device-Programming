# Kiểm chứng Tuần 05

Thực hiện ngày 2026-11-03 (Asia/Saigon) trên nhánh `claude/tuan05-firebase`, tách từ `main` tại `d370d70`. Báo cáo phân biệt rõ ba mức: chỉ có code, đã qua unit/emulator test, và đã chạy với Firebase thật.

## Khảo sát và phạm vi

- Git root `LapTrinhThietBiDiDong`, remote `https://github.com/DucTrung1211/Mobile-Device-Programming.git`, project `Tuan05/Tuan05_Project`, package `com.example.tuan05project`.
- Không có AGENTS.md trong repository hoặc thư mục cha. Báo cáo kiểm chứng chỉ nằm ở file này.
- Tuần 02/04 có thay đổi chưa commit của chủ repository; đã giữ nguyên, commit chỉ gồm README root (nếu đổi) và `Tuan05/`.
- Không đổi AGP/Kotlin/applicationId, không bật thanh toán, không deploy Hosting, không chạy Test Lab/quảng cáo trả phí.

## Thay đổi trong lần này

| Thay đổi | Lý do |
|---|---|
| `navigation/SessionGate.kt`: enum `Lesson`, đích `RESTORING` | Bỏ số bài 0..3; không nháy màn đăng nhập khi Firebase đang khôi phục phiên |
| `features/lessons/LessonPickerScreen.kt` + nhãn `‹ Bài 0x` trong mỗi bài | Chọn bài rõ ràng, có đường quay lại danh sách bài |
| `FirebaseTaskRepository.requireOnline()` lắng nghe `.info/connected` | **Lỗi thật**: `get()` trên `.info/connected` bị server từ chối (“Invalid token in path”) nên mọi thao tác thêm/sửa/xóa đều thất bại |
| `TokenRegistry.register()/revoke()` chạy trên `Dispatchers.IO`, timeout bằng `withTimeoutOrNull` | **Lỗi thật**: FCM báo “Must not be called on the main application thread” |
| `AuthViewModel.signOut` coi `TimeoutCancellationException` là dọn dẹp thất bại; xóa credential có timeout 8 s | **Lỗi thật**: `unregister()` quá hạn làm đăng xuất bị hủy giữa chừng, app kẹt ở Hồ sơ với `loading = true` |
| Log lỗi Firebase (chỉ bản debug, chỉ loại exception và thông điệp SDK) | Chẩn đoán lỗi; không ghi dữ liệu người dùng/token |
| `firebase_config.xml`: URL instance RTDB thật | Database vừa được tạo |
| `LessonFlowTest` (7 test) và cập nhật `TaskStateTest` | Kiểm thử luồng mới và các lỗi trên |

## Firebase thật (project `uthsmarttasks-week5`)

Chủ project tự đăng nhập Firebase CLI 15.34.2; agent không nhập mật khẩu.

| Hạng mục | Lệnh / bằng chứng | Kết quả |
|---|---|---|
| Realtime Database | Tạo instance mặc định qua Management API (cùng hàm `firebase init database` dùng) | `uthsmarttasks-week5-default-rtdb`, `asia-southeast1`, ACTIVE |
| Database rules | `firebase deploy --only database` | Deploy complete; GET REST không xác thực → 401 Permission denied |
| Remote Config | `firebase deploy --only remoteconfig`; `remoteconfig:get` | Phiên bản 1, có `home_task_limit` |
| Storage | `GET firebasestorage.googleapis.com/v0/b/uthsmarttasks-week5.firebasestorage.app/o` | 404: **bucket chưa tồn tại**, cần Blaze; không tự bật |
| Hosting / Test Lab / Google Ads | — | Không thực hiện (có thể phát sinh phí hoặc cần quyết định của chủ project) |

## Build / test

| Kiểm tra | Lệnh | Kết quả |
|---|---|---|
| Lệnh bắt buộc | `.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug` tại `Tuan05_Project` | Exit 0, BUILD SUCCESSFUL |
| Unit test | Kết quả JUnit XML | 16 test (AuthViewModelTest 4, LessonFlowTest 7, TaskStateTest 5), 0 fail, 0 skip |
| Source set release | `.\gradlew.bat :app:compileReleaseKotlin` | Exit 0; nút gây crash chỉ có trong debug |
| Rules trên emulator | `npx -y firebase-tools@latest emulators:exec --project demo-tuan05 --only database,storage "npm test"` tại `Tuan05/firebase` | Exit 0; 5/5 pass |

## Runtime

Thiết bị: AVD `Medium_Phone_API_36.1` (`emulator-5556`, `sdk_gphone64_x86_64`, API 36, 1080×2400, 420 dpi), có tài khoản Google của chủ project. AVD `Pixel_10_Pro` (API 37) bị kẹt `offline` ở cả boot thường lẫn cold boot nên không dùng.

| Luồng | Kết quả |
|---|---|
| Danh sách bài → Bài 01 / Bài 02 / bài cũ, nhãn quay lại | Đạt |
| Khôi phục phiên khi mở lại app | Đạt: vào thẳng trang chủ với tên/ảnh Google thật |
| Đăng nhập Google → hủy | Đạt: “Bạn đã hủy đăng nhập Google.”, nút hết trạng thái bận, vẫn ở màn đăng nhập |
| Đăng nhập Google → thành công | Đạt: chọn tài khoản → `FirebaseAuth` báo người dùng → trang chủ |
| Trang chủ → avatar → Hồ sơ | Đạt: tên, email, ảnh, “Đã liên kết tài khoản Google” |
| Xoay ngang tại Hồ sơ | Đạt: giữ màn Hồ sơ, nội dung cuộn được |
| Bài 02: thêm → hoàn thành → sửa nhóm → xóa | Đạt: trang chủ 0/1 → 1/1 (100%) → chip “Cuộc sống” và vẫn hoàn thành → 0/0; `database:get` xác nhận dữ liệu trên server |
| Đăng xuất → Back | Đạt (sau khi sửa lỗi timeout): “Đã đăng xuất.” sau ~3 s; Back về danh sách bài, vào lại bài chỉ thấy màn đăng nhập; Back nữa thoát app |
| FCM đăng ký | Đạt: FID lưu ở `users/{uid}/deviceRegistrations` (không in giá trị); bị gỡ khi đăng xuất |
| FCM nhận + nhấn | Đạt: tin data-only gửi qua HTTP v1 tới `fid` (HTTP 200) → hộp thư trong app ghi nhận, thông báo hiện trên kênh `todo_updates`, nhấn mở “Sửa công việc” đúng task |
| Quyền thông báo Android 13+ | Đạt: hộp thoại hệ thống → “Quyền thông báo đang bật.” |
| Remote Config | Đạt: “Fetch thành công”, giới hạn 4 |
| Analytics | Đạt ở client (debug mode): `screen_view`, `task_complete(category=study)` được ghi và upload, không có PII. DebugView trên Console CHƯA KIỂM CHỨNG |
| Crashlytics non-fatal (debug) | Đạt ở client: “Crashlytics report successfully enqueued to DataTransport”. Console CHƯA KIỂM CHỨNG. Không thử crash thật |
| AdMob | Đạt: banner thử nghiệm tải được, nhãn “Test Ad” |
| Upload ảnh Storage | CHƯA KIỂM CHỨNG: bucket chưa tồn tại |
| Hai tài khoản Google | CHƯA KIỂM CHỨNG trên thiết bị (chỉ có 1 tài khoản); phân quyền giữa UID đã kiểm bằng rules Emulator |
| Lỗi mạng thật (tắt mạng) | CHƯA KIỂM CHỨNG trên thiết bị; có unit test cho lỗi listener và retry |

Trong lúc test, logcat ghi nhận một lần đăng xuất và một lần chuyển tab Cài đặt mà agent không thao tác; nhiều khả năng do thao tác tay trên cửa sổ emulator cùng lúc. Không tìm thấy đường code nào đăng xuất mà không qua hộp xác nhận.

Dữ liệu còn lại sau test: một công việc “Nop bai tap Firebase” của tài khoản chủ project, có thể xóa trong app.

## Đối chiếu Figma

Đã lấy ảnh 6 node (17870:1183, 741, 1235, 878, 1000, 1451) và so với ảnh chụp runtime:

- **Đăng nhập, trang chủ, hồ sơ, form thêm mới:** khớp bố cục, màu `#2F5BEA`, card bo góc, vòng tiến độ, chip nhóm, thanh điều hướng có nút +.
- **Khác biệt có chủ đích:**
  - Số liệu, tên và ngày lấy từ dữ liệu thật.
  - Dòng “Dùng trên điện thoại và máy tính bảng” thay cho “iPhone, iPad và Mac”.
  - Hồ sơ hiển thị “Việc đã xong / Đang chờ / Hoàn thành” thay cho “Chuỗi liên tiếp / Đúng hạn”, vì app không có dữ liệu thật cho hai chỉ số này.
  - Có thêm nhãn `‹ Bài 0x` nhỏ phía trên trang chủ.
- Chưa so pixel-perfect; chưa thử tăng cỡ chữ hệ thống.

## Tự review

- **Kiến trúc:** UI chỉ nhận state bất biến (StateFlow) và gọi callback; mọi thao tác Firebase đi qua repository.
- **Ghi dữ liệu và upload:** chặn bấm lặp; listener được gỡ khi đổi UID.
- **Đăng xuất:** luôn kết thúc phiên Firebase, kể cả khi gỡ FCM hoặc xóa credential bị lỗi hay quá hạn. Trường hợp này có unit test riêng.
- **Bí mật:** không có server key, service account, mật khẩu hay token trong source. Script gửi FCM thử chỉ nằm ở scratchpad, dùng quyền CLI của chủ project, không được commit.
- **Giới hạn còn lại:**
  - Chưa có backend lập lịch nhắc việc hoặc tổng kết.
  - Thao tác ghi khi mất mạng trả lỗi “mất kết nối” thay vì xếp hàng chờ.
  - Tùy chọn giao diện chưa được lưu lại sau khi app bị hệ thống đóng.
