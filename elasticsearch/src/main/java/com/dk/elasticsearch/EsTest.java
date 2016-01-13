package com.dk.elasticsearch;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.deletebyquery.DeleteByQueryResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.collect.ImmutableList;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Before;
import org.junit.Test;

//import com.fasterxml.jackson.databind.ObjectMapper;

public class EsTest {
	
	
	String index = "crxy";
	String type = "emp";
	TransportClient transportClient;
	/**
	 * 相当于初始化方法，在执行其他测试类之前会首先被调用
	 * @throws Exception
	 */
	@Before
	public void before() throws Exception {
		transportClient = new TransportClient();
		TransportAddress transportAddress = new InetSocketTransportAddress("123.56.231.34", 9300);
		transportClient.addTransportAddress(transportAddress);
	}
	
	
	/**
	 * 自己写测试类的时候可以用这个
	 * @throws Exception
	 */
	@Test
	public void test1() throws Exception {
		
		
		GetResponse response = transportClient.prepareGet(index , type , "1").execute().actionGet();
		String sourceAsString = response.getSourceAsString();
		System.out.println(sourceAsString);
	}
	
	
	/**
	 * 工作中使用-1
	 * @throws Exception
	 */
	@Test
	public void test2() throws Exception {
		Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "elasticsearch").build();
		TransportClient transportClient = new TransportClient(settings);
		TransportAddress transportAddress = new InetSocketTransportAddress("hadoop", 9300);
		TransportAddress transportAddress1 = new InetSocketTransportAddress("hadoop ", 9300);
		transportClient.addTransportAddresses(transportAddress,transportAddress1);
		GetResponse response = transportClient.prepareGet(index , type , "1").execute().actionGet();
		String sourceAsString = response.getSourceAsString();
		System.out.println(sourceAsString);
	}
	
	/**
	 * 工作中使用-2
	 * @throws Exception
	 */
	@Test
	public void test3() throws Exception {
		Settings settings = ImmutableSettings.settingsBuilder()
				.put("cluster.name", "elasticsearch")
				.put("client.transport.sniff", true)//自动嗅探机制，可以自动链接集群中的其他节点
				.build();
		TransportClient transportClient = new TransportClient(settings);
		TransportAddress transportAddress = new InetSocketTransportAddress("192.168.1.170", 9300);
		TransportAddress transportAddress1 = new InetSocketTransportAddress("192.168.1.171", 9300);
		transportClient.addTransportAddresses(transportAddress,transportAddress1);
		
		ImmutableList<DiscoveryNode> connectedNodes = transportClient.connectedNodes();
		for (DiscoveryNode discoveryNode : connectedNodes) {
			System.out.println(discoveryNode.getHostAddress());
		}
		
	}
	
	
	
	/**
	 * index -json格式
	 * @throws Exception
	 */
	@Test
	public void test4() throws Exception {
		String source = "{\"name\":\"mm\",\"age\":\"19\"}";
		IndexResponse response = transportClient.prepareIndex(index, type, "2").setSource(source).execute().actionGet();
		String id = response.getId();
		System.out.println(id);
	}
	
	/**
	 * index - map
	 * @throws Exception
	 */
	@Test
	public void test5() throws Exception {
		HashMap<String, Object> source = new HashMap<String, Object>();
		source.put("name", "sha");
		source.put("age", 20);
		
		IndexResponse response = transportClient.prepareIndex(index, type)
				.setSource(source).setRouting("test").execute().actionGet();
		String id = response.getId();
		System.out.println(id);
		
	}
	
	/**
	 * index - 对象
	 * @throws Exception
	 */
	@Test
	public void test6() throws Exception {
//		User user = new User();
//		user.setAge(29);
//		user.setName("nn");
//
//		ObjectMapper objectMapper = new ObjectMapper();
//		IndexResponse response = transportClient.prepareIndex(index, type, "4").setSource(objectMapper.writeValueAsString(user)).execute().actionGet();
//		String id = response.getId();
//		System.out.println(id);
		
	}
	
	
	/**
	 * index -eshelp
	 * @throws Exception
	 */
	@Test
	public void test7() throws Exception {
		XContentBuilder endObject = XContentFactory.jsonBuilder()
				.startObject().field("name", "lk").field("age", 28).endObject();
		IndexResponse response = transportClient.prepareIndex(index, type, "5")
				.setSource(endObject).setRouting("test").get();
		String id = response.getId();
		System.out.println(id);
	}
	
	/**
	 * get
	 * @throws Exception
	 */
	@Test
	public void test8() throws Exception {
		GetResponse response = transportClient.prepareGet(index, type, "5").get();
		System.out.println(response.getSourceAsString());
	}
	
	
	/**
	 * update
	 * @throws Exception
	 */
	@Test
	public void test9() throws Exception {
		XContentBuilder endObject = XContentFactory.jsonBuilder().startObject().field("name","zs").endObject();
		
		UpdateResponse response = transportClient.prepareUpdate(index, type, "5").setDoc(endObject).get();
		System.out.println(response.getVersion());
		
	}
	
	/**
	 * upsert
	 * @throws Exception
	 */
	@Test
	public void test10() throws Exception {
		UpdateRequest request = new UpdateRequest();
		request.index(index);
		request.type(type);
		request.id("6");
		
		XContentBuilder endObject = XContentFactory.jsonBuilder().startObject().field("name", "aa").endObject();
		request.doc(endObject);
		
		XContentBuilder endObject2 = XContentFactory.jsonBuilder().startObject().field("name", "crxy").field("age", 10).endObject();
		request.upsert(endObject2);
		
		
		UpdateResponse response = transportClient.update(request ).get();
		
		System.out.println(response.getVersion());
	}
	
	
	/**
	 * 删除
	 * @throws Exception
	 */
	@Test
	public void test11() throws Exception {
		DeleteResponse response = transportClient.prepareDelete(index, type, "6").get();
		System.out.println(response.getVersion());
	}
	
	/**
	 * 删除
	 * @throws Exception
	 */
	@Test
	public void test11_1() throws Exception {
		DeleteByQueryResponse response = transportClient.prepareDeleteByQuery(index).setQuery(QueryBuilders.matchAllQuery()).get();
	}
	
	
	
	/**
	 * 查询索引库中数据的总量,类似于sql中的select *
	 * @throws Exception
	 */
	@Test
	public void test12() throws Exception {
		CountResponse response = transportClient.prepareCount(index).get();
		System.out.println(response.getCount());
	}
	
	
	/**
	 * 批量操作 bulk 
	 * @throws Exception
	 */
	@Test
	public void test13() throws Exception {
		BulkRequestBuilder bulkBuilder = transportClient.prepareBulk();
		IndexRequest indexrequest = new IndexRequest(index,type,"6");
		XContentBuilder endObject = XContentFactory.jsonBuilder().startObject().field("name", "sss").field("age111", 001).endObject();
		indexrequest.source(endObject);
		//TODO---
		
		bulkBuilder.add(indexrequest );
		DeleteRequest deleteRequest = new DeleteRequest(index,type,"5");
		bulkBuilder.add(deleteRequest);
		
		BulkResponse bulkResponse = bulkBuilder.get();
		
		if(bulkResponse.hasFailures()){
			System.out.println("执行失败：");
			BulkItemResponse[] items = bulkResponse.getItems();
			for (BulkItemResponse bulkItemResponse : items) {
				String failureMessage = bulkItemResponse.getFailureMessage();
				System.out.println(failureMessage);
			}
		}else{
			System.out.println("正常执行");
		}
		
	}
	/**
	 * 查询，排序，分页，高亮，过滤
	 * lt:小于
	 * lte：小于等于
	 * gt：大于
	 * gte：大于等于
	 * @throws Exception
	 */
	@Test
	public void test14() throws Exception {
		SearchResponse searchResponse = transportClient.prepareSearch(index)
							.setTypes(type)
							.setSearchType(SearchType.QUERY_THEN_FETCH)
							.setQuery(QueryBuilders.matchQuery("name", "zs"))
							.setPreference("_local")
							//.setPostFilter(FilterBuilders.rangeFilter("age").gt(20).lte(28))
							.setTimeout("10")
							.setFrom(0)
							.setSize(10)
							.addHighlightedField("name")
							.setHighlighterPreTags("<font color='red'>")
							.setHighlighterPostTags("</font>")
							.addSort("age", SortOrder.DESC)
							.get();
		
		SearchHits hits = searchResponse.getHits();
		long totalHits = hits.getTotalHits();
		System.out.println("总数："+totalHits);
		
		for (SearchHit searchHit : hits) {
			Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
			HighlightField highlightField = highlightFields.get("name");
			Text[] fragments = highlightField.fragments();
			System.out.println(searchHit.getSourceAsString());
			for (Text text : fragments) {
				System.out.println("高亮内容"+text);
			}
		}
	}
	
	/**
	 * 类似于这个select count(*),name from table group by name;
	 * @throws Exception
	 */
	@Test
	public void test15() throws Exception {
		SearchResponse searchResponse = transportClient.prepareSearch(index)
		.setTypes(type)
		.addAggregation(AggregationBuilders.terms("nameterm").field("name").size(0)).get();
		
		Terms terms = searchResponse.getAggregations().get("nameterm");
		
		List<Bucket> buckets = terms.getBuckets();
		for (Bucket bucket : buckets) {
			System.out.println(bucket.getKey()+"--->"+bucket.getDocCount());
		}
	}
	
	/**
	 * 类似于select sum(age),name from table group by name;
	 * @throws Exception
	 */
	@Test
	public void test16() throws Exception {
		SearchResponse searchResponse = transportClient.prepareSearch(index)
		.setTypes(type)
		.addAggregation(AggregationBuilders.terms("nameterm").field("name").
					subAggregation(AggregationBuilders.sum("agesum").field("age")).size(0)).get();
		
		Terms terms = searchResponse.getAggregations().get("nameterm");
		
		List<Bucket> buckets = terms.getBuckets();
		for (Bucket bucket : buckets) {
			Sum sum = bucket.getAggregations().get("agesum");
			
			System.out.println(bucket.getKey()+"--->"+sum.getValue());
		}
	}
	
	/**
	 * 删除索引库
	 * @throws Exception
	 */
	@Test
	public void test17() throws Exception {
		DeleteIndexResponse deleteIndexResponse = transportClient.admin().indices().prepareDelete("crxy").get();
		
	}
	
	
	/**
	 * 创建索引库-执行settings信息
	 * @throws Exception
	 */
	@Test
	public void test18() throws Exception {
		
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("number_of_shards", 4);
		hashMap.put("number_of_replicas", 0);
		CreateIndexResponse createIndexResponse = transportClient.admin().indices().prepareCreate("crxy2").setSettings(hashMap).get();
	}
	
	/**
	 * 修改索引库的副本数
	 * @throws Exception
	 */
	@Test
	public void test19() throws Exception {
		
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("number_of_replicas", 2);
		UpdateSettingsResponse updateSettingsResponse = transportClient.admin().indices().prepareUpdateSettings("crxy2").setSettings(hashMap).get();
		
	}
	@Test
	public void test20() throws Exception {
		SearchResponse searchResponse = transportClient.prepareSearch(index)
							.setTypes(type)
							.setPreference("_only_nodes:C-Y0POFGRwmSn53vN_Gs7Q,q96ZFY44QW6uZ_KVDIafDw")
							.get();
		
		SearchHits hits = searchResponse.getHits();
		long totalHits = hits.getTotalHits();
		System.out.println("总数："+totalHits);
		
		for (SearchHit searchHit : hits) {
			System.out.println(searchHit.getSourceAsString());
		}
	}
	
	
	
	

}
