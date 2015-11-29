package caridroutes;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by zzy on 15/11/24.
 */
public class CarIdRoutesMapper extends Mapper<Object,Text,Text,Text> {
    Text key2 = new Text();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);

        String line = value.toString();
        String [] splited = line .split(",");
//        String rowKey = splited[3];
//        key2.set("asdfa");
//
//        context.write(key2, value);
        if (splited.length == 21) {
            String rowKey = splited[2];
            key2.set(rowKey);
            context.write(key2, value);

        }

    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}
