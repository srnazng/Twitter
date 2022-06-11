# Project 2 - SimpleTwitter

SimpleTwitter is an android app that allows a user to view their Twitter timeline and post a new tweet. The app utilizes [Twitter REST API](https://dev.twitter.com/rest/public).

Time spent: **20** hours spent in total

## User Stories

The following **required** functionality is completed:

* [X] User can **sign in to Twitter** using OAuth login
* [X] User can **view tweets from their home timeline**
    * [X] User is displayed the username, name, and body for each tweet
    * [X] User is displayed the [relative timestamp](https://gist.github.com/nesquena/f786232f5ef72f6e10a7) for each tweet "8m", "7h"
* [X] User can ***log out of the application** by tapping on a logout button
* [X] User can **compose and post a new tweet**
    * [X] User can click a “Compose” icon in the Action Bar on the top right
    * [X] User can then enter a new tweet and post this to Twitter
    * [X] User is taken back to home timeline with **new tweet visible** in timeline
    * [X] Newly created tweet should be manually inserted into the timeline and not rely on a full refresh
* [X] User can **see a counter that displays the total number of characters remaining for tweet** that also updates the count as the user types input on the Compose tweet page
* [X] User can **pull down to refresh tweets timeline**
* [X] User can **see embedded image media within a tweet** on list or detail view.

The following **optional** features are implemented:

* [X] User is using **"Twitter branded" colors and styles**
* [X] User sees an **indeterminate progress indicator** when any background or network task is happening
* [X] User can **select "reply" from home timeline to respond to a tweet**
    * [X] User that wrote the original tweet is **automatically "@" replied in compose**
* [ ] User can tap a tweet to **open a detailed tweet view**
    * [X] User can **take favorite (and unfavorite) or retweet** actions on a tweet
* [X] User can view more tweets as they scroll with infinite pagination
* [ ] Compose tweet functionality is built using modal overlay
* [X] User can **click a link within a tweet body** on tweet details view. The click will launch the web browser with relevant page opened.
* [ ] Replace all icon drawables and other static image assets with [vector drawables](http://guides.codepath.org/android/Drawables#vector-drawables) where appropriate.
* [X] User can view following / followers list through any profile they view.
* [ ] Use the View Binding library to reduce view boilerplate.
* [ ] On the Twitter timeline, apply scrolling effects such as [hiding/showing the toolbar](http://guides.codepath.org/android/Using-the-App-ToolBar#reacting-to-scroll) by implementing [CoordinatorLayout](http://guides.codepath.org/android/Handling-Scrolls-with-CoordinatorLayout#responding-to-scroll-events).
* [ ] User can **open the twitter app offline and see last loaded tweets**. Persisted in SQLite tweets are refreshed on every application launch. While "live data" is displayed when app can get it from Twitter API, it is also saved for use in offline mode.

The following **additional** features are implemented:
* Action bar
* Show on timeline if a tweet is a reply or a retweet
* View profile of any user including their number of followers, number following

* [ ] List anything else that you can get done to improve the app functionality!

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='https://i.imgur.com/JrNVSZi.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [Kap](https://getkap.co/).

## Notes

Describe any challenges encountered while building the app.

1. Compose reply layout: When I was editing activity_compose_reply.xml my changes were not showing up. I thought it was my code but after a while I tried making large obvious changes and they also were not showing up. I had another view that looked very similar, activity_compose.xml for composing tweets, that I had copied to create activity_compose_reply.xml. I had a guess that perhaps I was viewing the wrong xml file in the activity and made a change to activity_compose.xml. When I did, I saw those changes on the screen. I therefore knew that the activity was being connected to the wrong layout file. I was then able to go into the activity and quickly find the line connecting it to the layout and was able to fix the bug.
2. Send a Reply (send original tweet to ComposeReplyActivity) - I had an onClick attribute in the XML of the reply button. However, because the reply button was associated to each tweet, the reply button was located in the ViewHolder of the RecyclerView. As a result, the onClick function was not in the scope of the XML. I tried putting the onClick function in different activities but could not find any that worked. I also searched for different ways to do this but could not find anything. I ended up asking a TA who said that my current method of adding the onClick was going to be very complicated and suggested I look into adding the onClick listener from the Java side. I took his advice and started over and was able to continue. All the other parts related to sending the reply I figured out by looking at past code.

## Open-source libraries used

* [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
* [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android

## License

    Copyright [2022] [Serena Zeng]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.