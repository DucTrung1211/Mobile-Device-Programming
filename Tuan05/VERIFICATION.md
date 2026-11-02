# Kiểm chứng Tuần 05

Thực hiện 2026-11-02 đến 2026-11-03 (Asia/Saigon). Báo cáo này phân biệt code, unit/emulator test, preview và dịch vụ cloud thực tế.

## Khảo sát và phạm vi

- Git root: repository Mobile-Device-Programming, project mở đúng `Tuan05/Tuan05_Project`, package `com.example.tuan05project`.
- Điểm bắt đầu: `main` tại `ac7eae9`; `origin/main` cùng commit khi fetch ban đầu. Làm việc trên `codex/tuan05-firebase`.
- Remote đã xác minh: `https://github.com/DucTrung1211/Mobile-Device-Programming.git`.
- Không tìm thấy AGENTS.md áp dụng tại các thư mục cha hoặc bên trong repository/project bằng công cụ IDE. Không có quy định nơi lưu báo cáo riêng; chỉ dùng file này.
- Tuần 02 và Tuần 04 có nhiều thay đổi của chủ repository, gồm một deletion đã stage ở Tuần 04. Giữ nguyên chúng; commit nhiệm vụ phải giới hạn README root và Tuần 05.
- Không thay Git identity, applicationId, AGP/Kotlin hiện có hoặc config Firebase thật. Không triển khai cloud, bật billing, chạy quảng cáo trả phí, Test Lab/DDP hay Hosting.

## Build / test

| Kiểm tra | Lệnh / công cụ | Exit / kết quả |
|---|---|---|
| Baseline trước chỉnh sửa | `.\gradlew.bat :app:assembleDebug` tại Tuan05_Project | 0, BUILD SUCCESSFUL |
| Lệnh bắt buộc trên mã cuối | `.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug --console=plain` | 0, BUILD SUCCESSFUL |
| Unit test thực thi | Android Studio Gradle build `:app:testDebugUnitTest :app:assembleDebug :app:compileReleaseKotlin --console=plain` | 9 passed, 0 skipped, 0 failed; build thành công |
| Source set release | `:app:compileReleaseKotlin` | Thành công; controls gây crash chỉ nằm trong debug |
| Rules local, schema cuối | `npx -y firebase-tools@latest emulators:exec --project demo-tuan05 --only database,storage "npm test"` tại Tuan05/firebase | 0; 5 nhóm test pass, 0 fail, 0 skipped |
| IDE Gradle Sync | studio_mcp_gradle_sync | Thành công |
| Consumers | get_artifact_consumers(:app/main) | Chỉ unitTest/androidTest của app; không có module production khác |
| Whitespace | `git diff --check -- README.md Tuan05` | 0, không có lỗi whitespace |

Unit tests kiểm tra: chỉ xác nhận Firebase mới cho success; chặn login trùng; cancellation không thành success; lỗi mạng khác lỗi config; restore phiên; cleanup token thất bại vẫn sign-out/clear credential; codec giữ completion; thống kê/filter từ dữ liệu đầu vào; route bảo vệ sau logout; guest tách biệt; thiếu thông tin người dùng; lỗi save giữ editor và đổi UID xóa state cũ. Repository fake chỉ dùng trong unit tests, không có mock Firebase trong luồng app.

Emulator tests thực thi rules thật: chủ UID có CRUD/completion; UID khác và anonymous bị từ chối đọc/ghi/xóa; đọc root users bị từ chối; dữ liệu sai/field thừa bị từ chối; registration FCM theo UID; Storage chặn UID khác/anonymous/MIME không hợp lệ/ảnh vượt 5 MiB và cho phép owner đọc/xóa.

Trong quá trình làm có compilation lỗi ở bước UI chưa nối xong và một lần build bị giới hạn thời gian; đã sửa và chạy lại thành công như bảng trên. Không lấy các lần lỗi làm kết quả cuối.

## Runtime

- AVD thử: `Medium_Phone_API_36.1`, serial `emulator-5554`; ADB liệt kê model `sdk_gphone64_x86_64`.
- API theo tên AVD là 36.1; `getprop ro.build.version.sdk` không trả kết quả trong thời hạn, nên **API thực tế CHƯA KIỂM CHỨNG**.
- Đã thử mở AVD thường và cold/headless bằng Android CLI; có xuất hiện trong `adb devices`, nhưng Android CLI start/run và các lệnh ADB shell tới emulator timeout. Chưa xác nhận cài APK/mở Activity thành công.
- Vì vậy **runtime ứng dụng CHƯA KIỂM CHỨNG**. Không dùng giao diện của thiết bị thật đang phục vụ công việc khác làm bằng chứng kiểm thử.

## Visual

Đọc riêng design context và screenshot của cả sáu node Figma: 17870:1183, 17870:741, 17870:1235, 17870:878, 17870:1000, 17870:1451.

Đã render và xem bằng Android Studio Compose Preview: LoginPreview, HomePreview, ListPreview, FormPreview, ProfilePreview, SettingsPreview (390 × 844 dp). Đối chiếu màu, card, progress ring, filter/chip, nút +/bottom bar, form, theme selector và biểu tượng hồ sơ; đã chỉnh camera/check/refresh/lock/logout, line height và phần chữ phụ tránh tràn.

Fixture chỉ ở `app/src/debug/.../preview/TodoPreviews.kt`, không được route trong app và không dùng để xác nhận Firebase thành công. Nội dung tài khoản, ngày và việc trong preview là minh họa; app runtime lấy phiên và dữ liệu thật.

Giới hạn: đây là **kiểm tra preview**, chưa phải screenshot runtime/pixel-perfect. Be Vietnam Pro được cấu hình qua Downloadable Google Fonts, có system fallback; chưa xác minh tải font thực trên thiết bị. Insets Android và dữ liệu/nhãn trạng thái khác mẫu thiết kế; chưa kiểm thử runtime ở màn hình nhỏ, landscape hay tăng cỡ chữ.

## Interaction

| Luồng | Đã kiểm tra | Chưa kiểm chứng |
|---|---|---|
| Mở riêng Bài 01, Bài 02 và bài cũ | Route/code review; unit gate | Nhấn menu trên thiết bị |
| Google lỗi/hủy/thành công | Unit state/error/duplicate guard | Chooser thật, Google token và Firebase login thật |
| Trang chủ → hồ sơ → đăng xuất → Back | Unit gate/logout; code review callbacks | Chuỗi thao tác runtime |
| Restore / xoay màn hình | Auth flow unit; ViewModel + primitive rememberSaveable form | Process restart, rotation trên thiết bị |
| CRUD/completion/thống kê | Codec/state unit; rules Emulator CRUD | Android client ↔ DB production, lỗi mạng production |
| Hai tài khoản | Rules Emulator deny/allow; state reset khi đổi UID | Hai Google accounts thật |
| Upload | Rules Emulator owner/MIME/size; code review progress/error/cancel | Upload ảnh thật tới bucket production và reload ảnh |
| Thông báo | Code review FID register/onRegistered, legacy token callback, UID guard, channel, permission, tap route | Quyền Android thực, gửi/nhận FCM foreground/background/killed, tap thật |
| Analytics / Remote Config / Crashlytics / AdMob | Có code, compile và preview cho khu vực UI liên quan | DebugView, fetch cloud, báo cáo crash trên Console, banner test load trên thiết bị |

Không dùng notification local để khẳng định đã kiểm chứng FCM. Không tạo crash tự động lúc mở app.

## Firebase / Console

- Firebase CLI 15.34.2 có sẵn; `login:list` trả “No authorized accounts” khi khảo sát.
- Config client hiện có: project `uthsmarttasks-week5`, package đúng, Android OAuth + Web OAuth; file giữ nguyên.
- Config chưa chứa Realtime Database URL. Resource `todo_database_url` để trống có chủ đích; app báo thiếu cấu hình thay vì đoán endpoint hoặc hiển thị fake success.
- Production Authentication, Database, Storage, FCM delivery, Analytics, Remote Config và Crashlytics: **CHƯA KIỂM CHỨNG**.
- Rules Emulator pass là bằng chứng phân quyền local, không phải bằng chứng rules đã deploy.
- Cần chủ tài khoản kiểm tra Google provider/SHA-1/SHA-256, URL DB, deploy rules, bucket/billing hiện có, Analytics/Crashlytics và parameter Remote Config. FCM còn cần Console/sender tin cậy, ràng buộc UID và backend lập lịch nếu muốn nhắc tự động.
- Hosting/Test Lab/DDP/Google Ads: chỉ chuẩn bị tài liệu/cấu hình, chưa thực thi; các sản phẩm cũ được giải thích trong bảng 15 mục của README.

## Self-review

- UI và Firebase tách trách nhiệm; state bất biến/StateFlow; listener được tháo khi hủy collection hoặc đổi UID; thao tác write/upload bị chặn lặp.
- Không giữ dữ liệu UID trước khi đổi phiên; root không render state của người khác khi chờ bind. Back không vượt qua session gate.
- Sign-out vẫn clear Credential Manager nếu cleanup FCM lỗi; unregister và tắt auto-init khi thoát. Backend phải tiếp tục xóa registration hết hạn, xử lý UID và retry/quota.
- Upload ảnh riêng tư bằng Storage SDK, không chia sẻ bearer download URL. Client/rules giới hạn MIME và 5 MiB.
- Analytics custom events không nhận email, tên, UID, token hoặc nội dung công việc; chỉ log sau hành động thật được xác nhận.
- RTDB local write có thể phát event trước server acknowledgement. UI chỉ báo lưu thành công sau await; nếu timeout thì nhắc kiểm tra dữ liệu sau khi kết nối lại vì write có thể còn chờ SDK. Chưa triển khai hàng đợi đồng bộ có trạng thái pending riêng.
- Tùy chọn giao diện cục bộ giữ qua xoay màn hình trong ViewModel, chưa persist qua process death. Không trình bày cài đặt nhắc/tổng kết như đã có scheduler.
- Chỉ dùng sample AdMob IDs chính thức; không có service account/private key/token được đưa vào app/source; public certificates trong font_certs.xml là chứng thư provider Google Fonts, không phải secret.
- Vẫn cần kiểm tra runtime/cloud và font thực trước khi khẳng định hoàn thành tích hợp đầu cuối.
