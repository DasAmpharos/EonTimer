import functools
import glob
import logging
import os
import sys
import threading
from datetime import datetime

import platformdirs


def init(level: int = logging.DEBUG):
    logger = logging.getLogger()
    formatter = logging.Formatter(
        fmt='%(asctime)s %(levelname)5s [%(processid)d] --- [%(threadname)s] %(name)-30s : %(message)s',
        datefmt='%Y-%m-%d %H:%M:%S')
    logger.setLevel(level)

    # Create console handler
    console_handler = logging.StreamHandler(sys.stdout)
    console_handler.setFormatter(formatter)
    console_handler.setLevel(level)
    # Create a file handler
    now = datetime.now()
    timestamp = now.strftime('%Y%m%d-%H%M%S')

    log_dir = platformdirs.user_log_dir('EonTimer', 'DasAmpharos', ensure_exists=True)
    file_handler = logging.FileHandler(os.path.join(log_dir, f'EonTimer-{timestamp}.log'))
    file_handler.setFormatter(formatter)
    file_handler.setLevel(level)

    # Remove old log files if more than 5 exists
    files = glob.glob('EonTimer-*.log', root_dir=log_dir)
    files = list(map(lambda it: os.path.join(log_dir, it), files))
    files.sort(key=os.path.getmtime, reverse=True)
    while len(files) > 5:
        os.remove(files.pop())

    custom_filter = CustomAttributesFilter()
    console_handler.addFilter(custom_filter)
    file_handler.addFilter(custom_filter)

    logger.addHandler(console_handler)
    logger.addHandler(file_handler)


def get_logger(arg: str | object | type) -> logging.Logger:
    if isinstance(arg, str):
        return logging.getLogger(__abbreviate(arg))

    if isinstance(arg, object):
        arg = type(arg)
    return logging.getLogger(__get_name(arg))


def log_method_calls(level: int = logging.DEBUG):
    def decorator(func):
        logger = get_logger(__get_name_for_function(func))

        @functools.wraps(func)
        def wrapper(*args, **kwargs):
            logger.log(level, f'Entering :: {func.__name__} ...')
            result = func(*args, **kwargs)
            logger.log(level, f'Exiting :: {func.__name__} ...')
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
    # abbreviate all components except the last one
    for i in range(len(components) - 1):
        components[i] = components[i][0]
    return '.'.join(components)


class CustomAttributesFilter(logging.Filter):
    def filter(self, record: logging.LogRecord):
        setattr(record, 'processid', os.getpid())
        current_thread = threading.current_thread()
        setattr(record, 'threadname', current_thread.name)
        return True
