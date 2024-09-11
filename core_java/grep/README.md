# Introduction
The goal of this project is to create a Java application that reads through files in a specified directory and prints out 
lines of text matching a given regular expression (Regex) pattern. The application utilizes Maven for dependency management 
and packaging, and my development was done using the IntelliJ IDEA IDE. The application consists of several Java classes 
designed to traverse directories, read files, and log lines of text that match the specified Regex pattern. The code 
employs modern Java features such as lambda expressions, streams, and ArrayLists to efficiently process the data. 
Finally, the project is containerized using Docker to enable easy distribution and deployment.

# Quick Start
How to use your apps?

# Implemenation
## process Pseudocode
```
matchedLines = []
for file in listFilesRecursively(rootDir)
  for line in readLines(file)
      if containsPattern(line)
        matchedLines.add(line)
writeToFile(matchedLines)
```

## Performance Issue
(30-60 words)
A Java heap space error may occur if we run out of memory. To address this we need to specify the required size of the
JVM heap space. To do this we use the -Xms (initial heap size) and -Xmx (maximum heap size) JVM options, or alternatively
we could optimize the code to reduce memory usage.

# Test
To ensure that the application was functioning correctly, I performed the following manual tests:

### Sample Data Preparation:

Gathering sample data with various text patterns to serve as test inputs.
### Manual Test Cases:

Ran the application with different Regex patterns to verify that it correctly identifies and logs matching lines.
Tested edge cases, such as empty files, files with no matching patterns, and directories with no files.
### Validation:

Used the LoggerFactory logger to output the results and manually verified that the correct lines were being logged.
Employed the IDE debugger to step through the code and ensure that the logic was functioning as expected.

# Deployment
To allow for easier distribution of the application I created a docker image to hold the Java execution environment with
all the dependencies, files, and necessary classes to run the application. Here are the steps:

### Dockerfile Creation
Created a dockerfile where I specified the base image, openjdk:8-alpine, and copies the application JAR file and sets an
entry point for the application

### Build the Docker Image
Used docker build command to build the docker image from the Dockerfile.

### Run the Container
Deployed the application by running a Docker container from the created image using the docker run command, ensuring 
consistent execution across different environments.

# Improvement
List three things you can improve in this project.
One thing I can improve in this project is the 