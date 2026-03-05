import { useState } from 'react';

interface ExternalLinkDialogProps {
  url: string | null;
  onConfirm: (disableConfirm: boolean) => void;
  onCancel: () => void;
}

export function ExternalLinkDialog({ url, onConfirm, onCancel }: ExternalLinkDialogProps) {
  const [dontShowAgain, setDontShowAgain] = useState(false);

  if (!url) return null;

  return (
    <div className="dialog-overlay" onClick={onCancel}>
      <div className="dialog" onClick={(e) => e.stopPropagation()}>
        <div className="dialog-title">Opening External Link</div>
        <div className="dialog-content">
          <p>You are about to leave EonTimer and open the following link in a new tab:</p>
          <p className="external-link-url">{url}</p>
          <label className="checkbox-label" style={{ marginTop: '12px' }}>
            <input
              type="checkbox"
              checked={dontShowAgain}
              onChange={(e) => setDontShowAgain(e.target.checked)}
            />
            Don't show this again (can be changed in Settings)
          </label>
        </div>
        <div className="dialog-buttons">
          <button className="btn" onClick={onCancel}>Cancel</button>
          <button className="btn btn-primary" onClick={() => onConfirm(dontShowAgain)}>Open</button>
        </div>
      </div>
    </div>
  );
}
