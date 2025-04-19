# 💰 Expense Tracker

**Expense Tracker** is a full-stack application that helps users manage their personal finances efficiently. It includes a backend built with **Node.js/Express.js** and a mobile app developed in **Java (Android)**.

---

## 🛠 Tech Stack

### Backend
- Node.js
- Express.js
- MongoDB
- JWT Authentication
- Firebase
- RESTful APIs

### Mobile App (Android)
- Java
- Retrofit (for API communication)
- SQLite (for local caching)
- Material Design Components

---

## ✨ Key Features

### 📱 Mobile App
- Add income and expense records with category, date, and note.
- View financial summaries by day, week, or month.
- Visualize spending via pie chart or bar chart.
- Shared fund with other people.
- Edit/Delete transactions.

### 🔧 Backend API
- User authentication (JWT)
- CRUD APIs for income and expenses
- Endpoints for filtered queries (e.g., by date, category)
- Data validation and error handling
- Firebase for storged images
- MongoDB integration

---

## 🚀 Getting Started

### 📦 Backend Installation

```bash
git clone https://github.com/DatTranDev/ExpenseTrackerBE.git
cd ExpenseTrackerBE
npm install
```

Create a `.env` file:

Run the backend:

```bash
npm start
```

### 🤖 Android App

1. Clone the mobile repository:
```bash
git clone https://github.com/DatTranDev/ExpenseTracker.git
```
2. Open the project in **Android Studio**
3. Set the API base URL in Retrofit client to match your backend
4. Build and run the app on emulator or real device

---

## 📸 Screenshots
![Shared fund](https://drive.google.com/file/d/11Mw-L9nXfrzijsGtdzJPCrDLVb_1y18Q/view?usp=sharing)


---

## ⚠️ Note

> The backend server may take a few seconds to wake up due to free hosting limitations. Please be patient when logging in or syncing data.

---
