import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import App from './App';
import './index.css';

console.info(`[EonTimer] version=${__APP_VERSION__} commit=${__COMMIT_HASH__}`);
console.info(`[EonTimer] platform=${navigator.platform}, userAgent=${navigator.userAgent}`);

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <App />
  </StrictMode>,
);
