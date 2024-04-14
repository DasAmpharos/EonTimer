# -*- mode: python ; coding: utf-8 -*-


a = Analysis(
    ['eon-timer.py'],
    pathex=[],
    binaries=[],
    datas=[
        ('eon_timer/resources/fonts/*.ttf', 'eon_timer/resources/fonts'),
        ('eon_timer/resources/icons/*.png', 'eon_timer/resources/icons'),
        ('eon_timer/resources/icons/*.svg', 'eon_timer/resources/icons'),
        ('eon_timer/resources/images/*.png', 'eon_timer/resources/images'),
        ('eon_timer/resources/sounds/*.wav', 'eon_timer/resources/sounds'),
        ('eon_timer/resources/styles/*.scss', 'eon_timer/resources/styles'),
    ],
    hiddenimports=[],
    hookspath=[],
    hooksconfig={},
    runtime_hooks=[],
    excludes=[],
    noarchive=False,
    optimize=0,
)
pyz = PYZ(a.pure)

exe = EXE(
    pyz,
    a.scripts,
    a.binaries,
    a.datas,
    [],
    name='eon-timer',
    debug=False,
    bootloader_ignore_signals=False,
    strip=False,
    upx=True,
    upx_exclude=[],
    runtime_tmpdir=None,
    console=True,
    disable_windowed_traceback=False,
    argv_emulation=False,
    target_arch=None,
    codesign_identity=None,
    entitlements_file=None,
)
