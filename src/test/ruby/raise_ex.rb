# Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved. This
# code is released under a tri EPL/GPL/LGPL license. You can use it,
# redistribute it and/or modify it under the terms of the:
#
# Eclipse Public License version 1.0
# GNU General Public License version 2
# GNU Lesser General Public License version 2.1

# Beware, RubyDebugTest use hard-coded line numbers from this file!

def shortArg(n)
  if n > 65535
    raise ArgumentError, 'Number too big', caller # Using a custom backtrace
  end
end

def throwMsg(msg)
  raise msg
end

shortArg 10
