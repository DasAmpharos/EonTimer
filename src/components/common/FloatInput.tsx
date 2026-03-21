import React, { useState, useCallback, useEffect } from 'react';

interface FloatInputProps {
  value: number;
  onChange: (value: number) => void;
  min?: number;
  max?: number;
  placeholder?: string;
  disabled?: boolean;
  id?: string;
}

export function FloatInput({
  value,
  onChange,
  min,
  max,
  placeholder,
  disabled,
  id,
}: FloatInputProps) {
  const [text, setText] = useState(String(value));

  useEffect(() => {
    setText(String(value));
  }, [value]);

  const handleChange = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      const raw = e.target.value;
      setText(raw);
      const parsed = parseFloat(raw);
      if (isNaN(parsed)) return;
      if (min !== undefined && parsed < min) return;
      if (max !== undefined && parsed > max) return;
      onChange(parsed);
    },
    [onChange, min, max],
  );

  const handleBlur = useCallback(() => {
    setText(String(value));
  }, [value]);

  return (
    <input
      id={id}
      type="text"
      inputMode="decimal"
      className="float-input"
      value={text}
      onChange={handleChange}
      onBlur={handleBlur}
      placeholder={placeholder}
      disabled={disabled}
    />
  );
}
