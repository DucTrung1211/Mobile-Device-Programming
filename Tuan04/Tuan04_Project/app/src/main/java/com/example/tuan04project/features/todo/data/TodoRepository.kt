package com.example.tuan04project.features.todo.data

import com.example.tuan04project.features.todo.model.OnboardingIllustration
import com.example.tuan04project.features.todo.model.OnboardingPage
import com.example.tuan04project.features.todo.model.TodoAppInfo

object TodoRepository {
    val appInfo = TodoAppInfo(
        brandName = "UTH",
        productName = "Todo",
        slogan = "Việc học, việc nhà – gọn gàng mỗi ngày",
        version = "Phiên bản 2.1.0"
    )

    fun getOnboardingPages(): List<OnboardingPage> {
        return listOf(
            OnboardingPage(
                title = "Mọi việc, gọn trong một chỗ",
                description = "Ghi lại bài tập, việc nhà, lịch hẹn. Không còn quên việc nào.",
                illustration = OnboardingIllustration.TASK_LIST,
                buttonText = "Tiếp tục"
            ),
            OnboardingPage(
                title = "Chia theo Học tập và Cuộc sống",
                description = "Mỗi việc có nhóm riêng, nhìn là biết hôm nay cần lo gì.",
                illustration = OnboardingIllustration.TASK_GROUPS,
                buttonText = "Tiếp tục"
            ),
            OnboardingPage(
                title = "Nhắc đúng lúc",
                description = "Bật thông báo để UTH Todo nhắc bạn trước hạn chót.",
                illustration = OnboardingIllustration.REMINDER,
                buttonText = "Bắt đầu ngay"
            )
        )
    }
}
