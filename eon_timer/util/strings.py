def strip_to_none(s: str | None) -> str | None:
    if s is None:
        return None
    stripped = s.strip()
    return stripped if stripped else None
