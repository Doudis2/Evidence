import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


// Třída reprezentující pojištěného

class InsuredPerson {
    private String firstName;
    private String lastName;
    private int age;
    private String phone;

    public InsuredPerson(String firstName, String lastName, int age, String phone) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("Jméno nesmí být prázdné!");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Příjmení nesmí být prázdné!");
        }
        if (age <= 0 || age > 120) {
            throw new IllegalArgumentException("Neplatný věk! Musí být mezi 1 a 120.");
        }
        if (phone == null || !phone.matches("\\+?\\d+")) {
            throw new IllegalArgumentException("Neplatné telefonní číslo! Pouze čísla a případně '+' na začátku.");
        }

        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.age = age;
        this.phone = phone.trim();
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }

    @Override
    public String toString() {
        return firstName + " " + lastName + ", věk: " + age + ", telefon: " + phone;
    }
}


// Třída pro správu seznamu pojištěných

class InsuranceRegistry {
    private List<InsuredPerson> insuredPeople = new ArrayList<>();

    public void addPerson(InsuredPerson person) {
        insuredPeople.add(person);
    }

    public List<InsuredPerson> getAllPeople() {
        return insuredPeople;
    }

    public List<InsuredPerson> findByName(String firstName, String lastName) {
        List<InsuredPerson> result = new ArrayList<>();
        for (InsuredPerson person : insuredPeople) {
            if (person.getFirstName().equalsIgnoreCase(firstName.trim()) &&
                    person.getLastName().equalsIgnoreCase(lastName.trim())) {
                result.add(person);
            }
        }
        return result;
    }
}

// Třída pro konzolové menu

class ConsoleUI {
    private InsuranceRegistry registry;
    private Scanner scanner;

    public ConsoleUI(InsuranceRegistry registry) {
        this.registry = registry;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        int choice;
        do {
            printMenu();
            choice = readInt("Vyberte akci: ");
            switch (choice) {
                case 1 -> createPerson();
                case 2 -> listAll();
                case 3 -> findPerson();
                case 4 -> System.out.println("Konec programu.");
                default -> System.out.println("Neplatná volba!");
            }
        } while (choice != 4);
    }

    private void printMenu() {
        System.out.println("\n--- Evidence pojištěných ---");
        System.out.println("1. Přidat nového pojištěného");
        System.out.println("2. Vypsat všechny pojištěné");
        System.out.println("3. Vyhledat pojištěného");
        System.out.println("4. Konec");
    }

    private void createPerson() {
        System.out.print("Zadejte jméno: ");
        String firstName = scanner.nextLine();

        System.out.print("Zadejte příjmení: ");
        String lastName = scanner.nextLine();

        int age = readInt("Zadejte věk: ");

        System.out.print("Zadejte telefonní číslo: ");
        String phone = scanner.nextLine();

        try {
            InsuredPerson person = new InsuredPerson(firstName, lastName, age, phone);
            registry.addPerson(person);
            System.out.println("Pojištěný byl úspěšně přidán.");
        } catch (IllegalArgumentException e) {
            System.out.println("Chyba: " + e.getMessage());
        }
    }

    private void listAll() {
        List<InsuredPerson> people = registry.getAllPeople();
        if (people.isEmpty()) {
            System.out.println("Žádní pojištění nejsou evidováni.");
        } else {
            System.out.println("\n--- Seznam pojištěných ---");
            for (InsuredPerson person : people) {
                System.out.println(person);
            }
        }
    }

    private void findPerson() {
        System.out.print("Zadejte jméno: ");
        String firstName = scanner.nextLine();

        System.out.print("Zadejte příjmení: ");
        String lastName = scanner.nextLine();

        List<InsuredPerson> results = registry.findByName(firstName, lastName);
        if (results.isEmpty()) {
            System.out.println("Pojištěný nebyl nalezen.");
        } else {
            for (InsuredPerson person : results) {
                System.out.println(person);
            }
        }
    }

    private int readInt(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Neplatné číslo, zkuste znovu.");
            }
        }
    }
}


// Spouštěcí třída

public class Main {
    public static void main(String[] args) {
        InsuranceRegistry registry = new InsuranceRegistry();
        ConsoleUI ui = new ConsoleUI(registry);
        ui.start();
    }
}
