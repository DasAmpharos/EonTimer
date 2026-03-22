import { useState, useCallback, useRef, useEffect } from 'react';
import { useProfilesStore, DEFAULT_PROFILE_ID } from '../store/profiles';
import { exportProfile } from '../utils/profileIO';

interface ProfileSelectorProps {
  disabled?: boolean;
  isDirty?: boolean;
  onProfileChange: () => void;
  onNewProfile: () => void;
  onImport: () => void;
}

export function ProfileSelector({
  disabled,
  isDirty,
  onProfileChange,
  onNewProfile,
  onImport,
}: ProfileSelectorProps) {
  const profiles = useProfilesStore((s) => s.profiles);
  const activeProfileId = useProfilesStore((s) => s.activeProfileId);
  const setActiveProfile = useProfilesStore((s) => s.setActiveProfile);
  const deleteProfile = useProfilesStore((s) => s.deleteProfile);
  const duplicateProfile = useProfilesStore((s) => s.duplicateProfile);
  const renameProfile = useProfilesStore((s) => s.renameProfile);

  const [menuOpen, setMenuOpen] = useState(false);
  const [renaming, setRenaming] = useState(false);
  const [renameValue, setRenameValue] = useState('');
  const menuRef = useRef<HTMLDivElement>(null);
  const renameInputRef = useRef<HTMLInputElement>(null);

  // Close menu on outside click
  useEffect(() => {
    if (!menuOpen) return;
    const handler = (e: MouseEvent) => {
      if (menuRef.current && !menuRef.current.contains(e.target as Node)) {
        setMenuOpen(false);
      }
    };
    document.addEventListener('mousedown', handler);
    return () => document.removeEventListener('mousedown', handler);
  }, [menuOpen]);

  // Focus rename input
  useEffect(() => {
    if (renaming) renameInputRef.current?.focus();
  }, [renaming]);

  const handleProfileChange = useCallback(
    (e: React.ChangeEvent<HTMLSelectElement>) => {
      setActiveProfile(e.target.value);
      setTimeout(onProfileChange, 0);
    },
    [setActiveProfile, onProfileChange],
  );

  const activeProfile = profiles.find((p) => p.id === activeProfileId);
  const isDefault = activeProfileId === DEFAULT_PROFILE_ID;

  const handleRenameStart = useCallback(() => {
    setRenameValue(activeProfile?.name ?? '');
    setRenaming(true);
    setMenuOpen(false);
  }, [activeProfile]);

  const handleRenameConfirm = useCallback(() => {
    const trimmed = renameValue.trim();
    if (trimmed && activeProfileId) {
      renameProfile(activeProfileId, trimmed);
    }
    setRenaming(false);
  }, [renameValue, activeProfileId, renameProfile]);

  const handleRenameKeyDown = useCallback(
    (e: React.KeyboardEvent) => {
      if (e.key === 'Enter') handleRenameConfirm();
      if (e.key === 'Escape') setRenaming(false);
    },
    [handleRenameConfirm],
  );

  const handleDuplicate = useCallback(() => {
    duplicateProfile(activeProfileId);
    setMenuOpen(false);
    setTimeout(onProfileChange, 0);
  }, [activeProfileId, duplicateProfile, onProfileChange]);

  const handleExport = useCallback(() => {
    if (activeProfile) exportProfile(activeProfile);
    setMenuOpen(false);
  }, [activeProfile]);

  const handleDelete = useCallback(() => {
    if (isDefault) return;
    if (!confirm(`Delete profile "${activeProfile?.name}"?`)) return;
    deleteProfile(activeProfileId);
    setMenuOpen(false);
    setTimeout(onProfileChange, 0);
  }, [isDefault, activeProfile, activeProfileId, deleteProfile, onProfileChange]);

  return (
    <div className="profile-selector-bar">
      {renaming ? (
        <input
          ref={renameInputRef}
          type="text"
          className="profile-rename-input"
          value={renameValue}
          onChange={(e) => setRenameValue(e.target.value)}
          onBlur={handleRenameConfirm}
          onKeyDown={handleRenameKeyDown}
        />
      ) : (
        <div className="profile-dropdown-wrapper">
          <select
            className="profile-dropdown"
            value={activeProfileId}
            onChange={handleProfileChange}
            disabled={disabled}
          >
            {profiles.map((p) => (
              <option key={p.id} value={p.id}>
                {p.name}
              </option>
            ))}
          </select>
          {isDirty && <span className="profile-dirty-dot" title="Unsaved changes" />}
        </div>
      )}
      <button
        className="btn btn-icon profile-btn"
        onClick={onNewProfile}
        disabled={disabled}
        title="New Profile"
      >
        ＋
      </button>
      <div className="profile-menu-container" ref={menuRef}>
        <button
          className="btn btn-icon profile-btn"
          onClick={() => setMenuOpen(!menuOpen)}
          disabled={disabled}
          title="Profile actions"
        >
          ⋮
        </button>
        {menuOpen && (
          <div className="profile-context-menu">
            <button className="profile-menu-item" onClick={handleRenameStart}>
              Rename
            </button>
            <button className="profile-menu-item" onClick={handleDuplicate}>
              Duplicate
            </button>
            <button className="profile-menu-item" onClick={handleExport}>
              Export
            </button>
            <div className="profile-menu-divider" />
            <button className="profile-menu-item" onClick={onImport}>
              Import…
            </button>
            {!isDefault && (
              <>
                <div className="profile-menu-divider" />
                <button
                  className="profile-menu-item profile-menu-item-danger"
                  onClick={handleDelete}
                >
                  Delete
                </button>
              </>
            )}
          </div>
        )}
      </div>
    </div>
  );
}
