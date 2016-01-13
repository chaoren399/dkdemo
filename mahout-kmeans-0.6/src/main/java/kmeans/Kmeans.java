package kmeans;
// mahout 0.6

//import org.apache.hadoop.mapreduce.Cluster;
//import org.apache.mahout.clustering.Cluster;
import org.apache.mahout.clustering.kmeans.KMeansClusterer;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.math.Vector;
import org.apache.mahout.clustering.kmeans.Cluster;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzy on 15/12/1.
 */
public class Kmeans {
    public static void main(String[] args) throws IOException {

        String path = "/Users/zzy/IdeaProjects/java_workspace/dkdemo/mahout-kmeans-0.6/src/main/resources/randomData.csv";
        List sampleData = MathUtil.readFileToVector(path);;
        int k = 3;
        double threshold = 0.01;

        List<Vector> randomPoints = MathUtil.chooseRandomPoints(sampleData,k);//初始化聚类中心
        for (Vector vector:randomPoints){
            System.out.println("Init Point center:"+vector);

        }
        List clusters = new ArrayList();
        for (int i = 0; i < k; i++) {
            clusters.add(new Cluster(randomPoints.get(i),i,new EuclideanDistanceMeasure()));
        }


        List<List<Cluster>> finalClusters = KMeansClusterer.clusterPoints(sampleData,clusters,new EuclideanDistanceMeasure(),k,threshold);
        for (Cluster cluster:finalClusters.get(finalClusters.size()-1)){
            System.out.println("Cluster id :"+ cluster.getId()+"center:"+ cluster.getCenter().asFormatString());

        }

    }
}
