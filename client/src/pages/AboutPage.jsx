export default function AboutPage() {
    return (
        <div>
            <h1>About This App</h1>
            <p>
                A decoupled full-stack practice application that mimics a production
                corporate environment. It combines a React client, a Spring Boot REST
                API service, and an Nginx gateway, each running in its own Docker
                container behind a single port.
            </p>

            <h2>Architecture</h2>
            <img src="/architecture-diagram.svg" alt="Application architecture diagram" style={{ maxWidth: '100%' }} />
            <ul>
                <li>
                    <strong>Gateway (Nginx):</strong> The single entry point for the
                    browser. Forwards <code>/api/*</code> traffic to the backend, and
                    routes all other requests to the React dev server.
                </li>
                <li>
                    <strong>Client (React + Vite):</strong> The frontend UI. Talks to
                    the backend using relative paths like{' '}
                    <code>/api/subscriptions</code>, so there's no need for CORS
                    configuration or hardcoded backend URLs.
                </li>
                <li>
                    <strong>Backend (Spring Boot + Gradle):</strong> Exposes a full
                    CRUD REST API under <code>/api/subscriptions</code>, backed by a
                    thread-safe in-memory store.
                </li>
            </ul>

            <h2>API Endpoints</h2>
            <table>
                <thead>
                    <tr>
                        <th>Method</th>
                        <th>Path</th>
                        <th>Description</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>POST</td>
                        <td>/api/subscriptions</td>
                        <td>Creates a new subscription.</td>
                    </tr>
                    <tr>
                        <td>GET</td>
                        <td>/api/subscriptions</td>
                        <td>Returns all subscriptions as a list of summary DTOs.</td>
                    </tr>
                    <tr>
                        <td>PUT</td>
                        <td>/api/subscriptions/{'{id}'}</td>
                        <td>Updates a subscription's email.</td>
                    </tr>
                    <tr>
                        <td>DELETE</td>
                        <td>/api/subscriptions/{'{id}'}</td>
                        <td>
                            Deletes a subscription. Idempotent — deleting a
                            non-existent id still returns success.
                        </td>
                    </tr>
                </tbody>
            </table>

            <h2>Error Handling</h2>
            <p>
                Business-rule failures are modeled as unchecked exceptions rather
                than status strings embedded in a response body, so the HTTP layer
                and the business logic stay cleanly separated. A centralized{' '}
                <code>GlobalExceptionHandler</code> maps each exception type to its
                HTTP response, keeping controllers free of try/catch blocks.
            </p>
            <table>
                <thead>
                    <tr>
                        <th>Failure</th>
                        <th>Exception</th>
                        <th>HTTP Status</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Malformed or missing email</td>
                        <td>InvalidEmailException</td>
                        <td>400 Bad Request</td>
                    </tr>
                    <tr>
                        <td>Email already subscribed</td>
                        <td>DuplicateException</td>
                        <td>409 Conflict</td>
                    </tr>
                    <tr>
                        <td>Subscription id does not exist (update)</td>
                        <td>NotFoundException</td>
                        <td>404 Not Found</td>
                    </tr>
                </tbody>
            </table>

            <h2>Testing</h2>
            <p>
                The backend runs automated checks at two layers using JUnit 5,
                Mockito, and Spring Boot Test Starters, covering the full CRUD
                surface:
            </p>
            <ul>
                <li>
                    <strong>Service layer:</strong> validates business rules directly
                    against the in-memory store — email format validation,
                    case-insensitive duplicate detection, thread-safe ID assignment,
                    not-found handling, no-op update detection, and that a deleted
                    subscription's email correctly becomes available for reuse.
                </li>
                <li>
                    <strong>Controller layer:</strong> uses <code>@WebMvcTest</code>{' '}
                    with a mocked service to verify HTTP routing, request/response
                    JSON shape, and that each exception type is translated into the
                    correct status code.
                </li>
            </ul>
            <p>
                A GitHub Actions workflow runs the full backend test suite on every
                push and pull request, so regressions are caught before merge.
            </p>
        </div>
    );
}