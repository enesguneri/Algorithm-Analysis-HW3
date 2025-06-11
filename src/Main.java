import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        //first line - base cost
        //second line - cashier count
        //third line - max types for cashier
        //fourth line - input of transaction elements

        try {
            Scanner scanner = new Scanner(new File("input1.txt"));
            List<InputData> inputList = new ArrayList<>();
            while (scanner.hasNextLine()) {
                try {
                    int baseCost = Integer.parseInt(scanner.nextLine());
                    int cashierCount = Integer.parseInt(scanner.nextLine());
                    int maxTypes = Integer.parseInt(scanner.nextLine());
                    String[] transactions = scanner.nextLine().split(",");
                    List<Integer> transactionsList = new ArrayList<>();
                    for (String transaction : transactions) {
                        transactionsList.add(Integer.parseInt(String.valueOf(transaction.charAt(transaction.length() - 1))));
                    }
                    inputList.add(new InputData(baseCost, cashierCount, maxTypes, transactionsList));
                } catch (Exception e) {
                    System.out.println("Input format is incorrect");
                }
            }
            if (!inputList.isEmpty()) {
                for (InputData inputData : inputList) {
                    List<Cashier> cashierList = inputData.cashiers;
                    int baseCost = inputData.baseCost;
                    int cashierCount = inputData.cashierCount;
                    int maxTypes = inputData.maxTypesPerCashier;
                    List<Integer> transactionsList = inputData.transactionTypes;
                    double totalCost;
                    if (isCashierCountSufficient(cashierCount, maxTypes, transactionsList)) {
                        for (int i = 0; i < cashierCount; i++) {
                            cashierList.add(new Cashier(baseCost, maxTypes));
                        }
                        totalCost = 0.0;
                        for (Integer type : transactionsList) {
                            double cost = processGreedyAlgorithm(type, cashierList);
                            totalCost += cost;
                            int totalCustomers = 0;
                            for (Cashier cashier : cashierList) {
                                totalCustomers += cashier.customerCount;
                            }
                            if (totalCustomers % 5 == 0) {
                                for (Cashier cashier : cashierList) {
                                    cashier.baseCost++;
                                }
                            }
                        }
                    } else
                        totalCost = -1.0;
                    System.out.printf("%.2f\n", totalCost);
                }
            }
            System.out.println();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }
    public static double processGreedyAlgorithm(int transactionType,List<Cashier> cashierList) {
        double minCost = Double.MAX_VALUE;
        Cashier minCostCashier = cashierList.getFirst();
        int index = 0;
        for (Cashier cashier : cashierList) {
            double cost = cashier.calculateCost(transactionType);
            if (cost < minCost && cost != -1) {
                minCost = cost;
                minCostCashier = cashier;
                index = cashierList.indexOf(cashier);
            }
        }
        minCostCashier.setCurrentTransaction(transactionType);
        System.out.printf("Cashier %d Type %d Cost:%.2f\n",index,transactionType,minCost);
        return minCost;
    }

    public static boolean isCashierCountSufficient(int cashierCount, int maxTypesPerCashier, List<Integer> transactionList) {
        Set<Integer> transactionTypes = new HashSet<>(transactionList);
        return transactionTypes.size() <= maxTypesPerCashier * cashierCount;
    }
}
