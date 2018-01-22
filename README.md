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

## How to Use?
### Setup
You need to include the plugin on your `build.gradle` classpath.
```gradle
buildscript {
    repositories {
        ...
        jcenter() // we release here
    }
    dependencies {
        ...
        classpath 'com.brunodles:testartifacts:0.1.0'
    }
}
```

Then apply the plugin on your `build.gradle`.
```gradle
apply plugin: 'java'
apply plugin: 'testartifacts'
```

Now just setup some of your project info.
```gradle

    archiver {
        projectName = "Your project name here"
        moduleName = "The name of your module" // Repeat the project name if no module
        buildNumber = project.properties.get('buildNumber', 0) // Here you can read a System environment, depends on how you want to pass this parameter to gradle
        firebaseUrl = "The subdomain of your firebase url"

        // here you register the type of your reports and where to find then
        files = [
                // type and a list of file paths, we read reports from `buildDir`
                'checkstyle': ['ktlint.xml', 'reports/checkstyle/main.xml'],
                'jacoco'    : ['reports/jacoco/test/jacocoTestReport.xml'],
                'jacocoCsv' : ['reports/jacoco/test/jacocoTestReport.csv'],
                'lint'      : ['lint-results.xml'],
                'test'      : ['test-results/test/TEST**.xml'] // we can use wildcards
        ]
    }
```

### Tasks
After setup you can use the tasks.
* mergeTestArtifacts - grab all reports and save into a jsonFile
* uploadTestArtifacts - upload the report to our storage
* totals - read reports file to extract totals (WIP)

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