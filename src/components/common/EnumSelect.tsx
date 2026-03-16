import React, { useCallback } from 'react';

interface EnumSelectProps<T extends string> {
  values: readonly T[];
  value: T;
  onChange: (value: T) => void;
  disabled?: boolean;
  id?: string;
}

export function EnumSelect<T extends string>({
  values,
  value,
  onChange,
  disabled,
  id,
}: EnumSelectProps<T>) {
  const handleChange = useCallback(
    (e: React.ChangeEvent<HTMLSelectElement>) => {
      onChange(e.target.value as T);
    },
    [onChange],
  );

  return (
    <select
      id={id}
      className="enum-select"
      value={value}
      onChange={handleChange}
      disabled={disabled}
    >
      {values.map((v) => (
        <option key={v} value={v}>
          {v}
        </option>
      ))}
    </select>
  );
}
