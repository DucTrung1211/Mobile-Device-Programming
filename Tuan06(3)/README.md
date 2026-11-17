# Tuần 06 (3) — Crypto Monitor

Ứng dụng riêng cho ảnh 5; applicationId `com.uth.week6.crypto`.

## Thiết kế và chức năng

Nguồn: [Figma 11245:1524](https://www.figma.com/design/ivYBomXm4kJqddjjzvqhl1/UTH-Mobile--Copy-?node-id=11245-1524).
Đã đọc trực tiếp context/screenshot, gồm dòng yêu cầu gọi đồng thời 4–5 API và gọi lại sau 3 giây.
Home, New/banner, Favorites và All Fluctuations (ETH/BTC/LTC/XMR).
Banner/icon gốc tải vào assets; biểu đồ là dữ liệu nhận trong phiên, không dùng đường vẽ giá mẫu Figma.
Nền #1D212D, card #33373D, chữ phụ #9197A9, tăng #2DAA20, giảm #F81506.
SF Pro Text trong Figma được thay bằng sans-serif Android; chưa khẳng định giống từng pixel.

## Bốn nguồn thật

| Nguồn | GET | Dữ liệu và ý nghĩa phần trăm |
|---|---|---|
| Kraken | https://api.kraken.com/0/public/Ticker?pair=XBTUSD,ETHUSD,LTCUSD,XMRUSD | 4 coin, last c[0], open o; biến động từ mở cửa ngày UTC |
| Coinbase Exchange | https://api.exchange.coinbase.com/products/BTC-USD/stats | BTC, last/open; 24 giờ |
| Gemini | https://api.gemini.com/v2/ticker/ethusd | ETH, close/open; 24 giờ |
| Bitstamp | https://www.bitstamp.net/api/v2/ticker/ltcusd/ | LTC, last, open_24 và percent_change_24; 24 giờ |

Tài liệu chính thức:
[Kraken](https://docs.kraken.com/api-reference/market-data/get-ticker-information),
[Coinbase](https://docs.cdp.coinbase.com/api-reference/exchange-api/rest-api/products/get-product-stats),
[Gemini](https://developer.gemini.com/trading/rest-api/market-data/get-ticker),
[Bitstamp](https://www.bitstamp.net/api/).
Tất cả endpoint trên đã trả HTTP 200 ngày 08/11/2026 trên máy phát triển,
và test Retrofit live nhận được 4/1/1/1 quote hợp lệ tương ứng.

Không yêu cầu API key. Giá là USD, giá trị và phần trăm không hardcode/random.
Ưu tiên nguồn tươi đầu tiên theo thứ tự bảng; nếu nguồn lỗi dùng nguồn tươi còn lại,
nếu không có thì giữ giá tốt gần nhất và gắn nhãn cũ. Màn chi tiết ghi nguồn, ý nghĩa phần trăm,
thời gian app nhận phản hồi; không mạo nhận đó là timestamp giao dịch của sàn.

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


## Cấu trúc và luồng

- model/Market.kt: coin, quote, kỳ biến động, snapshot từng nguồn và mẫu biểu đồ.
- data/CryptoRepository.kt: Retrofit suspend @GET/@Url, parser riêng mỗi nguồn,
  supervisorScope + async/awaitAll, timeout và backoff.
- viewmodel/CryptoViewModel.kt: StateFlow, vòng polling có Mutex, lịch sử tối đa 60 mẫu/coin/nguồn,
  yêu thích lưu SharedPreferences.
- ui/CryptoApp.kt: danh sách, card, biểu đồ Canvas, yêu thích và dialog thông tin coin.
- MainActivity: Compose + thanh hệ thống sáng trên nền tối.

Mỗi lượt chạy đồng thời 4 nguồn → đợi mọi nguồn kết thúc → delay(3000) → lượt tiếp.
Không dùng timer chồng request; recomposition không tạo thêm vòng polling.
repeatOnLifecycle(STARTED) hủy coroutine/HTTP khi app xuống nền và bắt đầu lại khi foreground.
Mutex bảo vệ vòng lặp/repository khi chuyển Activity. CancellationException luôn truyền tiếp.
Mỗi request timeout tối đa 12 giây. HTTP 429 giữ Retry-After (số giây hoặc HTTP date),
chờ tối thiểu 30 giây; lỗi khác backoff 3–60 giây riêng từng nguồn.
Giữ quote cũ, trạng thái lỗi và thời gian chờ; không thay lỗi thành giá 0.
Biểu đồ tách từng nguồn, tối đa 60 mẫu nhận trong phiên; dưới 2 mẫu ghi đang tích lũy.

## Kiểm chứng và giới hạn

- testDebugUnitTest, assembleDebug và lintDebug thành công, đã chạy lại sau khi sửa màu chữ nền tối.
- 7 unit test: 4 request thực sự đồng thời, cách ly nguồn lỗi, giữ giá/backoff,
  Retry-After số giây/HTTP date, 429, dữ liệu giá sai, cancellation và ý nghĩa % Coinbase
  (một test có nhiều kiểm tra liên quan).
- 1 test live opt-in `-PliveApi=true --tests "*livePublicEndpoints"` đã nhận cả 4 nguồn HTTP 200.
  Test live được skip trong lượt test thông thường.
- Đã chạy trên Samsung SM-F741N/API 37; quan sát giá cập nhật, biểu đồ tích lũy mẫu,
  mở ETH và thấy Kraken/Gemini, phần trăm riêng và thời gian nhận phản hồi.
- Đã phát hiện/sửa chữ đen trên nền tối bằng Surface contentColor và system bar style;
  cài lại và quan sát chữ/biểu tượng hệ thống màu sáng.
- Banner là minh họa tĩnh từ Figma, không phải nguồn tin tức trực tiếp.
- Chưa hoàn tất kiểm chứng runtime: mọi nguồn lỗi/429, mất mạng/phục hồi,
  yêu thích qua force-stop, xoay/màn nhỏ và đếm request bằng network inspector khi background.
  Các nhánh lỗi/backoff/cancellation có unit test, không được xem là đã thử đầy đủ trên điện thoại.
- Nguồn public có thể giới hạn vùng/tần suất hoặc thay schema; app hiện lỗi thật và giữ giá cũ.
