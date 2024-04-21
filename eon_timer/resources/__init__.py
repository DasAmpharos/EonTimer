import importlib.resources
import sys


def get_filepath(package: str, resource: str, normalize_path: bool = False) -> str:
    with importlib.resources.path(package, resource) as path:
        filepath = str(path)
        if normalize_path:
            filepath = normalize_filepath(filepath)
        return filepath


def get_bytes(package: str, resource: str) -> bytes:
    return importlib.resources.read_binary(package, resource)


def normalize_filepath(filepath: str) -> str:
    if hasattr(sys, 'getwindowsversion'):
        return filepath.replace('\\', '/')
    return filepath
