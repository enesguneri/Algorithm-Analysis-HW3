import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Cashier {
    int currentTransaction;
    double currentCost;
    double baseCost;
    List<Integer> transactionHistory;
    int customerCount;
    int transactionTypeLimit;
    Set<Integer> transactionTypes;
    public Cashier(int baseCost, int transactionTypeLimit) {
        currentCost = 0;
        this.baseCost = baseCost;
        transactionHistory = new ArrayList<>();
        customerCount = 0;
        this.transactionTypeLimit = transactionTypeLimit;
        transactionTypes = new HashSet<>();
    }
    public void setCurrentTransaction(int newTransaction) {
        if (!transactionHistory.isEmpty()) {
            currentTransaction = transactionHistory.getFirst();
            if (currentTransaction != newTransaction)
                currentCost = baseCost;
        }
        this.currentTransaction = newTransaction;
        transactionHistory.add(currentTransaction);
        customerCount++;
        transactionTypes.add(currentTransaction);

    }

    public double calculateCost(int newTransaction) {
        double tempCost = currentCost;
        if (!transactionHistory.isEmpty()) {
            currentTransaction = transactionHistory.getFirst();
            if (currentTransaction != newTransaction)
                tempCost = baseCost;
        }
        if (transactionTypes.size() >= transactionTypeLimit && !transactionTypes.contains(newTransaction))
            return -1.0;
        //Rule 2
        if (transactionHistory.size() >= 2 && transactionHistory.get(transactionHistory.size()-2) == currentTransaction && currentTransaction == newTransaction) {
            tempCost = baseCost * 1.5;//daha önceden sıfırsa bu durumu kontrol etmeden sıfır gönderiyordu. ifi kaldırınca düzeldi
        }
        else if (!transactionHistory.isEmpty() && transactionHistory.getLast() == newTransaction)//No switching cost
            return 0;

        //Rule 3
        for (int i = 0; i < transactionHistory.size(); i++) {
            if (newTransaction < transactionHistory.get(i)) {
                tempCost *= 0.8;
                break;
            }
        }

        return tempCost;
    }
}
