package com.passwordmanager;

import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Main {

    static final String FILE = System.getProperty("user.dir") + "/data/passwords.txt";
    static final String MASTER = System.getProperty("user.dir") + "/data/master.txt";
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // AUTH
    static boolean authenticate() {
        Scanner sc = new Scanner(System.in);
        File file = new File(MASTER);

        try {
            if (!file.exists() || file.length() == 0) {
                System.out.print("Set master password: ");
                String pass = sc.nextLine();

                FileWriter fw = new FileWriter(file);
                fw.write(pass);
                fw.close();

                System.out.println("Master password set!");
                return true;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String stored = br.readLine();
            br.close();

            int attempts = 3;

            while (attempts > 0) {
                System.out.print("Enter master password: ");
                String input = sc.nextLine();

                if (input.equals(stored)) return true;

                attempts--;
                System.out.println("Wrong password! Attempts left: " + attempts);
            }

            System.out.println("Too many attempts. Exiting...");
            return false;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // STRENGTH
    static String checkStrength(String p) {
        if (p.length() < 6) return "Weak";

        boolean u=false,l=false,d=false,s=false;

        for(char c:p.toCharArray()){
            if(Character.isUpperCase(c)) u=true;
            else if(Character.isLowerCase(c)) l=true;
            else if(Character.isDigit(c)) d=true;
            else s=true;
        }

        if(u&&l&&d&&s) return "Strong";
        return "Medium";
    }

    // ADD
    static void addPassword(String site, String pass) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE));
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                if (line.split(":")[0].equals(site)) {
                    System.out.println("Site already exists!");
                    br.close();
                    return;
                }
            }
            br.close();

            String enc = PasswordManager.encrypt(pass);
            String date = LocalDate.now().format(formatter);

            FileWriter fw = new FileWriter(FILE, true);
            fw.write(site + ":" + enc + ":" + date + "\n");
            fw.close();

            System.out.println("Saved!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // GET
    static void getPassword(String site) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE));
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] p = line.split(":");

                if (p[0].equals(site)) {
                    System.out.println("Password: " + PasswordManager.decrypt(p[1]));
                    System.out.println("Saved on: " + p[2]);
                    br.close();
                    return;
                }
            }

            br.close();
            System.out.println("Not found");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // UPDATE
    static void updatePassword(String site, String pass) {
        try {
            File in = new File(FILE);
            File temp = new File(System.getProperty("user.dir") + "/data/temp.txt");

            BufferedReader br = new BufferedReader(new FileReader(in));
            FileWriter fw = new FileWriter(temp);

            String line;
            boolean found=false;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] p = line.split(":");

                if (p[0].equals(site)) {
                    String enc = PasswordManager.encrypt(pass);
                    String date = LocalDate.now().format(formatter);
                    fw.write(site + ":" + enc + ":" + date + "\n");
                    found=true;
                } else {
                    fw.write(line + "\n");
                }
            }

            br.close();
            fw.close();

            in.delete();
            temp.renameTo(in);

            System.out.println(found ? "Updated!" : "Not found");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    static void deletePassword(String site) {
        try {
            File in = new File(FILE);
            File temp = new File(System.getProperty("user.dir") + "/data/temp.txt");

            BufferedReader br = new BufferedReader(new FileReader(in));
            FileWriter fw = new FileWriter(temp);

            String line;
            boolean found=false;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                if (!line.split(":")[0].equals(site)) {
                    fw.write(line + "\n");
                } else {
                    found=true;
                }
            }

            br.close();
            fw.close();

            in.delete();
            temp.renameTo(in);

            System.out.println(found ? "Deleted!" : "Not found");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // VIEW
    static void viewAllSites() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE));
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                System.out.println(line.split(":")[0]);
            }

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // SEARCH
    static void searchSite(String key) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE));
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                if (line.contains(key)) {
                    System.out.println(line.split(":")[0]);
                }
            }

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // COUNT
    static void countPasswords() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE));
            int c=0;

            while (br.readLine() != null) c++;

            br.close();
            System.out.println("Total: " + c);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // GENERATE
    static String generatePassword() {
        String chars="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        Random r=new Random();
        StringBuilder sb=new StringBuilder();

        for(int i=0;i<10;i++)
            sb.append(chars.charAt(r.nextInt(chars.length())));

        return sb.toString();
    }

    // MAIN
    public static void main(String[] args) {

        // create folders/files automatically
        new File("data").mkdirs();
        try {
            new File(FILE).createNewFile();
            new File(MASTER).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!authenticate()) return;

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1.Add 2.Get 3.Update 4.View 5.Search 6.Generate 7.Count 8.Delete 9.Exit");

            int ch = sc.nextInt();
            sc.nextLine();

            if (ch == 1) {
                System.out.print("Site: ");
                String s = sc.nextLine();

                String p;
                while (true) {
                    System.out.print("Password: ");
                    p = sc.nextLine();

                    if (!checkStrength(p).equals("Weak")) break;
                    System.out.println("Weak! Try again");
                }

                addPassword(s, p);

            } else if (ch == 2) {
                System.out.print("Site: ");
                getPassword(sc.nextLine());

            } else if (ch == 3) {
                System.out.print("Site: ");
                String s = sc.nextLine();

                String p;
                while (true) {
                    System.out.print("New Password: ");
                    p = sc.nextLine();

                    if (!checkStrength(p).equals("Weak")) break;
                }

                updatePassword(s, p);

            } else if (ch == 4) viewAllSites();
            else if (ch == 5) {
                System.out.print("Keyword: ");
                searchSite(sc.nextLine());
            }
            else if (ch == 6) System.out.println(generatePassword());
            else if (ch == 7) countPasswords();
            else if (ch == 8) {
                System.out.print("Site: ");
                deletePassword(sc.nextLine());
            }
            else if (ch == 9) break;
            else System.out.println("Invalid");
        }
    }
}
