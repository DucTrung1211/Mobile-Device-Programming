# BÀI TẬP TUẦN 1 - LẬP TRÌNH THIẾT BỊ DI ĐỘNG

**Sinh viên:** Trần Đức Trung

**MSSV:** 001206004489

## Câu 1. Mong muốn và định hướng sau khi học xong môn học

Sau khi học xong môn Lập trình Thiết bị Di động, em mong muốn có thể tự xây dựng một ứng dụng mobile hoàn chỉnh, từ thiết kế giao diện, xử lý chức năng đến kết nối dữ liệu và API.

Em muốn hiểu quy trình làm một ứng dụng Android thực tế: lên ý tưởng, thiết kế, lập trình, kiểm thử và triển khai. Sau môn học, em sẽ tiếp tục rèn luyện kỹ năng lập trình mobile, làm những ứng dụng giải quyết vấn đề thực tế và hướng tới phát hành sản phẩm trên Google Play.

## Câu 2. Theo bạn, trong tương lai gần (10 năm) lập trình di động có phát triển không? Giải thích tại sao?

Theo em, lập trình di động vẫn sẽ tiếp tục phát triển mạnh trong 10 năm tới. Điện thoại và các thiết bị di động đã trở thành một phần quen thuộc trong học tập, giao thông, mua sắm, thanh toán, giải trí, y tế và công việc.

AI, IoT, mạng 5G/6G, điện toán đám mây và AR/VR cũng sẽ tạo thêm nhu cầu về ứng dụng di động. Ứng dụng không chỉ chạy trên điện thoại mà còn có thể kết nối với đồng hồ thông minh, ô tô, thiết bị IoT và nhiều hệ thống khác. Vì vậy, em nghĩ kỹ năng phát triển ứng dụng mobile vẫn có giá trị và nhiều cơ hội trong tương lai.

## Câu 3. Xây dựng giao diện Profile

- Họ tên: Trần Đức Trung
- MSSV: 001206004489
- Ngôn ngữ: Kotlin
- UI Toolkit: Jetpack Compose
- Design: Material 3
- IDE: Android Studio
- Project: Tuan01_Profile
- Package: `com.example.tuan01profile`

### Build và chạy

Mở project hiện tại trong Android Studio, đợi Gradle Sync hoàn tất.

Trên Windows PowerShell, tại thư mục gốc project:

```powershell
.\gradlew.bat :app:testDebugUnitTest
.\gradlew.bat assembleDebug
```

# PHẦN TÌM HIỂU BỔ SUNG - TUẦN 1

## 1. Mô hình giáo dục HAA

### HAA là gì?

HAA là viết tắt của **Horowitz Andreessen Academy**, một cơ sở giáo dục tư nhân tại San Francisco dành chủ yếu cho người vừa tốt nghiệp trung học. Chương trình khóa đầu dự kiến bắt đầu vào tháng 9/2027. Đây là chương trình học trực tiếp, toàn thời gian. ([Thông tin chính thức của HAA](https://theacademysf.com/faq))

Người học chọn dự án mình muốn theo đuổi, học các khóa phù hợp, nhận góp ý từ người hướng dẫn và tham gia thực tập tại doanh nghiệp. AI được dùng như công cụ hỗ trợ học và làm. So với cách học chủ yếu nghe giảng rồi làm bài kiểm tra, HAA đặt việc thực hành và chủ động học vào trung tâm. ([Giới thiệu mô hình](https://theacademysf.com/about), [Chương trình học](https://theacademysf.com/program))

### Ưu điểm

Dựa trên cách tổ chức chương trình, mô hình này có những điểm mạnh:

- Học gắn với thực hành, giúp người học hiểu cách vận dụng kiến thức.
- Được chọn hướng học theo sở thích và mục tiêu.
- Có người hướng dẫn và bạn học góp ý trong quá trình làm.
- Có cơ hội tiếp xúc với doanh nghiệp và rèn kỹ năng dùng AI.


### Nhược điểm

- Đòi hỏi khả năng tự học và quản lý thời gian; người quen được hướng dẫn từng bước có thể khó thích nghi.
- Khóa đầu không cấp bằng đại học hoặc tín chỉ, nên không phù hợp với người cần bằng cấp đó.
- Học trực tiếp tại San Francisco và phải lo chi phí sinh hoạt, dù khóa đầu miễn học phí.
- Mô hình còn mới, chưa đủ dữ liệu để đánh giá hiệu quả lâu dài.


## 2. Internet và AI đã thay đổi cách con người tiếp cận tri thức như thế nào?

| Giai đoạn | Tìm kiếm kiến thức | Ghi nhớ kiến thức | Vận dụng kiến thức |
|-----------|--------------------|-------------------|--------------------|
| Trước khi có Internet | Chủ yếu qua sách, thư viện, thầy cô; tìm tài liệu mất nhiều thời gian. | Ghi chép, học thuộc và ôn lại; cần nhớ nhiều vì khó tra cứu nhanh. | Tự áp dụng điều đã học, hỏi người có kinh nghiệm khi gặp khó khăn. |
| Internet phổ biến | Dùng công cụ tìm kiếm, tài liệu số và khóa học trực tuyến; phải chọn nguồn đáng tin. | Có thể lưu tài liệu và tra cứu lại; vẫn cần hiểu và nhớ kiến thức cốt lõi. | Tham khảo nhiều cách làm, trao đổi trên mạng rồi tự lựa chọn và thực hiện. |
| AI tạo sinh hiện nay | Hỏi bằng ngôn ngữ tự nhiên, yêu cầu giải thích hoặc tóm tắt; cần kiểm chứng câu trả lời. | Nhờ AI hỗ trợ hệ thống hóa và ôn tập; dễ phụ thuộc nếu chỉ đọc đáp án. | AI hỗ trợ đề xuất cách làm và tạo bản nháp; người học phải kiểm tra và điều chỉnh. |

Trước khi có Internet, việc tiếp cận kiến thức phụ thuộc nhiều vào tài liệu sẵn có và người hướng dẫn. Khi Internet phổ biến, nguồn học rộng hơn và việc tra cứu nhanh hơn. AI tạo sinh cho phép hỏi tiếp, yêu cầu giải thích theo mức hiểu của mình và nhận hỗ trợ khi vận dụng. Tuy nhiên, câu trả lời nhanh không đồng nghĩa với hiểu đúng. Qua cả ba giai đoạn, người học vẫn cần kiến thức nền và khả năng đánh giá thông tin.

## 3. Chúng ta cần học gì khi AI có thể làm gần như mọi thứ?

Theo em, vẫn cần học kiến thức nền tảng để hiểu bản chất của vấn đề. Có nền tảng thì mới biết câu trả lời của AI có hợp lý không, thay vì chỉ chấp nhận vì thấy diễn đạt thuyết phục.

Cần rèn cách đặt câu hỏi, phân tích vấn đề và kiểm tra kết quả. Khi dùng AI, phải biết đối chiếu với nguồn đáng tin và nhận ra chỗ thiếu thông tin hoặc sai lập luận.

Cũng cần học cách dùng AI đúng mục đích: đưa yêu cầu rõ ràng, chọn phần việc cần hỗ trợ và tự quyết định kết quả cuối cùng. Khả năng tự học giúp chúng ta tiếp tục bổ sung kiến thức khi công cụ thay đổi.

## 4. Những năng lực nào chúng ta vẫn cần phát triển trong thời đại AI?

- **Tư duy phản biện:** Giúp kiểm tra thông tin và lập luận, tránh tin ngay vào câu trả lời của AI.
- **Giải quyết vấn đề:** Giúp xác định đúng điều cần xử lý, chọn cách làm và đánh giá kết quả.
- **Tự học:** Giúp cập nhật kiến thức và thích nghi khi công nghệ thay đổi.
- **Sử dụng AI:** Giúp đặt yêu cầu rõ ràng, hiểu giới hạn của công cụ và kiểm tra đầu ra.
- **Giao tiếp và làm việc nhóm:** Giúp trình bày ý tưởng, hiểu người khác và phối hợp công việc.
- **Sáng tạo:** Giúp tìm hướng tiếp cận mới và chọn ý tưởng phù hợp với mục tiêu.
- **Trách nhiệm và đạo đức:** Giúp bảo vệ thông tin riêng tư, tôn trọng quyền tác giả và chịu trách nhiệm về kết quả mình sử dụng.

