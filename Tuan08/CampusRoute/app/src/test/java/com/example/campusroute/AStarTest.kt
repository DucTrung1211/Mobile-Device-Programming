package com.example.campusroute

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class AStarTest {
    // --info exposes these outcomes without changing the app's Gradle test configuration.
    @get:Rule
    val outcomes: TestWatcher = object : TestWatcher() {
        override fun succeeded(description: Description) {
            println("PHASE2_PASS ${description.methodName}")
        }

        override fun failed(error: Throwable, description: Description) {
            println("PHASE2_FAIL ${description.methodName}: ${error.message}")
        }
    }

    @Test
    fun bookExample_hasCost17() {
        val result = BookExample.findPath()
        assertTrue(result.found)
        assertEquals(17, result.totalCost)
        println("PHASE2_COMPUTED_RESULT\n${displayShortestPath(result)}")
        println("PHASE2_COMPUTED_TRACE (node,g,h,f,previous)")
        result.visited.forEach { (node, attributes) ->
            println("$node,${attributes.gScore},${getHScore(node)},${attributes.fScore},${attributes.previousNode}")
        }
    }

    @Test
    fun bookExample_findsExpectedPath() {
        assertEquals(listOf("A", "D", "H", "K", "J"), BookExample.findPath().path)
    }

    @Test
    fun startEqualsGoal_returnsSingleNodeAndZeroCost() {
        val result = aStar(mapOf("S" to emptyMap()), "S", "S")
        assertTrue(result.found)
        assertEquals(listOf("S"), result.path)
        assertEquals(0, result.totalCost)
        assertEquals(listOf("S"), result.exploredNodes)
        assertNull(result.visited.getValue("S").previousNode)
    }

    @Test
    fun emptyGraph_isRejected() {
        assertThrows(IllegalArgumentException::class.java) { aStar(emptyMap(), "S", "T") }
    }

    @Test
    fun missingStart_isRejected() {
        assertThrows(IllegalArgumentException::class.java) {
            aStar(mapOf("T" to emptyMap()), "S", "T")
        }
    }

    @Test
    fun missingGoal_isRejected() {
        assertThrows(IllegalArgumentException::class.java) {
            aStar(mapOf("S" to emptyMap()), "S", "T")
        }
    }

    @Test
    fun unreachableTarget_doesNotExpandUnreachableNodes() {
        val graph = mapOf(
            "S" to mapOf("A" to 2),
            "A" to emptyMap(),
            "T" to mapOf("U" to 1),
            "U" to emptyMap()
        )
        val result = aStar(graph, "S", "T")
        assertFalse(result.found)
        assertTrue(result.path.isEmpty())
        assertNull(result.totalCost)
        assertEquals(listOf("S", "A"), result.exploredNodes)
    }

    @Test
    fun weightedGraph_prefersLowerCostOverFewerEdges() {
        val graph = mapOf(
            "S" to mapOf("T" to 10, "A" to 1),
            "A" to mapOf("B" to 1),
            "B" to mapOf("T" to 1),
            "T" to emptyMap()
        )
        val result = aStar(graph, "S", "T")
        assertEquals(listOf("S", "A", "B", "T"), result.path)
        assertEquals(3, result.totalCost)
    }

    @Test
    fun bookPath_containsOnlyRealEdgesAndItsCostMatches() {
        assertValidPath(BookExample.graph, "A", "J", BookExample.findPath())
    }

    @Test
    fun bookGraph_hasExactlyTheOriginalSymmetricEdges() {
        val expected = setOf(
            Triple("A", "B", 4), Triple("A", "C", 6), Triple("A", "D", 5),
            Triple("B", "C", 2), Triple("B", "E", 4), Triple("B", "F", 4),
            Triple("C", "D", 3), Triple("C", "F", 3),
            Triple("D", "G", 6), Triple("D", "H", 4), Triple("E", "I", 6),
            Triple("F", "G", 4), Triple("F", "I", 5),
            Triple("G", "I", 6), Triple("G", "K", 3), Triple("H", "K", 3),
            Triple("I", "J", 6), Triple("J", "K", 5),
            Triple("K", "L", 3), Triple("L", "M", 5)
        )
        val graph = BookExample.graph
        assertEquals(('A'..'M').map { it.toString() }.toSet(), graph.keys)
        assertEquals(40, graph.values.sumOf { it.size })
        val actual = graph.flatMap { (node, neighbors) ->
            neighbors.filterKeys { node < it }.map { (neighbor, cost) ->
                Triple(node, neighbor, cost)
            }
        }.toSet()
        assertEquals(expected, actual)
        graph.forEach { (node, neighbors) ->
            neighbors.forEach { (neighbor, cost) -> assertEquals(cost, graph[neighbor]?.get(node)) }
        }
    }

    @Test
    fun bookHeuristic_matchesEveryValueAndIsConsistent() {
        val expected = mapOf(
            "A" to 8, "B" to 6, "C" to 6, "D" to 6, "E" to 4, "F" to 4,
            "G" to 4, "H" to 4, "I" to 2, "J" to 0, "K" to 2, "L" to 4, "M" to 6
        )
        assertEquals(expected, BookExample.graph.keys.associateWith(::getHScore))
        assertEquals(0, getHScore("unknown"))
        BookExample.graph.forEach { (node, neighbors) ->
            neighbors.forEach { (neighbor, cost) ->
                assertTrue(getHScore(node) <= cost + getHScore(neighbor))
            }
        }
    }

    @Test
    fun repeatedSearches_areStableAndIndependentOfMapOrder() {
        val first = BookExample.findPath()
        val expectedOrder = listOf("A", "B", "D", "C", "E", "F", "H", "K", "G", "I", "J")
        assertEquals(expectedOrder, first.exploredNodes)
        repeat(20) {
            assertEquals(first, BookExample.findPath())
            assertEquals(expectedOrder, BookExample.findPath().exploredNodes)
        }
        val reversedGraph = BookExample.graph.entries.reversed().associate { (node, neighbors) ->
            node to neighbors.entries.reversed().associate { it.key to it.value }
        }
        val reversedResult = aStar(reversedGraph, "A", "J", ::getHScore)
        assertEquals(first, reversedResult)
        assertEquals(expectedOrder, reversedResult.exploredNodes)
    }

    @Test
    fun currentNode_breaksTiesByNameAfterFScore() {
        val unvisited = linkedMapOf(
            "Z" to Node(0, 9), "B" to Node(0, 3), "A" to Node(0, 3)
        )
        assertEquals("A", getCurrentNode(unvisited))
    }

    @Test
    fun currentNode_onEmptyFrontierReturnsNull() {
        assertNull(getCurrentNode(emptyMap()))
    }

    @Test
    fun cheaperRoute_updatesGScoreAndPredecessor() {
        val graph = mapOf(
            "S" to mapOf("A" to 5, "B" to 1),
            "B" to mapOf("A" to 1, "T" to 10),
            "A" to mapOf("T" to 2),
            "T" to emptyMap()
        )
        val result = aStar(graph, "S", "T")
        assertEquals(listOf("S", "B", "A", "T"), result.path)
        assertEquals(4, result.totalCost)
        assertEquals(Node(2, 2, "B"), result.visited["A"])
        assertEquals(Node(4, 4, "A"), result.visited["T"])
    }

    @Test
    fun negativeEdgeWeight_isRejected() {
        assertThrows(IllegalArgumentException::class.java) {
            aStar(mapOf("S" to mapOf("T" to -1), "T" to emptyMap()), "S", "T")
        }
    }

    @Test
    fun unknownNeighbor_isRejected() {
        assertThrows(IllegalArgumentException::class.java) {
            aStar(mapOf("S" to mapOf("ghost" to 1), "T" to emptyMap()), "S", "T")
        }
    }

    @Test(timeout = 1000)
    fun zeroWeightCycle_terminatesWithAValidPath() {
        val graph = mapOf(
            "S" to mapOf("A" to 0),
            "A" to mapOf("S" to 0, "T" to 1),
            "T" to emptyMap()
        )
        val result = aStar(graph, "S", "T")
        assertEquals(listOf("S", "A", "T"), result.path)
        assertEquals(1, result.totalCost)
        assertEquals(3, result.exploredNodes.size)
    }

    @Test
    fun isolatedStart_returnsNoPath() {
        val result = aStar(mapOf("S" to emptyMap(), "T" to emptyMap()), "S", "T")
        assertFalse(result.found)
        assertEquals(listOf("S"), result.exploredNodes)
    }

    @Test
    fun gScoreOverflow_isRejected() {
        assertThrows(ArithmeticException::class.java) {
            aStar(
                mapOf("S" to mapOf("A" to Int.MAX_VALUE), "A" to mapOf("T" to 1), "T" to emptyMap()),
                "S", "T"
            )
        }
    }

    @Test
    fun fScoreOverflow_isRejected() {
        assertThrows(ArithmeticException::class.java) {
            aStar(
                mapOf("S" to mapOf("A" to Int.MAX_VALUE), "A" to mapOf("T" to 1), "T" to emptyMap()),
                "S", "T", { if (it == "A") 1 else 0 }
            )
        }
    }

    @Test
    fun maximumIntCost_isStillAReachableCost() {
        val result = aStar(mapOf("S" to mapOf("T" to Int.MAX_VALUE), "T" to emptyMap()), "S", "T")
        assertTrue(result.found)
        assertEquals(Int.MAX_VALUE, result.totalCost)
        assertEquals(listOf("S", "T"), result.path)
    }

    @Test
    fun negativeHeuristic_isRejected() {
        assertThrows(IllegalArgumentException::class.java) {
            aStar(mapOf("S" to mapOf("T" to 1), "T" to emptyMap()), "S", "T") {
                if (it == "S") -1 else 0
            }
        }
    }

    @Test
    fun nonzeroGoalHeuristic_isRejected() {
        assertThrows(IllegalArgumentException::class.java) {
            aStar(mapOf("S" to mapOf("T" to 1), "T" to emptyMap()), "S", "T") { 1 }
        }
    }

    @Test
    fun inconsistentHeuristic_isRejectedRatherThanClosingNodesUnsafely() {
        assertThrows(IllegalArgumentException::class.java) {
            aStar(mapOf("S" to mapOf("T" to 1), "T" to emptyMap()), "S", "T") {
                if (it == "S") 2 else 0
            }
        }
    }

    @Test
    fun nullPredecessor_isRejectedDuringReconstruction() {
        assertThrows(IllegalStateException::class.java) {
            reconstructPath(
                mapOf("S" to mapOf("T" to 1), "T" to emptyMap()),
                mapOf("S" to Node(0, 0), "T" to Node(1, 1)), "S", "T"
            )
        }
    }

    @Test(timeout = 1000)
    fun predecessorCycle_isRejectedWithoutLoopingForever() {
        assertThrows(IllegalStateException::class.java) {
            reconstructPath(
                mapOf("S" to emptyMap(), "A" to mapOf("B" to 0), "B" to mapOf("A" to 0)),
                mapOf("S" to Node(0, 0), "A" to Node(0, 0, "B"), "B" to Node(0, 0, "A")),
                "S", "B"
            )
        }
    }

    @Test
    fun missingPredecessorNode_isRejectedDuringReconstruction() {
        assertThrows(IllegalStateException::class.java) {
            reconstructPath(
                mapOf("S" to mapOf("T" to 1), "T" to emptyMap()),
                mapOf("S" to Node(0, 0), "T" to Node(1, 1, "ghost")), "S", "T"
            )
        }
    }

    @Test
    fun nonexistentPredecessorEdge_isRejectedDuringReconstruction() {
        assertThrows(IllegalStateException::class.java) {
            reconstructPath(
                mapOf("S" to emptyMap(), "T" to emptyMap()),
                mapOf("S" to Node(0, 0), "T" to Node(1, 1, "S")), "S", "T"
            )
        }
    }

    @Test
    fun incorrectPredecessorCost_isRejectedDuringReconstruction() {
        assertThrows(IllegalStateException::class.java) {
            reconstructPath(
                mapOf("S" to mapOf("T" to 1), "T" to emptyMap()),
                mapOf("S" to Node(0, 0), "T" to Node(2, 2, "S")), "S", "T"
            )
        }
    }

    @Test
    fun invalidStartState_isRejectedDuringReconstruction() {
        assertThrows(IllegalStateException::class.java) {
            reconstructPath(mapOf("S" to emptyMap()), mapOf("S" to Node(1, 1)), "S", "S")
        }
    }

    @Test
    fun displayHelpers_formatComputedDataAndNoPath() {
        val result = BookExample.findPath()
        assertTrue(displayGraph(BookExample.graph).contains("Neighbors: B:4 C:6 D:5"))
        assertTrue(displayList(result.visited).contains("J: Node(gScore=17, fScore=17, previousNode=K)"))
        assertEquals("Path: A -> D -> H -> K -> J\nCost: 17", displayShortestPath(result))
        assertEquals("No path found", displayShortestPath(PathResult(emptyList(), null, emptyMap())))
    }

    @Test
    fun independentBellmanFord_agreesForEveryBookStart() {
        for (startNode in BookExample.graph.keys) {
            val expectedCost = referenceShortestCost(BookExample.graph, startNode, "J")
            val result = aStar(BookExample.graph, startNode, "J", ::getHScore)
            assertEquals("start=$startNode", expectedCost, result.totalCost)
            assertValidPath(BookExample.graph, startNode, "J", result)
            assertTrue(getHScore(startNode) <= checkNotNull(expectedCost))
        }
    }

    private fun assertValidPath(
        graph: Map<String, Map<String, Int>>,
        startNode: String,
        targetNode: String,
        result: PathResult
    ) {
        assertTrue(result.found)
        assertEquals(startNode, result.path.first())
        assertEquals(targetNode, result.path.last())
        assertEquals(result.path.size, result.path.toSet().size)
        var cost = 0L
        for ((from, to) in result.path.zipWithNext()) {
            assertTrue("Missing edge $from -> $to", graph[from]?.containsKey(to) == true)
            cost += graph.getValue(from).getValue(to)
        }
        assertEquals(checkNotNull(result.totalCost).toLong(), cost)
    }

    // Independent test-only Bellman–Ford relaxation: no A* heuristic/frontier/predecessor code.
    private fun referenceShortestCost(
        graph: Map<String, Map<String, Int>>,
        startNode: String,
        targetNode: String
    ): Int? {
        val distance = mutableMapOf(startNode to 0L)
        repeat(graph.size - 1) {
            for ((node, neighbors) in graph) {
                val from = distance[node] ?: continue
                for ((neighbor, weight) in neighbors) {
                    val candidate = from + weight
                    if (candidate < (distance[neighbor] ?: Long.MAX_VALUE)) {
                        distance[neighbor] = candidate
                    }
                }
            }
        }
        return distance[targetNode]?.toInt()
    }
}
