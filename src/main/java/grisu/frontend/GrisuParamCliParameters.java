package grisu.frontend;

import grisu.frontend.view.cli.GrisuCliParameters;

import com.beust.jcommander.Parameter;

public class ExampleCliParameters extends GrisuCliParameters {

	@Parameter(names = {"-n", "--n"}, description = "the wall time for every job")
	private String jobName;
	
	public String getJobName(){
		return jobName;
	}
	
	@Parameter(names = {"-w", "--w"}, description = "the wall time for every job")
	private int wallTime;
	
	public int getWallTime(){
		return wallTime;
	}
	
	@Parameter(names = { "-c", "--command" }, description = "the command to execute for every parameter")
	private String command;
	
	public String getCommand() {
		return command;
	}
	
	@Parameter(names = { "-f", "--file" }, description = "the path to a file")
	private String file;

	public String getFile() {
		return file;
	}

}
