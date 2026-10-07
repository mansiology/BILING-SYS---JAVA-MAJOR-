# ⚡ Electricity Billing Management System

A Java-based desktop application for managing electricity consumers, meter readings, billing, payments, searches, sorting, and financial reports.

The application is developed using **Java Swing** and the **Java Collections Framework** and follows a **3-Tier Architecture** consisting of the Presentation, Service, and Data Store layers.

---

## 📌 Project Overview

The **Electricity Billing Management System** automates the major operations involved in electricity billing management.

The system provides functionality for:

* Consumer registration and management
* Consumer CRUD operations
* Meter reading management
* Electricity consumption calculation
* Progressive slab-based tariff calculation
* Automatic bill generation
* Full and partial payment processing
* Payment history and audit tracking
* Consumer searching
* Consumer sorting
* Financial dashboard
* Operational reports
* Input validation
* Custom exception handling

The project is implemented as a **standalone in-memory desktop application**. No external SQL database or third-party libraries are required.

---

## 🎓 Academic Information

| Information           | Details                                                 |
| --------------------- | ------------------------------------------------------- |
| Course                | B.Tech Computer Science & Engineering                   |
| Semester              | III                                                     |
| Subject               | Java Programming & Object-Oriented Software Engineering |
| Application Type      | Desktop GUI Application                                 |
| Language              | Java                                                    |
| GUI Framework         | Java Swing                                              |
| Data Storage          | In-memory Java Collections                              |
| Architecture          | 3-Tier Architecture                                     |
| External Dependencies | None                                                    |

---

# ✨ Features

## 👤 Consumer Management

The system supports complete consumer record management.

### Operations

* Add consumer
* View consumer
* Update consumer
* Delete consumer
* Validate consumer information
* Prevent duplicate Consumer IDs

Consumer IDs are validated using the required pattern.

```text
[A-Za-z][0-9]{3,5}
```

Phone numbers are validated to contain exactly 10 digits.

```java
phone.matches("\\d{10}")
```

---

## ⚡ Meter Reading Management

The system records previous and current meter readings.

Electricity consumption is calculated using:

```text
Units Consumed = Current Reading - Previous Reading
```

The system prevents:

* Negative readings
* Current readings lower than previous readings

Invalid readings result in:

```text
InvalidMeterReadingException
```

---

# 💰 Electricity Billing

The application uses a progressive slab-based electricity tariff.

| Consumption           |       Rate |
| --------------------- | ---------: |
| 0–100 units           | ₹3.50/unit |
| 101–200 units         | ₹5.00/unit |
| 201–300 units         | ₹6.50/unit |
| Above 300 units       | ₹8.00/unit |
| Fixed Meter Surcharge |       ₹100 |

---

## 🧮 Example Billing Calculation

For a consumption of **350 units**:

### Slab 1

```text
100 × ₹3.50 = ₹350
```

### Slab 2

```text
100 × ₹5.00 = ₹500
```

### Slab 3

```text
100 × ₹6.50 = ₹650
```

### Slab 4

```text
50 × ₹8.00 = ₹400
```

### Fixed Charge

```text
₹100
```

### Total

```text
₹350 + ₹500 + ₹650 + ₹400 + ₹100
= ₹2,000
```

---

# 💳 Payment Management

The system supports multiple payment methods:

* Cash
* UPI
* Card
* Net Banking

It supports both:

* Full payments
* Partial payments

---

## Partial Payment Example

Suppose a bill is:

```text
Bill Amount = ₹1,500
```

The consumer first pays:

```text
₹500
```

The system records:

```text
Amount Paid = ₹500
Outstanding = ₹1,000
Status = UNPAID
```

The consumer later pays:

```text
₹1,000
```

The system updates:

```text
Amount Paid = ₹1,500
Outstanding = ₹0
Status = PAID
```

Every payment remains stored in the payment history.

---

# 🧾 Payment Audit Ledger

Payments are stored permanently in:

```java
LinkedList<Payment>
```

For example:

```text
PAY001 → ₹500 → UPI
PAY002 → ₹1000 → CASH
```

Payment records are not deleted when the bill becomes paid.

This provides:

* Transaction history
* Financial auditing
* Payment traceability
* Data integrity

---

# 🔎 Search

The system supports two main search methods.

## Exact Consumer ID Search

Consumer IDs are searched using:

```java
HashMap.get()
```

This provides an average lookup complexity of:

```text
O(1)
```

---

## Partial Name Search

The system supports substring searching.

Example:

```text
Search: "raj"
```

A name such as:

```text
Rajesh
```

can match.

The implementation uses:

```java
c.getName().toLowerCase()
    .contains(query.toLowerCase())
```

This makes the search case-insensitive.

---

# 🔃 Sorting

Consumers can be sorted using different criteria.

Supported sorting includes:

* Consumer name
* Bill amount
* Consumer ID

The project uses:

```java
Comparator
```

and:

```java
Double.compare()
```

for custom sorting.

Example:

```java
Comparator.comparing(Consumer::getName)
```

---

# 📊 Dashboard

The dashboard provides live financial information.

It calculates:

* Total billed amount
* Total collected amount
* Pending amount
* Collection rate

### Collection Rate

```text
Collection Rate =
(Total Collected / Total Billed) × 100
```

The system prevents division by zero by checking whether the total billed amount is zero before performing the calculation.

---

# 📈 Reports

The reporting section provides:

* Financial summaries
* Consumer-level balances
* Billing information
* Payment information
* Outstanding amounts

The reports are generated from the current in-memory application data.

---

# 🏗️ System Architecture

The project follows a **3-Tier Architecture**.

```text
┌─────────────────────────────────────────┐
│          PRESENTATION TIER              │
│                                         │
│             MainApp                     │
│          JFrame + CardLayout            │
│                                         │
│ DashboardPanel                          │
│ ConsumerPanel                           │
│ MeterPanel                              │
│ BillPanel                               │
│ PaymentPanel                            │
│ SearchSortPanel                         │
│ ReportPanel                             │
└───────────────────┬─────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────┐
│             SERVICE TIER                │
│                                         │
│ ConsumerService                         │
│ BillingService                          │
│ PaymentService                          │
└───────────────────┬─────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────┐
│           DATA STORE TIER               │
│                                         │
│ ConsumerStore                           │
│                                         │
│ ArrayList<Consumer>                     │
│ HashMap<String, Consumer>               │
│ TreeMap<String, Consumer>               │
│ LinkedList<Bill>                        │
│ LinkedList<Payment>                     │
│ HashMap<String, Meter>                  │
└─────────────────────────────────────────┘
```

---

# 🗂️ Data Structures Used

Different Java collections are intentionally used for different requirements.

| Data Structure              | Purpose                             |
| --------------------------- | ----------------------------------- |
| `double[][]`                | Stores tariff limits and rates      |
| `ArrayList<Consumer>`       | Stores consumers in insertion order |
| `HashMap<String, Consumer>` | Fast Consumer ID lookup             |
| `TreeMap<String, Consumer>` | Sorted Consumer ID storage          |
| `LinkedList<Bill>`          | Billing history                     |
| `LinkedList<Payment>`       | Payment audit ledger                |
| `HashMap<String, Meter>`    | Meter number to meter mapping       |

---

# 🧠 Why These Data Structures?

## Array

The tariff slab information is relatively fixed.

Therefore, a two-dimensional array is suitable.

```java
double[][] tariffSlabs
```

---

## ArrayList

Used for the main consumer collection because it:

* Dynamically grows
* Preserves insertion order
* Supports indexed access

---

## HashMap

Used for Consumer ID lookup.

Conceptually:

```text
Consumer ID → Consumer Object
```

Average lookup:

```text
O(1)
```

---

## TreeMap

Used when consumers need to remain ordered by Consumer ID.

TreeMap maintains keys in natural sorted order.

Typical operations:

```text
O(log n)
```

---

## LinkedList

Used for:

```java
LinkedList<Bill>
LinkedList<Payment>
```

because bills and payments form chronological transaction histories.

---

# 🧩 Object-Oriented Concepts Used

The project demonstrates the major OOP concepts.

## Encapsulation

Model attributes are kept private and accessed using getters and setters.

Example:

```java
private String consumerId;
```

---

## Inheritance

Inheritance allows a class to reuse properties and methods from another class.

---

## Polymorphism

Polymorphism allows the same interface or method structure to have different implementations.

---

## Abstraction

Abstraction hides unnecessary implementation details and exposes only required functionality.

---

# ⚠️ Exception Handling

The project uses custom exceptions for business-rule violations.

The five major custom exceptions are:

```text
InvalidConsumerException
InvalidMeterReadingException
InvalidPaymentException
ConsumerNotFoundException
DuplicateConsumerException
```

---

## InvalidConsumerException

Used when consumer information fails validation.

---

## InvalidMeterReadingException

Used when meter readings are invalid.

Examples:

* Negative reading
* Current reading less than previous reading

---

## InvalidPaymentException

Used when payment information is invalid.

For example:

```text
Payment > Outstanding Balance
```

---

## ConsumerNotFoundException

Used when the requested Consumer ID does not exist.

---

## DuplicateConsumerException

Used when a Consumer ID already exists.

---

# 🖥️ GUI Technology

The application uses **Java Swing**.

Important components include:

```text
JFrame
JPanel
JButton
JLabel
JTextField
JTable
JOptionPane
JComboBox
CardLayout
```

---

# 🪟 Single Window Design

The application uses one main:

```java
JFrame
```

and switches between different panels using:

```java
CardLayout
```

This avoids opening multiple windows for every operation.

---

# 🧵 Event Dispatch Thread

Swing GUI operations are performed on the Event Dispatch Thread.

The application uses:

```java
SwingUtilities.invokeLater()
```

to start the GUI safely.

Example:

```java
SwingUtilities.invokeLater(() -> {
    new MainApp();
});
```

This is important because Swing components are not thread-safe.

---

# 🎨 Custom GUI Design

The project includes a custom:

```text
RoundedPanel.java
```

The panel overrides:

```java
paintComponent()
```

and uses:

```java
Graphics2D
```

to draw rounded rectangles with anti-aliasing.

This creates a modern card-style appearance without external GUI libraries.

---

# 🔐 Data Validation and Integrity

The application protects data using several validation mechanisms.

### Consumer validation

```text
Consumer ID format
Phone number format
Required fields
```

### Meter validation

```text
Reading >= 0
Current Reading >= Previous Reading
```

### Payment validation

```text
Payment amount > 0
Payment amount <= Outstanding Balance
```

### Duplicate validation

```text
Consumer ID must be unique
```

---

# 🔄 Complete Application Workflow

```text
Application Launch
        ↓
Initialize ConsumerStore
        ↓
Load Sample Data
        ↓
Display Dashboard
        ↓
Register / Manage Consumer
        ↓
Enter Meter Reading
        ↓
Calculate Consumption
        ↓
Apply Tariff Slabs
        ↓
Generate Bill
        ↓
Process Payment
        ↓
Update Bill Status
        ↓
Store Payment in Ledger
        ↓
Refresh Dashboard
        ↓
Search / Sort / Generate Reports
```

---

# 📁 Project Structure

The project is organized into logical packages.

```text
src/
│
├── Main.java
│
├── gui/
│   ├── MainApp.java
│   ├── DashboardPanel.java
│   ├── ConsumerPanel.java
│   ├── MeterPanel.java
│   ├── BillPanel.java
│   ├── PaymentPanel.java
│   ├── SearchSortPanel.java
│   ├── ReportPanel.java
│   └── RoundedPanel.java
│
├── service/
│   ├── ConsumerService.java
│   ├── BillingService.java
│   └── PaymentService.java
│
├── datastructure/
│   └── ConsumerStore.java
│
├── mod
```
