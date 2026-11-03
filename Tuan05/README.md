# TUẦN 05 — UTH Todo và Firebase

**Sinh viên:** Trần Đức Trung · **MSSV:** 001206004489.

Project: [Tuan05_Project](./Tuan05_Project), package/applicationId `com.example.tuan05project`.
Thiết kế: [UTH Mobile](https://www.figma.com/design/ivYBomXm4kJqddjjzvqhl1/UTH-Mobile--Copy-?node-id=0-1&m=dev).

Đã triển khai code cho hai bài và giữ bài đăng nhập cũ. **Không coi việc có SDK hoặc build thành công là dịch vụ cloud đã hoạt động.**
Kết quả thực thi, thiết bị, kiểm chứng Firebase và giới hạn nằm trong [VERIFICATION.md](./VERIFICATION.md).

## Mở và chọn bài

1. Android Studio → Open → `Tuan05/Tuan05_Project`; chờ Gradle Sync.
2. Chọn emulator có Google Play hoặc thiết bị có Credential Manager/Google Play Services.
3. Run module `app`. Màn hình đầu tiên là danh sách bài: **Bài 01 — Firebase Authentication**, **Bài 02 — Tích hợp dịch vụ Firebase** và **Bài cũ — Login by Gmail** (`features/lessons/LessonPickerScreen.kt`).
4. Trong mỗi bài, dòng nhãn `‹ Bài 0x — …` phía trên trang chủ hoặc phím Back (từ tab Trang chủ) trở về danh sách bài; phiên Firebase vẫn được giữ. Đăng xuất tại Hồ sơ để kết thúc phiên.
5. Hai bài dùng chung một phiên Firebase nhưng tách luồng: Bài 01 chỉ dùng Authentication (không ghi dữ liệu); Bài 02 bật Realtime Database, Storage, FCM, Analytics, Remote Config, Crashlytics và AdMob thử nghiệm.

### Bài 01 — Firebase Authentication

- Credential Manager mở chọn tài khoản Google; ID token được xác thực bằng Firebase Authentication.
- Chỉ thông báo thành công sau khi `signInWithCredential` hoàn tất; có trạng thái bận, chặn bấm lặp, thông báo lỗi cấu hình/mạng/xác thực và phản hồi khi hủy.
- Trang chủ dùng tên/ảnh thật. Nhấn avatar để mở Hồ sơ: tên, email, provider, ảnh hoặc chữ viết tắt; có fallback khi thiếu dữ liệu.
- AuthStateListener khôi phục phiên; trong lúc Firebase chưa báo trạng thái phiên, app hiện vòng chờ thay vì nháy màn đăng nhập (`AuthUiState.restoring`). ViewModel và `rememberSaveable` giữ bài/route khi xoay màn hình. Khi đăng xuất, Firebase sign-out và Credential Manager clear đều được thực hiện; điều hướng kiểm tra phiên trước mọi màn hình bảo vệ.
- Phiên khách được gắn nhãn riêng, không liên kết Google và không ghi dữ liệu Firebase.
- Thống kê công việc ở Bài 01 bắt đầu rỗng; không dùng dữ liệu mẫu để giả lập Firebase. CRUD và đồng bộ nằm ở Bài 02.
- Không thấy tài liệu chấm bài local bắt buộc Facebook/email/phone ngoài Google; hai slide đề bài dùng Google làm màn hình minh họa.

### Bài 02 — Tích hợp dịch vụ Firebase

Tái sử dụng Authentication của Bài 01. Realtime Database lưu công việc theo UID, lắng nghe cập nhật, thêm/sửa/xóa/đánh dấu hoàn thành; trang chủ, tìm kiếm, nhóm và thống kê tính từ danh sách thật. Không có seed dữ liệu mẫu và không thay Realtime Database bằng Firestore.

Hồ sơ có chọn ảnh JPEG/PNG/WebP ≤ 5 MiB, tiến độ upload, báo thành công/lỗi. Ảnh nằm ở `users/{uid}/avatar`, được đọc bằng Storage SDK có xác thực; app không xuất download URL công khai.

Cài đặt → **Dịch vụ Firebase** (hoặc tab Thông báo trong Bài 02): cấp quyền thông báo, đăng ký lại token FCM, xem tin FCM đã nhận, fetch/activate Remote Config, kiểm thử Crashlytics trong debug và tải banner AdMob thử nghiệm ở khu vực riêng.

Nhắc việc trước hạn và tổng kết 21:00 **chưa có backend lập lịch**. Các công tắc phản hồi giới hạn này thay vì giả vờ đã lên lịch. Âm thanh thao tác và tùy chọn giao diện hoạt động cục bộ trong phiên ViewModel.

## Cấu trúc source

```text
Tuan05/
├── Tuan05_Project/
│   ├── app/src/main/java/com/example/tuan05project/
│   │   ├── MainActivity.kt
│   │   ├── auth/                    # repository, Google credential, state, ViewModel, lỗi
│   │   ├── core/ui/                 # màu icon system bars theo theme
│   │   ├── data/                    # RTDB, codec, avatar, registration, telemetry, Remote Config
│   │   ├── navigation/              # Lesson enum, điều hướng, kiểm tra phiên (SessionGate)
│   │   ├── messaging/               # FCM service, channel, inbox nhận thực tế
│   │   └── features/
│   │       ├── lessons/             # màn chọn Bài 01 / Bài 02 / bài cũ
│   │       ├── legacy/              # giữ UI Login by Gmail cũ
│   │       ├── services/            # trạng thái và trang demo dịch vụ
│   │       └── todo/
│   │           ├── model/
│   │           ├── viewmodel/       # immutable state, StateFlow, sự kiện một chiều
│   │           └── ui/{account,common,main,task,theme}/
│   ├── app/src/debug/               # nút crash/non-fatal chỉ trong debug
│   ├── app/src/release/             # không có nút gây crash
│   └── app/src/test/                # auth, restore phiên, navigation, CRUD/tiến độ, analytics không PII, lỗi/retry
├── firebase/                       # rules, emulator tests, Remote Config, mẫu FCM/Test Lab
├── hosting/index.html              # trang tĩnh chuẩn bị; chưa deploy
└── VERIFICATION.md
```

### Tái sử dụng Tuần 04 và đối chiếu Figma

Đọc trực tiếp bản local Tuần 04, gồm cả các UI đang chưa commit; **không sửa file Tuần 04** và không tạo phụ thuộc source giữa hai project.
Lấy phần phù hợp từ `features/todo/ui/{common,theme,main,task,account}`, model và formatter: màu `#2F5BEA`, nền `#F5F5F7`, card bo góc, vòng tiến độ, chip nhóm, thanh điều hướng có nút +, form ngày/giờ/ưu tiên và bố cục hồ sơ. Các icon vector tương ứng cũng được copy sang resources Tuần 05. Font Be Vietnam Pro được cấu hình qua Android Downloadable Google Fonts, có fallback hệ thống khi offline/không có provider; chứng thư provider công khai lấy từ mẫu Jetchat chính thức (giữ license).

Các màn hình được chuyển sang nhận state bất biến và callbacks, thay tài khoản/dữ liệu mẫu bằng repository Firebase. Không copy repository giả của Tuần 04 hoặc các bài thư viện/dataflow/onboarding không thuộc nhiệm vụ.

Đã đọc riêng cấu trúc, thông số và ảnh của các node:
[đăng nhập 17870:1183](https://www.figma.com/design/ivYBomXm4kJqddjjzvqhl1/UTH-Mobile--Copy-?node-id=17870-1183),
[trang chủ 17870:741](https://www.figma.com/design/ivYBomXm4kJqddjjzvqhl1/UTH-Mobile--Copy-?node-id=17870-741),
[hồ sơ 17870:1235](https://www.figma.com/design/ivYBomXm4kJqddjjzvqhl1/UTH-Mobile--Copy-?node-id=17870-1235),
[danh sách 17870:878](https://www.figma.com/design/ivYBomXm4kJqddjjzvqhl1/UTH-Mobile--Copy-?node-id=17870-878),
[thêm mới 17870:1000](https://www.figma.com/design/ivYBomXm4kJqddjjzvqhl1/UTH-Mobile--Copy-?node-id=17870-1000),
[cài đặt 17870:1451](https://www.figma.com/design/ivYBomXm4kJqddjjzvqhl1/UTH-Mobile--Copy-?node-id=17870-1451).
Không vẽ khung điện thoại, vùng canvas và tiêu đề đánh số vào app. Insets dùng vùng an toàn của Android, form có IME padding và cuộn. Mức khớp thị giác thực tế xem báo cáo kiểm chứng; chưa mặc định đạt pixel-perfect.

## Firebase: đã kết nối và phần còn lại

File `app/google-services.json` đúng package, project `uthsmarttasks-week5`, có OAuth client Android (SHA-1 khớp keystore debug) và Web; giữ nguyên file này. Đây là cấu hình client, không phải service-account key. Không có key máy chủ/token trong source.

Đã thực hiện ngày 2026-11-03 bằng Firebase CLI do chủ project tự đăng nhập:

| Hạng mục | Trạng thái |
|---|---|
| Realtime Database | Đang hỗ trợ; [Android read/write](https://firebase.google.com/docs/database/android/read-and-write), [rules](https://firebase.google.com/docs/database/security) | CRUD, completion, listener và tiến độ thật trong FirebaseTaskRepository; rules theo UID | **Đã chạy thật** trên emulator: thêm/sửa/hoàn thành/xóa, server phản ánh đúng; rules Emulator 5/5; anonymous bị 401 | Hai tài khoản Google thật CHƯA KIỂM CHỨNG (rules Emulator đã chặn UID khác) |
| Database rules | Đã deploy [database.rules.json](./firebase/database.rules.json) (`firebase deploy --only database`). Đọc REST không xác thực trả **401 Permission denied** |
| Remote Config | Đang hỗ trợ; [Android](https://firebase.google.com/docs/remote-config/android/get-started) | Default 4, fetchAndActivate, clamp home_task_limit, hiển thị trạng thái | Template đã publish (v1); app fetch thành công trên emulator | Đổi giá trị trên Console để thử kích hoạt giá trị mới |
| Storage | Cloud Storage đang hỗ trợ; [upload](https://firebase.google.com/docs/storage/android/upload-files), [billing](https://firebase.google.com/docs/storage/faqs-storage-changes-announced-sept-2024) | Upload avatar có progress/error, đọc bytes qua SDK, MIME/5 MiB, owner rules | Rules Emulator hai UID/MIME/size; upload thật **CHƯA KIỂM CHỨNG** vì bucket chưa tồn tại (404) | Nâng Blaze, tạo bucket, deploy storage rules |
| Authentication | Đang hỗ trợ; [Google + Credential Manager](https://firebase.google.com/docs/auth/android/google-signin) | Dùng chung AuthRepository/AuthViewModel cho hai bài; restore, sign-out, fallback hồ sơ | **Đã chạy thật**: hủy, đăng nhập thành công, khôi phục phiên, đăng xuất + Back; unit test | Thêm SHA release khi phát hành |

Việc còn lại cho chủ project:

1. Muốn dùng Storage: nâng Blaze → Console → Storage → *Get started*, rồi từ `Tuan05/firebase` chạy `npx -y firebase-tools@latest deploy --only storage --project uthsmarttasks-week5`.
2. Khi ký bản release, thêm SHA-1/SHA-256 release vào Firebase (`.\gradlew.bat :app:signingReport`) và tải lại `google-services.json` bằng `apps:sdkconfig`.
3. Mở Console → Crashlytics / Analytics DebugView để xem báo cáo đã gửi (client đã gửi, Console **CHƯA KIỂM CHỨNG**).

Dữ liệu: `users/{uid}/tasks/{pushId}`, `users/{uid}/profile/avatarVersion`, `users/{uid}/deviceRegistrations/{installationId}`. FCM 26 dùng `register()`/`onRegistered` bằng FID (chạy ngoài main thread) và `unregister()` khi đăng xuất; `onNewToken` vẫn xử lý token legacy. Không in identifier/token ra UI/log. Tham khảo [FCM hiện hành](https://firebase.google.com/docs/cloud-messaging/android/get-started) và [gửi bằng fid](https://firebase.google.com/docs/cloud-messaging/send/admin-sdk).

### Gửi FCM và kiểm tra điều hướng

Mẫu [fcm-message.example.json](./firebase/fcm-message.example.json) là **template**, không có endpoint/credential giả được dùng trong app.
Backend tin cậy dùng Admin SDK hoặc HTTP v1, kiểm tra UID sở hữu token và task trước khi gửi. Thay ba placeholder trên **máy chủ**, dùng trường fid cho registration kind=fid (token cho legacy), không đặt tài khoản dịch vụ trong APK. Khuyến nghị data-only với `recipientUid` và `taskId`: service kiểm tra UID, ghi inbox, tạo notification thật từ FCM và PendingIntent rõ đích. Nhấn tin chỉ mở task được tải qua rules của UID hiện tại.

Thông báo từ Notifications Composer có thể được hệ thống tự hiển thị khi app ở nền; thêm data `recipientUid`, `taskId` để điều hướng sau tap. Việc hệ thống hiển thị không chứng minh callback foreground đã chạy; kiểm tra cả foreground/background/killed. Chưa có sender backend, bộ lập lịch hay kết quả delivery production. Khi sign-out, app cố gỡ đăng ký token, unregister FCM, dọn thông báo hệ thống; backend vẫn phải xử lý token hết hạn và ràng buộc UID.

### Analytics, Crashlytics, quảng cáo

`login(method=google)` chỉ sau xác thực thành công; `screen_view` theo màn hình thực; `task_create/task_edit/task_complete(category)` sau server acknowledgement. Không log tên/email/UID/token/nội dung công việc trong custom events.

Debug: trang Dịch vụ có non-fatal và crash cần xác nhận. Mở lại app sau crash rồi kiểm tra Console; nút không tồn tại trong source set release.
AdMob dùng sample app ID `ca-app-pub-3940256099942544~3347511713` và banner test `ca-app-pub-3940256099942544/6300978111`. Chỉ request khi mở demo và nhấn tải; không đưa banner vào sáu màn Figma chính. Chưa tích hợp quảng cáo production/consent flow cho phát hành.

## Đối chiếu toàn bộ sơ đồ dịch vụ cũ của Bài 02

Đối chiếu tài liệu chính thức ngày 2026-11-02; kết nối Firebase thật và kiểm tra trên emulator ngày 2026-11-03. “Đã có code” khác “đã có xác nhận cloud”. Các sản phẩm ngoài Android không được coi là xong chỉ bằng dependency.

| Mục đề bài | Tình trạng và nguồn chính thức | Áp dụng / code hoặc cấu hình | Kiểm chứng | Phần chủ tài khoản cần làm |
|---|---|---|---|---|
| Realtime Database | Đang hỗ trợ; [Android read/write](https://firebase.google.com/docs/database/android/read-and-write), [rules](https://firebase.google.com/docs/database/security) | CRUD, completion, listener và tiến độ thật trong FirebaseTaskRepository; rules theo UID | **Đã chạy thật** trên emulator: thêm/sửa/hoàn thành/xóa, server phản ánh đúng; rules Emulator 5/5; anonymous bị 401 | Hai tài khoản Google thật CHƯA KIỂM CHỨNG (rules Emulator đã chặn UID khác) |
| Authentication | Đang hỗ trợ; [Google + Credential Manager](https://firebase.google.com/docs/auth/android/google-signin) | Dùng chung AuthRepository/AuthViewModel cho hai bài; restore, sign-out, fallback hồ sơ | **Đã chạy thật**: hủy, đăng nhập thành công, khôi phục phiên, đăng xuất + Back; unit test | Thêm SHA release khi phát hành |
| Cloud Messaging | FCM hiện hành; [Android setup](https://firebase.google.com/docs/cloud-messaging/android/get-started), [receive](https://firebase.google.com/docs/cloud-messaging/android/receive-messages) | FCM 26: register/onRegistered bằng FID, callback token legacy, đăng ký UID, service nhận data, channel, tap task | **Đã chạy thật**: FID lưu theo UID, tin HTTP v1 gửi tới `fid` được nhận, hiện thông báo, nhấn mở đúng task; gỡ đăng ký khi đăng xuất | Backend gửi tự động/lịch nhắc chưa có; không có server key/service account trong app |
| Storage | Cloud Storage đang hỗ trợ; [upload](https://firebase.google.com/docs/storage/android/upload-files), [billing](https://firebase.google.com/docs/storage/faqs-storage-changes-announced-sept-2024) | Upload avatar có progress/error, đọc bytes qua SDK, MIME/5 MiB, owner rules | Rules Emulator hai UID/MIME/size; upload thật **CHƯA KIỂM CHỨNG** vì bucket chưa tồn tại (404) | Nâng Blaze, tạo bucket, deploy storage rules |
| Hosting | Hosting cho web, không phải Android SDK; [quickstart](https://firebase.google.com/docs/hosting/quickstart) | Trang giới thiệu/quyền riêng tư ở hosting/index.html, firebase.json public dir | Có file; CHƯA deploy, CHƯA có URL Hosting được xác minh | Chọn site/project và chủ động deploy khi được phép; không coi file là dịch vụ đã chạy |
| Remote Config | Đang hỗ trợ; [Android](https://firebase.google.com/docs/remote-config/android/get-started) | Default 4, fetchAndActivate, clamp home_task_limit, hiển thị trạng thái | Template đã publish (v1); app fetch thành công trên emulator | Đổi giá trị trên Console để thử kích hoạt giá trị mới |
| Test Lab | Đã deprecated, dự kiến đóng 2027-09-30; [migration sang Developer Device Platform](https://firebase.google.com/docs/test-lab/migrate) | test-lab.example.yaml chuẩn bị Robo test APK; DDP là sản phẩm thay thế riêng | CHƯA submit cloud test; kiểm tra quota và chọn device ID thật trước chạy | Quyền project, quota/billing, chọn thiết bị/API; không tự chạy dịch vụ có phí |
| Crash Reporting | Sản phẩm cũ đã ngừng 2018-09-09; [thông báo Firebase](https://firebase.googleblog.com/2018/03/firebase-crashlytics-graduates-from-beta.html). Thay bằng **Crashlytics**, không gọi là SDK cũ | Crashlytics SDK + plugin, controls riêng debug; [setup hiện hành](https://firebase.google.com/docs/crashlytics/android/get-started) | Non-fatal debug được ghi và gửi (log “report successfully enqueued”); hiển thị trên Console CHƯA KIỂM CHỨNG | Mở Crashlytics trên Console để xác nhận |
| Analytics | Google Analytics for Firebase hiện hành; [get started](https://firebase.google.com/docs/analytics/get-started) | Telemetry event sau hành động thật, không gửi PII | Debug mode trên emulator: `screen_view`, `task_complete(category)` được ghi và upload; không có tên/email/UID | DebugView trên Console CHƯA KIỂM CHỨNG |
| Notifications | Notifications Composer hiện thuộc FCM; [gửi từ Console](https://firebase.google.com/docs/cloud-messaging/send/firebase-console) | Cùng pipeline FCM + quyền Android 13+, channel và tap route, không thêm SDK “Notifications” riêng | Quyền POST_NOTIFICATIONS cấp trên API 36; tin FCM thật hiển thị và tap mở task; không dùng local notification | Lịch nhắc/tổng kết cần backend chưa có |
| App Indexing | Không còn là cách khuyến nghị cho Google Search; [Firebase FAQ](https://firebase.google.com/support/faq#app-indexing) đề xuất Android App Links/AppSearch | Không đưa task riêng tư lên Search; tìm kiếm task trong app. App Links là phương án khác, chưa khai báo host không sở hữu | CHƯA triển khai verified App Links | Nếu cần: domain sở hữu, HTTPS, assetlinks.json với SHA-256 thật, Digital Asset Links và kiểm tra quyền task |
| Dynamic Links | Đã ngừng ngày 2025-08-25; [FAQ](https://firebase.google.com/support/dynamic-links-faq) | Không thêm SDK/link page.link. Tap FCM dùng intent nội bộ; App Links có thể dùng khi có domain, không cung cấp deferred deep link tương đương | Không tuyên bố đã chạy Dynamic Links | Chọn giải pháp deep link phù hợp nếu cần chia sẻ web/install; chưa có domain và chưa deploy |
| Invites | SDK cũ deprecated/removed; [Android release notes](https://firebase.google.com/support/release-notes/android), [Apple removal notes](https://firebase.google.com/support/release-notes/ios) | Không dùng Firebase Invites hoặc gợi ý Dynamic Links đã ngừng. Có thể dùng Android Sharesheet để chia sẻ URL trang giới thiệu sau khi có URL thật; chưa triển khai referral backend | CHƯA kiểm chứng referral/invite; không xem Sharesheet là Firebase Invites | Quyết định domain/link, quy trình mời và kiểm soát truy cập; không chia sẻ task riêng tư công khai |
| AdWords | Tên hiện hành là **Google Ads**; [Firebase integration](https://firebase.google.com/docs/ads) | Analytics có sự kiện phù hợp để chọn conversion; không có Android AdWords SDK cần thêm | CHƯA link tài khoản, import conversion hoặc chạy campaign | Chủ project và admin Ads liên kết tài khoản, xét conversion/ngân sách; agent không bật chiến dịch |
| AdMob | Đang hỗ trợ; GMA Legacy 25.5.0 còn có hướng dẫn, có đường chuyển Next-Gen; [setup](https://developers.google.com/admob/android/quick-start), [test ads](https://developers.google.com/admob/android/test-ads) | Trang demo riêng với sample app/banner ID chính thức, lifecycle destroy AdView, báo load/error | **Đã chạy**: banner thử nghiệm tải được, có nhãn “Test Ad” | Chưa dùng ad unit production; phát hành cần tài khoản/consent/policy phù hợp |

## Dependencies và kiểm thử

Giữ AGP `9.5.0-alpha08`, built-in Kotlin, Compose compiler `2.4.21`, Compose BoM `2026.02.01`, Firebase BoM `35.1.0` và applicationId hiện có. Không thêm `org.jetbrains.kotlin.android`. SDK Firebase dùng main modules, không dùng KTX đã ngừng phát hành; phiên bản được đối chiếu [release notes chính thức](https://firebase.google.com/support/release-notes/android). Chỉ thêm SDK và hạ tầng lifecycle/coroutines/ảnh cần cho hai bài.

Tại `Tuan05/Tuan05_Project`:

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug
```

Tại `Tuan05/firebase`, Node và Java phù hợp Firebase CLI:

```powershell
npm ci
npx -y firebase-tools@latest emulators:exec --project demo-tuan05 --only database,storage "npm test"
```

`demo-tuan05` chỉ là project ID **của emulator local** để kiểm tra rules; không thay cấu hình Firebase production trong app. Test dùng mock auth context của Emulator để xác minh allow/deny thật, không kiểm tra luôn-đúng. Các test production, Google chooser, upload, FCM và Console còn cần môi trường được cấp quyền.

Không commit local.properties, service-account JSON, private key/token/mật khẩu, node_modules, build outputs hoặc dữ liệu thiết bị. Không tự triển khai Hosting, chạy Test Lab/DDP, bật billing hay quảng cáo trả phí.
