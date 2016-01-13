package com.dk.elasticsearch;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

/**
 * Created by zzy on 15/12/7.
 */
public class Es {
    public static void main(String[] args) {
        Es es = new Es();
        es.test1();
    }
    public void test1(){

        TransportClient client = new TransportClient().addTransportAddress
                (new InetSocketTransportAddress("hadoop", 9300));

        String index = "crxy";
        String type = "emp";
        GetResponse response = client.prepareGet(index,type,"1").execute().actionGet();
        String sourceAsString = response.getSourceAsString();
        System.out.println(sourceAsString);

    }
}
