package agenesdemo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzy on 15/11/16.
 */
public class ClusterAnalysis {


    public List<Cluster> startAnalysis(List<Route> routes,int ClusterNum){
        List<Cluster> finalClusters = new ArrayList<Cluster>();
        List<Cluster> originalClusters = initialCluster(routes);
        finalClusters = originalClusters;
        while (finalClusters.size()>ClusterNum){
            double min = Double.MAX_VALUE;
            int mergeIndexA = 0;
            int mergeIndexB = 0;
            for (int i = 0; i < finalClusters.size(); i++) {
                for (int j = 0; j < finalClusters.size(); j++) {
                    if (i != j){
                        Cluster clusterA = finalClusters.get(i);
                        Cluster clusterB = finalClusters.get(j);

                        List<Route> routesA = clusterA.getRoutes();
                        List<Route> routesB = clusterB.getRoutes();

                        for (int m = 0; m < routesA.size(); m++) {
                            for (int n = 0; n < routesB.size(); n++) {
//                                double tempDis = getDistance (routesA.get(m),routesB.get(n));
                                  double tempDis = LevenshteinDistance.sim(routesA.get(m),routesB.get(n));
                                  if (tempDis < min){
                                      min = tempDis;
                                      mergeIndexA = i;
                                      mergeIndexB = j;
                                  }
                            }
                        }
                    }
                }// for j
            } // for i

            // 合并 cluster [megeIndexA]和 cluster[mergeIndexB]
            finalClusters = mergeCluster(finalClusters,mergeIndexA,mergeIndexB);
        } // end while
        return finalClusters;
    }



    /*
    初始化类簇
     */
    private List<Cluster> initialCluster(List<Route> routes) {
        List<Cluster> originalClusters = new ArrayList<Cluster>();
        for (int i = 0; i < routes.size(); i++) {
            Route tempRoute = routes.get(i);
            List<Route> tempRoutes = new ArrayList<Route>();
            tempRoutes.add(tempRoute);

            Cluster tempCluster = new Cluster();
            tempCluster.setClusterName("Cluster"+String.valueOf(i));
            tempCluster.setRoutes(tempRoutes);

            tempRoute.setCluster(tempCluster);
            originalClusters.add(tempCluster);
        }
        return originalClusters;
    }

    private List<Cluster> mergeCluster(List<Cluster> clusters, int mergeIndexA, int mergeIndexB) {

        if (mergeIndexA != mergeIndexB){
            //将 cluster[mergeIndexB] 中的 route 加入到 cluster [megeIndexA] 中
            Cluster clusterA = clusters.get(mergeIndexA);
            Cluster clusterB = clusters.get(mergeIndexB);

            List<Route> routeA = clusterA.getRoutes();
            List<Route> routeB = clusterB.getRoutes();

            for (Route route :routeB){
                Route tempRoute = new Route();
                tempRoute = route;
                tempRoute.setCluster(clusterA);
                routeA.add(tempRoute);
            }
            clusterA.setRoutes(routeA);

            //List<Cluster> clusters中移除cluster[mergeIndexB]

            clusters.remove(mergeIndexB);

        }
        return clusters;
    }



    public static  void main(String[] args){
        ArrayList<Route>routes = new ArrayList<Route>();
        Route route1 = new Route("东海路与燕儿岛路路口 山东路海泊桥 山东路与抚顺路路口 辽阳西路与劲松四路路口 重庆中路与振华路路口");
        Route route2 = new Route("青岛东收费站 夏庄主站收费站 S217朱诸路-张应大朱戈 胶宁高架路三百惠桥上");
        Route route3 = new Route("青兰高速（双埠-管家楼）K23+800桩号增大方向");
        Route route4 = new Route("山东路");
        Route route5 = new Route("青岛路");
        Route route6 = new Route("山东路海泊桥");
        Route route7 = new Route("山东路与鞍山路路口");
        Route route8 = new Route("宁夏路与福州南路路口");

        routes.add(route1);
        routes.add(route2);
        routes.add(route3);
        routes.add(route4);
        routes.add(route5);
        routes.add(route6);
        routes.add(route7);
        routes.add(route8);

        int clusterNum = 3;// 类簇数
        ClusterAnalysis ca = new ClusterAnalysis();
        List<Cluster> clusters = ca.startAnalysis(routes,clusterNum);
        for (Cluster cl : clusters){
            System.out.println("------"+cl.getClusterName()+"----");
            List<Route> tempRoutes = cl.getRoutes();
            for (Route temproute :tempRoutes){
                System.out.println(temproute.getRoutename());
            }
        }
    }
}


