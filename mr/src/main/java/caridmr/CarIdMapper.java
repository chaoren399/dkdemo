package caridmr;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Objects;

/**
 * Created by zzy on 15/12/2.
 */
public class CarIdMapper extends Mapper<Object,Text,Text,Text> {
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        String line = value.toString();
        String[] splited = line.split(",");
        if (splited.length > 10) {

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
    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}
