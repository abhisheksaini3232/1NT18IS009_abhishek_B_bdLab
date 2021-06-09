package asr.pack.example;

import java.io.IOException;
import java.util.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

public class ComulativeAwards {
	//MAPPER CODE	
	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {	
		private Text word = new Text("Comulative Awards");
		public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
			String myvalue = value.toString();
			String[] data = myvalue.split(",");
			output.collect(word, new IntWritable(Integer.parseInt(data[3])));
		}
	}

	//REDUCER CODE	
	public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
		public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {  
			int count=0;
			while(values.hasNext())
			{
				count+=values.next().get();

			}	
			output.collect(key,new IntWritable(count));

		}
	}

	//DRIVER CODE
	public static void main(String[] args) throws Exception {
		JobConf conf = new JobConf(ComulativeAwards.class);
		conf.setJobName("Total number of cummulative awards the company had this year");
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

