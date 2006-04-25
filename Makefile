PREFIX=javac -cp classes -d classes sources/
all: client

client: enums vente utilisateur objet sessionclient message
	${PREFIX}Client.java

enums:
	${PREFIX}Enums.java

message:
	${PREFIX}Message.java

sessionclient:
	${PREFIX}SessionClient.java

objet:
	${PREFIX}Objet.java

vente:
	${PREFIX}Vente.java

utilisateur: participant
	${PREFIX}Utilisateur.java

participant:
	${PREFIX}Participant.java

clean:
	rm -rf ./classes/*
