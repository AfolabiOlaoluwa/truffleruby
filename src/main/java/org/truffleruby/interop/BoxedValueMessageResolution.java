/*
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 1.0, or
 * GNU General Public License version 2, or
 * GNU Lesser General Public License version 2.1.
 */
package org.truffleruby.interop;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.CanResolve;
import com.oracle.truffle.api.interop.MessageResolution;
import com.oracle.truffle.api.interop.Resolve;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.Node;

@MessageResolution(receiverType = BoxedValue.class)
public class BoxedValueMessageResolution {

    @CanResolve
    public abstract static class Check extends Node {

        protected static boolean test(TruffleObject receiver) {
            return receiver instanceof BoxedValue;
        }

    }

    @Resolve(message = "IS_BOXED")
    public static abstract class ForeignIsBoxedNode extends Node {

        protected Object access(VirtualFrame frame, BoxedValue number) {
            return true;
        }

    }

    @Resolve(message = "UNBOX")
    public static abstract class ForeignUnboxNode extends Node {

        protected Object access(VirtualFrame frame, BoxedValue number) {
            return number.getNumber();
        }

    }

}
