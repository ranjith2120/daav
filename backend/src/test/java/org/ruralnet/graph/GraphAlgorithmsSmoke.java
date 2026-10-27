package org.ruralnet.graph;

import java.math.BigDecimal;
import java.util.*;

/** Dependency-free executable checks for the pure graph module: javac/java this class with GraphSnapshot and GraphAlgorithms. */
public final class GraphAlgorithmsSmoke {
  private static void check(boolean condition,String message){if(!condition)throw new AssertionError(message);}
  public static void main(String[] args){
    UUID a=UUID.fromString("00000000-0000-0000-0000-000000000001"),b=UUID.fromString("00000000-0000-0000-0000-000000000002"),c=UUID.fromString("00000000-0000-0000-0000-000000000003"),d=UUID.fromString("00000000-0000-0000-0000-000000000004"),z=UUID.fromString("00000000-0000-0000-0000-000000000005");
    var nodes=List.of(new GraphSnapshot.Node(a,"A","A","VILLAGE",0,0),new GraphSnapshot.Node(b,"B","B","HOSPITAL",0,0),new GraphSnapshot.Node(c,"C","C","SCHOOL",0,0),new GraphSnapshot.Node(d,"D","D","WAREHOUSE",0,0),new GraphSnapshot.Node(z,"Z","Isolated","VILLAGE",0,0));
    var edges=List.of(edge(1,a,b,"2","2"),edge(2,b,c,"3","3"),edge(3,a,c,"4","4"),edge(4,c,d,"1","5"));var g=new GraphSnapshot(nodes,edges,7);
    var k=GraphAlgorithms.kruskal(g);var p=GraphAlgorithms.prim(g);check(k.components()==2&&p.components()==2,"disconnected forest component count");check(k.edges().size()==nodes.size()-k.components(),"forest edge cardinality");check(k.totalCost().compareTo(p.totalCost())==0,"Prim/Kruskal totals");check(k.totalCost().compareTo(new BigDecimal("10"))==0,"exact cost total");check(k.isolatedNodeIds().contains(z),"isolated node reporting");
    var route=GraphAlgorithms.dijkstra(g,a,d,"DISTANCE");check(route.reachable()&&route.nodeIds().equals(List.of(a,c,d)),"shortest route reconstruction");check(route.totalDistanceKm().compareTo(new BigDecimal("5"))==0,"route physical distance");check(!GraphAlgorithms.dijkstra(g,a,z,"COST").reachable(),"unreachable route");
    var critical=GraphAlgorithms.criticality(g);check(critical.bridgeEdgeIds().containsAll(List.of(edges.get(3).id())),"bridge detection");check(critical.articulationNodeIds().contains(c),"articulation point detection");check(g.edges().size()==4,"snapshot immutability");System.out.println("Graph algorithms checks passed.");
  }
  private static GraphSnapshot.Edge edge(int n,UUID a,UUID b,String distance,String cost){return new GraphSnapshot.Edge(new UUID(0,n),a,b,new BigDecimal(distance),new BigDecimal(cost),BigDecimal.TEN,"ACTIVE");}
}
