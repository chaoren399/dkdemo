import org.apache.hadoop.mapred.JobConf;

/**
 * Created by zzy on 15/11/30.
 */
public class TestMahoutKmeans {
    private static final String HDFS = "hdfs:://192.168.122.211:9000";

    public static void main(String[] args) {
        String localFile = "randomData.csv";
        String inPath = HDFS+"/user/";
        String seqFile = inPath+"/seqfile";
        String seeds = inPath+"/seeds";
        String outPath = inPath +"/result/";
        String clusteredPoints = outPath +"clusteredPoints";

        JobConf con = config();
//        HdfsDao hdfs = new HdfsDao


    }


    public static JobConf config() {
        JobConf conf = new JobConf();
        conf.setJobName("ItemCFHadoop");
        conf.addResource("classpath:/hadoop/core-site.xml");
        conf.addResource("classpath:/hadoop/hdfs-site.xml");
        conf.addResource("classpath:/hadoop/mapred-site.xml");

        return conf;
    }

}
