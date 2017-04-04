# MRI Tests

MRI tests are located in the `test/mri` directory.

## Running
MRI tests are run using the `jt` tool. By default these commands will exclude running tests that are ignored using test exclude files in the `test/mri/excludes_truffle` directory (more exclude details below).

### Run test suite
The `jt test mri` command is used to run all MRI test files specified in the `test/mri_truffle.index` file.

### Run a single test
A single MRI test can be run using the command `jt test mri ruby/test_time.rb`.

## Excluding tests 
Test excludes are created in the `test/mri/excludes_truffle` directory using the test's class name as the file name.

For example the `TestTime` class found in `test/mri/ruby/test_time.rb` has a corresponding exclude file: `test/mri/excludes_truffle/TestTime.rb`.

For a nested test class structure like `TestPrime::TestInteger` in `test_prime.rb`, the exclude directory for `TestInteger` is created in a `TestPrime` sub-directory.

The lines in exclude file appear like this:
```
exclude :test_asctime, "needs investigation"
exclude :test_at, "needs investigation"
```
Each line starts with the `exclude` method followed by two arguments: first the test method name to exclude, and then a comment describing the reason for the exclude.

There is no tool currently to assist in tagging/untagging tests so this is currently done by manually adding/removing the exclude lines from the test exclude files.
