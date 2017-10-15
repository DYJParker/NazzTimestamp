# NazzTimestamp
I've been doing work-study with Theo Nazz, a local craftsman, and he asked me to keep track of my hours spent working on my own projects vs projects under his direction. There's no kill like overkill, so I decided to use that as a prompt for a new project, just as much a testbed as a functional app.

So far, this app features:
- MVP Architecture
  - featuring Presenter unit tests with JUnit and Mockito
- Kotlin
  - My very first Kotlin program
  - Anko from the folks at Kotlin for a more idiomatic SQLiteOpenDatabaseHelper and some other utilities

A not-very-strongly-ordered WIP list:
- [X] UI: Add "time started" text field to active category markers, reactivate menuless ToolBar
- [ ] Re-write for new WorkLog encapsulating object, and move some logic there from Presenter
- [ ] Write Espresso (maybe look into Robolectric?) tests
- [ ] Activate and plumb RecyclerView for read-only session history
- [ ] Allow editing log from RV
- [ ] Tie in to Google Sheets API per "client" request
- [ ] Write persistent notification with shortcuts and service backend
