# EV Navigator
Dieses Projekt wurde im Rahmen der Abschlussprüfung des Java-Backend Pathways in der Fakultät 73 entwickelt.
Es handelt sich um eine JavaFX-Applikation, die ein modernes Infotainmentsystem nachbildet und Routenplanungsfunktionen
mit Fokus auf Ladestationen für E-Fahrzeuge bietet.


## Anforderungen

Für die korrekte Ausführung der Applikation wird eine installierte JRE 8 inklusive JavaFX benötigt. Dies ist
beispielsweise bei Versionen von Oracle gegeben. 
Für die Entwicklung wurde Java™ SE Development Kit 8, Update 241 (JDK 8u241) von Oracle verwendet.

## Start der Applikation

Um die Applikation zu starten, gibt es folgende Möglichkeiten:

### JAR-Datei
Unter dem Verzeichnis `/dist` ist eine ausführbare `jar`-Datei abgelegt. Diese kann mittels `java -jar dateiname.jar`
gestartet werden. Hierbei sei darauf zu achten, dass in der Kommandozeile eine geeignete JRE/JDK 8 geladen ist. Dies kann 
mit dem Befehl `java -version` geprüft werden.

```bash
git clone https://devstack.vwgroup.com/bitbucket/scm/fak73exam/f73_g2_abschlusspruefung_jahr_2_gruppe_04.git
cd f73_g2_abschlusspruefung_jahr_2_gruppe_04
cd dist
java -jar evnavigator-1.0.0.jar
```

### Maven Exec
Alternativ kann folgender Befehl auf der Wurzelebene des Projektordners ausgeführt werden:
```bash
mvn clean compile exec:java
```

Ist Maven nicht auf dem System installiert, kann der mitgelieferte Wrapper verwendet werden:

```bash
.\mvnw clean compile exec:java
```

### Manuell

Die Applikation kann auch als Maven-Projekt in einer IDE wie IntelliJ IDEA oder Eclipse importiert werden.
Hier kann die Applikation manuell, nach Installation der Maven-Abhängigkeiten über die Klasse 
`de.volkswagen.f73.evnavigator.EvNavigatorApplication` mit ihrer `main`-Methode ausgeführt werden.

Die Applikation generiert bei ihrem ersten Start eine H2-Datenbank, welche auf der Wurzelebene des Projekts bzw. 
im Ordner der gestarteten `jar`-Datei erstellt wird.


## Tests

Um die Tests der Applikation auszuführen, kann Maven verwendet werden:

```bash
mvn test
```

Ist Maven nicht auf dem System installiert, kann der mitgelieferte Wrapper verwendet werden:

```bash
.\mvnw test
```