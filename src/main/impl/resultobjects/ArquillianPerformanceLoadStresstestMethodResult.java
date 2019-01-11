package src.main.impl.resultobjects;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;

public class ArquillianPerformanceLoadStresstestMethodResult {

	public String name;

	// public TestResult result;

	// Annotations ArquillianPerformanceLoadStresstestExtension
	public int usercount;
	public int iterationcount;
	public int timeDisplaced;

	/*
	 * // Results public Duration averageTimeOfAllUsers; // mean of all
	 * averageTimePerUser Durations public Duration[] averageTimePerUser; // mean of
	 * every Duration per User over all Iterations; Example: //
	 * averageTimePerUser[0] is the mean of the Durations of User // 0 over all
	 * Iterations; Therefore it is the average Time User 0 needed for // the task.
	 * public Duration averageTimeOfAllIterations;// mean of all
	 * averageTimePerIteration Durations public Duration[] averageTimePerIteration;
	 * // mean of every Iteration over all Users; Example: //
	 * averageTimePerIteration[0] is the mean of all User-Durations in // Iteration
	 * 0; Therefore it´s the average Time a User needed for the // task in Iteration
	 * 0
	 */
	public Duration[][] actualTime; // Array[Iteration][User]; Example: The Duration of the third User in the first
									// Iteration would be saved in actualTime[0][2]

	/*
	 * // the variance is small if the accuracy of the Tests is high; therefore a
	 * small // variance is to be desired public long varianceUserDuration;//
	 * squared deviation of the mean UserDuration to the actualTime of the Users; //
	 * deviation is the difference between the mean and the actualTime public long
	 * varianceIterationDuration; // squared deviation of the mean or average
	 * Duration of the iterations; // deviation is the difference between the mean
	 * and the actualTime
	 */
	public ArquillianPerformanceLoadStresstestMethodResult(String name, int usercount, int iterationcount,
			int timedisplaced, Duration[][] actualTime) {
		this.name = name;
		this.usercount = usercount;
		this.iterationcount = iterationcount;
		this.timeDisplaced = timedisplaced;
		this.actualTime = actualTime;

		/*
		 * this.averageTimePerUser = getAverageTimePerUser(actualTime);
		 * this.averageTimePerIteration = getAverageTimePerIteration(actualTime);
		 * this.averageTimeOfAllUsers =
		 * getAverageTimeOfGivenArray(this.averageTimePerUser);
		 * this.averageTimeOfAllIterations =
		 * getAverageTimeOfGivenArray(this.averageTimePerIteration);
		 */

	}

	public ArquillianPerformanceLoadStresstestMethodResult(String name, int usercount, int iterationcount,
			int timedisplaced) {
		this.name = name;
		this.usercount = usercount;
		this.iterationcount = iterationcount;
		this.timeDisplaced = timedisplaced;
		this.actualTime = new Duration[iterationcount][usercount];

	}

	private Duration getAverageTimeOfGivenArray(Duration[] averageTimeArray) {
		Duration averageTime;
		Duration tmpDuration = null;
		int arrayLength = averageTimeArray.length;

		// add all Durations to the temporary tmpDuration
		for (int i = 0; i < arrayLength; i++) {
			tmpDuration = tmpDuration.plus(averageTimeArray[i]);
		}

		// divide the temporary Duration by the amount of Durations from the original
		// Array, to get the mean Duration
		averageTime = tmpDuration.dividedBy(arrayLength);

		return averageTime;
	}

	private Duration[] getAverageTimePerIteration() {

		Duration tmpDurationForUsers = null;

		Duration[] averageTimePerIteration = new Duration[this.iterationcount];

		for (int i = 0; i < this.iterationcount; i++) {
			// add all durations from the users in iteration i
			for (int j = 0; j < this.usercount; j++) {
				tmpDurationForUsers = tmpDurationForUsers.plus(this.actualTime[i][j]);
			}

			// set average duration for iteration i by dividing the sum by the amount of
			// users
			averageTimePerIteration[i] = tmpDurationForUsers.dividedBy(this.usercount);

			// reset temporary duration
			tmpDurationForUsers = null;
		}

		return averageTimePerIteration;
	}

	private Duration[] getAverageTimePerUser() {

		Duration tmpDuration = null;
		Duration[] averageDurationPerUser = new Duration[this.usercount];

		// for each User
		for (int j = 0; j < this.usercount; j++) {
			// get all Durations from each Iteration and add them to the temporary Duration
			for (int i = 0; i < this.iterationcount; i++) {
				tmpDuration = tmpDuration.plus(this.actualTime[i][j]);
			}

			// set the average duration for the user j by dividing the sum by the amount of
			// iterations
			averageDurationPerUser[j] = tmpDuration.dividedBy(this.iterationcount);
			tmpDuration = null;

		}
		return averageDurationPerUser;
	}

	public Duration getAverageDurationOfIterations() {
		return getAverageTimeOfGivenArray(getAverageTimePerIteration());
	}

	public Duration getAverageDurationOfUsers() {
		return getAverageTimeOfGivenArray(getAverageTimePerUser());
	}

	public Duration[] getAverageDurationArrayForIterations() {
		return getAverageTimePerIteration();
	}

	public Duration[] getAverageDurationArrayForUsers() {
		return getAverageTimePerUser();
	}

	public void saveData(String directory) throws IOException {
		// create a File with a name that consists of the annotated values as well as
		// the localdate
		FileWriter writer = new FileWriter(directory + this.name + "usercount" + this.usercount + "iterationcount"
				+ this.iterationcount + "timedisplaced" + this.timeDisplaced + LocalDate.now() + ".xml");

		// create a String that will include all data of this.acutalTime
		String tmp = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<root>\n";

		/*
		 * add all information of this.actualTime result: <?xml version="1.0"
		 * encoding="UTF-8" standalone="yes"?> <root> <iteration id="0"> <user
		 * id="0">this.actualTime[0][0]</user> <user id="1">this.actualTime[0][1]</user>
		 * . . </iteration> <iteration id="1"> <user id="0">this.actualTime[1][0]</user>
		 * <user id="1">this.actualTime[1][1]</user> . . . </iteration> </root>
		 *
		 */
		for (int i = 0; i < this.iterationcount; i++) {
			tmp = tmp + "\t<iteration id=\"" + i + "\">\n";
			for (int j = 0; j < this.usercount; j++) {
				tmp = tmp + "\t\t<user id=\"" + j + "\">" + this.actualTime[i][j].toMillis() + "</user>\n";
			}
			tmp = tmp + "\t</iteration>\n";
		}
		tmp = tmp + "</root>";

		writer.write(tmp);
		writer.flush();
		writer.close();

	}

	public void setDuration(int iterationNumber, int userNumber, Duration time) {
		this.actualTime[iterationNumber][userNumber] = time;
	}

	/*
	 * time should be the duration in milliseconds
	 */
	public void setMillisTimeLong(int iterationNumber, int userNumber, long time) {
		Duration tmp = null;

		this.actualTime[iterationNumber][userNumber] = tmp.plusMillis(time);
	}
	// public long getVarianceOfIterationsDurationsOfOneSpecificUser(Integer
	// UserNumber)
	// public long getVarianceOfAverageUserDurations
	// public long geVarianceOfUserDurationsInOneIteration(Integer IterationNumber)
	// public long getVarianceOfAverageIterationDurations

}
