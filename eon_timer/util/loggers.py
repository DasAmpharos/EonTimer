import functools
import glob
import os
import sys
from datetime import datetime

import platformdirs
from loguru import logger


def init():
    logger.remove()  # Remove the default stderr handler

    fmt = '{time:YYYY-MM-DD HH:mm:ss} {level:>5} [{process}] --- [{thread.name}] {extra[name]:<30} : {message}'
    logger.add(sys.stdout, format=fmt, level='DEBUG', colorize=False)

    log_dir = platformdirs.user_log_dir('EonTimer', 'DasAmpharos', ensure_exists=True)
    timestamp = datetime.now().strftime('%Y%m%d-%H%M%S')
    log_path = os.path.join(log_dir, f'EonTimer-{timestamp}.log')
    logger.add(log_path, format=fmt, level='DEBUG')

    # Remove old log files, keeping the 5 most recent
    files = glob.glob('EonTimer-*.log', root_dir=log_dir)
    files = [os.path.join(log_dir, f) for f in files]
    files.sort(key=os.path.getmtime, reverse=True)
    for old_file in files[5:]:
        os.remove(old_file)


def get_logger(arg: str | object | type):
    if isinstance(arg, str):
        name = __abbreviate(arg)
    else:
        if not isinstance(arg, type):
            arg = type(arg)
        name = __get_name(arg)
    return logger.bind(name=name)


def log_method_calls(level: str = 'DEBUG'):
    def decorator(func):
        _log = get_logger(__get_name_for_function(func))

        @functools.wraps(func)
        def wrapper(*args, **kwargs):
            _log.log(level, f'Entering :: {func.__name__} ...')
            result = func(*args, **kwargs)
            _log.log(level, f'Exiting :: {func.__name__} ...')
            return result

        return wrapper

    return decorator


def __get_name(arg: type) -> str:
    return __abbreviate('.'.join([arg.__module__, arg.__qualname__]))


def __get_name_for_function(func) -> str:
    owner_name = func.__qualname__.rsplit('.', 1)[0]
    return __abbreviate('.'.join([func.__module__, owner_name]))


def __abbreviate(s: str) -> str:
    components = s.split('.')
    for i in range(len(components) - 1):
        components[i] = components[i][0]
    return '.'.join(components)
