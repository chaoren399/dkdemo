package com.dk.kmeans;

import com.dk.hdfs.HdfsDAO;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobConf;
import org.apache.mahout.clustering.conversion.InputDriver;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.clustering.kmeans.RandomSeedGenerator;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.utils.clustering.ClusterDumper;

/**
 * Created by zzy on 15/11/30.
 *
 * 配置环境变量 HADOOP_USER_NAME = root
 */
public class TestMahoutKmeans {
    private static final String HDFS = "hdfs://192.168.122.211:9000";

    public static void main(String[] args) throws Exception {
        String localFile = "datafile/randomData.csv";
        String inPath = HDFS+"/user/hadoop/mix_data/";
        String seqFile = inPath+"/seqfile";
        String seeds = inPath+"/seeds";
        String outPath = inPath +"/result/";
        String clusteredPoints = outPath +"clusteredPoints";

        JobConf con = config();
//        HdfsDao hdfs = new HdfsDao
        HdfsDAO hdfs = new HdfsDAO(HDFS,con);
//        hdfs.ls(inPath);
        hdfs.rmr(inPath);
        hdfs.mkdirs(inPath);
        hdfs.copyFile("/Users/zzy/IdeaProjects/java_workspace/dkdemo/mahout-kmeans/datafile/randomData.csv", inPath);
        hdfs.ls(inPath);

        InputDriver.runJob(new Path(inPath),new Path(seqFile),"org.apache.mahout.math.RandomAccessSparseVector");

        int k = 3;
        Path seqFilePath = new Path(seqFile);
        Path clustersSeeds = new Path(seeds);
        DistanceMeasure measure = new EuclideanDistanceMeasure();
        clustersSeeds = RandomSeedGenerator.buildRandom(con,seqFilePath,clustersSeeds, k, measure);
        KMeansDriver.run(con, seqFilePath, clustersSeeds, new Path(outPath),measure,0.01,10,true,0.01,false);

        Path outGlobPath = new Path(outPath,"clusters-*-final");
        Path clusteedPointsPath = new Path(clusteredPoints);
//        System.out.printf("Dumping out clusters from clusters : s% and clusterPoints:s% \n",outGlobPath,clusteedPointsPath);

        ClusterDumper clusterDumper = new ClusterDumper(outGlobPath,clusteedPointsPath);
        clusterDumper.printClusters(null);

    }


    public static JobConf config() {
        JobConf conf = new JobConf();
        conf.setJobName("TestMahoutKmeans");
//        conf.addResource("classpath:/hadoop/core-site.xml");
//        conf.addResource("classpath:/hadoop/hdfs-site.xml");
//        conf.addResource("classpath:/hadoop/mapred-site.xml");

        return conf;
    }

}
