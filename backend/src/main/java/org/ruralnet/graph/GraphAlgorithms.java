package org.ruralnet.graph;

import java.math.BigDecimal;
import java.util.*;

/** Deterministic Kruskal/Prim, non-negative Dijkstra, and Tarjan analyses. */
public final class GraphAlgorithms {
  private GraphAlgorithms() {}
  private static Comparator<GraphSnapshot.Edge> order() { return Comparator.comparing(GraphSnapshot.Edge::cost).thenComparing(e -> min(e.a(),e.b()).toString()).thenComparing(e -> max(e.a(),e.b()).toString()); }
  private static UUID min(UUID a, UUID b) { return a.compareTo(b) <= 0 ? a : b; }
  private static UUID max(UUID a, UUID b) { return a.compareTo(b) <= 0 ? b : a; }
  public record Forest(List<GraphSnapshot.Edge> edges, BigDecimal totalCost, int components, Map<String,BigDecimal> componentCosts, List<UUID> isolatedNodeIds, long executionTimeMs) {}
  public static Forest kruskal(GraphSnapshot g) {
    long t=System.nanoTime(); Map<UUID,UUID> p=new HashMap<>(); g.nodes().forEach(n->p.put(n.id(),n.id()));
    List<GraphSnapshot.Edge> chosen=new ArrayList<>(); BigDecimal sum=BigDecimal.ZERO;
    for (var e:g.activeEdges().stream().sorted(order()).toList()) if (!find(p,e.a()).equals(find(p,e.b()))) { p.put(find(p,e.a()),find(p,e.b())); chosen.add(e); sum=sum.add(e.cost()); }
    return forest(g,chosen,sum,(System.nanoTime()-t)/1_000_000);
  }
  public static Forest prim(GraphSnapshot g) {
    long t=System.nanoTime(); Map<UUID,List<GraphSnapshot.Edge>> adj=adj(g); Set<UUID> seen=new HashSet<>(); List<GraphSnapshot.Edge> picked=new ArrayList<>(); BigDecimal sum=BigDecimal.ZERO;
    for (UUID root:g.nodes().stream().map(GraphSnapshot.Node::id).sorted().toList()) if(seen.add(root)) {
      PriorityQueue<GraphSnapshot.Edge> q=new PriorityQueue<>(order()); q.addAll(adj.getOrDefault(root,List.of()));
      while(!q.isEmpty()){var e=q.remove(); boolean a=seen.contains(e.a()), b=seen.contains(e.b()); if(a==b)continue; UUID next=a?e.b():e.a(); seen.add(next); picked.add(e); sum=sum.add(e.cost()); for(var x:adj.getOrDefault(next,List.of())) if(!seen.contains(x.a())||!seen.contains(x.b()))q.add(x);}
    }
    return forest(g,picked,sum,(System.nanoTime()-t)/1_000_000);
  }
  private static Forest forest(GraphSnapshot g,List<GraphSnapshot.Edge> es,BigDecimal cost,long ms) {
    Map<UUID,UUID> p=new HashMap<>(); g.nodes().forEach(n->p.put(n.id(),n.id())); es.forEach(e->p.put(find(p,e.a()),find(p,e.b())));
    Map<String,BigDecimal> totals=new TreeMap<>(); Map<String,Integer> sizes=new HashMap<>(); g.nodes().forEach(n->{String r=find(p,n.id()).toString(); sizes.merge(r,1,Integer::sum); totals.putIfAbsent(r,BigDecimal.ZERO);}); es.forEach(e->totals.merge(find(p,e.a()).toString(),e.cost(),BigDecimal::add));
    Set<UUID> incident=new HashSet<>(); es.forEach(e->{incident.add(e.a());incident.add(e.b());});
    return new Forest(List.copyOf(es),cost,totals.size(),totals,g.nodes().stream().map(GraphSnapshot.Node::id).filter(x->!incident.contains(x)).toList(),ms);
  }
  private static UUID find(Map<UUID,UUID> p,UUID x){UUID r=p.get(x);if(r==null)return x;if(!r.equals(x)){r=find(p,r);p.put(x,r);}return r;}
  private static Map<UUID,List<GraphSnapshot.Edge>> adj(GraphSnapshot g){Map<UUID,List<GraphSnapshot.Edge>> m=new HashMap<>();for(var e:g.activeEdges()){m.computeIfAbsent(e.a(),k->new ArrayList<>()).add(e);m.computeIfAbsent(e.b(),k->new ArrayList<>()).add(e);}return m;}
  public record Route(boolean reachable,List<UUID> nodeIds,List<UUID> edgeIds,BigDecimal metricTotal,BigDecimal totalDistanceKm,BigDecimal totalCost,String explanation) {}
  public static Route dijkstra(GraphSnapshot g,UUID source,UUID destination,String metric){
    Map<UUID,List<GraphSnapshot.Edge>> a=adj(g); Map<UUID,BigDecimal>d=new HashMap<>();Map<UUID,UUID>prev=new HashMap<>();Map<UUID,GraphSnapshot.Edge>via=new HashMap<>();
    PriorityQueue<Map.Entry<UUID,BigDecimal>>q=new PriorityQueue<>(Map.Entry.comparingByValue());d.put(source,BigDecimal.ZERO);q.add(Map.entry(source,BigDecimal.ZERO));
    while(!q.isEmpty()){var cur=q.poll();if(cur.getValue().compareTo(d.get(cur.getKey()))!=0)continue;if(cur.getKey().equals(destination))break;for(var e:a.getOrDefault(cur.getKey(),List.of())){UUID v=e.a().equals(cur.getKey())?e.b():e.a();BigDecimal w=metric.equals("DISTANCE")?e.distanceKm():e.cost(), nd=cur.getValue().add(w);if(nd.compareTo(d.getOrDefault(v,new BigDecimal("1E100")))<0){d.put(v,nd);prev.put(v,cur.getKey());via.put(v,e);q.add(Map.entry(v,nd));}}}
    if(!d.containsKey(destination))return new Route(false,List.of(),List.of(),BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,"The destination is unreachable in the active network.");
    List<UUID> ns=new ArrayList<>(),es=new ArrayList<>();BigDecimal dist=BigDecimal.ZERO,cost=BigDecimal.ZERO;UUID x=destination;ns.add(x);while(!x.equals(source)){var e=via.get(x);es.add(e.id());dist=dist.add(e.distanceKm());cost=cost.add(e.cost());x=prev.get(x);ns.add(x);}Collections.reverse(ns);Collections.reverse(es);return new Route(true,ns,es,d.get(destination),dist,cost,null);
  }
  public record Criticality(List<UUID> bridgeEdgeIds,List<UUID> articulationNodeIds){}
  public static Criticality criticality(GraphSnapshot g){Map<UUID,List<GraphSnapshot.Edge>> a=adj(g);Map<UUID,Integer> disc=new HashMap<>(),low=new HashMap<>();Set<UUID> points=new HashSet<>();Set<UUID> bridges=new HashSet<>();int[] time={0};for(var n:g.nodes())if(!disc.containsKey(n.id()))dfs(n.id(),null,a,disc,low,points,bridges,time);return new Criticality(bridges.stream().sorted().toList(),points.stream().sorted().toList());}
  private static void dfs(UUID u,UUID parent,Map<UUID,List<GraphSnapshot.Edge>>a,Map<UUID,Integer>d,Map<UUID,Integer>l,Set<UUID>p,Set<UUID>b,int[]t){d.put(u,++t[0]);l.put(u,d.get(u));int kids=0;for(var e:a.getOrDefault(u,List.of())){UUID v=e.a().equals(u)?e.b():e.a();if(v.equals(parent))continue;if(!d.containsKey(v)){kids++;dfs(v,u,a,d,l,p,b,t);l.put(u,Math.min(l.get(u),l.get(v)));if(l.get(v)>d.get(u))b.add(e.id());if(parent!=null&&l.get(v)>=d.get(u))p.add(u);}else l.put(u,Math.min(l.get(u),d.get(v)));}if(parent==null&&kids>1)p.add(u);}
}
