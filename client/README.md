# project-typo

Building a hackable chat client during a hackaton.

## TODO Preparation Hackaton

* Setup Server with single web socket and in-memory conversation
* Add app state, basic handlers and subs.
* Add basic UI.
* Packaging

## Installation

```
npm install electron-prebuilt -g
```

## Development

Execute these commandos in two separate consoles:

```
lein trampoline cljsbuild auto electron-dev
lein trampoline figwheel frontend-dev
electron .
lein less auto
````

##Release

```
lein cljsbuild once frontend-release
lein cljsbuild once electron-release
```

To verify the output run: `electron .`.

Create OS X build:
```
electron-packager . Typo --platform=darwin --arch=x64 --version=0.36.1 --icon=./electron_src/electron/images/Typo-icon-orange.icns
```
