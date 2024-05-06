#!/usr/bin/env python3

import argparse
import os
import zipfile


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('-d', '--dir')
    parser.add_argument('-o', '--output', default='EonTimer.zip')
    parser.add_argument('files', nargs='+')
    args = parser.parse_args()

    if args.dir is not None:
        os.chdir(args.dir)
    with zipfile.ZipFile(args.output, 'w') as zf:
        for file in args.files:
            zf.write(file)


if __name__ == '__main__':
    main()
