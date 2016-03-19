# An application to crawl or spider a web site

Multi threaded event based application, with the ability to control how many pages are crawled or to what depth the
web site should be crawled to.

Uses file backed Sets and Maps so the crawl can be stopped and resumed.

TODO:
* Extract URIDownloader to an interface and implement a headless browser solution for executing JS
