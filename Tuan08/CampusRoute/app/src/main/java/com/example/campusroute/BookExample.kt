package com.example.campusroute


object BookExample {
    val graph: Map<String, Map<String, Int>> = mapOf(
        "A" to mapOf("B" to 4, "C" to 6, "D" to 5),
        "B" to mapOf("A" to 4, "C" to 2, "E" to 4, "F" to 4),
        "C" to mapOf("A" to 6, "B" to 2, "D" to 3, "F" to 3),
        "D" to mapOf("A" to 5, "C" to 3, "G" to 6, "H" to 4),
        "E" to mapOf("B" to 4, "I" to 6),
        "F" to mapOf("B" to 4, "C" to 3, "G" to 4, "I" to 5),
        "G" to mapOf("D" to 6, "F" to 4, "I" to 6, "K" to 3),
        "H" to mapOf("D" to 4, "K" to 3),
        "I" to mapOf("E" to 6, "F" to 5, "G" to 6, "J" to 6),
        "J" to mapOf("I" to 6, "K" to 5),
        "K" to mapOf("G" to 3, "H" to 3, "J" to 5, "L" to 3),
        "L" to mapOf("K" to 3, "M" to 5),
        "M" to mapOf("L" to 5)
    )

    const val startNode = "A"
    const val targetNode = "J"

    fun findPath(): PathResult = aStar(graph, startNode, targetNode, ::getHScore)
}


fun getHScore(node: String): Int = when (node) {
    "A" -> 8
    "B", "C", "D", "M" -> 6
    "E", "F", "G", "H", "L" -> 4
    "I", "K" -> 2
    "J" -> 0
    else -> 0
}


fun displayGraph(graph: Map<String, Map<String, Int>>): String =
    graph.entries.joinToString("\n") { (node, neighbors) ->
        "Node: $node\nNeighbors: " +
            neighbors.entries.joinToString(" ") { (neighbor, cost) -> "$neighbor:$cost" }
    }

fun displayList(mapList: Map<String, Node>): String =
    "(g-score, f-score, previous)\n" +
        mapList.entries.joinToString("\n") { (node, attributes) -> "$node: $attributes" }


fun displayShortestPath(result: PathResult): String =
    if (result.found) {
        "Path: ${result.path.joinToString(" -> ")}\nCost: ${result.totalCost}"
    } else {
        "No path found"
    }
