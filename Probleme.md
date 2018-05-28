# Probleme mit dem Projekt

## Diese Probleme versuchen wir momentan zu lösen:
1. Vec2.getMagnitude scheint bei kleinen Zahlen falsche (zu große) Ergebnisse
zurückzugeben (Wahrscheinlich wegen der Quadratwurzel).

## Diese Probleme wären in einer richtigen Umsetzung nicht vorhanden:

1. Alle Daten werden an einen zentralen Server gesendet. Das ist sehr
gefährlich für die Sicherheit und Privatsphäre der Nutzer, da die Besitzer
dieses Servers theoretisch die Positionen von allen Nutzern abfragen könnten.
Zwar werden nicht Namen sondern IMEI-Adressen übermittelt, aber dennoch könnten
diese Personen zugeordnet werden. Die Lösung hierzu ist, ein verteiltes
Netzwerk zu bauen, wobei Seretra nicht an einen Server sendet, sondern nur an
die umliegenden Server Module, welches in Autos eingebaut werden. Aufgrund von
den benötigten Funk Lizenzen war dies in unserem Projekt nicht möglich.

## Diese Probleme wären auch in einer richtigen Umsetzung vorhanden:

1. Da die GPS-Sensoren der Handys nur auf ca. 5m genau sind und es vorkommen kann,
dass das Handy eine Positionsänderung erkennt, obwohl sich das Handy
an der exakt gleichen Stelle befindet, kann unser Vorhersage Programm nicht
korrekt arbeiten. Deswegen haben wir uns dazu entschieden eine Verzögerung 
bei der Positionsermittlung einzubauen, um sowohl die Sprünge, als auch
eine ungewollte DOS-Attacke auf den Server zu verhindern.

2. Das ständige Abfragen und Hochladen der Position des Handys ist sehr
arbeitsintensiv und wirkt sich somit negativ auf die Akku-Laufzeit aus.
Darum haben wir eine Pause-Funktion eingebaut, damit der Handy-Akku 
geschont wird, wenn man sich nicht im Straßenverkehr befindet. Ein weiterer
Ansatz wäre die Entwicklung eines eigenen Gerätes, das speziell für diese Zwecke
konzipiert worden ist.

3. Auch wenn das System dezentralisiert funktioniert, ist sind die Berechnungen
in z.B. Großstädten sehr arbeitsintensiv.

4. Ein Großteil der Verkehrsteilnehmer muss Seretra benutzen, damit ein
deutlicher Effekt erzielt werden kann.
