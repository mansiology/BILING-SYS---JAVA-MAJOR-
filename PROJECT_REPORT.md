# ⚡ ELECTRICITY BILLING MANAGEMENT SYSTEM
## Comprehensive Case Study & Project Report
**Course:** B.Tech Computer Science & Engineering — Semester III  
**Subject:** Java Programming & Object-Oriented Software Engineering  
**Application Type:** Desktop GUI Application (Java Swing + Core Collections)  

---

## 1. Executive Summary & Objective

The **Electricity Billing Management System** is a standalone, in-memory desktop software developed to automate and manage core utility operations:
1. **Consumer Registration & Record Management (CRUD)**
2. **Meter Reading Acquisition & Consumption Validation**
3. **Multi-Slab Electricity Tariff Calculation**
4. **Automated Bill Generation**
5. **Flexible Payment Processing (Partial & Full Payments)**
6. **Immutable Transaction & Payment History Audit**
7. **Consumer Search (Exact & Substring Matching)**
8. **Multi-Criterion Sorting (Alphabetical & Bill Value)**
9. **Real-time Analytical Dashboard & Operational Reports**

### Key Engineering Constraints Followed
* **Zero External Dependencies:** Built purely with Standard Java SE (`java.awt`, `javax.swing`, `java.util`). No Maven, Gradle, Spring, Hibernate, or third-party JARs.
* **No SQL Database / JDBC:** All records reside in a shared, multi-collection data structure layer (`ConsumerStore`), demonstrating deep mastery of the **Java Collections Framework**.
* **Clean Single-Window Ergonomics:** Employs a single `JFrame` hosting a `CardLayout` architecture, matching modern dashboard ergonomics inspired by contemporary point-of-sale and utility apps.

---

## 2. System Architecture & Component Design

The project strictly follows a decoupled **3-Tier Architecture**:

```
┌─────────────────────────────────────────────────────────────┐
│                       PRESENTATION TIER                     │
│               gui.MainApp (JFrame + CardLayout)             │
│   ├── DashboardPanel       ├── BillPanel                    │
│   ├── ConsumerPanel        ├── PaymentPanel                 │
│   ├── MeterPanel           ├── SearchSortPanel              │
│                            └── ReportPanel                  │
└──────────────────────────────┬──────────────────────────────┘
                               │ Method Calls / DTO Passing
┌──────────────────────────────▼──────────────────────────────┐
│                        SERVICE TIER                         │
│  ├── service.ConsumerService (Validation, CRUD, Sorting)    │
│  ├── service.BillingService  (Tariff Array, Slabs, Bills)   │
│  └── service.PaymentService  (Payment Logic, Status Updates)│
└──────────────────────────────┬──────────────────────────────┘
                               │ State Synchronization
┌──────────────────────────────▼──────────────────────────────┐
│                      DATA STORE TIER                        │
│                datastructure.ConsumerStore                  │
│  ├── ArrayList<Consumer>       (Insertion Order List)       │
│  ├── HashMap<String, Consumer> (O(1) Exact ID Lookups)      │
│  ├── TreeMap<String, Consumer> (Sorted ID Tree)             │
│  ├── LinkedList<Bill>          (Billing Audit Chain)        │
│  ├── LinkedList<Payment>       (Immutable Payment Ledger)   │
│  └── HashMap<String, Meter>    (Meter Number -> Meter)      │
└─────────────────────────────────────────────────────────────┘
```

---

## 3. Mandatory Collections & Data Structures Mapping

Each data structure specified in the project guidelines has an active, distinct purpose:

| Data Structure | Implementation Instance | Algorithmic Purpose & Why Used |
|---|---|---|
| **`double[][]` (Array)** | `BillingService.tariffSlabs` | Fixed lookup matrix for electricity slab thresholds and rates. Allows loop-driven slab traversal. |
| **`ArrayList<Consumer>`** | `ConsumerStore.consumerList` | Dynamically resizable list storing all consumer objects. Preserves insertion order and supports indexed iterations. |
| **`HashMap<String, Consumer>`** | `ConsumerStore.consumerMap` | Provides average $O(1)$ fast lookup of consumers by unique `consumerId`. Used during reading inputs, billing lookups, and duplicate checks. |
| **`TreeMap<String, Consumer>`** | `ConsumerStore.sortedConsumerMap` | Self-balancing Red-Black Tree that automatically maintains consumers sorted lexicographically by `consumerId`. |
| **`LinkedList<Bill>`** | `ConsumerStore.billList` | Sequential billing history. Efficient head/tail appending without continuous array reallocations. |
| **`LinkedList<Payment>`** | `ConsumerStore.paymentList` | Append-only ledger of all payment receipts. Records are never deleted, ensuring financial auditing. |
| **`HashMap<String, Meter>`** | `ConsumerStore.meterReadings` | Maps `meterNumber` to its most recent `Meter` reading. |

---

## 4. Electricity Tariff Mathematical Model

The application enforces progressive tiered electricity consumption brackets.

### Slab Matrix:
* **Slab 1 (0 – 100 units):** ₹3.50 per unit
* **Slab 2 (101 – 200 units):** ₹5.00 per unit
* **Slab 3 (201 – 300 units):** ₹6.50 per unit
* **Slab 4 (Above 300 units):** ₹8.00 per unit
* **Fixed Meter Surcharge:** ₹100.00 (applied to all active bills)

### Mathematical Formulation:
$$\text{Total Bill} = \text{Fixed Surcharge} + \sum_{i=1}^{n} \left(\text{Units in Slab}_i \times \text{Rate}_i\right)$$

#### Viva Case Study Example (350 Units):
$$\begin{aligned}
\text{Slab 1 (0-100 units):} & \quad 100 \times 3.50 = ₹350.00 \\
\text{Slab 2 (101-200 units):} & \quad 100 \times 5.00 = ₹500.00 \\
\text{Slab 3 (201-300 units):} & \quad 100 \times 6.50 = ₹650.00 \\
\text{Slab 4 (>300 units):} & \quad 50 \times 8.00 = ₹400.00 \\
\text{Fixed Meter Surcharge:} & \quad ₹100.00 \\
\hline
\mathbf{Total\ Payable:} & \quad \mathbf{₹2,000.00}
\end{aligned}$$

---

## 5. Payment Lifecycle & Partial Payment Mechanics

```
               [Bill Generated]
             Status: UNPAID (₹1500)
                     │
         User pays ₹500 (Partial Payment)
                     │
    ┌────────────────┴────────────────┐
    ▼                                 ▼
Bill Record Updated             Payment Ledger Appended
Paid: ₹500                      Record: PAY001, ₹500, UPI
Pending: ₹1000                  Stored in LinkedList<Payment>
Status: Still UNPAID
                     │
         User pays ₹1000 (Final Payment)
                     │
    ┌────────────────┴────────────────┐
    ▼                                 ▼
Bill Record Updated             Payment Ledger Appended
Paid: ₹1500                     Record: PAY002, ₹1000, CASH
Pending: ₹0                     Stored in LinkedList<Payment>
Status: Switched to PAID
```

* **Zero Data Loss:** Old payments are never overwritten or cleared upon bill completion.
* **Overpayment Guard:** `PaymentService` throws `InvalidPaymentException` if a payment amount exceeds the current outstanding balance.

---

## 6. End-to-End Operational Workflow

1. **System Launch:**
   * Executed via `Main.java` inside `SwingUtilities.invokeLater()`.
   * Sample records (5 consumers, 5 meter readings, 3 bills, 3 payments) preload into `ConsumerStore`.
2. **Dashboard Initialization:**
   * Reads from `ConsumerStore` to calculate total billed, collected, pending, and collection rates dynamically.
3. **Consumer Registration:**
   * Validates ID regex (`[A-Za-z][0-9]{3,5}`), phone (10 digits), and checks `store.consumerExists()`.
4. **Meter Reading Acquisition:**
   * Validates non-negative values and enforces $\text{Current Reading} \ge \text{Previous Reading}$.
5. **Bill Creation:**
   * Computes consumption $\Delta$, evaluates tariff slabs, generates a unique Bill ID (`B00X`), and sets due date.
6. **Payment Settlement:**
   * Accepts Cash, UPI, Card, or Net Banking; updates outstanding balances; and appends transaction logs.
7. **Search & Sort:**
   * Executes $O(1)$ search via `HashMap.get()` or substring search via `.contains()`.
   * Sorts via `Comparator.comparing(Consumer::getName)` or descending total bill comparator.
8. **Live Reporting:**
   * Aggregates live numbers and displays consumer-level financial balances.

---

## 7. 20 Crucial Viva Questions & Comprehensive Answers

1. **Q: Why use an in-memory store instead of an external SQL database?**  
   **A:** For academic assessment, an in-memory store demonstrates explicit understanding of Java data structures, thread safety on the EDT, and direct collection lifecycle management without external abstractions.

2. **Q: Why is `SwingUtilities.invokeLater()` necessary in `Main.java`?**  
   **A:** Swing UI components are not thread-safe. `invokeLater()` ensures all component creation, rendering, and event handling occurs on the dedicated **Event Dispatch Thread (EDT)**, avoiding race conditions.

3. **Q: What is the benefit of `CardLayout` over opening multiple `JFrame` instances?**  
   **A:** `CardLayout` maintains a single window shell and swaps panels in-place. This prevents desktop window clutter, reduces memory overhead, and keeps menu navigation unified.

4. **Q: Why use `HashMap` for consumer lookups?**  
   **A:** `HashMap` provides an average time complexity of $O(1)$ for searching a consumer by their unique alphanumeric Consumer ID via hash bucket hashing.

5. **Q: How does `TreeMap` differ from `HashMap` in this project?**  
   **A:** While `HashMap` is unordered and runs in $O(1)$, `TreeMap` maintains keys in natural sorted order ($O(\log n)$ operations) using a Red-Black Tree. It provides an automatically sorted consumer catalog.

6. **Q: Why is a `LinkedList` preferred for payment and billing history?**  
   **A:** Payments and bills form a time-stamped transaction log. Appending elements to the end of a `LinkedList` is an $O(1)$ pointer update without array-resizing overhead.

7. **Q: How is the tariff array structured and traversed?**  
   **A:** As a 2D array `double[][] tariffSlabs = {{100, 3.50}, {200, 5.00}, {300, 6.50}, {Double.MAX_VALUE, 8.00}}`. A loop computes each slab slice against the remaining units consumed.

8. **Q: How does the system handle partial bill payments?**  
   **A:** Each payment adds to `amountPaid` on the `Bill` instance. The remaining balance is `billAmount - amountPaid`. The status remains `UNPAID` until `pendingAmount <= 0.001`.

9. **Q: Are payment receipts deleted once a bill is marked PAID?**  
   **A:** No. Every payment creates an independent `Payment` entity permanently appended to `LinkedList<Payment>` for accounting and audit integrity.

10. **Q: What custom exceptions exist and why are they needed?**  
    **A:** Five custom exceptions: `InvalidConsumerException`, `InvalidMeterReadingException`, `InvalidPaymentException`, `ConsumerNotFoundException`, and `DuplicateConsumerException`. They provide precise error classification instead of vague runtime failures.

11. **Q: How are duplicate consumer IDs prevented?**  
    **A:** `ConsumerService.validateConsumer()` checks `store.consumerExists(id)` (a `HashMap.containsKey()` check) before registration and throws `DuplicateConsumerException` if true.

12. **Q: What validation rules protect the phone number?**  
    **A:** A regular expression check `phone.matches("\\d{10}")` ensures the phone string contains exactly 10 numeric digits.

13. **Q: How is invalid meter reading entry caught?**  
    **A:** Input values are parsed and verified: readings cannot be negative, and `currentReading < previousReading` triggers an `InvalidMeterReadingException`.

14. **Q: How is case-insensitive consumer name search implemented?**  
    **A:** Iterating through `store.getAllConsumers()` and filtering with `c.getName().toLowerCase().contains(query.toLowerCase())`.

15. **Q: How is sorting by consumer bill amount performed?**  
    **A:** Using `list.sort((a, b) -> Double.compare(totalB, totalA))`, where totals are aggregated from all bills associated with consumer `a` and `b`.

16. **Q: How does the Dashboard reflect real-time updates?**  
    **A:** All panels reference a shared singleton `ConsumerStore`. Whenever a CRUD or payment event occurs, calling `app.refreshDashboard()` recalculates totals from active records.

17. **Q: How is division by zero prevented when calculating Collection Rate?**  
    **A:** `ConsumerStore.getCollectionRate()` checks if `totalBilled == 0` and immediately returns `0.0` before performing division.

18. **Q: What role does Encapsulation play in the Model classes?**  
    **A:** All member attributes (`consumerId`, `amount`, `status`, etc.) are declared `private` and accessed only via explicit public getters and setters.

19. **Q: How was the modern rounded card aesthetic achieved without external JARs?**  
    **A:** Through `RoundedPanel.java`, a custom subclass of `JPanel` that overrides `paintComponent()` to draw smooth anti-aliased rounded rectangles using `Graphics2D`.

20. **Q: How does the application prevent accidental window closure?**  
    **A:** `setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE)` combined with a `WindowListener` prompts the user with a `JOptionPane.showConfirmDialog()` before exiting.

---

## 8. Requirements Verification & Audit Table

| Case Study Requirement | Implemented? | Source Class | Implementation Detail |
|---|:---:|---|---|
| Consumer Management | ✅ | `ConsumerPanel.java`, `ConsumerService.java` | Full CRUD operations with regex validation |
| Meter Readings | ✅ | `MeterPanel.java`, `BillingService.java` | Previous vs. current readings tracking |
| Consumption Calculation | ✅ | `Meter.java` | `getUnitsConsumed()` calculating current - previous |
| Progressive Tariff Slabs | ✅ | `BillingService.java` | `double[][] tariffSlabs` array traversed in loop |
| Fixed Meter Charge | ✅ | `BillingService.java` | Static ₹100.00 surcharge added to all bills |
| Automated Bill Generation | ✅ | `BillPanel.java`, `BillingService.java` | Unique ID generation, due date setting, breakdown string |
| Payment Management | ✅ | `PaymentPanel.java`, `PaymentService.java` | Full & partial payments, amount validation |
| Payment History | ✅ | `PaymentPanel.java`, `ConsumerStore.java` | Permanent `LinkedList<Payment>` audit ledger |
| Consumer Search | ✅ | `SearchSortPanel.java`, `ConsumerService.java` | $O(1)$ ID search & partial substring name search |
| Multi-Criterion Sorting | ✅ | `SearchSortPanel.java`, `ConsumerService.java` | `Comparator` sorting by Name, Bill Amount & TreeMap |
| Analytical Reports | ✅ | `ReportPanel.java` | Live financial summary and per-consumer ledger |
| Array (`double[][]`) | ✅ | `BillingService.java` | Used for slab rates and thresholds |
| `ArrayList<Consumer>` | ✅ | `ConsumerStore.java` | Stores primary consumer collection |
| `LinkedList<Bill>` | ✅ | `ConsumerStore.java` | Stores chronological bill sequence |
| `LinkedList<Payment>` | ✅ | `ConsumerStore.java` | Stores permanent payment ledger |
| `HashMap<String, Consumer>` | ✅ | `ConsumerStore.java` | Fast $O(1)$ consumer ID index |
| `TreeMap<String, Consumer>` | ✅ | `ConsumerStore.java` | Natural sorted consumer ID tree |
| Custom Exceptions | ✅ | `exception/*.java` (5 classes) | Thrown on business logic/validation failures |
| User Input Validation | ✅ | Service and Model layers | Pattern matching, range checking, existence checks |
| Java Swing GUI | ✅ | `gui/*.java` (9 classes) | Single-frame `CardLayout` dashboard |
| Thread Safety | ✅ | `Main.java` | Launched on Swing EDT via `SwingUtilities.invokeLater` |
| Zero External Dependencies | ✅ | Entire Codebase | Pure Standard Java SE 8+ |
