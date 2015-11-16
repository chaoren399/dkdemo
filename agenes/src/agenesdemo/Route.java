package agenesdemo;

/**
 * Created by zzy on 15/11/16.
 */
public class Route {

    public   String routename; // 样本点名
    public   Cluster cluster; // 样本点所属类簇
    public  String carnumber;// 车牌


    public Route(){

    }
    public Route(String routename){
        this.routename = routename;
    }

    public String getRoutename() {
        return routename;
    }

    public void setRoutename(String routename) {
        this.routename = routename;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public String getCarnumber() {
        return carnumber;
    }

    public void setCarnumber(String carnumber) {
        this.carnumber = carnumber;
    }

    @Override
    public String toString() {
        return "Route{" +
                "routename='" + routename + '\'' +
                ", cluster=" + cluster +
                ", carnumber='" + carnumber + '\'' +
                '}';
    }
}
