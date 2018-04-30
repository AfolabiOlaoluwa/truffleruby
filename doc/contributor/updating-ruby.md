# Updating our version of Ruby

Before you do anything, check with Chris Seaton for clearance to upgrade.

## Create a diff against MRI

We want to create diffs of all changes we have applied on top of the last set
of files we got from MRI. Get the hash of the commit with the unmodified version
of the files from the last version of MRI. This will be called something like
`MRI a.b.c unmodified files`.

```
git diff <hash> HEAD -- lib/mri lib/cext/include src/main/c/openssl src/main/c/zlib test/mri/tests > ../mri.patch
```

## Import new files from MRI

Then we want to create a new commit with the new, unmodified files imported from
MRI. You need MRI checked out into `../ruby` at the version you want to import.
Then run `tool/import-mri-files.sh`.

## Create a reference commit of the imported unmodified files

So that we can create the diffs that we created in our first step in the future,
now create a commit called something like `MRI a.b.d unmodified files`.

```
git commit -am 'MRI a.b.d unmodified files'
```

## Restore our modifications on top of the imported unmodified files

```
git apply -3 ../mri.patch
git commit -am 'Restore MRI modifications'
```

## Make other changes

* Copy and paste `-h` and `--help` output to `RubyLauncher`
* Copy and paste the TruffleRuby `--help` output to `doc/user/options.md`.
* Update version information (version, base version, and revision) in `TruffleRuby`
* Update `doc/user/compatibility.md`
* Update `doc/legal/legal.md`
* Search for other instances of the old version number (there are a
  couple in tests). The version numbers may use `.` or `_` depending
  on context, and may be escaped as regular expressions so when
  searching it is best to use a regexp like `a\\*[._]b\\*[._]c` to try
  and find as many cases as possible.
* The version numbers of commands like `gem` may also have changed, so
  perform similar searches for those.

## Update libraries from third-party repos

Look in `../ruby/ext/json` to see the version of `flori/json` being used, and
then copy the original source of `flori/json` into `lib/json`.

## Updating bundled gems

The current list of bundled gems their versions are found at
https://github.com/ruby/ruby/blob/ruby_a_b/gems/bundled_gems (replace `_a_b`
with the right version branch for what you are importing). See if we need to
update any bundled gems.

To update a bundled gem, follow these steps:

* Remove the current gem and gemspec from `lib/gems/a.b.c/gems` and `lib/gems/a.b.c/specifications`
* Run the gem install command with the desired version. E.g. `gem install rake -v 10.4.2 --no-doc`
* Update the project `.gitignore` to allow the newly install gem sources and gemspec
* If the gem installs any executables like `rake` in `bin`. Add these to the `.gitignore` using `!bin/rake` if not already and ensure that the shebang has a format as follows:

```bash
#!/usr/bin/env bash
# ignored by Ruby interpreter

# get the absolute path of the executable and resolve symlinks
SELF_PATH=$(cd "$(dirname "$0")" && pwd -P)/$(basename "$0")
while [ -h "$SELF_PATH" ]; do
  # 1) cd to directory of the symlink
  # 2) cd to the directory of where the symlink points
  # 3) get the pwd
  # 4) append the basename
  DIR=$(dirname "$SELF_PATH")
  SYM=$(readlink "$SELF_PATH")
  SELF_PATH=$(cd "$DIR" && cd "$(dirname "$SYM")" && pwd)/$(basename "$SYM")
done
exec "$(dirname $SELF_PATH)/ruby" "$SELF_PATH" "$@"

#!ruby
# ^ marks start of Ruby interpretation

# ... the content of the executable
```
