import { useState, useCallback } from 'react';
import { TimerType } from '../utils/types';
import { FormField } from './common/FormField';
import { EnumSelect } from './common/EnumSelect';

// Exclude Default — that's the built-in profile
const TIMER_TYPES = Object.values(TimerType).filter((t) => t !== TimerType.DEFAULT) as TimerType[];

interface NewProfileDialogProps {
  open: boolean;
  onClose: () => void;
  onCreate: (data: { name: string; timerType: TimerType; description: string }) => void;
}

export function NewProfileDialog({ open, onClose, onCreate }: NewProfileDialogProps) {
  const [name, setName] = useState('');
  const [timerType, setTimerType] = useState<TimerType>(TimerType.GEN5);
  const [description, setDescription] = useState('');

  const [lastOpen, setLastOpen] = useState(false);
  if (open && !lastOpen) {
    setName('');
    setTimerType(TimerType.GEN5);
    setDescription('');
  }
  if (open !== lastOpen) setLastOpen(open);

  const handleOk = useCallback(() => {
    const trimmed = name.trim();
    if (!trimmed) return;
    onCreate({ name: trimmed, timerType, description: description.trim() });
    onClose();
  }, [name, timerType, description, onCreate, onClose]);

  const handleKeyDown = useCallback(
    (e: React.KeyboardEvent) => {
      if (e.key === 'Enter') handleOk();
      if (e.key === 'Escape') onClose();
    },
    [handleOk, onClose],
  );

  if (!open) return null;

  return (
    <div className="dialog-overlay" onClick={onClose}>
      <div className="dialog" onClick={(e) => e.stopPropagation()} onKeyDown={handleKeyDown}>
        <div className="dialog-title">New Profile</div>
        <div className="dialog-content">
          <div className="settings-panel">
            <FormField label="Name">
              <input
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="Profile name"
                autoFocus
              />
            </FormField>
            <FormField label="Timer Type">
              <EnumSelect values={TIMER_TYPES} value={timerType} onChange={setTimerType} />
            </FormField>
            <FormField label="Description">
              <input
                type="text"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="Optional notes"
              />
            </FormField>
          </div>
        </div>
        <div className="dialog-buttons">
          <button className="btn" onClick={onClose}>
            Cancel
          </button>
          <button className="btn btn-primary" onClick={handleOk} disabled={!name.trim()}>
            Create
          </button>
        </div>
      </div>
    </div>
  );
}
