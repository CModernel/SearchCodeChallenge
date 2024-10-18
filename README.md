# Android Search Challenge - Clean Architecture

This project demonstrates an Android Search Functionality using Clean Architecture principles, with a focus on Flow and View State Management. The application is designed as a multi-module structure with separate app, data, and common modules. The project includes mock data for the search feature, avoiding the need for network requests. It provides a simple, user-friendly search experience and showcases best practices in Android development, including ViewModels, UseCases, and Repository patterns.

## Project Architecture

The project is structured around a multi-module Clean Architecture design, which separates the app into different layers to promote scalability, testability, and maintainability. 

The layers are:
	
-	App Module: Manages the UI components (Activities, Fragments) and handles user interactions. It communicates with ViewModels to display data.
-	Domain Module: Contains the core business logic. This layer is independent of frameworks and holds:
    -	UseCases: Define the actions that can be performed in the app.
    -	Repository Interfaces: Abstract definitions for data handling, with implementations in the Data module.
-	Data Module: Provides the data to the app. It includes:
    -	Repository Implementations: Actual implementations of the repository interfaces.
    -	Mock Data: Simulates search results and error scenarios.
-	Common Module: Holds shared utilities and components used across the app.

> [!NOTE]
> Unit tests were implemented for the ViewModels, Repositories and Use-Cases. This ensures the correct interactions between the data layer and UI logic. 

## Mock Data

The project uses predefined mock data to simulate various scenarios
- Success Scenario: The search query "mock" returns mock results.
-	Error Scenario: The search query "error" simulates an error message.
 
Mock request objects are structured as follows:
  ```
  data class SearchRequestParams(
    val sectionTitle: String,
    val query: String,
    val size: Int = 10,
    val contentTypes: List<String>
  )
  ```
Results are modeled by SearchSectionResult objects, each containing a list of SearchItem objects, which include properties such as title, description, contentType, and action.

## Screens and Flow

1.	Search State:
    -	Initial screen displays a prompt: “Search our comprehensive library of resources.”
    -	As users enter queries, the UI updates with real-time feedback such as: “Searching for ‘{query}’”.
2.	Result Display:
    -	The app displays different sections based on the search query. These sections can include various types of search results and are arranged in a specified chronological order.
    -	Titles and views dynamically adapt based on the search outcome.
3.	Error State:
    -	If no results are found for a query or the query "error" is entered, the app will display: “We apologize, but it seems like we’re facing a temporary hiccup loading search results.”

## How to Run the Project

This project is compatible with Android Studio Koala | 2024.1.2 Patch 1.

To run the project:

	1.	Clone the repository to your local machine.
	2.	Open the project in Android Studio.
	3.	Build and run the app on an emulator or physical device.
	4.	The search functionality will be pre-configured with mock data for testing.

## Git Workflow

The project follows a Git feature-branch workflow. Changes are committed in separate branches (for individual tasks) and merged into the main branch upon completion. 
