# Seretra

## Der Code von unserem Mint-Projekt

Von der Schule aus nehmen wir an einem MINT-Wettbewerb teil.
Ziel des MINT-Wettbewerbes ist es, junge Menschen für den Bereich
MINT (Mathematik - Informatik - Naturwissenschaften - Technik) zu
begeistern. Jede teilnehmende Gruppe bekommt ein Unternehmen als
Pate für technische und finazielle Unterstützung. Unser Pate ist
das Unternehmen Grunewald GmbH & Co. KG in Bocholt.

## Was müssen wir bei diesem Wettbewerb machen?
Wir sollen uns ein Projekt zum Thema "Safety-First" überlegen und
das Projekt mit Hilfe von Lehrern und Unternehmen in die Tat umsetzen.

## Was für ein Projekt machen wir?
Wir haben uns dazu entschieden ein Frühwarnsystem für Autofahrer zu 
entwickeln, dass die Autofahrer rechtzeitig auf Fußgänger aufmerksam
macht. Die Fußgänger installieren sich eine App auf ihrem Handy, und 
wenn sich ein Auto mit einem Seretra-Modul nähert wird es auf den 
Fußgänger aufmerksam gemacht. Das ganze funktioniert auch mit anderen
Autos und je nachdem, wie schwerwiegend die Gefahr ist, wird sie anders
dargestellt.

## Installation
Die Seretra App kommuniziert mit dem Server ueber eine Website. Sie brauchen
einen Webserver, der PHP unterstützt (Apache/nginx).
```
# git clone https://github.com/nivram710/Seretra <Seretra>
# cd <Webserver>
# ln -s <Seretra>/PHP-Scripts/upload.php upload.php
# ln -s <Seretra> Seretra
# mkdir .data
# mkdir .danger
```
\<Seretra\> = wo sie Seretra installieren wollen

\<Webserver\> = wo ihr Webserver nach Seiten sucht (/var/www/...).

## Sicherheitsmaßnahmen
### Apache
Wir empfehlen die Ordner .data und .danger mit einer .htaccess Datei zu schützen. Wir nutzen dazu folgenden Code:
```
order deny,allow
deny from all
allow from 127.0.0.1
```
Allerdings kann es sein, dass die Konfigurationsdatei angepasst werden muss, um für Unterverzeichnisse 
neue Zugriffsberechtigungen festzulegen.

### Nginx
Hier muss keine .htaccess Datei erstellt werden, da Nginx den Zugriff auf Ordner mit der standard Konfiguration verbietet.

# Euer Seretra-Team 
Edgar, Marvin K., Olaf, Ben, Timo, Marvin J.
