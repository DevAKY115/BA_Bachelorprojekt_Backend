# Bachelorprojekt-Backend

Dieses Projekt ist das Spring Backend zu dem Frontend.

## XR-Tools
### Erstellung
Die eingegebenen Werte für das Tool werden validiert, und sollte ein Fehler aufkommen wird eine Fehlermeldung übertragen.
Alles außer den Dateien wird an den Service übergeben, und von diesem in die Datenbank eingetragen.
Die Dateinamen werden ebenfalls in die Datenbank eingefügt, damit diese wieder gefunden werden können.
Die Dateien werden an den Fileserver übertragen.

### Edit
Funktioniert nahezu identisch zu der Erstellung.
Nur die Validierung des Titels fällt weg, da es bereits vorhanden ist.

### Suche
Es wird die Textsuche und die Advanced Search, wie sie im Frontend beschrieben wurde, implementiert.

## Security
Es wird Spring Security verwendet. Die Konfiguration ist in WebSecurityConfig.java.
Es wird Basic Authentication verwendet. 
Bestimmte APIs werden mit Nutzerrollen gesichert.
CORS wird konfiguriert, und ein CSRF-Token generiert.
Für das Hashen der Passwörter wird BCrypt genutzt.

### Users
Es wird eine User-Klasse, annotiert mit @Entity, imlpementiert, um diese in der Datenbank zu speichern mittels Spring Data JPA.
Es wird eine MyUserDetails Klasse nach dem Interface UserDetails implementiert, damit Spring Security damit arbeiten kann.

### Rollen
Es gibt eine Nutzer-Rolle, die jeder Nutzer bei der Registrierung erhält, und eine Admin-Rolle die nur manuell gegeben werden kann.

## Exceptions
Es wurde ein Global-Exception-Handler erstellt, damit eigene Exceptions erstellt werden können, für spezifische und bekannte Fehler.

Diese haben die Kategorien, SearchError, UserException und XRToolError.

