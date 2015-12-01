package com.dk.hdfs;

import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.ipc.Client;

import java.io.IOException;
import java.net.URI;

/**
 * Created by zzy on 15/11/30.
 */
public class HdfsDAO {
    private  static final String HDFS = "hdfs://192.168.122.211:9000/";
    private String hdfsPath;
    private Configuration conf;


    public static void main(String[] args) throws IOException {
        JobConf conf = config();
        HdfsDAO hdfs = new HdfsDAO(conf);
        hdfs.ls("/");
        String localFile = "pom.xml";
        String inPath = HDFS+"/user/hdfs/mix_data/";
//        hdfs.copyFile(localFile, inPath);
        hdfs.copyFile("/Users/zzy/IdeaProjects/java_workspace/dkdemo/mahout-kmeans/datafile/randomData.csv", "/user/hbase/dk");

    }
    public  HdfsDAO( Configuration conf){
        this(HDFS, conf);
    }
    public HdfsDAO(String hdfs,Configuration conf){
        this.conf = conf;
        this.hdfsPath = hdfs;

    }
    public  static JobConf config(){
        JobConf conf = new JobConf(HdfsDAO.class);
        conf.setJobName("HdfsDAO");
//        conf.addResource("core-site.xml");
//        conf.addResource("hdfs-site.xml");
//        conf.addResource("mapred-site.xml");
        return conf;
    }

    public void mkdirs(String folder) throws IOException {
        Path path = new Path(folder);
//        FileSystem fs = FileSystem.get(URI.create(hdfsPath),conf);
        FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
        if (!fs.exists(path)){
            fs.mkdirs(path);
            System.out.println("Create:"+folder);
        }

        fs.close();
    }
    public void ls(String folder) throws IOException {
        Path path = new Path(folder);
        FileSystem fs = FileSystem.get(URI.create(hdfsPath),conf);
        FileStatus[] list = fs.listStatus(path);
        System.out.println("ls:" + folder);
        System.out.printf("==============================");
        for (FileStatus f :list){
            System.out.printf("name : %s,folder:%s ,size :%d \n",f.getPath(), fs.isFile(f.getPath()),f.getLen());
        }
        System.out.printf("==============================");
        fs.close();

    }

    public void createFile (String file ,String content) throws IOException {
        FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
        byte[] buff = content.getBytes();
        FSDataOutputStream os = null;
        try {
            os = fs.create(new Path(file));
            os.write(buff, 0, buff.length);
            System.out.println("Create: " + file);
        } finally {
            if (os != null)
                os.close();
        }
        fs.close();
    }

    public void copyFile (String local,String remote) throws IOException {
        FileSystem fs = FileSystem.get(URI.create(hdfsPath),conf);
        fs.copyFromLocalFile(new Path(local),new Path(remote));
        System.out.println("copy from: " + local + " to " + remote);
        fs.close();
    }

    public void download (String remote,String local) throws IOException {
        Path path = new Path(remote);
        FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
        fs.copyToLocalFile(path, new Path(local));
        System.out.println("download: from" + remote + " to " + local);
        fs.close();
    }

    public void cat (String remoteFile ) throws IOException {
        Path path = new Path(remoteFile);
        FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
        FSDataInputStream fsdis = null;
        System.out.println("cat: " + remoteFile);
        try {
            fsdis =fs.open(path);
            IOUtils.copyBytes(fsdis, System.out, 4096, false);
        } finally {
            IOUtils.closeStream(fsdis);
            fs.close();
        }
    }

    public void rmr(String folder) throws IOException {
        Path path = new Path(folder);
        FileSystem fs = FileSystem.get(URI.create(hdfsPath),conf);
        fs.deleteOnExit(path);
        System.out.printf("Delete:"+folder);
        fs.close();

    }
}
