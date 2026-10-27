package org.ruralnet.graph;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/** Immutable graph input. Algorithms depend on these records only, never on persistence. */
public record GraphSnapshot(List<Node> nodes, List<Edge> edges, long revision) {
  public GraphSnapshot { nodes = List.copyOf(nodes); edges = List.copyOf(edges); }
  public record Node(UUID id, String code, String name, String type, double latitude, double longitude) {}
  public record Edge(UUID id, UUID a, UUID b, BigDecimal distanceKm, BigDecimal cost, BigDecimal capacity, String status) {}
  public List<Edge> activeEdges() { return edges.stream().filter(e -> e.status().equals("ACTIVE")).toList(); }
}
