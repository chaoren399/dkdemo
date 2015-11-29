import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;

public class CarMapreduce {
//	public static Configuration conf =null;
	private static final String tableName = "kmeans";
//	static {
//
//		conf.set("hbase.zookeeper.quorum","192.168.122.213:2181");
//		conf.set("hbase.rootdir","hdfs://192.168.122.211:9000/hbase");
//		conf.set(TableOutputFormat.OUTPUT_TABLE, tableName);
//	}

    public static class ImportHbaseMapper extends Mapper<Object, Text, Text, Text>{
		   @Override
		   public void map(Object key, Text value, Context context) throws IOException, InterruptedException {			   			   
			   StringTokenizer st = new StringTokenizer(value.toString());
			   String dateStr = st.nextToken();
			   String place = st.nextToken();
			   String carId = st.nextToken();
			   String dateStr2 = null;
			   String str =dateStr+"------>"+place;
			   
			   SimpleDateFormat ymdhms = new SimpleDateFormat("yyyyMMdd");
			   try {
				Date date = ymdhms.parse(dateStr);
				dateStr2 = ymdhms.format(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			   context.write(new Text(carId+":"+dateStr2), new Text(str));			   
		   }
		   
		   
}
    
    public static class ReducerClass extends TableReducer<Text,Text,ImmutableBytesWritable>{
    	public ReducerClass(){}
		
		
        public void reduce(Text key,Iterable<Text> values,Context context) throws IOException, InterruptedException{
			 
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
    
    
	public static void main(String[] args) throws Exception {		
//		String path = args[0];
//		String tableName = "car";
//			System.out.println("start---new---job---tablName---"+tableName+"-----");
//			Job job = new Job(conf,"from Hadoop to hbase");
//		    job.setJarByClass(CarMapreduce.class);
//		    job.setMapperClass(ImportHbaseMapper.class);
//		    job.setReducerClass(ReducerClass.class);
//		    job.setOutputFormatClass(TableOutputFormat.class);
//		    job.getConfiguration().set(TableOutputFormat.OUTPUT_TABLE, tableName);
//		    job.setMapOutputKeyClass(Text.class);
//		    job.setMapOutputValueClass(Text.class);
//		    job.setOutputKeyClass(ImmutableBytesWritable.class);
//		    job.setOutputValueClass(Put.class);
//		    job.setPartitionerClass(HashPartitioner.class);
//			FileInputFormat.addInputPath(job, new Path(path));
//			job.setNumReduceTasks(2);
//		    job.waitForCompletion(true);


		Configuration conf = new Configuration();
		conf.set("hbase.zookeeper.quorum","192.168.122.213:2181");
		conf.set("hbase.rootdir","hdfs://192.168.122.211:9000/hbase");
		conf.set(TableOutputFormat.OUTPUT_TABLE, tableName);

		Job job = Job.getInstance(conf,CarIdMr.class.getSimpleName());

		TableMapReduceUtil.addDependencyJars(job);
		job.setJarByClass(CarIdMr.class);
		job.setMapperClass(ImportHbaseMapper.class);
		job.setReducerClass(ReducerClass.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

//  job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TableOutputFormat.class);

		FileInputFormat.setInputPaths(job, "hdfs://192.168.122.211:9000/user/hbase/dk/20151125.txt");
		job.waitForCompletion(true);

	}

}
