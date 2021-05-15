package asr.pack.example;

import java.io.IOException;
import java.util.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
	
public class Score {

//MAPPER CODE	
	   
public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
	String myvalue = value.toString();
	String[] tokens = myvalue.split(","); 
	output.collect(new Text(tokens[1]),new IntWritable(Integer.parseInt(tokens[2])));
	}
}

//REDUCER CODE	
public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException { //{little: {1,1}} 
	int limit =60;
	int val;
	while(values.hasNext())
	{
		if((val=values.next().get())>limit) {
		output.collect(key, new IntWritable(val));
	
	}
	}	
}
	
//DRIVER CODE
public static void main(String[] args) throws Exception {
	JobConf conf = new JobConf(Score.class);
	conf.setJobName("Students who scored > 60");
	conf.setOutputKeyClass(Text.class);
	conf.setOutputValueClass(IntWritable.class);
	conf.setMapperClass(Map.class);
	conf.setCombinerClass(Reduce.class);
	conf.setReducerClass(Reduce.class);
	conf.setInputFormat(TextInputFormat.class);
	conf.setOutputFormat(TextOutputFormat.class); // hadoop jar jarname classpath inputfolder outputfolder
	FileInputFormat.setInputPaths(conf, new Path(args[0]));
	FileOutputFormat.setOutputPath(conf, new Path(args[1]));
	JobClient.runJob(conf);   
}
}
}

