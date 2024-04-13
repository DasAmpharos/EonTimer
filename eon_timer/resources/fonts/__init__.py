import os


def resource_filename(filename: str) -> str:
    directory = os.path.dirname(__file__)
    return os.path.join(directory, filename)
