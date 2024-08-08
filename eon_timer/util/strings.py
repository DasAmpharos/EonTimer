def strip_to_none(s: str) -> str | None:
    stripped = s.strip()
    return stripped if stripped else None
