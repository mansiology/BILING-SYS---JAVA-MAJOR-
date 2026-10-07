package datastructure;

import model.*;
import java.util.*;

/**
 * ConsumerStore — Central data repository for the entire application.
 *
 * ALL GUI panels use this single store so data is always consistent.
 *
 * Data Structures used (all required by the case study):
 *   ArrayList<Consumer>   — stores the main list of consumers (ordered insertion)
 *   HashMap<String,Consumer> — fast O(1) lookup by Consumer ID
 *   TreeMap<String,Consumer> — keeps consumers sorted by Consumer ID automatically
 *   LinkedList<Bill>      — stores billing history (efficient add/remove at ends)
 *   LinkedList<Payment>   — stores full payment history (never deleted)
 *   HashMap<String,Meter> — latest meter reading indexed by meter number
 */
public class ConsumerStore {

    // --- ArrayList: main consumer list (insertion order preserved) ---
    private ArrayList<Consumer> consumerList = new ArrayList<>();

    // --- HashMap: fast lookup by Consumer ID ---
    private HashMap<String, Consumer> consumerMap = new HashMap<>();

    // --- TreeMap: consumers sorted by Consumer ID (lexicographic) ---
    private TreeMap<String, Consumer> sortedConsumerMap = new TreeMap<>();

    // --- LinkedList: billing history ---
    private LinkedList<Bill> billList = new LinkedList<>();

    // --- LinkedList: complete payment history (never removed) ---
    private LinkedList<Payment> paymentList = new LinkedList<>();

    // --- HashMap: latest meter reading per meter number ---
    private HashMap<String, Meter> meterReadings = new HashMap<>();

    // Counters for generating unique IDs
    private int billCounter = 1;
    private int paymentCounter = 1;

    // ========================
    //   CONSUMER OPERATIONS
    // ========================

    /** Add a new consumer to all three data structures. */
    public void addConsumer(Consumer c) {
        consumerList.add(c);                        // ArrayList: ordered list
        consumerMap.put(c.getConsumerId(), c);       // HashMap: fast lookup
        sortedConsumerMap.put(c.getConsumerId(), c); // TreeMap: sorted by ID
    }

    /** Check if a Consumer ID already exists. */
    public boolean consumerExists(String id) {
        return consumerMap.containsKey(id); // HashMap O(1) lookup
    }

    /** Find consumer by exact ID — uses HashMap for fast lookup. */
    public Consumer findById(String id) {
        return consumerMap.get(id);
    }

    /** Remove consumer from all structures. */
    public void removeConsumer(String id) {
        Consumer c = consumerMap.get(id);
        if (c != null) {
            consumerList.remove(c);
            consumerMap.remove(id);
            sortedConsumerMap.remove(id);
        }
    }

    /** Get all consumers as an ArrayList (insertion order). */
    public ArrayList<Consumer> getAllConsumers() {
        return consumerList;
    }

    /** Get consumers sorted by ID using TreeMap (natural sort order). */
    public List<Consumer> getConsumersSortedById() {
        return new ArrayList<>(sortedConsumerMap.values());
    }

    // ========================
    //   METER OPERATIONS
    // ========================

    /** Save or update the latest meter reading for a meter number. */
    public void saveMeterReading(Meter meter) {
        meterReadings.put(meter.getMeterNumber(), meter);
    }

    /** Get latest meter reading for a given meter number. */
    public Meter getMeterReading(String meterNumber) {
        return meterReadings.get(meterNumber);
    }

    /** Check if a meter reading exists. */
    public boolean hasMeterReading(String meterNumber) {
        return meterReadings.containsKey(meterNumber);
    }

    // ========================
    //   BILL OPERATIONS
    // ========================

    /** Add a new bill to the LinkedList. */
    public void addBill(Bill bill) {
        billList.add(bill); // LinkedList efficient insertion
    }

    /** Get all bills. */
    public LinkedList<Bill> getAllBills() {
        return billList;
    }

    /** Find a bill by Bill ID. */
    public Bill findBillById(String billId) {
        for (Bill b : billList) {
            if (b.getBillId().equals(billId)) return b;
        }
        return null;
    }

    /** Get all bills for a specific consumer. */
    public List<Bill> getBillsByConsumer(String consumerId) {
        List<Bill> result = new ArrayList<>();
        for (Bill b : billList) {
            if (b.getConsumerId().equals(consumerId)) result.add(b);
        }
        return result;
    }

    /** Generate a unique Bill ID. */
    public String generateBillId() {
        return "B" + String.format("%03d", billCounter++);
    }

    // ========================
    //   PAYMENT OPERATIONS
    // ========================

    /** Record a payment in the LinkedList payment history. */
    public void addPayment(Payment payment) {
        paymentList.add(payment); // LinkedList: history is never removed
    }

    /** Get complete payment history. */
    public LinkedList<Payment> getAllPayments() {
        return paymentList;
    }

    /** Get payments for a specific consumer. */
    public List<Payment> getPaymentsByConsumer(String consumerId) {
        List<Payment> result = new ArrayList<>();
        for (Payment p : paymentList) {
            if (p.getConsumerId().equals(consumerId)) result.add(p);
        }
        return result;
    }

    /** Generate a unique Payment ID. */
    public String generatePaymentId() {
        return "PAY" + String.format("%03d", paymentCounter++);
    }

    // ========================
    //   DASHBOARD STATISTICS
    // ========================

    public int getTotalConsumers() { return consumerList.size(); }
    public int getTotalBills()     { return billList.size(); }

    public double getTotalBilledAmount() {
        double total = 0;
        for (Bill b : billList) total += b.getBillAmount();
        return total;
    }

    public double getTotalCollected() {
        double total = 0;
        for (Bill b : billList) total += b.getAmountPaid();
        return total;
    }

    public double getTotalPending() {
        return getTotalBilledAmount() - getTotalCollected();
    }

    public int getPaidBillsCount() {
        int count = 0;
        for (Bill b : billList) if ("PAID".equals(b.getStatus())) count++;
        return count;
    }

    public int getUnpaidBillsCount() {
        int count = 0;
        for (Bill b : billList) if ("UNPAID".equals(b.getStatus())) count++;
        return count;
    }

    public double getTotalUnitsConsumed() {
        double total = 0;
        for (Bill b : billList) total += b.getUnitsConsumed();
        return total;
    }

    public double getCollectionRate() {
        double billed = getTotalBilledAmount();
        if (billed == 0) return 0.0;
        return (getTotalCollected() / billed) * 100.0;
    }
}
