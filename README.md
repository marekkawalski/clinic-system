# Clinic System

## About

Clinic System is a backend for an application designed to manage a small medical clinic. It is built using Java and
Spring Boot, and it leverages MongoDB for data storage. The project was configured to
use docker compose to automatically set up the MongoDB database.
The project can be run locally or deployed to a Kubernetes cluster.
For deployment on Kubernetes cluster look at the k8s directory.
The system supports user roles such as admin, doctor, patient, and registrar, and it includes features for
managing appointments and user data.
This project can be used as backend for one of the frontend projects:

- [Clinic System Angular](https://github.com/marekkawalski/clinic-system-angular)
- [Clinic System React](https://github.com/marekkawalski/clinic-system-react)
- [Clinic System Vue](https://github.com/marekkawalski/clinic-system-vue)

# Table of Contents

<!-- TOC -->

* [Clinic System](#clinic-system)
    * [About](#about)
* [Table of Contents](#table-of-contents)
* [Prerequisites](#prerequisites)
* [Setup](#setup)
* [Running the Project](#running-the-project)

<!-- TOC -->

# Prerequisites

To run this project, you will need to have the following installed:

- Java 21
- Docker deamon, example: [Docker Desktop](https://www.docker.com/products/docker-desktop)

# Setup

1. Clone the repository

    ```bash
    git clone https://github.com/marekkawalski/clinic-system
    ```

2. Navigate to the project directory

    ```bash
    cd clinic-system
    ```
3. Add necessary permissions to the gradlew file

    ```bash
    chmod +x gradlew
    ```

# Running the Project

1. Run the following command to start the application:

```bash
./gradlew bootRun
```