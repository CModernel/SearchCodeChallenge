# Android Search Challenge - Clean Architecture

This project demonstrates an Android Search Functionality using **Clean Architecture** principles, with a focus on **Flow** and **View State Management**. 
The application is designed as a multi-module structure with separate app, data, and domain modules.

The project includes mock data for the search feature, avoiding the need for network requests. It provides a simple, user-friendly search experience and showcases best practices in Android development, including **Jetpack Compose** for the UI, **Coil** for image rendering, **ViewModels**, **UseCases**, and **Repository** patterns.

## Project Architecture

The project is structured around a multi-module **Clean Architecture** design, which separates the app into different layers to promote scalability, testability, and maintainability. 

<img src="/app/images/Sonder Search App.gif" width="30%" />

The layers are:
	
-	**App Module**: Manages the UI components (Activities, Fragments) and handles user interactions. It communicates with ViewModels to display data. The UI is implemented using Jetpack Compose, which simplifies building dynamic UI components in a declarative way.
-	**Domain Module**: Contains the core business logic. This layer is independent of frameworks and holds:
    -	UseCases: Define the actions that can be performed in the app.
    -	Repository Interfaces: Abstract definitions for data handling, with implementations in the Data module.
-	**Data Module**: Provides the data to the app. It includes:
    -	Repository Implementations: Actual implementations of the repository interfaces.
    -	Mock Data: Simulates search results and error scenarios.
-	**Common Module**: Holds shared utilities and components used across the app.

> [!NOTE]
> Unit tests were implemented for the ViewModels, Repositories and Use-Cases. This ensures the correct interactions between the data layer and UI logic. 

## Screens and Flow
The project uses predefined mock data to simulate different scenarios during the search functionality. The app’s flow adapts based on user input and search results:

1. **Initial screen**:
	- The app starts with the prompt: “Search our comprehensive library of resources.”
	- As users enter queries, the UI updates dynamically with feedback like: “Searching for ‘{query}’” and displays a progress indicator during the simulated data fetch.
2. **Search Results**:
	- The query "mock" simulates a success scenario, returning results based on mock data. Each section title and layout adapts to the data provided, with results modeled by SearchSectionResult objects.
 	- Images within the search results are loaded efficiently using Coil to enhance the performance of the app.
3. **Error State**:
	- The query "error" triggers the error scenario, where no results are found. The error state is also simulated through mock data.

## How to Run the Project

This project is compatible with Android Studio Koala | 2024.1.2 Patch 1.

To run the project:

	1.	Clone the repository to your local machine.
	2.	Open the project in Android Studio.
	3.	Build and run the app on an emulator or physical device.
	4.	The search functionality will be pre-configured with mock data for testing.

## Git Workflow

The project follows a Git feature-branch workflow. Changes are committed in separate branches (for individual tasks) and merged into the main branch upon completion. 
