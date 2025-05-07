# Aufgabenverwaltung (Task List App)

Eine Konsolenanwendung zur Verwaltung von Aufgaben (To-Do-Liste), geschrieben in Java.

## Funktionen

- ✨ Aufgaben erstellen mit Titel, Beschreibung und Fälligkeitsdatum
- 🎯 Prioritäten festlegen (Hoch, Mittel, Niedrig)
- ✅ Aufgaben als erledigt markieren
- 🗑️ Aufgaben löschen
- 🔍 Aufgaben durchsuchen
- 💾 Automatisches Speichern der Aufgaben
- 🎨 Farbige Anzeige der Prioritäten
- 📅 Sortierung nach Priorität und Datum

## Installation

1. Stellen Sie sicher, dass Java auf Ihrem System installiert ist
2. Klonen Sie das Repository:
   ```bash
   git clone https://github.com/niklasmarderx/Task-List-App.git
   ```
3. Wechseln Sie in das Projektverzeichnis:
   ```bash
   cd Task-List-App
   ```
4. Kompilieren Sie die Anwendung:
   ```bash
   javac TaskListApp.java
   ```
5. Starten Sie die Anwendung:
   ```bash
   java TaskListApp
   ```

## Verwendung

Die Anwendung bietet ein einfaches Konsolenmenü mit folgenden Optionen:

1. Neue Aufgabe hinzufügen
2. Aufgaben anzeigen
3. Aufgabe als erledigt markieren
4. Aufgabe löschen
5. Aufgaben suchen
6. Beenden

## Datenspeicherung

Alle Aufgaben werden automatisch in einer Datei `aufgaben.dat` gespeichert und beim Programmstart wieder geladen.

