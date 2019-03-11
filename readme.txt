***************************************************************************************************************************************************
This project contains 6 java files in Barrier/src. You only need to modify the file in semaphoreBarrier.java and monitorBarrier.java. The methods you need to implement have been added to these files. 
Feel free to add any files or methods of your own. However, do no modify the Test.java, vectorNormalize.java, Barrier.java or spinBarrier.java files. The main function is inside Test.java.

The Barrier.java file contains the interface for the Barrier class implemented by monitorBarrier, semaphoreBarrier and spinBarrier.

spinBarrier.java contains a sample code for Barrier that does spinning instead of using monitors or semaphores. Feel free to refer to it, but do not modify it. The correctness of your code will be evaluated by using the spinBarrier.

Remember to remove the line that says "throw new java.lang.UnsupportedOperationException("Semaphore not supported yet.")" in semaphoreBarrier.java and similarly for monitorBarrier.java.

To compile, go to src folder and run the command "javac -d ../bin *.java". This will make sure all your class files are generated in the bin folder.

To execute, go to bin folder and run the command "java Test".

During evaluation, the Test.java file would be slightly modified to check special cases. Ensure that there are no premature exits from the barrier or any deadlock situations. Feel free to write any tests of your own to ensure your functionality.

Submit all your source files(files in src with extension .java)(!!DO NOT SUBMIT .class FILES!!) in a zip with a readme(in the zip) on how to compile and execute your code and any need-to-knows.
***************************************************************************************************************************************************
