package practice_java;

import java.util.List;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        CustomerDAO dao = new CustomerDAO();

        while (true) {
            System.out.println("\n=== Customer Management System ===");
            System.out.println("1. View All Customers");
            System.out.println("2. Add Customer");
            System.out.println("3. Update Customer");
            System.out.println("4. Delete Customer");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    List<Customer> list = dao.getAllCustomers();
                    for (Customer c : list) {
                        System.out.println(c);
                    }
                    break;

                case 2:
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();
                    sc.nextLine(); // consume newline
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Phone: ");
                    String phone = sc.nextLine();
                    System.out.print("Enter Location: ");
                    String loc = sc.nextLine();

                    dao.addCustomer(new Customer(id, name, phone, loc));
                    break;

                case 3:
                    System.out.print("Enter ID to update: ");
                    int uid = sc.nextInt();
                    sc.nextLine();
                    System.out.print("New Name: ");
                    String newName = sc.nextLine();
                    System.out.print("New Phone: ");
                    String newPhone = sc.nextLine();
                    System.out.print("New Location: ");
                    String newLoc = sc.nextLine();
                    dao.updateCustomer(uid, newName, newPhone, newLoc);
                    break;

                case 4:
                    System.out.print("Enter ID to delete: ");
                    int did = sc.nextInt();
                    dao.deleteCustomer(did);
                    break;

                case 5:
                    System.out.println("Exiting...");
                    sc.close();
                    System.exit(0);
            }
        }
    }
}
