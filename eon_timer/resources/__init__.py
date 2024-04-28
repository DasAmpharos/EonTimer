import importlib.resources
import sys


def get_filepath(resource: str, normalize_path: bool = False) -> str:
    package, resource_name = __get_resource_name(resource)
    with importlib.resources.path(package, resource_name) as path:
        filepath = str(path)
        if normalize_path:
            filepath = normalize_filepath(filepath)
        return filepath


def get_bytes(resource: str) -> bytes:
    package, resource_name = __get_resource_name(resource)
    return importlib.resources.read_binary(package, resource_name)


def __get_resource_name(resource: str) -> tuple[str, str]:
    components = resource.split('/')
    resource_name = components.pop()
    package = '.'.join(['eon_timer.resources', *components])
    return package, resource_name


def normalize_filepath(filepath: str) -> str:
    if hasattr(sys, 'getwindowsversion'):
        return filepath.replace('\\', '/')
    return filepath
