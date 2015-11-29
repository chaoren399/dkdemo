import org.json.JSONArray;

import java.util.*;

import org.json.JSONObject;

/**
 * Created by zzy on 15/11/19.
 */
public class TestApi {


    public static void main(String[] args) {

        Bean bean = new Bean();
        List<Bean> beanList = new ArrayList<Bean>();
        beanList.add(bean);
        beanList.add(bean);
        Map<Object, Bean> beanMap = new HashMap<Object, Bean>();
        beanMap.put("bean1", bean);
        List<Object> objList = new ArrayList<Object>();
        objList.add(bean.getId());
        objList.add(bean.getName());
        objList.add(bean.getDate());
        Map<Object, Object> objMap = new HashMap<Object, Object>();
        objMap.put("id", bean.getId());
        objMap.put("name", bean.getName());
        objMap.put("date", bean.getDate());


        // object map
        System.err.println("object map:");
        String objMapJson = JsonUtil.toJson(objMap);
        System.out.println(objMapJson);
        Map<Object, Object> objMapObject = JsonUtil.toObject(objMapJson, HashMap.class, Map.class, Object.class, Object.class);
        System.out.println(objMapObject);


}





static class Bean{

    // 序列化时忽略此属性
//    @JsonIgnore
    private int id = 1;
    // 修改序列化后key
//    @JsonProperty("str")
    private String name = "name";
    // 日期格式化为字符串
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date date = new Date();

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    @Override
    public String toString() {
        return super.toString() +": id="+ getId() +", name="+ getName() +", date="+ getDate();
    }

}
}
