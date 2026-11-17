package com.example.campusroute

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.campusroute.ui.theme.CampusRouteTheme
import com.example.campusroute.ui.theme.GoalRed
import com.example.campusroute.ui.theme.PathBlue
import com.example.campusroute.ui.theme.StartGreen
import com.example.campusroute.ui.theme.WallGray

private enum class ScreenMode(val title: String) {
    CAMPUS("CampusRoute"),
    BOOK("Ví dụ trong ebook")
}


private val WallsSaver = listSaver<Set<Cell>, Int>(
    save = { walls -> walls.map { it.row * GRID_COLS + it.col } },
    restore = { ids ->
        ids.filter { it in 0 until GRID_ROWS * GRID_COLS }
            .map { Cell(it / GRID_COLS, it % GRID_COLS) }
            .filter { it != DEFAULT_START && it != DEFAULT_GOAL }
            .toSet()
    }
)

@Composable
fun CampusRouteScreen(modifier: Modifier = Modifier) {

    var selectedMode by rememberSaveable { mutableStateOf(ScreenMode.CAMPUS) }
    var walls by rememberSaveable(stateSaver = WallsSaver) { mutableStateOf(createDefaultWalls()) }
    var gridResult by remember { mutableStateOf<GridPathResult?>(null) }
    var bookResult by remember { mutableStateOf<PathResult?>(null) }
    val gridScroll = rememberScrollState()
    val bookScroll = rememberScrollState()

    Scaffold(modifier = modifier, contentWindowInsets = WindowInsets.safeDrawing) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding).consumeWindowInsets(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 560.dp)
                    .fillMaxSize()
                    .verticalScroll(if (selectedMode == ScreenMode.CAMPUS) gridScroll else bookScroll)
                    .padding(horizontal = 12.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AppHeader()
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ScreenMode.entries.forEach { mode ->
                        FilterChip(
                            selected = selectedMode == mode,
                            onClick = { selectedMode = mode },
                            label = {
                                Text(
                                    mode.title,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            },
                            modifier = Modifier.weight(1f).heightIn(min = 48.dp)
                        )
                    }
                }
                if (selectedMode == ScreenMode.CAMPUS) {
                    CampusPanel(
                        walls = walls,
                        result = gridResult,
                        onCellClick = { cell ->
                            if (cell != DEFAULT_START && cell != DEFAULT_GOAL) {
                                // Replace the Set; never mutate a collection behind Compose's state.
                                walls = if (cell in walls) walls - cell else walls + cell
                                gridResult = null
                            }
                        },
                        // Search happens only on an event, never during recomposition.
                        onSearch = { gridResult = findGridPath(walls = walls) },
                        onClear = { walls = emptySet(); gridResult = null },
                        onRestore = { walls = createDefaultWalls(); gridResult = null }
                    )
                } else {
                    BookPanel(result = bookResult, onRun = { bookResult = BookExample.findPath() })
                }
            }
        }
    }
}

@Composable
private fun AppHeader() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = CircleShape) {
                Box(Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                    Text("A*", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleLarge)
                }
            }
            Column {
                Text("CampusRoute", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("A* Pathfinding Visualizer", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge)
            }
        }
        Text(
            "Mô phỏng thuật toán tìm đường đi có chi phí thấp nhất.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun CampusPanel(
    walls: Set<Cell>,
    result: GridPathResult?,
    onCellClick: (Cell) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    onRestore: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Bản đồ CampusRoute", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text("${GRID_ROWS * GRID_COLS} ô", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge, softWrap = false)
        }
        Text(
            "${GRID_ROWS} × ${GRID_COLS} · ${walls.size} vật cản · Chi phí mỗi bước: 1",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        GridBoard(walls, result?.path.orEmpty().toSet(), onCellClick)
        Button(onClick = onSearch, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) {
            Text("Tìm đường")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(onClick = onClear, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) { Text("Xóa cản") }
            OutlinedButton(onClick = onRestore, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) { Text("Khôi phục") }
        }
        GridResultCard(result)
        GridLegend()
    }
}

@Composable
private fun GridBoard(walls: Set<Cell>, path: Set<Cell>, onCellClick: (Cell) -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            repeat(GRID_ROWS) { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    repeat(GRID_COLS) { col ->
                        val cell = Cell(row, col)
                        GridCell(cell, cell in walls, cell in path, { onCellClick(cell) }, Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun GridCell(cell: Cell, wall: Boolean, onPath: Boolean, onClick: () -> Unit, modifier: Modifier) {
    val start = cell == DEFAULT_START
    val goal = cell == DEFAULT_GOAL
    val color = when {
        start -> StartGreen
        goal -> GoalRed
        wall -> WallGray
        onPath -> PathBlue
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val description = when {
        start -> "Điểm bắt đầu S"
        goal -> "Điểm đích G"
        wall -> "Vật cản"
        onPath -> "Đường đi tối ưu"
        else -> "Ô trống"
    }
    val symbol = when {
        start -> "S"
        goal -> "G"
        wall -> "×"
        onPath -> "•"
        else -> ""
    }
    val shape = RoundedCornerShape(6.dp)
    Surface(
        modifier = modifier
            .aspectRatio(1f)
            .semantics(mergeDescendants = true) {
                contentDescription = "Hàng ${cell.row + 1}, cột ${cell.col + 1}"
                stateDescription = description
            }
            .clickable(
                enabled = !start && !goal,
                role = Role.Button,
                onClickLabel = if (wall) "Xóa vật cản" else "Thêm vật cản",
                onClick = onClick
            ),
        shape = shape,
        color = color
    ) {
        Box(Modifier.border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape), contentAlignment = Alignment.Center) {
            Text(symbol, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
private fun GridResultCard(result: GridPathResult?) {
    ContentCard("Kết quả tìm đường") {
        when {
            result == null -> {
                Text("Chưa tìm đường", fontWeight = FontWeight.SemiBold)
                Text("Nhấn Tìm đường để bắt đầu.", style = MaterialTheme.typography.bodyMedium)
            }
            !result.found -> {
                Text("Không tìm thấy đường đi.", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold)
                Text("Thử xóa bớt vật cản rồi tìm lại.", style = MaterialTheme.typography.bodyMedium)
                Text("Số node đã xét: ${result.exploredNodeCount}", style = MaterialTheme.typography.bodySmall)
            }
            else -> {
                Text("Tìm thấy đường đi", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.SemiBold)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Stat("Số bước", result.stepCount.toString(), Modifier.weight(1f))
                    Stat("Tổng chi phí", result.totalCost.toString(), Modifier.weight(1f))
                    Stat("Node đã xét", result.exploredNodeCount.toString(), Modifier.weight(1f))
                }
                Text(
                    "${result.path.size} Cell trên tuyến · ${result.stepCount} bước di chuyển",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun GridLegend() {
    ContentCard("Chú thích") {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LegendItem(StartGreen, "S: Bắt đầu")
            LegendItem(GoalRed, "G: Đích")
            LegendItem(WallGray, "WALL: Vật cản")
            LegendItem(PathBlue, "PATH: Đường đi")
            LegendItem(MaterialTheme.colorScheme.surfaceVariant, "EMPTY: Ô trống")
        }
        Text(
            "Chạm vào ô trống để thêm vật cản. Chạm vào ô vật cản để xóa.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Box(Modifier.size(12.dp).background(color, RoundedCornerShape(3.dp)))
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun BookPanel(result: PathResult?, onRun: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ContentCard("Project 32 – Heuristic Searching with A*") {
            Text("Kotlin from Scratch · Chapter 7", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge)
            Text("Graph: Weighted Graph · ${BookExample.graph.size} nodes", style = MaterialTheme.typography.bodyMedium)
            Text("Start: ${BookExample.startNode}   →   Goal: ${BookExample.targetNode}   ·   Algorithm: A*", style = MaterialTheme.typography.bodyMedium)
            Text("f(n) = g(n) + h(n)", fontWeight = FontWeight.SemiBold)
        }
        Button(onClick = onRun, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) {
            Text("Chạy thuật toán A*")
        }
        ContentCard("Kết quả ví dụ ebook") {
            when {
                result == null -> Text("Nhấn Chạy thuật toán A* để xem kết quả.", style = MaterialTheme.typography.bodyMedium)
                !result.found -> Text("Không tìm thấy đường đi.", color = MaterialTheme.colorScheme.error)
                else -> {
                    Text("Tìm thấy đường đi", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.SemiBold)
                    Text(result.path.joinToString(" → "), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Stat("Tổng chi phí", result.totalCost.toString(), Modifier.weight(1f))
                        Stat("Node đã xét", result.exploredNodes.size.toString(), Modifier.weight(1f))
                    }
                    Text("Chi phí từng cạnh", fontWeight = FontWeight.SemiBold)
                    result.path.zipWithNext().forEach { (from, to) ->
                        val cost = BookExample.graph.getValue(from).getValue(to)
                        Text("$from → $to: $cost", style = MaterialTheme.typography.bodyMedium)
                    }
                    Text("Chi phí được tính từ đồ thị có trọng số, không phải số cạnh.", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
private fun ContentCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            content()
        }
    }
}

@Composable
private fun Stat(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Preview(showBackground = true)
@Composable
private fun CampusRoutePreview() {
    CampusRouteTheme(dynamicColor = false) { CampusRouteScreen() }
}
