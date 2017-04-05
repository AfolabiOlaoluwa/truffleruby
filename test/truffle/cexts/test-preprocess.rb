# Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved. This
# code is released under a tri EPL/GPL/LGPL license. You can use it,
# redistribute it and/or modify it under the terms of the:
#
# Eclipse Public License version 1.0
# GNU General Public License version 2
# GNU Lesser General Public License version 2.1

require_relative '../../../lib/cext/preprocess'

def test(input, expected=input)
  got = preprocess(input)
  raise "expected #{expected.inspect}, got #{got.inspect}" unless got == expected
end

test '  VALUE args[6], failed, a1, a2, a3, a4, a5, a6;',        '  VALUE failed, a1, a2, a3, a4, a5, a6; VALUE *args = truffle_managed_malloc(6 * sizeof(VALUE));'
test '  VALUE args, failed, a1, a2, a3, a4, a5, a6;'
test '  VALUE a,b[2],c;',                                       '  VALUE a, c; VALUE *b = truffle_managed_malloc(2 * sizeof(VALUE));'
test '  VALUEx, b, c;'
test '  VALUE *argv = alloca(sizeof(VALUE) * argc);',           '  VALUE *argv = truffle_managed_malloc(sizeof(VALUE) * argc);'
test '  VALUE *arg_v = (VALUE*) alloca(sizeof(VALUE) * argc);', '  VALUE *arg_v = truffle_managed_malloc(sizeof(VALUE) * argc);'
test '  long *argv = alloca(sizeof(long) * argc);'
test '  va_end(args);',                                         '  // va_end(args);'
