package grisu.frontend;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import grisu.control.ServiceInterface;
import grisu.control.exceptions.JobPropertiesException;
import grisu.control.exceptions.JobSubmissionException;
import grisu.frontend.control.login.LoginManager;
import grisu.frontend.model.job.JobObject;
import grisu.frontend.view.cli.GrisuCliClient;
import grisu.jcommons.constants.Constants;
import grisu.model.FileManager;

public class Client extends GrisuCliClient<GrisuParamCliParameters> {

	public static void main(String[] args) {

		// basic housekeeping
		LoginManager.initGrisuClient("grisu-param");

		// helps to parse commandline arguments, if you don't want to create
		// your own parameter class, just use DefaultCliParameters
		GrisuParamCliParameters params = new GrisuParamCliParameters();
		// create the client
		Client client = null;
		try {
			client = new Client(params, args);
		} catch(Exception e) {
			System.err.println("Could not start grisu-param: "
					+ e.getLocalizedMessage());
			System.exit(1);
		}

		// finally:
		// execute the "run" method below
		client.run();

		// exit properly
		System.exit(0);

	}

	public Client(GrisuParamCliParameters params, String[] args) throws Exception {
		super(params, args);
	}
	
//modified
	@Override
	public void run() {

		String file = getCliParameters().getFile();	//file containing the filenames for each job (one per line)
		String command = getCliParameters().getCommand();	
		int wallTime = getCliParameters().getWallTime();
		String jobName = getCliParameters().getJobName();
		Boolean mpi = getCliParameters().getMpi();
		Boolean single = getCliParameters().getSingle();
		
		if(jobName==null)
			jobName="cat_job";
		if(wallTime==0)
			wallTime=60;
		
		if(mpi && single)
		{
			System.err.println("Cannot set job type to both mpi and single");
			System.exit(1);
		}
			

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String filename;
			int jobCount=0;
			/*
			 * loop through all the filenames specified in the input file and create a job for each file using the command given
			 */
			while((filename=br.readLine())!=null){
				
				// all login stuff is implemented in the parent class
				System.out.println("Getting serviceinterface...");
				ServiceInterface si = null;
				try {
					si = getServiceInterface();
				} catch (Exception e) {
					System.err.println("Could not login: " + e.getLocalizedMessage());
					System.exit(1);
				}

				System.out.println("Creating job...");
				JobObject job = new JobObject(si);
				
				
				System.out.println("File to use for the job: " + filename);
				
				job.setApplication(Constants.GENERIC_APPLICATION_NAME);
				job.setCommandline(command+" " + filename);
				System.out.println(command+" " + filename);
				job.addInputFileUrl(filename);
				job.setWalltimeInSeconds(wallTime);
				job.setForce_mpi(mpi);
				job.setForce_single(single);
				
				System.out.println("jobtype: mpi-"+job.isForce_mpi()+" single-"+job.isForce_single());

				job.setTimestampJobname(jobName+"_"+jobCount);

				System.out.println("Set jobname to be: " + job.getJobname());

				try {
					System.out.println("Creating job on backend...");
					job.createJob("/nz/nesi");
				} catch (JobPropertiesException e) {
					System.err.println("Could not create job: "
							+ e.getLocalizedMessage());
					System.exit(1);
				}

				try {
					System.out.println("Submitting job to the grid...");
					job.submitJob();
				} catch (JobSubmissionException e) {
					System.err.println("Could not submit job: "
							+ e.getLocalizedMessage());
					System.exit(1);
				} catch (InterruptedException e) {
					System.err.println("Jobsubmission interrupted: "
							+ e.getLocalizedMessage());
					System.exit(1);
				}

				System.out.println("Job submission finished.");
				System.out.println("Job submitted to: "
						+ job.getJobProperty(Constants.SUBMISSION_SITE_KEY));

				System.out.println("Waiting for job to finish...");

				// for a realy workflow, don't check every 5 seconds since that would
				// put too much load on the backend/gateways
				//job.waitForJobToFinish(5);

//				System.out.println("Job finished with status: "
//						+ job.getStatusString(false));

				//System.out.println("Stdout: " + job.getStdOutContent());
				//System.out.println("Stderr: " + job.getStdErrContent());
				
				jobCount++;
			}
			br.close();
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
//		System.out.println("File to use for the job: " + file);
//
//		// all login stuff is implemented in the parent class
//		System.out.println("Getting serviceinterface...");
//		ServiceInterface si = null;
//		try {
//			si = getServiceInterface();
//		} catch (Exception e) {
//			System.err.println("Could not login: " + e.getLocalizedMessage());
//			System.exit(1);
//		}
//
//		System.out.println("Creating job...");
//		JobObject job = new JobObject(si);
//		String filename = FileManager.getFilename(file);
//		job.setApplication(Constants.GENERIC_APPLICATION_NAME);
//		job.setCommandline("cat " + filename);
//		job.addInputFileUrl(file);
//		job.setWalltimeInSeconds(60);
//
//		job.setTimestampJobname("cat_job");
//
//		System.out.println("Set jobname to be: " + job.getJobname());
//
//		try {
//			System.out.println("Creating job on backend...");
//			job.createJob("/nz/nesi");
//		} catch (JobPropertiesException e) {
//			System.err.println("Could not create job: "
//					+ e.getLocalizedMessage());
//			System.exit(1);
//		}
//
//		try {
//			System.out.println("Submitting job to the grid...");
//			job.submitJob();
//		} catch (JobSubmissionException e) {
//			System.err.println("Could not submit job: "
//					+ e.getLocalizedMessage());
//			System.exit(1);
//		} catch (InterruptedException e) {
//			System.err.println("Jobsubmission interrupted: "
//					+ e.getLocalizedMessage());
//			System.exit(1);
//		}
//
//		System.out.println("Job submission finished.");
//		System.out.println("Job submitted to: "
//				+ job.getJobProperty(Constants.SUBMISSION_SITE_KEY));
//
//		System.out.println("Waiting for job to finish...");
//
//		// for a realy workflow, don't check every 5 seconds since that would
//		// put too much load on the backend/gateways
//		job.waitForJobToFinish(5);
//
//		System.out.println("Job finished with status: "
//				+ job.getStatusString(false));
//
//		System.out.println("Stdout: " + job.getStdOutContent());
//		System.out.println("Stderr: " + job.getStdErrContent());

	}

}
