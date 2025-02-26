import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Login {
    public static void main(String[] args) {
    // Obtenez le répertoire de travail actuel
        String currentDir = System.getProperty("user.dir");
        String filePath;

        // Déterminez le chemin du fichier en fonction du répertoire actuel
        if (currentDir.equals("/home/onyxia/work")) {
            filePath = "ENSAI-2A-Java-TP/tp1/data/user_hashpwd.csv";
        } else {
            filePath = "../data/user_hashpwd.csv";
        }

        HashMap<String, String> userDatabase = loadUserDatabase(filePath);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            if (userDatabase.containsKey(username)) {
                int attempts = 3;
                while (attempts > 0) {
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    String hashedPassword = Password.hashPassword(password);

                    if (userDatabase.get(username).equals(hashedPassword)) {
                        System.out.println("Login successful!");
                        scanner.close();
                        return;
                    } else {
                        attempts--;
                        if (attempts > 0) {
                            System.out.println("Incorrect password. " + attempts + " attempts remaining.");
                        } else {
                            System.out.println("Maximum attempts reached. Restarting username input.");
                            break;
                        }
                    }
                }
            } else {
                System.out.println("Username not found. Please try again.");
            }
        }
    }

    /**
     * Loads a user database from a CSV file.
     * The CSV file is expected to have two columns: username and hashed password.
     * 
     * @param filename The path to the CSV file containing user data.
     * @return A HashMap where keys are usernames and values are hashed passwords.
     * 
     * @throws IOException If an error occurs while reading the file.
     */
    public static HashMap<String, String> loadUserDatabase(String filename) {
        HashMap<String, String> userDatabase = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    userDatabase.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
        return userDatabase;
    }
}
