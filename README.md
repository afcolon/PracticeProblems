# Full-Stack Microservice Landing Page

A decoupled full-stack practice application that mimics a production corporate environment. It combines a modern **React client**, a robust **Spring Boot REST API service**, and an **Nginx Gateway** proxy running entirely within independent Docker containers on a single port.

## 🏗️ Architecture Overview

Unlike a standard monolith, this application runs as a distributed network of decoupled services managed by an API Gateway.

```text
               ┌─── Local Browser (Accesses everything via Port 8080)
               │
               ▼
┌──────────────────────────────┐
│       Nginx API Gateway      │ (Listens on Port 8080)
└──────────────┬───────────────┘
               │
      ┌────────┴────────────────┐
      ▼                         ▼
┌───────────┐             ┌───────────┐
│ React UI  │             │ Java API  │
│ (Client)  │             │ (Backend) │
│ Port 5173 │             │ Port 8081 │
└───────────┘             └───────────┘
```

*   **`gateway` (Nginx)**: The single entry point for the browser. It runs on port `8080`, reading incoming request paths. It forwards `/api/*` traffic to the backend, and routes all other requests to the React dev server.
*   **`client` (React + Vite + pnpm 11)**: The frontend user interface. It communicates using relative routing (`/api/leads`), removing the need to configure CORS or manage hardcoded backend URLs.
*   **`backend-service` (Spring Boot + Gradle)**: The independent data processor. It exposes a single REST endpoint (`/api/leads`) to process and display incoming lead submissions.

---

## 🛠️ Project Structure

```text
microservice-practice/
├── gateway/
│   └── nginx.conf            # Nginx proxy mapping rules
├── backend-service/
│   ├── src/main/java/...     # Spring Boot Java source files
│   ├── build.gradle          # Gradle project configuration
│   └── settings.gradle       # Gradle root module definition
├── client/
│   ├── src/                  # React components (App.jsx, main.jsx)
│   ├── index.html            # Vite entry point layout
│   ├── package.json          # Node dependency tracking
│   └── pnpm-workspace.yaml   # pnpm 11 build script permissions
├── docker-compose.yml        # Main multi-container orchestrator
└── README.md                 # Project documentation
```

---

## 🚀 Getting Started

### Prerequisites
Make sure you have downloaded and launched the following tool on your local machine:
*   [Docker Desktop](https://docker.com) (Ensure the Docker engine is running in your background system tray).

### Step 1: Clean Local Build Folders
To prevent file-locking permissions or cache conflicts between your local OS and the isolated Linux containers, wipe out any residual build garbage files from your workspace directory:
```bash
rm -rf backend-service/.gradle backend-service/build client/node_modules
```

### Step 2: Spin Up the Stack
Open your terminal in the root `microservice-practice/` directory and execute:
```bash
docker compose up
```

*Note: The very first launch will take a couple of minutes because Docker needs to fetch the framework layers and download the Spring/Node library packages securely into its sandbox container volumes.*

---

## 💻 How to Verify and Test

1.  **Open the App**: Once the terminal output logs settle and show that the Vite dev server and Spring Boot app are successfully started, open your web browser and go to:
    👉 **`http://localhost:8080`**
2.  **Submit a Lead**: Type an email address into the input field on the page and click the submit button.
3.  **Trace the Logs**: Look back at your VS Code integrated terminal logs. You will see the network packet flow cleanly past the Nginx gateway and safely print inside the `backend-service` logs:
    `microservice-practice-backend-service-1 | Microservice received lead: test@example.com`

---

## 🛑 Troubleshooting

*   **502 Bad Gateway Error**: This means Nginx is ready, but the upstream application servers are still warming up or downloading dependencies. Give the terminal **1-2 minutes** to finish initializing the Gradle build, then refresh your browser page.
*   **Wiping the State completely**: If your environment state ever becomes corrupted due to code typos or interrupted package updates, run this command string to purge everything and run a fresh environment network boot:
    ```bash
    docker compose down -v
    docker compose up --build
    ```
