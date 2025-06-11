import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Cashier {
    int currentTransaction;
    double baseCost;
    List<Integer> transactionHistory;
    int customerCount;
    int transactionTypeLimit;
    Set<Integer> transactionTypes;
    public Cashier(int baseCost, int transactionTypeLimit) {
        this.baseCost = baseCost;
        transactionHistory = new ArrayList<>();
        customerCount = 0;
        this.transactionTypeLimit = transactionTypeLimit;
        transactionTypes = new HashSet<>();
    }
    public void setCurrentTransaction(int newTransaction) {
        this.currentTransaction = newTransaction;
        transactionHistory.add(currentTransaction);
        customerCount++;
        transactionTypes.add(currentTransaction);

    }

    public double calculateCost(int newTransaction) {
        double tempCost = 0;
        if (transactionTypes.size() >= transactionTypeLimit && !transactionTypes.contains(newTransaction))
            return -1.0;

        if (!transactionHistory.isEmpty() && currentTransaction != newTransaction)
            tempCost = baseCost;

        //Rule 2
        if (transactionHistory.size() >= 2) {
            if (currentTransaction == newTransaction && transactionHistory.get(transactionHistory.size()-2) == newTransaction)
                tempCost = baseCost * 1.5;
            else if (transactionHistory.getLast() == newTransaction)
                return 0;
        }

        //Rule 3
        for (int i = 0; i < transactionHistory.size(); i++) {
            if (newTransaction < transactionHistory.get(i)) {
                if (tempCost == 0)
                    tempCost = baseCost * 0.8;
                else
                    tempCost *= 0.8;
                break;
            }
        }

        return tempCost;
    }

}


