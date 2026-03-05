interface ExternalLinkDialogProps {
  url: string | null;
  onConfirm: () => void;
  onCancel: () => void;
}

export function ExternalLinkDialog({ url, onConfirm, onCancel }: ExternalLinkDialogProps) {
  if (!url) return null;

  return (
    <div className="dialog-overlay" onClick={onCancel}>
      <div className="dialog" onClick={(e) => e.stopPropagation()}>
        <div className="dialog-title">Opening External Link</div>
        <div className="dialog-content">
          <p>You are about to leave EonTimer and open the following link in a new tab:</p>
          <p className="external-link-url">{url}</p>
        </div>
        <div className="dialog-buttons">
          <button className="btn" onClick={onCancel}>Cancel</button>
          <button className="btn btn-primary" onClick={onConfirm}>Open</button>
        </div>
      </div>
    </div>
  );
}
