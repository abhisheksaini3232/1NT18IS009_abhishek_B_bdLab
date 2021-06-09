package asr.pack.example;

import java.io.IOException;
import java.util.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

public class Salary {
	//MAPPER CODE	
	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
		private Text word = new Text("Total awards whose salary is 30000");
		public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
			String myvalue = value.toString();
			String[] data = myvalue.split(",");
			if(Integer.parseInt(data[2])==30000) {
				output.collect(word, new IntWritable(Integer.parseInt(data[3])));
			}
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
		JobConf conf = new JobConf(Salary.class);
		conf.setJobName("Total awards obtained by the employee's whose salary is 30000");
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

