import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * 将对象转换成json字符串
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public static String toJson(Object object) throws Exception {
		return objectMapper.writeValueAsString(object);
	}
	
	/**
	 * 将json字符串转换成对象
	 * @param json
	 * @param valueType
	 * @return
	 * @throws Exception
	 */
	public static <T> T toObject(String json, Class<T> valueType) throws Exception {
		return objectMapper.readValue(json, valueType);
	}
	
	/**
	 * json转对象(处理复杂类型对象) 
	 * List<bean> : json, ArrayList.class, List.class, Bean.class
	 * Map<bean1, bean2> : json, HashMap.class, Map.class, Bean1.class, Bean2.class
	 * @param json
	 * @param parametrized
	 * @param parametersFor
	 * @param parameterClasses
	 * @return
	 * @throws Exception
	 */
	public static <T> T toObject(String json, Class<?> parametrized, Class<?> parametersFor, Class<?>... parameterClasses) throws JsonParseException, JsonMappingException, IOException {
		return objectMapper.readValue(json, objectMapper.getTypeFactory().constructParametrizedType(parametrized, parametersFor, parameterClasses));
	}
	
	/**
	 * json转对象(处理复杂类型对象) 
	 * json, new TypeReference<T>(){}
	 * @param json
	 * @param valueTypeRef 
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> T toObject(String json, TypeReference<T> valueTypeRef) throws JsonParseException, JsonMappingException, IOException {
		return objectMapper.readValue(json, valueTypeRef);
	}

}
