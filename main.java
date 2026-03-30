package com.passwordmanager;

import java.io.*;
import java.util.Scanner;

public class password_manager_java_24BAI10720 {

    static final String FILE = "data/passwords.txt";

    //  MASTER PASSWORD AUTH
    static boolean authenticate() {
        Scanner sc = new Scanner(System.in);
        File file = new File("data/master.txt");

        try {
            if (!file.exists() || file.length() == 0) {
                System.out.print("Set master password: ");
                String newPass = sc.nextLine();

                FileWriter fw = new FileWriter(file);
                fw.write(newPass);
                fw.close();

                System.out.println("Master password set!");
                return true;
            } else {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String stored = br.readLine();
                br.close();

                System.out.print("Enter master password: ");
                String input = sc.nextLine();

                return stored != null && stored.equals(input);
            }
        } catch (IOException e) {
            System.out.println("Authentication error");
            return false;
        }
    }

    // ADD PASSWORD
    public static void addPassword(String site, String password) {
        try {
            File file = new File(FILE);

            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(FILE));
                String line;

                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts[0].equals(site)) {
                        System.out.println("Site already exists! Use update.");
                        br.close();
                        return;
                    }
                }
                br.close();
            }

            FileWriter fw = new FileWriter(FILE, true);
            String encrypted = PasswordManager.encrypt(password);
            fw.write(site + ":" + encrypted + "\n");
            fw.close();

            System.out.println("Password saved securely!");

        } catch (IOException e) {
            System.out.println("Error saving password");
        }
    }

    // GET PASSWORD
    public static void getPassword(String site) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].equals(site)) {
                    String decrypted = PasswordManager.decrypt(parts[1]);
                    System.out.println("Password: " + decrypted);
                    return;
                }
            }

            System.out.println("No entry found");

        } catch (IOException e) {
            System.out.println("Error reading file");
        }
    }

    //  UPDATE PASSWORD
    public static void updatePassword(String site, String newPassword) {
        try {
            File file = new File(FILE);
            File temp = new File("data/temp.txt");

            BufferedReader br = new BufferedReader(new FileReader(file));
            FileWriter fw = new FileWriter(temp);

            String line;
            boolean found = false;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");

                if (parts[0].equals(site)) {
                    String encrypted = PasswordManager.encrypt(newPassword);
                    fw.write(site + ":" + encrypted + "\n");
                    found = true;
                } else {
                    fw.write(line + "\n");
                }
            }

            br.close();
            fw.close();

            file.delete();
            temp.renameTo(file);

            if (found)
                System.out.println("Password updated!");
            else
                System.out.println("Site not found!");

        } catch (IOException e) {
            System.out.println("Error updating password");
        }
    }

    //  VIEW ALL SITES
    public static void viewAllSites() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            System.out.println("Saved sites:");

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                System.out.println("- " + parts[0]);
            }

        } catch (IOException e) {
            System.out.println("Error reading file");
        }
    }

    //  SEARCH
    public static void searchSite(String keyword) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            boolean found = false;

            while ((line = br.readLine()) != null) {
                if (line.toLowerCase().contains(keyword.toLowerCase())) {
                    String[] parts = line.split(":");
                    System.out.println("Found: " + parts[0]);
                    found = true;
                }
            }

            if (!found)
                System.out.println("No matching site found");

        } catch (IOException e) {
            System.out.println("Error reading file");
        }
    }

    // COUNT
    public static void countPasswords() {
        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            while (br.readLine() != null)
                count++;

            System.out.println("Total saved accounts: " + count);

        } catch (IOException e) {
            System.out.println("Error reading file");
        }
    }

    //  STRENGTH CHECK
    public static void checkStrength(String password) {
        if (password.length() < 6) {
            System.out.println("Password Strength: Weak");
        } else if (password.matches(".*[0-9].*") && password.matches(".*[!@#$%^&*].*")) {
            System.out.println("Password Strength: Strong");
        } else {
            System.out.println("Password Strength: Medium");
        }
    }

    // GENERATE PASSWORD
    public static String generatePassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder pass = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int index = (int) (Math.random() * chars.length());
            pass.append(chars.charAt(index));
        }

        return pass.toString();
    }

    public static void main(String[] args) {

        if (!authenticate()) {
            System.out.println("Wrong password. Exiting...");
            return;
        }

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Add Password");
            System.out.println("2. Retrieve Password");
            System.out.println("3. Update Password");
            System.out.println("4. View All Sites");
            System.out.println("5. Search Site");
            System.out.println("6. Generate Password");
            System.out.println("7. Count Passwords");
            System.out.println("8. Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Enter site: ");
                String site = sc.nextLine();

                String password;

                while (true) {
                    System.out.print("Enter password: ");
                    password = sc.nextLine();

                    if (password.length() < 6) {
                        System.out.println("Weak password! Please enter a stronger password.");
                    } else {
                        checkStrength(password);
                        break;
                    }
                }

                addPassword(site, password);

            } else if (choice == 2) {
                System.out.print("Enter site: ");
                String site = sc.nextLine();
                getPassword(site);

            } else if (choice == 3) {
                System.out.print("Enter site: ");
                String site = sc.nextLine();

                System.out.print("Enter new password: ");
                String newPassword = sc.nextLine();

                updatePassword(site, newPassword);

            } else if (choice == 4) {
                viewAllSites();

            } else if (choice == 5) {
                System.out.print("Enter keyword: ");
                String keyword = sc.nextLine();
                searchSite(keyword);

            } else if (choice == 6) {
                String generated = generatePassword();
                System.out.println("Generated Password: " + generated);

            } else if (choice == 7) {
                countPasswords();

            } else if (choice == 8) {
                break;

            } else {
                System.out.println("Invalid choice");
            }
        }
    }
}
