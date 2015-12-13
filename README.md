# project-typo

Buiding a hackable chat client during a hackaton.

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
