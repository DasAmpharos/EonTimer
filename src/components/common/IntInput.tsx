import React, { useCallback } from 'react';

interface IntInputProps {
  value: number | null;
  onChange: (value: number | null) => void;
  min?: number;
  max?: number;
  placeholder?: string;
  disabled?: boolean;
  allowBlank?: boolean;
  radix?: 10 | 16;
  id?: string;
}

export function IntInput({
  value,
  onChange,
  min,
  max,
  placeholder,
  disabled,
  allowBlank = false,
  radix = 10,
  id,
}: IntInputProps) {
  const displayValue = value === null ? '' : (radix === 16 ? value.toString(16).toUpperCase() : String(value));

  const handleChange = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      const raw = e.target.value.trim();
      if (raw === '' && allowBlank) {
        onChange(null);
        return;
      }
      const parsed = radix === 16 ? parseInt(raw, 16) : parseInt(raw, 10);
      if (isNaN(parsed)) return;
      if (min !== undefined && parsed < min) return;
      if (max !== undefined && parsed > max) return;
      onChange(parsed);
    },
    [onChange, min, max, allowBlank, radix],
  );

  return (
    <input
      id={id}
      type="text"
      className="int-input"
      value={displayValue}
      onChange={handleChange}
      placeholder={placeholder}
      disabled={disabled}
    />
  );
}
