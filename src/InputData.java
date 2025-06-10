import java.util.ArrayList;
import java.util.List;

public class InputData {
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
