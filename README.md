# GithubRepositoryExplorer
MVVM Architecture based Android app using Retrofit, Room, Dagger2, LiveData with infinite scrolling experience.

![Gif](https://media.giphy.com/media/Zvx7P7h1WI0umth8uS/giphy.gif)

The project written in Kotlin, which list all repositories of a user or organization in GitHub via [GitHub API](https://developer.github.com/v3/). Shows all stargazers (people who starred the repository) of the repository when repository is clicked. Currently default repository is [Square's Repository.](https://github.com/square)

The app uses the MVVM pattern. repositories can be bookmarked and are then stored locally with [Room](https://developer.android.com/topic/libraries/architecture/room). [Retrofit](https://github.com/square/retrofit) is used to fetch the repositories and it's details from the API. [Dagger 2](https://github.com/google/dagger) is used for dependency injection.

This project can also be used as a template for new apps. 

### TODO
* Use Paging library instead of [InfiniteScrollListener.kt](https://github.com/Swisyn/GithubRepositoryExplorer/blob/master/app/src/main/java/com/cuneytayyildiz/githubrepositoryexplorer/utils/InfiniteScrollListener.kt)
* Bug fixes
* Move out some logic from activities

All pull requests are welcome.
