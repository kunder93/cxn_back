<h1>CHESS CLUB API</h1>
<h2>Description</h2>
<p>API for managing a chess club. It includes member registration, club administration processes among others.</p>
<h2>Getting started</h2>
<h3>Dependencies</h3>

- **Mandatory:**
  - Java 21
  - Maven 3.x
  - DB PostgreSQL 14.X or higher or other compatible DB.  
- **Optional:**  
  - Docker for run in container.
  - Mail server service for sending mails
    
#### 1. Downloading the Program

You have two main options to download the program:

- **Clone the Repository:**  
  Use Git to clone the repository to your local machine:
  ```bash
  git clone https://github.com/your-username/chess-club-api.git

- **Download as ZIP:**
  Alternatively, you can download the repository as a ZIP file directly from GitHub.
Click on the "Code" button at the top of the repository page and select Download ZIP. Then, unzip the file to your desired location.

#### 2. Modifications Needed
Before running the application, you may need to make a few adjustments:

 **Environment Configuration:**
     
  Set app profile: dev/devdocker/prod  see more about profiles in Dockerfile.
     
     - BUILD_PROFILE=prod     # Options: prod | dev | devdocker
    
     #Mail is OPTIONAL for all profiles: 
     - SPRING_MAIL_HOST=org.mailservice.com
     - SPRING_MAIL_PORT=587
     - SPRING_MAIL_USERNAME=mailUserName
     - SPRING_MAIL_PASSWORD=mailPassword
    
     #For devdocker/prod profile: 
     - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/databaseName
     - SPRING_DATASOURCE_USERNAME=databaseUsername
     - SPRING_DATASOURCE_PASSWORD=DatabasePassword
     #For prod profile:
     - SSL_CERTIFICATE_PATH=/path/certs/fullchain.pem
     - SSL_CERTIFICATE_PRIVATE_KEY_PATH=/path/certs/privkey.pem
     #For all profiles:
     - STORAGE_LOCATION_PATH=/location/storage/folder




#### 3. Executing program
To run the API, follow these steps:

- Using Maven:
  - Development mode:

  Run the application with:

        ```bash
         mvn spring-boot:run

  - Production mode:
 
    Package the application into an executable JAR:

     ```bash
        mvn clean package

   Then run the JAR file:


     ```bash
        java -jar target/chess-club-apiName.jar
     
- Using Docker (Optional)

   If you have Docker installed and a proper Dockerfile/docker-compose.yml configured, run:

      ```bash
          docker-compose up -d

#### 4. Authors:

[Kunder93 aka Santiago Paz](https://github.com/kunder93)


 
