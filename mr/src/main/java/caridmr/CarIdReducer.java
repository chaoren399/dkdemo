package caridmr;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by zzy on 15/12/2.
 */
public class CarIdReducer extends TableReducer<Text,Text,ImmutableBytesWritable>{

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }



    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
//        super.reduce(key, values, context);

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

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}
