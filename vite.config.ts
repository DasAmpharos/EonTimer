import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { VitePWA } from 'vite-plugin-pwa';
import { execSync } from 'child_process';
const commitHash = execSync('git rev-parse --short HEAD').toString().trim();
const commitDate = execSync('git log -1 --format=%cs').toString().trim().replaceAll('-', '.');

export default defineConfig({
  base: '/EonTimer/',
  define: {
    __APP_VERSION__: JSON.stringify(commitDate),
    __COMMIT_HASH__: JSON.stringify(commitHash),
  },
  plugins: [
    react(),
    VitePWA({
      registerType: 'autoUpdate',
      includeAssets: ['favicon.svg', 'icon-192.svg', 'icon-512.svg'],
      manifest: {
        name: 'EonTimer',
        short_name: 'EonTimer',
        description: 'A precision timer for Pokémon RNG manipulation',
        theme_color: '#5b9bd5',
        background_color: '#c8dce8',
        display: 'standalone',
        orientation: 'any',
        start_url: '/EonTimer/',
        icons: [
          {
            src: 'icon-192.svg',
            sizes: '192x192',
            type: 'image/svg+xml',
            purpose: 'any',
          },
          {
            src: 'icon-512.svg',
            sizes: '512x512',
            type: 'image/svg+xml',
            purpose: 'any maskable',
          },
        ],
      },
      workbox: {
        globPatterns: ['**/*.{js,css,html,svg}'],
      },
    }),
  ],
});
