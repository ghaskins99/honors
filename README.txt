SRC: contains all code and tests.

src/main/java/honours/heaps/merge contains all heap implementations.
src/main/java/org/jheaps contains the base code for most heaps
src/main/java/edu/princeton/cs/algs4 contains the base code for the Binomial Queue.

src/test/java/honours/heaps/merge contains two directories, correct and perf.

"correct" contains correctness tests for each heap implemented.
"perf" at the top level contains examples of JMH tests, which were ultimately not used
perf/manual contains the performance tests used throughout the report.

All tests are using JUnit 4.13.2

----------------------

Results.txt files are at the top level corresponding to each test as noted in the report.
pom.xml contains information for the Java build tool "maven" if needed, including test configuration.