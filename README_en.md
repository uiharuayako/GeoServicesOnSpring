# Spatial Information Sharing Service System

[中文](README.md) | [English](README_en.md)  

This repository contains the source code for a spatial information sharing service system based on Spring Boot 3.0. The system is designed to address the needs of the spatial information sharing domain, providing efficient and stable data querying, uploading, and sharing.

## Features

- Server-side architecture using Spring Boot as the foundational framework
- Data storage using file systems and PostGIS databases
- Performance optimization with Redis caching technology
- Vector data format conversion using GeoTools library
- Containerized deployment for simplified deployment process and enhanced portability
- Accessing and caching vector data in the SHP format

## Installation and Setup

1. Clone the repository:
```
git clone https://github.com/uiharuayako/GeoServicesOnSpring.git
cd GeoServicesOnSpring
```
2. Install dependencies:
```
./gradlew build
```
3. Start the services using Docker:
```
docker-compose up -d
```
4. Access the API documentation at `http://localhost:8080/swagger-ui.html`

## Usage

Refer to the API documentation for details on how to interact with the system.

## Contributing

We welcome contributions! Please feel free to open issues or submit pull requests for improvements, bug fixes, or new features.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.