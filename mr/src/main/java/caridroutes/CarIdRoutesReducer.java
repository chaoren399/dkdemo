package caridroutes;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by zzy on 15/11/24.
 *
 * 1、根据HDFS中的数据编写MR程序，将HDFS中数据按需求清洗到HBase中，

 建议方案：

 行键:车牌号

 列：每天的日期

 列内容：时间->地点:时间->地点....

 然后用聚类算法归类指定车牌号车辆的规律路径。

 (1.先将每天的行车路线清洗出来2.聚类)

 create 'carroutes','date'
 */
public class CarIdRoutesReducer extends TableReducer<Text,Text,NullWritable>{

    private byte[] family = "date".getBytes();
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
//        super.reduce(key, values, context);
        Put put =  new Put(key.toString().getBytes());
        for (Text line: values){
            String [] splited = line.toString().split(",");
//            put.add(family,"1000000".getBytes(),line.getBytes());
//            put.add(family,"10000001".getBytes(),"dsfaasdf".getBytes());

            put.add(family,"dccollectiondate".getBytes(),splited[3].getBytes());
            String time_station = splited[3]+":"+splited[5];
            put.add(family,"dccollectiondate_ccollectionaddress".getBytes(),time_station.getBytes());
            put.add(family, "ccollectionaddress".getBytes(), splited[5].getBytes());
//      put.add(family,"cdevicecode".getBytes(),splited[7].getBytes());
//      put.add(family,"rowkey".getBytes(),key.toString().getBytes());


            context.write(NullWritable.get(), put);
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}
