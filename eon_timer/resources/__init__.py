import importlib.resources
import sys


def get_filepath(package: str, resource: str) -> str:
    with importlib.resources.path(package, resource) as path:
        return normalize_filepath(str(path))


def get_bytes(package: str, resource: str) -> bytes:
    return importlib.resources.read_binary(package, resource)


def normalize_filepath(filepath: str) -> str:
    if hasattr(sys, 'getwindowsversion'):
        return filepath.replace('\\', '/')
    return filepath
