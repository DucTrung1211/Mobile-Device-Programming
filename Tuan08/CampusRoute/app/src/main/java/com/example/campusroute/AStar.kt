package com.example.campusroute

/**
 * Project 32 node scores (PDF 399–400).
 * ADAPTED: immutable values and null instead of the book's "none" predecessor.
 */
data class Node(
    val gScore: Int = Int.MAX_VALUE,
    val fScore: Int = Int.MAX_VALUE,
    val previousNode: String? = null
)

/** ADAPTED: a computed path/cost plus the closed nodes in expansion order. */
data class PathResult(
    val path: List<String>,
    val totalCost: Int?,
    val visited: Map<String, Node>
) {
    val found: Boolean get() = totalCost != null
    val exploredNodes: List<String> get() = visited.keys.toList()
}

/**
 * A* on a finite weighted graph, without Android or Compose dependencies.
 *
 * ADAPTED from Project 32 (PDF 404–407):
 * - Only discovered nodes enter unvisited; unreachable infinity scores are never expanded.
 * - A supplied heuristic must be nonnegative, zero at the goal, and consistent on every edge.
 *   These conditions allow a closed set without reopening nodes and guarantee optimality.
 * - The default zero heuristic works for any goal; BookExample explicitly supplies getHScore.
 *
 * Invalid input/heuristics throw IllegalArgumentException. A valid graph with no route returns
 * found=false, an empty path and null cost. Scores exceeding Int throw ArithmeticException;
 * overflow is never silently wrapped. The caller must not mutate the graph during a search.
 */
fun aStar(
    graph: Map<String, Map<String, Int>>,
    startNode: String,
    targetNode: String,
    heuristic: (String) -> Int = { 0 }
): PathResult {
    require(graph.isNotEmpty()) { "Graph must not be empty" }
    require(startNode in graph) { "Start node does not exist: $startNode" }
    require(targetNode in graph) { "Target node does not exist: $targetNode" }
    for ((node, neighbors) in graph) {
        for ((neighbor, cost) in neighbors) {
            require(neighbor in graph) { "Unknown neighbor $neighbor of $node" }
            require(cost >= 0) { "Negative edge weight: $node -> $neighbor" }
        }
    }

    // Evaluate once so validation and the search use exactly the same h-scores.
    val hScores = graph.keys.associateWith { node ->
        heuristic(node).also { require(it >= 0) { "Negative heuristic for $node" } }
    }
    require(hScores.getValue(targetNode) == 0) { "Target heuristic must be zero" }
    for ((node, neighbors) in graph) {
        for ((neighbor, cost) in neighbors) {
            require(hScores.getValue(node).toLong() <= cost.toLong() + hScores.getValue(neighbor)) {
                "Inconsistent heuristic on edge $node -> $neighbor"
            }
        }
    }

    val nodes = graph.keys.associateWith { Node() }.toMutableMap()
    val visited = linkedMapOf<String, Node>()
    val unvisited = linkedMapOf<String, Node>()
    val hScore = hScores.getValue(startNode)
    val start = Node(gScore = 0, fScore = hScore)
    nodes[startNode] = start
    unvisited[startNode] = start

    while (unvisited.isNotEmpty()) {
        val currentNode = checkNotNull(getCurrentNode(unvisited))
        val current = checkNotNull(unvisited.remove(currentNode))
        visited[currentNode] = current

        if (currentNode == targetNode) {
            val path = reconstructPath(graph, visited, startNode, targetNode)
            return PathResult(path, current.gScore, visited.toMap())
        }

        val neighbors = graph.getValue(currentNode)
        for ((node, cost) in neighbors) {
            if (node in visited) continue

            val newGScore = checkedScore(current.gScore.toLong() + cost, "gScore")
            // Membership, rather than MAX_VALUE alone, distinguishes an undiscovered node.
            if (node !in unvisited || newGScore < nodes.getValue(node).gScore) {
                val updated = Node(
                    gScore = newGScore,
                    fScore = checkedScore(newGScore.toLong() + hScores.getValue(node), "fScore"),
                    previousNode = currentNode
                )
                nodes[node] = updated
                unvisited[node] = updated
            }
        }
    }

    return PathResult(emptyList(), null, visited.toMap())
}

/** ADAPTED: stable ties by node name, and null for an empty frontier instead of !!. */
fun getCurrentNode(unvisited: Map<String, Node>): String? =
    unvisited.entries.minWithOrNull(
        compareBy<Map.Entry<String, Node>> { it.value.fScore }.thenBy { it.key }
    )?.key

/**
 * ADAPTED from displayShortestPath (PDF 403): return IDs, not a concatenated string.
 * Guard malformed chains as well as cycles; this helper never invents a missing predecessor.
 */
internal fun reconstructPath(
    graph: Map<String, Map<String, Int>>,
    visited: Map<String, Node>,
    startNode: String,
    targetNode: String
): List<String> {
    val path = mutableListOf<String>()
    val seen = mutableSetOf<String>()
    var currentNode = targetNode

    while (true) {
        check(seen.add(currentNode)) { "Cycle in predecessor chain at $currentNode" }
        val current = checkNotNull(visited[currentNode]) { "Missing path node: $currentNode" }
        path.add(currentNode)
        if (currentNode == startNode) {
            check(current.gScore == 0 && current.previousNode == null) { "Invalid start state" }
            break
        }

        val previousNode = checkNotNull(current.previousNode) {
            "Missing predecessor for $currentNode"
        }
        val previous = checkNotNull(visited[previousNode]) { "Missing path node: $previousNode" }
        val cost = checkNotNull(graph[previousNode]?.get(currentNode)) {
            "Predecessor edge does not exist: $previousNode -> $currentNode"
        }
        check(cost >= 0 && previous.gScore.toLong() + cost == current.gScore.toLong()) {
            "Predecessor cost does not match at $currentNode"
        }
        currentNode = previousNode
    }

    return path.asReversed().toList()
}

private fun checkedScore(value: Long, label: String): Int {
    if (value !in 0L..Int.MAX_VALUE.toLong()) {
        throw ArithmeticException("$label exceeds the nonnegative Int range: $value")
    }
    return value.toInt()
}
