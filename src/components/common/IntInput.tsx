import React, { useState, useCallback, useEffect } from 'react';

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
  const format = (v: number | null) =>
    v === null ? '' : radix === 16 ? v.toString(16).toUpperCase() : String(v);

  const [text, setText] = useState(format(value));

  useEffect(() => {
    setText(format(value));
  }, [value, radix]);

  const handleChange = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      const raw = e.target.value;
      setText(raw);
      const trimmed = raw.trim();
      if (trimmed === '') {
        if (allowBlank) onChange(null);
        return;
      }
      const parsed = radix === 16 ? parseInt(trimmed, 16) : parseInt(trimmed, 10);
      if (isNaN(parsed)) return;
      if (min !== undefined && parsed < min) return;
      if (max !== undefined && parsed > max) return;
      onChange(parsed);
    },
    [onChange, min, max, allowBlank, radix],
  );

  const handleBlur = useCallback(() => {
    setText(format(value));
  }, [value, radix]);

  return (
    <input
      id={id}
      type="text"
      className="int-input"
      value={text}
      onChange={handleChange}
      onBlur={handleBlur}
      placeholder={placeholder}
      disabled={disabled}
    />
  );
}
