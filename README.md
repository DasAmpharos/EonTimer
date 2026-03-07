<div align="center">
<img src="public/favicon.svg" width="128"/>

### EonTimer

A precision timer for Pokémon RNG manipulation — now on the [web](https://dasampharos.github.io/EonTimer/).

A port of the Pokémon RNG Timer originally written by
[ToastPlusOne](https://bitbucket.org/ToastPlusOne/eontimer/downloads/).

[![Deploy](https://github.com/DasAmpharos/EonTimer/actions/workflows/deploy.yml/badge.svg?branch=web-rewrite)](https://github.com/DasAmpharos/EonTimer/actions?query=workflow:deploy+branch:web-rewrite)

</div>

## Features

- **Gen 5** — Standard, C-Gear, Entralink, and Enhanced Entralink modes
- **Gen 4** — Delay and second calibration
- **Gen 3** — Standard and Variable Target modes
- **Custom** — Multi-phase timers with configurable units
- **PWA** — Installable from your browser, works fully offline
- **Responsive** — Works on desktop, tablet, and mobile

## Getting Started

### Prerequisites

- [Node.js](https://nodejs.org/) 20+

### Install Dependencies

```sh
npm install
```

### Development

```sh
npm run dev
```

### Production Build

```sh
npm run build
```

The build output is in the `dist/` directory and can be served by any static file server.

### Preview Production Build

```sh
npm run preview
```

## Tech Stack

- **React 19** — UI framework
- **TypeScript** — Type-safe codebase
- **Vite** — Build tool and dev server
- **Zustand** — State management with localStorage persistence
- **Web Workers** — High-precision timer loop off the main thread
- **Web Audio API** — Synthesized action sounds
- **Workbox** — Service worker for offline PWA support

## Deployment

This project includes a GitHub Actions workflow (`.github/workflows/deploy.yml`) that builds and deploys to GitHub Pages on push to the `main` branch.

To enable: go to your repo **Settings → Pages → Source** and select **GitHub Actions**.

## License

EonTimer is released under the [MIT License](LICENSE.md).

## Credits

### Icon Attribution

- **Concept:** [DasAmpharos](https://github.com/DasAmpharos)
- **Preliminary Renderings:** Provided by ChatGPT, OpenAI
- **Final Design:** [dartanian300](https://github.com/dartanian300)
