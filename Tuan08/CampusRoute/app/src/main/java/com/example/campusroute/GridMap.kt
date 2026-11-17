package com.example.campusroute

import kotlin.math.abs

/** A value coordinate: equality/hashCode compare row and col; both are immutable integers. */
data class Cell(val row: Int, val col: Int)

const val GRID_ROWS = 7
const val GRID_COLS = 7
val DEFAULT_START = Cell(0, 0)
val DEFAULT_GOAL = Cell(6, 6)

/** A fresh set on every call; changes to a caller's copy cannot change the defaults. */
fun createDefaultWalls(): Set<Cell> = setOf(
    Cell(0, 2),
    Cell(1, 2),
    Cell(2, 2),
    Cell(3, 2),
    Cell(4, 2),
    Cell(4, 3),
    Cell(4, 4)
)

fun isInsideGrid(cell: Cell): Boolean =
    cell.row in 0 until GRID_ROWS && cell.col in 0 until GRID_COLS

/** Canonical IDs avoid collisions such as joining row/column digits without a delimiter. */
fun Cell.toNodeId(): String {
    require(isInsideGrid(this)) { "Cell outside grid: $this" }
    return "$row,$col"
}

fun cellFromNodeId(nodeId: String): Cell {
    val parts = nodeId.split(',')
    require(parts.size == 2) { "Invalid cell ID: $nodeId" }
    val row = requireNotNull(parts[0].toIntOrNull()) { "Invalid row in ID: $nodeId" }
    val col = requireNotNull(parts[1].toIntOrNull()) { "Invalid column in ID: $nodeId" }
    val cell = Cell(row, col)
    require(isInsideGrid(cell)) { "Cell outside grid: $nodeId" }
    require(cell.toNodeId() == nodeId) { "Cell ID must be canonical: $nodeId" }
    return cell
}

/** Up, down, left, right only. A wall itself has no traversable neighbors. */
fun getNeighbors(cell: Cell, walls: Set<Cell> = emptySet()): List<Cell> {
    require(isInsideGrid(cell)) { "Cell outside grid: $cell" }
    validateWalls(walls)
    if (cell in walls) return emptyList()

    return listOf(
        Cell(cell.row - 1, cell.col),
        Cell(cell.row + 1, cell.col),
        Cell(cell.row, cell.col - 1),
        Cell(cell.row, cell.col + 1)
    ).filter { isInsideGrid(it) && it !in walls }
}

/** Every walkable position is a node; only adjacent walkable cells get unit-cost edges. */
fun createGridGraph(walls: Set<Cell> = createDefaultWalls()): Map<String, Map<String, Int>> {
    validateWalls(walls)
    val graph = linkedMapOf<String, Map<String, Int>>()
    for (row in 0 until GRID_ROWS) {
        for (col in 0 until GRID_COLS) {
            val cell = Cell(row, col)
            if (cell !in walls) {
                graph[cell.toNodeId()] = getNeighbors(cell, walls).associate { it.toNodeId() to 1 }
            }
        }
    }
    return graph
}

/** Safe Int arithmetic here because both coordinates are bounded to this 7 x 7 grid. */
fun manhattanDistance(current: Cell, goal: Cell): Int {
    require(isInsideGrid(current)) { "Cell outside grid: $current" }
    require(isInsideGrid(goal)) { "Goal outside grid: $goal" }
    return abs(current.row - goal.row) + abs(current.col - goal.col)
}

/** Statistics are derived from the actual Phase 2 result, never from expected path lengths. */
data class GridPathResult(
    val path: List<Cell>,
    val searchResult: PathResult
) {
    val found: Boolean get() = searchResult.found
    val stepCount: Int? get() = if (found) path.size - 1 else null
    val totalCost: Int? get() = searchResult.totalCost
    val exploredNodeCount: Int get() = searchResult.visited.size
}

/**
 * Grid -> graph -> the existing A* -> cell path.
 * Invalid coordinates/wall endpoints throw IllegalArgumentException.
 * A valid but disconnected map returns found=false, an empty path and null steps/cost.
 */
fun findGridPath(
    start: Cell = DEFAULT_START,
    goal: Cell = DEFAULT_GOAL,
    walls: Set<Cell> = createDefaultWalls()
): GridPathResult {
    require(isInsideGrid(start)) { "Start outside grid: $start" }
    require(isInsideGrid(goal)) { "Goal outside grid: $goal" }
    validateWalls(walls)
    require(start !in walls) { "Start must not be a wall: $start" }
    require(goal !in walls) { "Goal must not be a wall: $goal" }

    val graph = createGridGraph(walls)
    val result = aStar(graph, start.toNodeId(), goal.toNodeId()) { nodeId ->
        manhattanDistance(cellFromNodeId(nodeId), goal)
    }
    return GridPathResult(result.path.map(::cellFromNodeId), result)
}

private fun validateWalls(walls: Set<Cell>) {
    require(walls.all(::isInsideGrid)) { "Wall outside grid" }
}
