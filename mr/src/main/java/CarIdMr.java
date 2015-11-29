import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.ToolRunner;
import util.PropUtils;
//import java.util.Objects;

/**
 * Created by zzy on 15/11/23.
 *
 * 1、根据HDFS中的数据编写MR程序，将HDFS中数据按需求清洗到HBase中，

 建议方案：

 行键:车牌号

 列：每天的日期

 列内容：时间->地点:时间->地点....

 然后用聚类算法归类指定车牌号车辆的规律路径。

 (1.先将每天的行车路线清洗出来2.聚类)

 create 'carroutes','f'
  得到聚类所要的数据

 create 'traffic_route','f'

 */

public class CarIdMr {



 static class BatchImportMapper extends Mapper<Object,Text,Text,Text> {


  @Override
  protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {


   String line = value.toString();
   String [] splited = line .split(",");
   if (splited.length>10) {

    String dateStr = splited[3];//2014-01-01 22:50:46
    String place = splited[5];
    String carId = splited[2];
//    String  lu = new String("鲁".getBytes(),"unicode");
//    if (carId.contains("鲁")) {
//     carId = carId.replace("鲁", "lu");
//    }



    String str = dateStr + "--" + place;


    context.write(new Text(carId + ":" + dateStr), new Text(str));

   }
  }
 }

 static  class  CarImportReducer extends  TableReducer<Text,Text,ImmutableBytesWritable>{
  public CarImportReducer(){}
//  @Override
  protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

   byte[] row ;
   byte[] family;
   byte[] qualifier;
   byte[] realValue;
   String carIdDate = key.toString();
   StringTokenizer st = new StringTokenizer(carIdDate,":");
   String carId = st.nextToken();
   String date = st.nextToken();
   String  allplace ="";
   for (Text val : values) {
    if(allplace == ""){
     allplace = val.toString();
    }else{
     allplace = allplace+":"+val.toString();
    }
   }
   row = Bytes.toBytes(carId);
   Put put = new Put(row);
   family = Bytes.toBytes("f");
   qualifier = Bytes.toBytes(date);
   realValue = Bytes.toBytes(allplace);
   put.add(family, qualifier, realValue);
   context.write(new ImmutableBytesWritable(row), put);
  }
   }





 private static final String tableName = "traffic_route";
 public   static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
  if(args.length != 2){
   System.out.println("Usage: <inputpath>");
   ToolRunner.printGenericCommandUsage(System.err);
   return ;
  }
  Properties prop = PropUtils.getProp("hbase.properties");//
  Configuration conf = new Configuration();
  conf.set("hbase.zookeeper.quorum", prop.getProperty("hbase.zookeeper.quorum1"));
  conf.set("hbase.rootdir", prop.getProperty("hbase.rootdir"));
  conf.set(TableOutputFormat.OUTPUT_TABLE, prop.getProperty("hbase.table.traffic_route"));

  Job job = Job.getInstance(conf,CarIdMr.class.getSimpleName());

  TableMapReduceUtil.addDependencyJars(job);
  job.setJarByClass(CarIdMr.class);
  job.setMapperClass(BatchImportMapper.class);
  job.setReducerClass(CarImportReducer.class);

  job.setMapOutputKeyClass(Text.class);
  job.setMapOutputValueClass(Text.class);

  job.setNumReduceTasks(Integer.parseInt(args[1]));
//  job.setInputFormatClass(TextInputFormat.class);
  job.setOutputFormatClass(TableOutputFormat.class);

//  FileInputFormat.setInputPaths(job, "hdfs://192.168.122.211:9000/user/hbase/dk/10.txt");
  FileInputFormat.setInputPaths(job, args[0]);

  job.waitForCompletion(true);
//        FileInputFormat.setInputPaths(job,"");

 }
}
