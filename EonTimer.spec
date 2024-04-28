# -*- mode: python ; coding: utf-8 -*-


a = Analysis(
    ['EonTimer.py'],
    pathex=[],
    binaries=[],
    datas=[
        ('eon_timer/resources/*.png', 'eon_timer/resources'),
        ('eon_timer/resources/*.zip', 'eon_timer/resources'),
        ('eon_timer/resources/fonts/*.ttf', 'eon_timer/resources/fonts'),
        ('eon_timer/resources/sounds/*.wav', 'eon_timer/resources/sounds'),
    ],
    hiddenimports=['eon_timer.action'],
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
    name='EonTimer',
    debug=False,
    bootloader_ignore_signals=False,
    strip=False,
    upx=True,
    upx_exclude=[],
    runtime_tmpdir=None,
    console=False,
    disable_windowed_traceback=False,
    argv_emulation=False,
    target_arch=None,
    codesign_identity=None,
    entitlements_file=None,
    icon='eon_timer/resources/icon-512.png'
)
app = BUNDLE(
    exe,
    name='EonTimer.app',
    icon='eon_timer/resources/icon-512.png',
    bundle_identifier=None,
)
