# Web Development
This repository showcases completed web development projects, highlighting proficiency in diverse skills such as HTML, database management, cloud integration, distributed systems, web scraping, and various web models. 

Coding Language: **Java**


## Project Details
**Project1Task1**: Developed a website to compute MD-5 and SHA-256 hashes of user inputs.

(./image/Project1Task1.png)

**Project1Task2**: Developed a class clicker enabling students to make single-answer choices during class quizzes. Additionally, implemented a result display page that shows the count of each answer selected.

**Project1Task3**: Developed a baseball information board that dynamically showcases player statistics and game schedules. Utilized data retrieval techniques, including interfacing with a third-party API and implementing web scraping to aggregate information.

**Project2Task0**: This program implements a UDP Echo Client and a UDP Echo Server. The client sends user-entered messages to a UDP Echo Server running on the same machine ("localhost"). The client can send messages to the server, and the server echoes them back.

**Project2Task1**: This program simulates an Eavesdropper UDP application. It listens on a specified port, intercepts messages from clients, and forwards them to a target server while altering specific content in the messages(changing like in string to dislike). The eavesdropper then captures and echoes the server's responses back to the original client.

**Project2Task2**: This program implements a basic UDP Adding Client and UDP Adding Server. The client allows users to enter integer values that are sent to a remote Adding Server. The client receives the server's response, which is typically the result of adding the sent value to a running total on the server. The client can continue to send values until the user enters "halt!" to terminate the client. The server listens on a specified port, receives integer values from clients, adds them to a running total, and returns the updated total to the clients. The server continues to run indefinitely, processing client requests.

**Project2Task3**: This program implements a basic UDP Remote Variable Client and a basic UDP Remote Variable Server. The client allows users to interact with a remote variable server by performing operations such as addition, subtraction, and retrieval of variable values associated with unique IDs. Users can select operations and provide necessary input values through a menu interface. The server listens on a specified port and handles requests from clients to perform various operations on remote variables identified by unique IDs. Supported operations include addition, subtraction, and retrieval of variable values. The server maintains a collection of variables and their current values using a TreeMap.

**Project2Task4**: This program implements a basic TCP Remote Variable Client and a basic TCP Remote Variable Server similar to Project2Task3.

**Project2Task5**: The task adds a identity verification step to Project2Task5. The client application communicates securely with a remote server using the RSA digital signature. It allows the user to perform mathematical operations (addition, subtraction and get) on a remote variable stored on the server, ensuring data integrity and security through digital signatures. The client generates RSA key pairs, signs messages, and sends them to the server for processing. The server validates the signatures and performs the requested operations on the remote variable. The server receives and verifies client requests with digital signatures before processing them. This server listens on a specified port for incoming client requests, verifies the digital signatures on these requests, and processes them if they are valid.

