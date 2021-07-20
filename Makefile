mvn-refresh:
	@ mvn clean install compile

run-on-docker: mvn-refresh
	@ docker build -t carangobom-api:latest .
	@ docker-compose up -d
	@ docker-compose up