import json
import os.path
import sys
from datetime import datetime


def get_version() -> str:
    dirname = os.path.dirname(__file__)
    filename = os.path.join(dirname, 'properties.json')

    properties = {}
    if os.path.exists(filename):
        with open(filename, 'r') as file:
            properties.update(json.load(file))
    if 'version' in properties:
        return properties['version']

    now = datetime.now()
    timestamp = now.strftime('%Y.%m.%d')
    return f'v{timestamp}'


def is_bundled() -> bool:
    return getattr(sys, 'frozen', False) and hasattr(sys, '_MEIPASS')
