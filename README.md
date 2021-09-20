# Progetto-E21: Casa d'Aste :hourglass_flowing_sand: :computer:  :man_judge:

**Applicativo per acquistare o effettuare un'asta online**

![image](https://drive.google.com/uc?id=1R9SAA4RzZff9ROwLHzviA4g83Yxr5Gne)

------------

Istruzioni per l'uso corretto del sistema:

## 0) Setup Repository

### 0.1) Dowload di git

Scaricare e installare [git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git) seguendo attentamente i passaggi indicati

### 0.2) Clone del Repository

Scegliere una cartella dove inserire il file del progetto. Una volta scelta la directory, avviare il terminale all'interno di essa
e copiare il comando seguente
```
git clone https://github.com/IngSW-unipv/Progetto-E21.git
```

------------

### 0.3) Download di Gradle (facoltativo)

L'applicazione può essere avviata anche da gradle. Nel caso non si volesse installare gradle andare al passo successivo `1)`.
Per una corretta installazione si consiglia di seguire i passaggi sul sito di [gradle](https://gradle.org/install/) facendo attenzione alla giusta scelta del vostro sistema operativo
Nel caso aveste optato per l'installazione di gradle andate al passo `1.2.1)`

## 1) Setup Progetto

### 1.1.1) Importazione del progetto

Nel caso non aveste intenzione di avviare l'applicativo da gradle potete importare il progetto in un IDE di vostra scelta

### 1.1.2) Avvio dell'applicativo

Eseguire la classe [main.java](https://github.com/IngSW-unipv/Progetto-E21/blob/main/src/main/java/server/Main.java) per poter avviare il sito

------------

### 1.2.1) Avvio e compilazione tramite gradle
Recarsi nella root del progetto e aprire all'interno di esso il terminale di vostra scelta.
All'interno del terminale digitare: 
```
gradle clean build
```
### 1.2.2) Esecuzione applicativo
In base al terminale di vostra scelta dovrete digitare

Windows : 
```
gradlew run
```
Linux:
```
./gradlew run
```

### 2) Accesso al sito web

Mediante il vostro browser, dirigetevi sulla vostra barra di ricerca e inserite il seguente link

[http://localhost:8080](http://localhost:8080)


## 3) Esecuzione dell'applicativo dei moderatori
Per accedere alla pagina web dei moderatori è neccessario avviare dal vostro IDE la classe
[mainModerator.java](https://github.com/IngSW-unipv/Progetto-E21/blob/main/src/main/java/serverModerator/MainModerator.java)

Entrare successivamente al seguente link:

[http://localhost:8081](http://localhost:8081)

