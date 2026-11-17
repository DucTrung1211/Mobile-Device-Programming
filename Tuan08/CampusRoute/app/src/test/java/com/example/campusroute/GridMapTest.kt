package com.example.campusroute

import kotlin.math.abs
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class GridMapTest {
    @get:Rule
    val outcomes: TestWatcher = object : TestWatcher() {
        override fun succeeded(description: Description) {
            println("PHASE3_PASS ${description.methodName}")
        }

        override fun failed(error: Throwable, description: Description) {
            println("PHASE3_FAIL ${description.methodName}: ${error.message}")
        }
    }

    @Test
    fun t01_emptyGrid_cost12() {
        val result = findGridPath(walls = emptySet())
        assertTrue(result.found)
        assertEquals(12, result.totalCost)
        assertEquals(49, createGridGraph(emptySet()).size)
    }

    @Test
    fun t02_defaultObstacles_cost12() {
        val result = findGridPath()
        assertTrue(result.found)
        assertEquals(12, result.totalCost)
        assertEquals(42, createGridGraph().size)
        println("PHASE3_RESULT path=${result.path.joinToString(" -> ") { it.toNodeId() }}")
        println("PHASE3_STATS cells=${result.path.size},steps=${result.stepCount},cost=${result.totalCost},explored=${result.exploredNodeCount}")
        result.searchResult.visited.forEach { (nodeId, node) ->
            val h = manhattanDistance(cellFromNodeId(nodeId), DEFAULT_GOAL)
            println("PHASE3_NODE $nodeId g=${node.gScore} h=$h f=${node.fScore} previous=${node.previousNode}")
        }
    }

    @Test
    fun t03_pathValidity_allCellsAreInsideGrid() {
        val result = findGridPath()
        assertTrue(result.found)
        assertEquals(Cell(0, 0), result.path.first())
        assertEquals(Cell(6, 6), result.path.last())
        assertTrue(result.path.all(::isInsideGrid))
        assertEquals(result.path.size, result.path.toSet().size)
    }

    @Test
    fun t04_obstacleAvoidance_noWallsInPath() {
        val walls = createDefaultWalls()
        val result = findGridPath(walls = walls)
        assertTrue(result.found)
        assertTrue(result.path.none { it in walls })
        assertTrue(result.searchResult.exploredNodes.none { cellFromNodeId(it) in walls })
    }

    @Test
    fun t05_fourDirectionMovement_noDiagonalOrShortcut() {
        val result = findGridPath()
        assertTrue(result.found)
        result.path.zipWithNext().forEach { (from, to) ->
            assertEquals(1, abs(from.row - to.row) + abs(from.col - to.col))
        }
    }

    @Test
    fun t06_unreachableGoal_returnsNoPath() {
        val result = findGridPath(walls = setOf(Cell(5, 6), Cell(6, 5)))
        assertFalse(result.found)
        assertTrue(result.path.isEmpty())
        assertNull(result.totalCost)
        assertNull(result.stepCount)
        assertFalse(result.searchResult.exploredNodes.contains("6,6"))
    }

    @Test
    fun t07_startEqualsGoal_costZero() {
        val cell = Cell(3, 3)
        val result = findGridPath(start = cell, goal = cell)
        assertTrue(result.found)
        assertEquals(listOf(cell), result.path)
        assertEquals(0, result.totalCost)
        assertEquals(0, result.stepCount)
        assertEquals(1, result.exploredNodeCount)
    }

    @Test
    fun t08_invalidCoordinates_areRejectedSafely() {
        val invalid = listOf(Cell(-1, 0), Cell(0, -1), Cell(7, 0), Cell(0, 7), Cell(Int.MIN_VALUE, Int.MAX_VALUE))
        invalid.forEach { cell ->
            assertFalse(isInsideGrid(cell))
            assertThrows(IllegalArgumentException::class.java) { findGridPath(start = cell) }
            assertThrows(IllegalArgumentException::class.java) { findGridPath(goal = cell) }
            assertThrows(IllegalArgumentException::class.java) { getNeighbors(cell) }
            assertThrows(IllegalArgumentException::class.java) { cell.toNodeId() }
        }
    }

    @Test
    fun t09_graphConversion_hasExactlyTheValidNodesAndUnitEdges() {
        listOf(emptySet(), createDefaultWalls()).forEach { walls ->
            val graph = createGridGraph(walls)
            val valid = allPositions().filter { it !in walls }
            val expectedIds = valid.map { "${it.row},${it.col}" }.toSet()
            assertEquals(expectedIds, graph.keys)
            valid.forEach { cell ->
                // Independent adjacency rule, without calling getNeighbors/manhattanDistance.
                val expectedNeighbors = valid.filter { other ->
                    abs(cell.row - other.row) + abs(cell.col - other.col) == 1
                }.map { "${it.row},${it.col}" }.toSet()
                val neighbors = graph.getValue("${cell.row},${cell.col}")
                assertEquals(expectedNeighbors, neighbors.keys)
                neighbors.forEach { (neighbor, cost) ->
                    assertEquals(1, cost)
                    assertEquals(1, graph.getValue(neighbor)[cell.toNodeId()])
                }
            }
            assertFalse(graph.getValue("0,0").containsKey("6,6"))
        }
    }

    @Test
    fun t10_manhattanDistance_hasExpectedValues() {
        assertEquals(7, manhattanDistance(Cell(2, 3), Cell(6, 6)))
        assertEquals(12, manhattanDistance(Cell(0, 0), Cell(6, 6)))
        assertEquals(0, manhattanDistance(Cell(6, 6), Cell(6, 6)))
        assertEquals(1, manhattanDistance(Cell(2, 3), Cell(2, 4)))
        allPositions().forEach { from ->
            allPositions().forEach { to ->
                val h = manhattanDistance(from, to)
                assertTrue(h in 0..12)
                assertEquals(h, manhattanDistance(to, from))
            }
        }
    }

    @Test
    fun t11_manhattanIsConsistentOnEveryEdgeAndAStarAcceptsIt() {
        listOf(emptySet(), createDefaultWalls()).forEach { walls ->
            val graph = createGridGraph(walls)
            allPositions().forEach { goal ->
                assertEquals(0, manhattanDistance(goal, goal))
                graph.forEach { (node, neighbors) ->
                    val h = manhattanDistance(cellFromNodeId(node), goal)
                    assertTrue(h >= 0)
                    neighbors.forEach { (neighbor, cost) ->
                        val neighborH = manhattanDistance(cellFromNodeId(neighbor), goal)
                        assertTrue(h <= cost + neighborH)
                    }
                }
            }
            var heuristicCalls = 0
            val result = aStar(graph, "0,0", "6,6") { node ->
                heuristicCalls++
                manhattanDistance(cellFromNodeId(node), Cell(6, 6))
            }
            assertTrue(result.found)
            assertEquals(graph.size, heuristicCalls)
        }
    }

    @Test
    fun t12_resultStatistics_distinguishCellsStepsCostAndExploredNodes() {
        listOf(emptySet(), createDefaultWalls()).forEach { walls ->
            val result = findGridPath(walls = walls)
            assertTrue(result.found)
            assertEquals(13, result.path.size)
            assertEquals(12, result.stepCount)
            assertEquals(result.path.size - 1, result.stepCount)
            assertEquals(result.stepCount, result.totalCost)
            assertEquals(result.searchResult.totalCost, result.totalCost)
            assertEquals(result.searchResult.exploredNodes.size, result.exploredNodeCount)
            assertEquals(result.exploredNodeCount, result.searchResult.exploredNodes.toSet().size)
            assertTrue(result.exploredNodeCount >= result.path.size)
            assertTrue(result.exploredNodeCount <= createGridGraph(walls).size)
        }
    }

    @Test
    fun t13_repeatedExecution_isStableWithoutLeakingState() {
        val first = findGridPath()
        repeat(10) {
            val next = findGridPath()
            assertEquals(first, next)
            assertEquals(first.searchResult.exploredNodes, next.searchResult.exploredNodes)
        }
        findGridPath(walls = setOf(Cell(5, 6), Cell(6, 5)))
        assertEquals(first, findGridPath())
    }

    @Test
    fun t14_originalEbookRegression_keepsPathAndCost17() {
        val result = BookExample.findPath()
        assertEquals(listOf("A", "D", "H", "K", "J"), result.path)
        assertEquals(17, result.totalCost)
    }

    // T15 is verified by running the unchanged 33 AStarTest methods in the Gradle suite.

    @Test
    fun cellModel_comparesCoordinatesByValue() {
        val cell = Cell(2, 3)
        assertEquals(Cell(2, 3), cell)
        assertEquals(Cell(2, 3).hashCode(), cell.hashCode())
        assertEquals(1, setOf(cell, Cell(2, 3)).size)
        assertNotEquals(Cell(3, 2), cell)
        assertNotEquals(cell, cell.copy(row = 1))
    }

    @Test
    fun cellIds_roundTripAll49PositionsWithoutCollisions() {
        assertEquals(7, GRID_ROWS)
        assertEquals(7, GRID_COLS)
        assertEquals(49, allPositions().size)
        assertEquals(49, allPositions().map { it.toNodeId() }.toSet().size)
        assertEquals("2,3", Cell(2, 3).toNodeId())
        allPositions().forEach { cell ->
            assertEquals(cell, cellFromNodeId(cell.toNodeId()))
        }
    }

    @Test
    fun malformedOrNonCanonicalIds_areRejected() {
        listOf("", "2", "2,3,4", "-1,3", "7,0", "0,7", "02,3", "+2,3", " 2,3", "2, 3", "2.0,3", "2147483648,0", "2,").forEach { id ->
            assertThrows(IllegalArgumentException::class.java) { cellFromNodeId(id) }
        }
    }

    @Test
    fun defaultWalls_areExactlyTheSevenSpecifiedPositions() {
        val expected = setOf(
            Cell(0, 2), Cell(1, 2), Cell(2, 2), Cell(3, 2),
            Cell(4, 2), Cell(4, 3), Cell(4, 4)
        )
        assertEquals(expected, createDefaultWalls())
        assertEquals(7, createDefaultWalls().size)
        val edited = createDefaultWalls().toMutableSet()
        edited.clear()
        assertEquals(expected, createDefaultWalls())
        assertEquals(Cell(0, 0), DEFAULT_START)
        assertEquals(Cell(6, 6), DEFAULT_GOAL)
    }

    @Test
    fun neighbors_respectCornersWallsAndFourDirections() {
        assertEquals(setOf(Cell(1, 0), Cell(0, 1)), getNeighbors(Cell(0, 0)).toSet())
        assertEquals(
            setOf(Cell(2, 3), Cell(4, 3), Cell(3, 2), Cell(3, 4)),
            getNeighbors(Cell(3, 3)).toSet()
        )
        assertEquals(listOf(Cell(1, 0)), getNeighbors(Cell(0, 0), setOf(Cell(0, 1))))
        assertTrue(getNeighbors(Cell(0, 2), createDefaultWalls()).isEmpty())
    }

    @Test
    fun wallsOutsideGrid_areRejectedAtEveryEntryPoint() {
        val walls = setOf(Cell(7, 1))
        assertThrows(IllegalArgumentException::class.java) { createGridGraph(walls) }
        assertThrows(IllegalArgumentException::class.java) { getNeighbors(Cell(0, 0), walls) }
        assertThrows(IllegalArgumentException::class.java) { findGridPath(walls = walls) }
    }

    @Test
    fun wallStartOrGoal_isRejectedAsInvalidConfiguration() {
        assertThrows(IllegalArgumentException::class.java) { findGridPath(walls = setOf(DEFAULT_START)) }
        assertThrows(IllegalArgumentException::class.java) { findGridPath(walls = setOf(DEFAULT_GOAL)) }
        assertThrows(IllegalArgumentException::class.java) {
            findGridPath(start = Cell(0, 2), goal = Cell(0, 2))
        }
    }

    @Test
    fun allWallPositions_createAnEmptyGraphAndRejectSearch() {
        val walls = allPositions().toSet()
        assertTrue(createGridGraph(walls).isEmpty())
        assertThrows(IllegalArgumentException::class.java) { findGridPath(walls = walls) }
    }

    @Test
    fun isolatedStart_hasNoPathAndOneExploredNode() {
        val result = findGridPath(walls = setOf(Cell(0, 1), Cell(1, 0)))
        assertFalse(result.found)
        assertTrue(result.path.isEmpty())
        assertNull(result.stepCount)
        assertNull(result.totalCost)
        assertEquals(1, result.exploredNodeCount)
    }

    @Test
    fun customGoal_usesManhattanForThatGoal() {
        val result = findGridPath(start = Cell(6, 0), goal = Cell(0, 0), walls = emptySet())
        assertTrue(result.found)
        assertEquals(6, result.totalCost)
        assertEquals(Cell(6, 0), result.path.first())
        assertEquals(Cell(0, 0), result.path.last())
        assertEquals(6, result.searchResult.visited.getValue("6,0").fScore)
        assertEquals(0, result.searchResult.visited.getValue("0,0").fScore - checkNotNull(result.totalCost))
    }

    @Test
    fun invalidManhattanCoordinates_areRejectedBeforeArithmetic() {
        assertThrows(IllegalArgumentException::class.java) {
            manhattanDistance(Cell(Int.MIN_VALUE, 0), Cell(6, 6))
        }
        assertThrows(IllegalArgumentException::class.java) {
            manhattanDistance(Cell(0, 0), Cell(Int.MAX_VALUE, 0))
        }
    }

    @Test
    fun gridIntegration_returnsTheExistingAStarResult() {
        val graph = createGridGraph()
        val direct = aStar(graph, "0,0", "6,6") { node ->
            manhattanDistance(cellFromNodeId(node), DEFAULT_GOAL)
        }
        val gridResult = findGridPath()
        assertEquals(direct, gridResult.searchResult)
        assertEquals(direct.path.map(::cellFromNodeId), gridResult.path)
        assertEquals(12, gridResult.searchResult.visited.getValue("0,0").fScore)
    }

    private fun allPositions(): List<Cell> =
        (0..6).flatMap { row -> (0..6).map { col -> Cell(row, col) } }

    @Test
    fun presentationWall_onDefaultPath_reroutesWithoutIncreasingCost() {
        val demoWall = Cell(5, 6)
        val defaultResult = findGridPath()
        assertTrue(demoWall in defaultResult.path)

        val walls = createDefaultWalls() + demoWall
        val rerouted = findGridPath(walls = walls)
        assertTrue(rerouted.found)
        assertNotEquals(defaultResult.path, rerouted.path)
        assertTrue(rerouted.path.none { it in walls })
        assertTrue(rerouted.path.all(::isInsideGrid))
        assertEquals(DEFAULT_START, rerouted.path.first())
        assertEquals(DEFAULT_GOAL, rerouted.path.last())
        rerouted.path.zipWithNext().forEach { (from, to) ->
            assertEquals(1, abs(from.row - to.row) + abs(from.col - to.col))
        }
        assertEquals(12, rerouted.totalCost)
        assertEquals(rerouted.stepCount, rerouted.totalCost)
        println("PHASE8_DEMO_GRID path=${rerouted.path.joinToString(" -> ") { it.toNodeId() }}")
        println("PHASE8_DEMO_STATS cells=${rerouted.path.size},steps=${rerouted.stepCount},cost=${rerouted.totalCost},explored=${rerouted.exploredNodeCount}")

        assertEquals(defaultResult, findGridPath(walls = walls - demoWall))
        val blocked = findGridPath(walls = walls + Cell(6, 5))
        assertFalse(blocked.found)
        assertTrue(blocked.path.isEmpty())
        assertNull(blocked.totalCost)
        assertNull(blocked.stepCount)
    }
}
