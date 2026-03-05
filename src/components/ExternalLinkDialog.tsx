interface ExternalLinkDialogProps {
  url: string | null;
  onConfirm: () => void;
  onCancel: () => void;
}

export function ExternalLinkDialog({ url, onConfirm, onCancel }: ExternalLinkDialogProps) {
  if (!url) return null;

  const host = (() => {
    try { return new URL(url).hostname; }
    catch { return url; }
  })();

  return (
    <div className="dialog-overlay" onClick={onCancel}>
      <div className="dialog" onClick={(e) => e.stopPropagation()}>
        <div className="dialog-title">Leave EonTimer?</div>
        <div className="dialog-content">
          <p>You are about to leave EonTimer and go to:</p>
          <p><strong>{host}</strong></p>
        </div>
        <div className="dialog-buttons">
          <button className="btn" onClick={onCancel}>Cancel</button>
          <button className="btn btn-primary" onClick={onConfirm}>Continue</button>
        </div>
      </div>
    </div>
  );
}
