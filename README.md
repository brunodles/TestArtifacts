# Test Artifacts
A gradle plugin to store test artifacts of open source projects.

## Why?
I have a lot of open source projects and they have a lot of tests and other code quality tools that generate reports.

The main problem is that those reports are not stored anywhere and I used to loose then.
I've search for a tool that could store then, but they only store **coverage** info.

So, I decided to create my own storage for my reports.

## What is the objective of this repo
Create a tool to store and visualize report artifacts, to improve quality of open source projects.

## What reports this can store
We can store reports from these tools:
* Checkstyle
* KTLint, when the report is on checkstyle format
* Android Lint
* Test Reports
* Jacoco (xml and csv)

## How does it work?
We have a task to convert data in *xml* or *csv* to json.

Then we upload it to **firebase**.

## What is missing?
We need to create a build data reader and a dashboard.

Later we can create a **status image** like all other tools.

## Can I contribute?
You can help us to:
* Add new report formats
* Add other languages (actually we only support reports from java world. Also includes groovy, kotlin and android)
* Give us your idea of dashboard
* Build the dashboard
* Make code review
* Anything you think would help us...

## Especial Thanks!
Firebase to serve as a storage for free!!!