import React from 'react';

interface FormFieldProps {
  label: string;
  children: React.ReactNode;
  visible?: boolean;
  htmlFor?: string;
}

export function FormField({ label, children, visible = true, htmlFor }: FormFieldProps) {
  if (!visible) return null;
  return (
    <div className="form-field">
      <label className="form-field-label" htmlFor={htmlFor}>{label}</label>
      <div className="form-field-input">{children}</div>
    </div>
  );
}
