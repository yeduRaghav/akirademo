# akirademo

This project was built and test on AndrioidStudio 4.1.1

The App loads the MainScreen upon launch.
The MainScreen allows user to search places using googleplaces web api.
The app only lists a place if it is a restaurent or a liqour-store.

App uses MVVM architecure
There are unitTests for bussiness logic and other model transformation logic.
A few behavioural test are also added to ensure Coroutine-job is handled properly in ViewModel.
The GoogleApiKey is currently hardcoded, in an ideal case the Api key should be kept in memory(received from server) or stored in encrypted sharedpreference.
