def get_version() -> str:
    global_vars = globals()
    if '__version__' in global_vars:
        return global_vars['__version__']

    from datetime import datetime

    now = datetime.now()
    timestamp = now.strftime('%Y%m%d%H%M%S')
    return f'v{timestamp}'
