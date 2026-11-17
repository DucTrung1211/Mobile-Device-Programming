package com.example.campusroute

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Integration regressions on the real Activity, using the existing Compose test dependencies. */
@RunWith(AndroidJUnit4::class)
class CampusRouteScreenTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun recreation_preservesWallsAndMode_butClearsComputedResults() {
        cell(0, 1).performScrollTo().performClick()
        click("Tìm đường")
        compose.onNodeWithText("Tìm thấy đường đi").assertExists()
        click("Ví dụ trong ebook")
        click("Chạy thuật toán A*")
        compose.onNodeWithText("A → D → H → K → J").assertExists()

        // Actual Activity recreation, rather than only a local saveable-registry simulation.
        compose.activityRule.scenario.recreate()
        compose.waitForIdle()
        compose.onNodeWithText("Project 32 – Heuristic Searching with A*").assertExists()
        compose.onNodeWithText("Nhấn Chạy thuật toán A* để xem kết quả.").assertExists()
        compose.onNodeWithText("A → D → H → K → J").assertDoesNotExist()

        click("CampusRoute")
        cell(0, 1).assert(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Vật cản"))
        compose.onNodeWithText("7 × 7 · 8 vật cản · Chi phí mỗi bước: 1").assertExists()
        compose.onNodeWithText("Chưa tìm đường").assertExists()
        click("Tìm đường")
        compose.onNodeWithText("Tìm thấy đường đi").assertExists()
    }

    @Test
    fun wallEdits_noPath_clearAndRestore_doNotLeakOldResults() {
        click("Tìm đường")
        compose.onNodeWithText("13 Cell trên tuyến · 12 bước di chuyển").assertExists()
        cell(6, 5).performScrollTo().performClick()
        compose.onNodeWithText("Chưa tìm đường").assertExists()
        compose.onNodeWithText("13 Cell trên tuyến · 12 bước di chuyển").assertDoesNotExist()
        cell(5, 6).performScrollTo().performClick()
        click("Tìm đường")
        compose.onNodeWithText("Không tìm thấy đường đi.").assertExists()
        compose.onNodeWithText("Tìm thấy đường đi").assertDoesNotExist()

        cell(5, 6).performScrollTo().performClick()
        click("Tìm đường")
        compose.onNodeWithText("Tìm thấy đường đi").assertExists()
        click("Xóa cản")
        compose.onNodeWithText("7 × 7 · 0 vật cản · Chi phí mỗi bước: 1").assertExists()
        compose.onNodeWithText("Chưa tìm đường").assertExists()
        click("Khôi phục")
        compose.onNodeWithText("7 × 7 · 7 vật cản · Chi phí mỗi bước: 1").assertExists()
        cell(0, 2).assert(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Vật cản"))
        compose.onNodeWithText("Chưa tìm đường").assertExists()
    }

    @Test
    fun defaultSearch_hasCorrectStatistics_andAccessibleProtectedEndpoints() {
        compose.onAllNodes(hasContentDescription("Hàng ", substring = true)).assertCountEquals(49)
        cell(0, 0).assertIsNotEnabled()
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Điểm bắt đầu S"))
        cell(6, 6).assertIsNotEnabled()
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Điểm đích G"))
        click("Tìm đường")
        compose.onNodeWithText("13 Cell trên tuyến · 12 bước di chuyển").performScrollTo().assertIsDisplayed()
        compose.onAllNodesWithText("12").assertCountEquals(2)
        compose.onNodeWithText("24").assertExists()
    }

    private fun cell(row: Int, col: Int) =
        compose.onNodeWithContentDescription("Hàng ${row + 1}, cột ${col + 1}")

    private fun click(label: String) {
        // The app header and the CampusRoute chip share text; only the chip is clickable.
        compose.onNode(hasText(label) and hasClickAction()).performScrollTo().performClick()
    }
}
