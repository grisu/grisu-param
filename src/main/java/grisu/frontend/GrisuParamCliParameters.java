package grisu.frontend;

import grisu.frontend.view.cli.GrisuCliParameters;

import com.beust.jcommander.Parameter;

public class GrisuParamCliParameters extends GrisuCliParameters {

	@Parameter(names = {"-mpi", "--mpi"}, description = "sets the job type to be mpi")
	private boolean mpi;
	
	public Boolean getMpi(){
		return mpi;
	}

	@Parameter(names = {"-single", "--single"}, description = "sets the job type to be single")
	private boolean single;
	
	public Boolean getSingle(){
		return single;
	}
	
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
