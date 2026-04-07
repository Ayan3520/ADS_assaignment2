import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    static LinkedList<BankAccount> accounts = new LinkedList<>();

    static Stack<String> transactionHistory = new Stack<>();

    static Queue<String> billQueue = new LinkedList<>();

    static Queue<BankAccount> accountRequests = new LinkedList<>();

    static Scanner scanner = new Scanner(System.in);
    static int nextAccountNumber = 4;

    public static void main(String[] args) {
        demonstratePhysicalDataStructure();

        boolean running = true;
        while (running) {
            System.out.println("\n=== Главное Меню ===");
            System.out.println("1 – Enter Bank");
            System.out.println("2 – Enter ATM");
            System.out.println("3 – Admin Area");
            System.out.println("4 – Exit");
            System.out.print("Выберите опцию: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Очистка буфера

            switch (choice) {
                case 1:
                    bankMenu();
                    break;
                case 2:
                    atmMenu();
                    break;
                case 3:
                    adminMenu();
                    break;
                case 4:
                    running = false;
                    System.out.println("Выход из системы...");
                    break;
                default:
                    System.out.println("Неверный ввод, попробуйте снова.");
            }
        }
    }

    private static void demonstratePhysicalDataStructure() {
        System.out.println("\n--- Task 6: Физическая структура (Массив BankAccount[3]) ---");
        BankAccount[] predefinedAccounts = new BankAccount[3];
        predefinedAccounts[0] = new BankAccount(1, "Ali", 150000);
        predefinedAccounts[1] = new BankAccount(2, "Sara", 220000);
        predefinedAccounts[2] = new BankAccount(3, "Berik", 50000);

        // Добавляем их сразу в наш LinkedList, чтобы было с чем работать в меню
        for (BankAccount acc : predefinedAccounts) {
            System.out.println(acc.toString());
            accounts.add(acc);
        }
    }

    private static void bankMenu() {
        System.out.println("\n--- Bank Menu ---");
        System.out.println("1 - Отправить заявку на открытие счета");
        System.out.println("2 - Внести деньги (Deposit)");
        System.out.println("3 - Снять деньги (Withdraw)");
        System.out.println("4 - Добавить счет на оплату в очередь (Bill)");
        System.out.println("5 - Отменить последнюю транзакцию (Undo)");
        System.out.print("Выбор: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Введите ваше имя: ");
                String name = scanner.nextLine();
                System.out.print("Введите начальный депозит: ");
                double initialDep = scanner.nextDouble();
                accountRequests.add(new BankAccount(nextAccountNumber++, name, initialDep));
                System.out.println("Заявка отправлена в очередь администратору.");
                break;
            case 2:
                processDeposit();
                break;
            case 3:
                processWithdrawal();
                break;
            case 4:
                System.out.print("Введите название счета (напр., Electricity Bill): ");
                String bill = scanner.nextLine();
                billQueue.add(bill);
                System.out.println("Счет добавлен в очередь: " + bill);
                break;
            case 5:
                if (!transactionHistory.isEmpty()) {
                    System.out.println("Отменено: " + transactionHistory.pop());
                } else {
                    System.out.println("История пуста.");
                }
                break;
        }
    }

    private static void atmMenu() {
        System.out.println("\n--- ATM Menu ---");
        System.out.println("1 - Проверить баланс");
        System.out.println("2 - Снять деньги");
        System.out.print("Выбор: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            BankAccount acc = findAccount();
            if (acc != null) System.out.println("Баланс: " + acc.getBalance());
        } else if (choice == 2) {
            processWithdrawal();
        }
    }

    private static void adminMenu() {
        System.out.println("\n--- Admin Menu ---");
        System.out.println("1 - Посмотреть все счета");
        System.out.println("2 - Обработать заявки на открытие счетов (Queue)");
        System.out.println("3 - Обработать очередь оплаты счетов (Bill Queue)");
        System.out.println("4 - Посмотреть последнюю транзакцию банка");
        System.out.print("Выбор: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.println("Список счетов:");
                for (BankAccount acc : accounts) System.out.println(acc.toString());
                break;
            case 2:
                if (accountRequests.isEmpty()) {
                    System.out.println("Нет новых заявок.");
                } else {
                    BankAccount newAcc = accountRequests.poll();
                    accounts.add(newAcc);
                    System.out.println("Одобрен и добавлен счет: " + newAcc.getUsername());
                }
                break;
            case 3:
                if (billQueue.isEmpty()) {
                    System.out.println("Нет счетов на оплату.");
                } else {
                    System.out.println("Оплачивается: " + billQueue.poll());
                    if (!billQueue.isEmpty()) {
                        System.out.println("Осталось в очереди: " + billQueue.peek());
                    }
                }
                break;
            case 4:
                if (!transactionHistory.isEmpty()) {
                    System.out.println("Последняя транзакция: " + transactionHistory.peek());
                } else {
                    System.out.println("Транзакций пока не было.");
                }
                break;
        }
    }
    private static BankAccount findAccount() {
        System.out.print("Введите имя пользователя (username): ");
        String name = scanner.nextLine();
        for (BankAccount acc : accounts) {
            if (acc.getUsername().equalsIgnoreCase(name)) {
                return acc;
            }
        }
        System.out.println("Аккаунт не найден.");
        return null;
    }

    private static void processDeposit() {
        BankAccount acc = findAccount();
        if (acc != null) {
            System.out.print("Сумма депозита: ");
            double amount = scanner.nextDouble();
            acc.deposit(amount);
            String transaction = "Deposit " + amount + " to " + acc.getUsername();
            transactionHistory.push(transaction);
            System.out.println("Успешно. " + transaction);
        }
    }

    private static void processWithdrawal() {
        BankAccount acc = findAccount();
        if (acc != null) {
            System.out.print("Сумма снятия: ");
            double amount = scanner.nextDouble();
            if (acc.withdraw(amount)) {
                String transaction = "Withdraw " + amount + " from " + acc.getUsername();
                transactionHistory.push(transaction);
                System.out.println("Успешно. " + transaction);
            } else {
                System.out.println("Недостаточно средств.");
            }
        }
    }
}