run:
	mvn clean install

test:
	mvn clean test

sonar:
	mvn -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar

stest: test sonar
