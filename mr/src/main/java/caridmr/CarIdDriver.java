package caridmr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import util.PropUtils;

import java.util.Properties;

/**
 * Created by zzy on 15/12/2.
 */
public class CarIdDriver extends Configured implements Tool {
    public int run(String[] args) throws Exception {

//        if (args.length !=2){
//            System.out.println("please check your input");
//            return -1;
//        }
        Properties prop = PropUtils.getProp("hbase.properties");//
        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", prop.getProperty("hbase.zookeeper.quorum1"));
        conf.set("hbase.rootdir", prop.getProperty("hbase.rootdir"));
        conf.set(TableOutputFormat.OUTPUT_TABLE, prop.getProperty("hbase.table.traffic_route"));

        Job job = Job.getInstance(conf,CarIdDriver.class.getSimpleName());

        TableMapReduceUtil.addDependencyJars(job);
        job.setJarByClass(CarIdDriver.class);
        job.setMapperClass(CarIdMapper.class);
        job.setReducerClass(CarIdReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

//        job.setNumReduceTasks(Integer.parseInt(args[1]));
//  job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TableOutputFormat.class);

//  FileInputFormat.setInputPaths(job, "hdfs://192.168.122.211:9000/user/hbase/dk/10.txt");
       String inputpath = "hdfs://123.56.231.34:9000/dk/10.txt";
        FileInputFormat.setInputPaths(job, inputpath);



        return job.waitForCompletion(true)?0:1 ;
    }


    public static void main(String[] args) throws Exception {
        ToolRunner.run(new CarIdDriver(),args);
    }


}
