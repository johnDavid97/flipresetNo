# Flip Reset - Rocket League Esports Data Platform

A full-stack application for tracking and displaying Rocket League Championship Series (RLCS) data, featuring real-time updates and comprehensive statistics. This project was only built to showcase backend experience.

---

## 🏗 Architecture

- **Frontend**: Next.js application
- **Backend**: Spring Boot Java application
- **Message Broker**: Apache Kafka
- **Database**: MongoDB
- **Infrastructure**: Docker Compose

---

## 📋 Table of Contents

- [About The Project](#about-the-project)
- [Built With](#built-with)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [Features](#features)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgments](#acknowledgments)

---

## 🛠 About The Project

This application tracks and displays Rocket League Championship Series (RLCS) data. Designed to demonstrate backend skills, the project incorporates a modern tech stack to deliver a simple and reliable forntend.

---

## 💻 Built With

- **Spring Boot**
- **Next.js**
- **Apache Kafka**
- **MongoDB**
- **Docker Compose**

---

## 🚀 Getting Started

### Prerequisites

- Docker and Docker Compose
- Java 21
- Node.js (LTS version)
- Maven
- MongoDB

### Installation

1. Clone the repository:

```bash
git clone https://github.com/johnDavid97/flipresetNo.git
cd flipresetNo
```

2. Start the infrastructure services:

```bash
docker-compose up -d
```

This will start Kafka, Zookeeper, and MongoDB services.

3. Start the backend:

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

4. Start the frontend:

```bash
cd frontend
npm install
npm run dev
```

5. Access the application at `http://localhost:3000`

---

## 📁 Project Structure

```
.
├── frontend/ # Next.js frontend application
├── backend/ # Spring Boot backend application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── flipreset/
│   │   │   │           ├── api/ # API related code
│   │   │   │           ├── models/ # Data models
│   │   │   │           ├── services/ # Business logic
│   │   │   │           └── kafka/ # Kafka producers/consumers
│   └── pom.xml
└── docker-compose.yml # Docker infrastructure setup
```

---

## 🔧 Configuration

### Backend Configuration

- MongoDB connection: `backend/src/main/java/com/flipreset/db/MongoDbConnectionFR.java`
- Kafka settings: Check the consumer and producer configurations in the Kafka package

### Frontend Configuration

- API endpoints: Configure in your Next.js environment files
- Environment variables: Create `.env` file based on `.env.example`

---

## 🌟 Features

- Match history
- Match filter
- Real-time RLCS match data

---

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## 🙏 Acknowledgments

- RLCS data providers
- The Rocket League community
- All contributors to this project
