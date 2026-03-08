# Loom ETL Uploader

## Descripción

**Loom ETL Uploader** es una aplicación ETL (Extract, Transform, Load) desarrollada en Java que utiliza **Project Loom**
para aprovechar las capacidades de hilos virtuales de Java. El proyecto está diseñado para procesar y cargar datos de
manera eficiente y escalable.

## Tecnologías

- **Java 25** con soporte de Project Loom (Virtual Threads)
- **Spring MVC** - Framework para desarrollo de aplicaciones web
- **Jakarta EE** - APIs empresariales Java
- **Lombok** - Reducción de código boilerplate
- **Maven** - Gestión de dependencias y construcción
- **LocalStack** - Simulación de servicios AWS para pruebas
- **Spock Framework** - Testing con Groovy (Contract Testing)

## Características Principales

### Virtual Threads (Project Loom)

Este proyecto aprovecha los **Virtual Threads** de Java 25 para:

- Procesar grandes volúmenes de datos de forma concurrente
- Mejorar la escalabilidad sin el overhead tradicional de threads del sistema operativo
- Optimizar operaciones I/O intensivas

### Pipeline ETL

- **Extract**: Extracción de datos desde múltiples fuentes
- **Transform**: Transformación y validación de datos
- **Load**: Carga de datos procesados a destinos configurados

### Testing Avanzado

- **Integration Tests** con LocalStack para simular servicios AWS
- **Contract Testing** con Groovy para validación de contratos (Weather API)
- Utilities de testing reutilizables

## Requisitos Previos

- **Java 25** o superior
- **Maven 3.8+**
- **Docker** (opcional, para LocalStack en pruebas)

## Instalación

1. **Clonar el repositorio**

```bash 
git clone https://github.com/davinchicoder/loom-etl-uploader.git 
cd loom-etl-uploader
``` 

2. **Compilar el proyecto**

```bash 
./mvnw clean install
```

3. **Ejecutar la aplicación**

```bash 
./mvnw spring-boot:run
``` 

## Configuración

La configuración de la aplicación se encuentra en:

- `src/main/resources/application.yml` - Configuración principal
- `src/test/resources/application.yml` - Configuración de pruebas