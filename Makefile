all: client

client: sessionclient enums
	javac -cp classes -d classes sources/Client.java

enums:
	javac -cp classes -d classes sources/Enums.java

sessionclient:
	javac -cp classes -d classes sources/SessionClient.java

clean:
	rm -rf ./classes/*
