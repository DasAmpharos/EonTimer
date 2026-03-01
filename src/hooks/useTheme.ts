import { useEffect } from 'react';
import { useSettingsStore } from '../store';
import { Theme } from '../utils/types';

export function useTheme() {
  const theme = useSettingsStore((s) => s.theme);

  useEffect(() => {
    const apply = (resolved: 'light' | 'dark') => {
      document.documentElement.setAttribute('data-theme', resolved);
      // Update PWA theme-color meta tag
      const meta = document.querySelector('meta[name="theme-color"]');
      if (meta) meta.setAttribute('content', resolved === 'dark' ? '#0f1923' : '#5b9bd5');
    };

    if (theme === Theme.LIGHT) {
      apply('light');
      return;
    }
    if (theme === Theme.DARK) {
      apply('dark');
      return;
    }

    // System: follow OS preference
    const mq = window.matchMedia('(prefers-color-scheme: dark)');
    apply(mq.matches ? 'dark' : 'light');

    const handler = (e: MediaQueryListEvent) => apply(e.matches ? 'dark' : 'light');
    mq.addEventListener('change', handler);
    return () => mq.removeEventListener('change', handler);
  }, [theme]);
}
