/**
 * Created by zzy on 15/11/15.
 */

import java.util.List;
import java.util.ArrayList;


public class Kmeans {
    private List<Route> routes = new ArrayList<Route>();
    /*
     * 初始化列表
     */
    private List<Route>  initRoutes;
    /*
     * 分类数
     */
    private int k = 1;

    public Kmeans(){

    }

    /*
     * 初始化列表
     */
    public Kmeans(List<Route> list,int k ){
        this.routes = list;
        this.k = k;
        Route r = list.get(0);

        initRoutes = new ArrayList<Route>();
        for(int i = 0; i< k ; i++){
            initRoutes.add(routes.get(i));
        }

    }

    public List <Route>[] comput() {
        List <Route>[] results = new  ArrayList[k];
        boolean centerchange = true;
        while (centerchange){
            centerchange = false;
            for (int i = 0; i < k; i++){
                results[i] = new ArrayList<Route>();
            }
            for (int i = 0; i < routes.size(); i++){
                Route r = routes.get(i);
                double [] dists = new double [k];
                for (int j = 0; j < initRoutes.size(); j++){
                    Route initR = initRoutes.get(j);
					/*
					 * 计算距离
					 * 所有点与 中心点计算距离
					 */
                    double dist = distance (initR, r);
                    dists[j] = dist;

                } // for j

                int dist_index = computOrder(dists);
                results[dist_index].add(r); // 将样本点放到对应的簇中
            }// for i
            for (int i = 0; i < k; i++){
                Route route_new = findNewCenter (results[i]);
                Route route_old = initRoutes.get(i);
                if (!IsPlayerEqual(route_new, route_old)){
                    centerchange = true;
                    initRoutes.set(i, route_new);// 更新聚簇中心点
                }
            }
        }

        return  results;
    }
    /**
     * 得到新聚类中心对象
     * @param ps
     * @return
     */
    private Route findNewCenter(List<Route> ps) {

        Route r = new Route();
        if (ps == null || ps.size() == 0){
            return r;
        }
        double ds ;
        for ( Route route : ps){
            // 遍历簇中得每个对象,计算相似度,求平均值.

            LevenshteinDistance.sim()


        }
        return null;
    }

    /**
     * 生成 索引,
     * @param dists
     * @return
     */

    private int computOrder(double[] dists) {
        // TODO Auto-generated method stub
        double min = 0;
        int index = 0;
        for (int i = 0; i<dists.length -1; i++){
            double dist0 = dists[i];
            if (i == 0){
                min = dist0;
                index = 0;
            }
            double dist1 = dists[i + 1];
            if (min > dist1){
                min = dist1;
                index = i +1;
            }
        }
        return index;
    }

    /**
     * 计算距离
     * @param initR
     * @param r
     * @return
     */
    private double distance(Route initR, Route r) {
        // TODO Auto-generated method stub

        return LevenshteinDistance.sim(initR,r);
    }



}

