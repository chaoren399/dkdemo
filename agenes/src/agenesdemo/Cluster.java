package agenesdemo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzy on 15/11/16.
 */
public class Cluster {
    private List<Route> routes = new ArrayList<Route>(); // 类簇中的样本点
    private String clusterName;

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }
}
