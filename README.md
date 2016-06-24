# Project 2 - *Name of App Here*

**Name of your app** is an android app that allows a user to search for articles on web using simple filters. The app utilizes [New York Times Search API](http://developer.nytimes.com/docs/read/article_search_api_v2).

Time spent: **X** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **search for news article** by specifying a query and launching a search. Search displays a grid of image results from the New York Times Search API.
* [x] User can **scroll down to see more articles**. The maximum number of articles is limited by the API search.
* [x] User can tap on any image in results to see the full text of article **full-screen**

The following **optional** features are implemented:

* [x] Used the **ActionBar SearchView** or custom layout as the query box
* [x] User can **share an article link** to their friends or email it to themselves
* [ ] Improved the user interface and experiment with image assets and/or styling and coloring
* [x] User can click on "settings" which allows selection of **advanced search options** to filter results
  * [x] User can configure advanced search filters such as:
    * [x] Begin Date (using a date picker)
    * [x] News desk values (Arts, Fashion & Style, Sports)
    * [x] Sort order (oldest or newest)
  * [x] Subsequent searches have any selected filters applied to the results
* [x] Replaced Filter Settings Activity with a lightweight modal overlay
* [ ] Implements robust error handling, [check if internet is available](http://guides.codepath.com/android/Sending-and-Managing-Network-Requests#checking-for-network-connectivity), handle error cases, network failures
* [ ] Apply the popular [Butterknife annotation library](http://guides.codepath.com/android/Reducing-View-Boilerplate-with-Butterknife) to reduce view boilerplate.
* [x] Replace `GridView` with the [RecyclerView](http://guides.codepath.com/android/Using-the-RecyclerView) and the `StaggeredGridLayoutManager` to improve the grid of image results displayed
* [x] Replace Picasso with [Glide](http://inthecheesefactory.com/blog/get-to-know-glide-recommended-by-google/en) for more efficient image rendering.
* [x] Before an article search is triggered by the user, displays the current top stories of the day by default.
* [x] Hides the `Toolbar` at the top as the user scrolls down through the results using the [CoordinatorLayout and AppBarLayout](http://guides.codepath.com/android/Using-the-App-ToolBar#reacting-to-scroll).
The following **additional** features are implemented:

* [x] List anything else that you can get done to improve the app functionality!
* [x] Implemented Parcelable
* [x] Data persists configuration changes (orientation changing)
* [x] Custom progress bar persists through configuration changes
* [x] Clear all button in search filter
## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/cCPFjxJ.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Describe any challenges encountered while building the app.

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android

## License

    Copyright [yyyy] [name of copyright owner]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
