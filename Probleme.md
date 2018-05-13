# Probleme mit dem Projekt

## Diese Probleme versuchen wir momentan zu lösen:
1. Vec2.getMagnitude scheint bei kleinen Zahlen falsche (zu große) Ergebnisse
zurückzugeben (Wahrscheinlich wegen der Quadratwurzel).

## Diese Probleme wären in einer richtigen Umsetzung nicht vorhanden:

1. Handys können aufgrund der Android API nur ungefähr ein mal pro Sekunde
ihre Position abfragen. Dies hat zur Folge, dass unser Vorhersage Programm in
einigen Situationen nicht genug Daten hat, um eine korrekte Abschätzung zu
geben. Zusätzlich ist die ständige Kommunikation mit dem Server sehr intensiv,
was sich negativ auf die Batterie des Handys ausübt.
Die Lösung hierzu ist ein eigenes Gerät zu bauen, welches die Positionen
abfragt.

2. Alle Daten werden an einen zentralen Server gesendet. Das ist sehr
gefährlich für die Sicherheit und Privatsphäre der Nutzer, da die Besitzer
dieses Servers theoretisch die Positionen von allen Nutzern abfragen könnten.
Zwar werden nicht Namen sondern IMEI-Adressen übermittelt, aber dennoch könnten
diese Personen zugeordnet werden. Die Lösung hierzu ist, ein verteiltes
Netzwerk zu bauen, wobei Seretra nicht an einen Server sendet, sondern nur an
die umliegenden Server Module, welches in Autos eingebaut werden. Aufgrund von
den benötigten Funk Lizenzen war dies in unserem Projekt nicht möglich.


## Diese Probleme wären auch in einer richtigen Umsetzung vorhanden:

1. Man kann die Höhe von Personen aufgrund von GPS-Limitierungen nicht messen,
was zur Folge hat, dass zwei Personen die unter und über einer Brücke stehen
als kollidierend erkannt werden.

2. Auch wenn das System dezentralisiert funktioniert, ist sind die Berechnungen
sehr arbeitsintensiv.
