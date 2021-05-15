package asr.pack.example;

import java.io.IOException;
import java.util.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
	
public class TransactionAmount {

//MAPPER CODE	
	   
public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
//private final static IntWritable one = new IntWritable(1);

public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
	String myvalue = value.toString();
	String[] TransactionTokens = myvalue.split(",");
	output.collect(new Text(TransactionTokens[2]),new IntWritable(Integer.parseInt(TransactionTokens[3])));
	}
}

//REDUCER CODE	
public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException { //{little: {1,1}} 
	int transactionAmountCount = 0;
	while(values.hasNext()) {
		transactionAmountCount += values.next().get();
	}
	output.collect(key,new IntWritable(transactionAmountCount));
	}
}
	
//DRIVER CODE
public static void main(String[] args) throws Exception {
	JobConf conf = new JobConf(TransactionAmount.class);
	conf.setJobName("Counting the total amount");
	conf.setOutputKeyClass(Text.class);
	conf.setOutputValueClass(IntWritable.class);
	conf.setMapperClass(Map.class);
	conf.setCombinerClass(Reduce.class);
	conf.setReducerClass(Reduce.class);
	conf.setInputFormat(TextInputFormat.class);
	conf.setOutputFormat(TextOutputFormat.class);
	FileInputFormat.setInputPaths(conf, new Path(args[0]));
	FileOutputFormat.setOutputPath(conf, new Path(args[1]));
	JobClient.runJob(conf);   
}
}