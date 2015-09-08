# AUTOGRINDER
AUTOGRINDER is an adaptation of the GRINDER fault injection (FI) framework for AUTOSAR.
It requires a set of third party tools for its compilation and for the execution of experiments.
This README details the installation of a MySQL-compatible database and the installation of the Java development environment (based on the Eclipse IDE).

## Installing a MySQL-Compatible Database
AUTOGRINDER retrieves and stores information about test case configurations, campaign compositions, experiment runs, and available target systems in a MySQL-compatible database.
We chose MariaDB  as database system, due to the wide support from the open source community, but other MySQL-compatible database implementations may be used as well.
At the time of writing, version 10.0.21 Stable was the current one, and is available from https://downloads.mariadb.org/

After the installation process, a user to be used by AUTOGRINDER shall be created that can create tables and modify table content.
AUTOGRINDER uses the credentials autogrinder / autogrinder by default, which can be changed by modifying the file GRINDER/server/ext/database/src/main/resources/META-INF/persistence.xml.
We also suggest enabling UTF8, though this setting is not critical.
All other settings are kept at their default values.

HeidiSQL is a free and open source client for MySQL databases and shipped with MariaDB.
We use HeidiSQL to configure test cases and campaigns and review experiment logs by hand.
Before the first use of AUTOGRINDER, a new database called "grinder" has to be created.
To do so, create a new connection with IP 127.0.0.1, port 3306, and the credentials you chose above, and save it.
Afterwards, connect to the database server and create the grinder database.
The database structure (i.e., tables) will automatically be created at a later stage (during the compilation) by the Hibernate framework. 

## Installing the Java Development Environment
As AUTOGRINDER is mostly written in Java, the Java SE Development Kit (JDK) is required for development and compilation.
At the time of writing, version 8u60 was the current one, and is available from http://www.oracle.com/technetwork/java/javase/downloads/

No special installation options are necessary.
As IDE, we use Eclipse IDE for Java Developers, which includes support for Maven (a Java build automation tool), and which is available from http://www.eclipse.org/downloads/

At the time of writing, Mars was the current version.
Eclipse requires no installation and can be unzipped to any destination directory (e.g., c:\eclipse).
After the installation, the directory of the JDK has to be set as JRE home in Eclipse, via Window->Preferences->Java->Installed JRE (e.g., C:\Program Files\Java\jdk1.8.0_60).

Import the AUTOGRINDER repository contents to your Eclipse workspace (e.g. c:\eclipse\workspace\AUTOGRINDER).
To conduct the import into Eclipse, select File->Import->Maven->Existing Maven Projects and choose the AUTOGRINDER directory as root directory.

AUTOGRINDER can now be built by right-clicking on the top-level pom.xml file in the project and choosing Run As->Maven install.
Doing so for the first time will also setup the build environment, configure the database, and download necessary packages.
After a successful build (verify via the Console log), the executable JAR file is located in GRINDER\client\core\target\grinder.client.core-0.0.1-SNAPSHOT.jar.

## Running AUTOGRINDER
To execute AUTOGRINDER, open a Command Prompt (shell) in Windows, navigate to the directory AUTOGRINDER\client\core\target in your workspace, and start AUTOGRINDER via java -jar grinder.client.core-0.0.1-SNAPSHOT.jar.
Although AUTOGRINDER can also be executed from within Eclipse, we recommend to launch it from the command prompt, as log and debug output is elsewise invisible.

### Adding an AUTOSAR target
AUTOGRINDER is capable of distinguishing several targets to connect to.
In order to configure an example AUTOSAR target, it has to be added via the File->New target menu item within AUTOGRINDER.
Navigate to the folder AUTOGRINDER\target_configs, and load the AUTOSAR file.
AUTOGRINDER is now ready for use with AUTOSAR.

### Adding campaigns
After connecting to a target, AUTOGRINDER lets you select the campaign to execute on that target.
A campaign represents a group of single test cases and the configuration of test cases and their mapping to campaigns is done in the database.
The table "campaigns" in the "grinder" database contains all campaign IDs.
To add a new campaign, add a new entry to the "campaigns" table with a unique ID.

### Adding test cases
Test cases are added in the table "testcases".
A test case configuration consists of a concatenation of error model configurations, which are delimited by the character '|'.
An error model configuration consists of the error model identifier (i.e., name) and the respective configuration of that model.
An example of a test case configuration would be "Constant=2|Hang=40:20000|Logger=1", which configures the "Constant" error model with a value of 2, the "Hang" error model with a value of 40:20000 and the "Logger" error model with a value of 1.

### Mapping test cases to campaigns
The mapping of test cases to campaigns is configured in the table "campaigns_testcases".
For each "campaigns_id" a respective "testCases_id" can be mapped to.

### Experiment results
The results of each test case are stored in the table "experiment_run".
In the row "log" the actual log is stored, while the row "time" contains a timestamp of the log event and the row "testCase_id" contains the ID of the test case that generated the log data.
