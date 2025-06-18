import java.io.*;
import java.util.*;

public class Greedy {
    public static void main(String[] args) {
        //first line - base cost
        //second line - cashier count
        //third line - max types for cashier
        //fourth line - input of transaction elements
        String fileName = "input.txt";

        try {
            Scanner scanner = new Scanner(new File(fileName));
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
                try(BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) {
                    for (InputData inputData : inputList) {
                        List<Cashier> cashierList = inputData.cashiers;
                        int baseCost = inputData.baseCost;
                        int cashierCount = inputData.cashierCount;
                        int maxTypes = inputData.maxTypesPerCashier;
                        List<Integer> transactionsList = inputData.transactionTypes;
                        Set<Integer> transactionTypes = new HashSet<>(transactionsList);
                        int b = transactionTypes.size();
                        if (baseCost > 0 && baseCost <= 100 && maxTypes > 0 && maxTypes <= 100 && b > 0 && b <= 50) {
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
                            bw.write(String.format("%.2f\n", totalCost));
                            System.out.printf("%.2f\n", totalCost);
                        } else
                            System.out.println("c, k, b values are out of range");
                    }
                } catch (IOException e) {
                    System.out.println("Error in writing to file");
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
        //int index = 0;
        for (Cashier cashier : cashierList) {
            double cost = cashier.calculateCost(transactionType);
            if (cost < minCost && cost != -1) {
                minCost = cost;
                minCostCashier = cashier;
                //index = cashierList.indexOf(cashier);
            }
        }
        minCostCashier.setCurrentTransaction(transactionType);
        //System.out.printf("Cashier %d Type %d Cost:%.2f\n",index,transactionType,minCost);
        return minCost;
    }

    public static boolean isCashierCountSufficient(int cashierCount, int maxTypesPerCashier, List<Integer> transactionList) {
        Set<Integer> transactionTypes = new HashSet<>(transactionList);
        return transactionTypes.size() <= maxTypesPerCashier * cashierCount;
    }
}

class Cashier {
    int currentTransaction;
    double baseCost;
    List<Integer> transactionHistory;
    int maxTypeTransaction;
    int customerCount;
    int transactionTypeLimit;
    Set<Integer> transactionTypes;
    public Cashier(int baseCost, int transactionTypeLimit) {
        this.baseCost = baseCost;
        transactionHistory = new ArrayList<>();
        maxTypeTransaction = 1;//calculateCost'un düzgün çalışması için başlangıçta 1 olarak ayarlanır. 1 olsa da learning curve kuralını etkilemez.
        customerCount = 0;
        this.transactionTypeLimit = transactionTypeLimit;
        transactionTypes = new HashSet<>();
    }
    public void setCurrentTransaction(int newTransaction) {
        this.currentTransaction = newTransaction;
        if (newTransaction > maxTypeTransaction)
            maxTypeTransaction = newTransaction;
        transactionHistory.add(currentTransaction);
        customerCount++;
        transactionTypes.add(currentTransaction);

    }

    public double calculateCost(int newTransaction) {
        double tempCost = 0;
        if (transactionTypes.size() >= transactionTypeLimit && !transactionTypes.contains(newTransaction))
            return -1.0;

        if (!transactionTypes.isEmpty() && currentTransaction != newTransaction)
            tempCost = baseCost;

        //Rule 2
        if (transactionHistory.size() >= 2) {
            if (currentTransaction == newTransaction && transactionHistory.get(transactionHistory.size()-2) == newTransaction)
                tempCost = baseCost * 1.5;
            else if (transactionHistory.getLast() == newTransaction)
                return 0;
        }

        //Learning Curve
        if (newTransaction < maxTypeTransaction) {
            if (tempCost == 0)
                tempCost = baseCost * 0.8;
            else
                tempCost *= 0.8;
        }


        return tempCost;
    }

}

class InputData {
    public int baseCost;
    public int cashierCount;
    public int maxTypesPerCashier;
    public List<Integer> transactionTypes;
    public List<Cashier> cashiers;

    public InputData(int baseCost, int cashierCount, int maxTypesPerCashier, List<Integer> transactionTypes) {
        this.baseCost = baseCost;
        this.cashierCount = cashierCount;
        this.maxTypesPerCashier = maxTypesPerCashier;
        this.transactionTypes = transactionTypes;
        this.cashiers = new ArrayList<>();
    }
}

