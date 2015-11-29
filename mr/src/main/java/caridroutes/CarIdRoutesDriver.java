package caridroutes;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Created by zzy on 15/11/24.
 */
public class CarIdRoutesDriver extends Configured implements Tool{

    private static final String tableName = "carroutes";
    public int run(String[] args) throws Exception {

        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum","192.168.122.213:2181");
        conf.set("hbase.rootdir","hdfs://192.168.122.211:9000/hbase");
        conf.set(TableOutputFormat.OUTPUT_TABLE, tableName);

        Job job = Job.getInstance(conf,CarIdRoutesDriver.class.getSimpleName());

        TableMapReduceUtil.addDependencyJars(job);
        job.setJarByClass(CarIdRoutesDriver.class);
        job.setMapperClass(CarIdRoutesMapper.class);
        job.setReducerClass(CarIdRoutesReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

//  job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TableOutputFormat.class);

        FileInputFormat.setInputPaths(job, "hdfs://192.168.122.211:9000/user/hbase/dk");
        boolean status =job.waitForCompletion(true);
        return status?0:1 ;
    }
    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new CarIdRoutesDriver(), args);
        System.exit(run);
    }
}
