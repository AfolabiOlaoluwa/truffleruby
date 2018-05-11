# Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved. This
# code is released under a tri EPL/GPL/LGPL license. You can use it,
# redistribute it and/or modify it under the terms of the:
# 
# Eclipse Public License version 1.0
# GNU General Public License version 2
# GNU Lesser General Public License version 2.1

require_relative '../../ruby/spec_helper'

describe "TruffleRuby.full_memory_barrier" do
  
  it "can be called" do
    TruffleRuby.full_memory_barrier.should be_nil
  end
  
end
