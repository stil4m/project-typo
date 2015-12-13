# project-typo

Building a hackable chat client during a hackaton.

## TODO Preparation Hackaton

* Setup Server with single websocket and in-memory conversation
* Add [Photon](http://photonkit.com/)
* Add app state, basic handlers and subs.
* Add basic UI.


## Installation

```
gem install foreman
npm install electron-prebuilt -g
```

## Development

Execute these commandos in two separate consoles:

```
foreman start
electron .
````

##Release

```
lein cljsbuild once frontend-release
lein cljsbuild once electron-release
```

To verify the output run: `electron .`.
