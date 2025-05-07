import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Comparator;
import java.io.*;

class Task implements Serializable {
    private String titel;
    private String beschreibung;
    private boolean istErledigt;
    private LocalDate faelligkeitsDatum;
    private Prioritaet prioritaet;

    public enum Prioritaet {
        HOCH("\u001B[31mHoch\u001B[0m"),      // Rot
        MITTEL("\u001B[33mMittel\u001B[0m"),  // Gelb
        NIEDRIG("\u001B[32mNiedrig\u001B[0m"); // Grün

        private final String farbText;

        Prioritaet(String farbText) {
            this.farbText = farbText;
        }

        public String getFarbText() {
            return farbText;
        }
    }

    public Task(String titel, String beschreibung, LocalDate faelligkeitsDatum, Prioritaet prioritaet) {
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.istErledigt = false;
        this.faelligkeitsDatum = faelligkeitsDatum;
        this.prioritaet = prioritaet;
    }

    public String getTitel() { return titel; }
    public String getBeschreibung() { return beschreibung; }
    public boolean istErledigt() { return istErledigt; }
    public LocalDate getFaelligkeitsDatum() { return faelligkeitsDatum; }
    public Prioritaet getPrioritaet() { return prioritaet; }

    public void alsErledigtMarkieren() {
        istErledigt = true;
    }
}

public class TaskListApp {
    private static ArrayList<Task> aufgaben = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static final String DATEI_NAME = "aufgaben.dat";
    private static final DateTimeFormatter DATUM_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static void main(String[] args) {
        aufgabenLaden();
        
        while (true) {
            menueAnzeigen();
            int auswahl = scanner.nextInt();
            scanner.nextLine();  // Zeilenumbruch verarbeiten

            switch (auswahl) {
                case 1: aufgabeHinzufuegen(); break;
                case 2: aufgabenAnzeigen(); break;
                case 3: aufgabeAlsErledigtMarkieren(); break;
                case 4: aufgabeLoeschen(); break;
                case 5: aufgabenSuchen(); break;
                case 6: aufgabenSpeichern(); System.out.println("Auf Wiedersehen!"); System.exit(0);
                default: System.out.println("Ungültige Auswahl. Bitte versuchen Sie es erneut.");
            }
        }
    }

    private static void menueAnzeigen() {
        System.out.println("\n=== Aufgabenverwaltung ===");
        System.out.println("1. Neue Aufgabe hinzufügen");
        System.out.println("2. Aufgaben anzeigen");
        System.out.println("3. Aufgabe als erledigt markieren");
        System.out.println("4. Aufgabe löschen");
        System.out.println("5. Aufgaben suchen");
        System.out.println("6. Beenden");
        System.out.print("Ihre Auswahl: ");
    }

    private static void aufgabeHinzufuegen() {
        System.out.print("Titel der Aufgabe: ");
        String titel = scanner.nextLine();
        
        System.out.print("Beschreibung: ");
        String beschreibung = scanner.nextLine();
        
        LocalDate faelligkeitsDatum = null;
        while (faelligkeitsDatum == null) {
            System.out.print("Fälligkeitsdatum (TT.MM.JJJJ): ");
            String datumEingabe = scanner.nextLine();
            try {
                faelligkeitsDatum = LocalDate.parse(datumEingabe, DATUM_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("Ungültiges Datumsformat. Bitte verwenden Sie TT.MM.JJJJ");
            }
        }

        System.out.println("Priorität:");
        System.out.println("1. Hoch");
        System.out.println("2. Mittel");
        System.out.println("3. Niedrig");
        System.out.print("Ihre Auswahl (1-3): ");
        
        Task.Prioritaet prioritaet;
        int prioritaetWahl = scanner.nextInt();
        scanner.nextLine();
        
        switch (prioritaetWahl) {
            case 1: prioritaet = Task.Prioritaet.HOCH; break;
            case 2: prioritaet = Task.Prioritaet.MITTEL; break;
            default: prioritaet = Task.Prioritaet.NIEDRIG;
        }

        Task aufgabe = new Task(titel, beschreibung, faelligkeitsDatum, prioritaet);
        aufgaben.add(aufgabe);
        aufgabenSpeichern();
        System.out.println("Aufgabe erfolgreich hinzugefügt!");
    }

    private static void aufgabenAnzeigen() {
        if (aufgaben.isEmpty()) {
            System.out.println("\nKeine Aufgaben vorhanden.");
            return;
        }

        // Sortieren nach Priorität und Datum
        Collections.sort(aufgaben, Comparator
            .comparing(Task::getPrioritaet)
            .thenComparing(Task::getFaelligkeitsDatum));

        System.out.println("\nAufgabenliste:");
        for (int i = 0; i < aufgaben.size(); i++) {
            Task aufgabe = aufgaben.get(i);
            String status = aufgabe.istErledigt() ? "✓" : "□";
            System.out.printf("%d. %s [%s] %s - %s (Fällig: %s) - %s%n",
                i + 1,
                status,
                aufgabe.getPrioritaet().getFarbText(),
                aufgabe.getTitel(),
                aufgabe.getBeschreibung(),
                aufgabe.getFaelligkeitsDatum().format(DATUM_FORMAT),
                aufgabe.istErledigt() ? "Erledigt" : "Offen"
            );
        }
    }

    private static void aufgabeAlsErledigtMarkieren() {
        System.out.print("Nummer der erledigten Aufgabe: ");
        int nummer = scanner.nextInt();
        scanner.nextLine();

        if (nummer >= 1 && nummer <= aufgaben.size()) {
            Task aufgabe = aufgaben.get(nummer - 1);
            aufgabe.alsErledigtMarkieren();
            aufgabenSpeichern();
            System.out.println("Aufgabe als erledigt markiert!");
        } else {
            System.out.println("Ungültige Aufgabennummer.");
        }
    }

    private static void aufgabeLoeschen() {
        System.out.print("Nummer der zu löschenden Aufgabe: ");
        int nummer = scanner.nextInt();
        scanner.nextLine();

        if (nummer >= 1 && nummer <= aufgaben.size()) {
            Task aufgabe = aufgaben.remove(nummer - 1);
            aufgabenSpeichern();
            System.out.println("Aufgabe gelöscht: " + aufgabe.getTitel());
        } else {
            System.out.println("Ungültige Aufgabennummer.");
        }
    }

    private static void aufgabenSuchen() {
        System.out.print("Suchbegriff eingeben: ");
        String suchbegriff = scanner.nextLine().toLowerCase();

        System.out.println("\nSuchergebnisse:");
        boolean gefunden = false;
        
        for (int i = 0; i < aufgaben.size(); i++) {
            Task aufgabe = aufgaben.get(i);
            if (aufgabe.getTitel().toLowerCase().contains(suchbegriff) ||
                aufgabe.getBeschreibung().toLowerCase().contains(suchbegriff)) {
                if (!gefunden) {
                    gefunden = true;
                }
                String status = aufgabe.istErledigt() ? "✓" : "□";
                System.out.printf("%d. %s [%s] %s - %s (Fällig: %s)%n",
                    i + 1,
                    status,
                    aufgabe.getPrioritaet().getFarbText(),
                    aufgabe.getTitel(),
                    aufgabe.getBeschreibung(),
                    aufgabe.getFaelligkeitsDatum().format(DATUM_FORMAT)
                );
            }
        }
        
        if (!gefunden) {
            System.out.println("Keine Aufgaben gefunden.");
        }
    }

    private static void aufgabenSpeichern() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATEI_NAME))) {
            oos.writeObject(aufgaben);
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern der Aufgaben: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void aufgabenLaden() {
        File datei = new File(DATEI_NAME);
        if (datei.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATEI_NAME))) {
                aufgaben = (ArrayList<Task>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Fehler beim Laden der Aufgaben: " + e.getMessage());
                aufgaben = new ArrayList<>();
            }
        }
    }
}
