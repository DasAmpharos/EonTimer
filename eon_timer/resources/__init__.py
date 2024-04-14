import importlib.resources


def get_filepath(package: str, resource: str) -> str:
    with importlib.resources.path(package, resource) as path:
        return str(path)
