#!/usr/bin/env python3

import argparse
import json
import os.path
import sys
import zipfile


def main() -> int:
    parser = argparse.ArgumentParser()
    subparsers = parser.add_subparsers(required=True)
    # build
    bundled_flag = '--bundled'
    subparser = subparsers.add_parser('build', help='Build a theme')
    subparser.add_argument(bundled_flag, action='store_true')
    subparser.add_argument('-d', '--directory', required=bundled_flag not in sys.argv)
    subparser.add_argument('-o', '--output')
    subparser.set_defaults(func=build)

    # parse arguments
    args = parser.parse_args()
    return args.func(args)


def build(args: argparse.Namespace) -> int:
    if args.bundled:
        return __build_bundled_themes()
    return __build_impl(args.directory, args.output)


def __build_bundled_themes() -> int:
    themes_dir = os.path.join('themes', 'bundled')
    for dirname in os.listdir(themes_dir):
        if os.path.isdir(os.path.join(themes_dir, dirname)):
            info_filepath = os.path.join(themes_dir, dirname, 'info.json')
            stylesheet_filepath = os.path.join(themes_dir, dirname, 'main.scss')
            if os.path.exists(info_filepath) and os.path.exists(stylesheet_filepath):
                output = os.path.join('eon_timer', 'resources', 'themes', f'{dirname}.zip')
                retcode = __build_impl(os.path.join(themes_dir, dirname), output)
                if retcode != 0:
                    return retcode
    return 0


def __build_impl(dirname: str, output: str | None) -> int:
    if not os.path.isdir(dirname):
        print(f"Directory {dirname} does not exist", file=sys.stderr)
        return -1

    # get and validate info
    info_filepath = os.path.join(dirname, 'info.json')
    if not os.path.exists(info_filepath):
        print(f"File {info_filepath} does not exist", file=sys.stderr)
        return -1
    with open(info_filepath, 'r') as file:
        info = json.load(file)

    # get and validate entrypoint
    entrypoint_path = os.path.join(dirname, 'main.scss')
    if not os.path.exists(entrypoint_path):
        print(f"File {entrypoint_path} does not exist", file=sys.stderr)
        return -1

    # get/create output if not exists
    output = output or f'build/themes/{info["name"]}.zip'
    output_dir = os.path.abspath(os.path.dirname(output))
    os.makedirs(output_dir, exist_ok=True)

    with zipfile.ZipFile(output, 'w') as zip_file:
        zip_file.write(info_filepath, 'info.json')
        zip_file.write(entrypoint_path, 'main.scss')
        # fonts
        fonts_dir = os.path.join(dirname, 'fonts')
        if os.path.exists(fonts_dir) and os.path.isdir(fonts_dir):
            for font in os.listdir(fonts_dir):
                font_path = os.path.join(fonts_dir, font)
                font_arcpath = os.path.join('fonts', font)
                zip_file.write(font_path, font_arcpath)
        # images
        images_dir = os.path.join(dirname, 'images')
        if os.path.exists(images_dir) and os.path.isdir(images_dir):
            for image in os.listdir(images_dir):
                image_path = os.path.join(images_dir, image)
                zip_file.write(image_path, os.path.join('images', image))
    return 0


if __name__ == '__main__':
    sys.exit(main())
