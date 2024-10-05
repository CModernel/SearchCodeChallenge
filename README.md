**Sonder Code Challenge**

The task is to create a Search functionality. The project is setup in a multi module Clean architecture with app, data and common modules. We want to test your knowledge of Flow and View State Management. That's why mock data is provided in the data module, there are no network requests required.

Basic UI setup is already provided. There is a `MainActivity` and `MainFragment`. Layouts are added in XML. You can choose to use Jetpack Compose and ask for extra time, please let us know if you choose to do that. Layouts already contain some UI setup to get you started: SearchView with a Toolbar, title TextView, ProgressBar, RecyclerViews and the item layouts for each type.

We have also added a SearchRepository interface, a few UseCases and implemented them in the ViewModels. You will need to implement the SearchRepository. You can change it however you like. Data exchange between Repository and  ViewModels should only go via UseCases. Add unit tests for the ViewModels.

If you find any improvement areas in the existing code, feel free to do that. If you choose to do that please add a comment to the code for us to review what was changed.

We are providing a zip of the project. Please upload it to your own repository on your favourite platform (Github, Bitbucket etc.) and commit your changes there. When all the work is done share the repo link with us. Please use git features like branching and merging. For example you can create task branches and merge them to main branch as you go, instead of directly committing everything to main. 

Project should compile on Android Studio Koala | 2024.1.2 Patch 1.

Expected behaviour:
- We have provided a video of this Search functionality from our Sonder app and screenshots of results view for your reference. You will get the idea of the UI and functionality from the video.
- When the app opens, title should say "Search our comprehensive library of resources".
- The search query for this test is "mock". You will see that this query is hardcoded in the request objects. So searching for "mock" should load the results. There is another query "error" to simulate the error scenario.
- When user searches, title should change to "Searching for ‘{search query}‘" and progress bar should be visible. Since there are no network requests, you can simulate the a network call with a delay of 2 seconds.
- All the required requests added to the `MockRequests` file. All the required results are added to the `MockResults` file. 
- Following is the request object. `size` is the maximum number of items to show in the respective section. `contentTypes` requests only certain types of items.
  ```
  data class SearchRequestParams(
    val sectionTitle: String,
    val query: String,
    val size: Int = 10,
    val contentTypes: List<String>
  )
  ```
- The results are a nested list of fragments, each fragment is a section and each section is a list of items of the same view. `addFragments()` function will help you add thez fragments. There are a total of 4 types of views for these sections.
- The sections should be loaded in the following chronological order: Horizontal Compact, Vertical Compact, Horizontal Detailed, Vertical Detailed. We have provided layouts for each of these list items and screenshots of each view which data it is supposed to show. The mock data functions names are also related to each view. For example `getHorizontalCompactRequestParams()` and `getHorizontalCompactSearchResults()` are requests and results for Horizontal Compact view.
- Title should change to "Showing results for ‘{search query}‘ and progress bar should disappear. You can match which section goes where based on the section title. This is the result object of a single section:
  ```
  data class SearchSectionResult(
    val sectionTitle: String,
    val items: List<SearchItem>
  )
  ```
- This is the object of a single item in a section.
  ```
  data class SearchItem(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val contentType: SearchContentType? = null,
    val thumbnail: String? = null,
    val action: SearchAction? = null
  )
  ```
- Each item in a search section has a unique click action as defined in the `SearchAction` object. `scheme` is what should execute when user clicks on the item. `title` is the title of the item. `type` is a way to determine how 
  the scheme should be handled. For this test you can keep it simple by doing the following: If `scheme` is an URL open it in the browser or a WebView. If `scheme` is just a text, show a toast message showing the `scheme` data.
  ```
  data class SearchAction(
    val scheme: String,
    val title: String? = null,
    val type: SearchActionType
  )
  ```
- `contentType` is the `SearchContentType` enum which defines what type of item it is. Use this to filter out unwanted items from a section. The section should only show items that are accepted by it.
- If a section does not have any data then that section should be hidden. Example of this in the mock data is the Vertical Compact section results `getCategoriesSectionSearchResults()`. So Vertical Compact section should never be visible since in the results it always returns empty list, but the request should still be made.
- If all the sections return empty result then show error scenario: Title changes to "We apologise, but it seems like we’re facing a temporary hiccup loading search results", progress bar is invisilble. In mock data there is `getErrorRequestParams()` and `getErrorResult()` to simulate error scenario. Using these, if the search query is "error" then it should produce the error scenario.
- If user clears the search query by tapping the X button in the `SearchView` or by erasing the query with the keyboard then the UI state should go back to Started state and ready for new query i.e. the previous search results should be cleared, the UI should look like the Started state and user can search again.
- Please handle any edge cases you might think of. For example what happens to the results if user puts the app in background and comes back.

