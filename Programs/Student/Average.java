package asr.pack.example;

import java.io.IOException;
import java.util.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
	
public class Average {

//MAPPER CODE	
	   
public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
	String myvalue = value.toString();
	String[] tokens = myvalue.split(",");
	
	int averageMarks = (Integer.parseInt(tokens[2]) + Integer.parseInt(tokens[3]) + Integer.parseInt(tokens[4])) / 3;
	
	output.collect(new Text(tokens[1]), new IntWritable(averageMarks));
	
}
}

//REDUCER CODE	
public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException { //{little: {1,1}} 
	int average = 0 ; 
	while(values.hasNext()) {
		average += values.next().get();
	}
	output.collect(key, new IntWritable(average));
}
}
	
//DRIVER CODE
public static void main(String[] args) throws Exception {
	JobConf conf = new JobConf(Average.class);
	conf.setJobName("average marks");
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
