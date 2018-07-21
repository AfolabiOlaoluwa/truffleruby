/*
 * Copyright (c) 2014, 2017 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 1.0, or
 * GNU General Public License version 2, or
 * GNU Lesser General Public License version 2.1.
 */
package org.truffleruby.platform.graal;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.object.DynamicObject;
import org.truffleruby.language.RubyNode;
import org.truffleruby.language.control.RaiseException;

public abstract class AssertNotCompiledNode extends RubyNode {

    @Specialization
    public DynamicObject assertNotCompiled() {
        if (CompilerDirectives.inCompiledCode()) {
            compiledBoundary();
        }

        return nil();
    }

    @TruffleBoundary
    private void compiledBoundary() {
        throw new RaiseException(getContext(), coreExceptions().graalErrorAssertNotCompiledCompiled(this), true);
    }

}
