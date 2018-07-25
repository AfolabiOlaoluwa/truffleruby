/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 1.0, or
 * GNU General Public License version 2, or
 * GNU Lesser General Public License version 2.1.
 */
package org.truffleruby.core.format.read.bytes;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.object.DynamicObject;
import org.jcodings.specific.USASCIIEncoding;
import org.truffleruby.core.format.FormatFrameDescriptor;
import org.truffleruby.core.format.FormatNode;
import org.truffleruby.core.format.MissingValue;
import org.truffleruby.core.rope.CodeRange;
import org.truffleruby.core.string.StringNodes;
import org.truffleruby.extra.ffi.Pointer;
import org.truffleruby.language.control.JavaException;
import org.truffleruby.language.control.RaiseException;
import org.truffleruby.language.objects.TaintNode;

@NodeChildren({
        @NodeChild(value = "value", type = FormatNode.class),
})
public abstract class ReadStringPointerNode extends FormatNode {

    @Child private StringNodes.MakeStringNode makeStringNode = StringNodes.MakeStringNode.create();

    private final int limit;

    public ReadStringPointerNode(int limit) {
        this.limit = limit;
    }

    @Specialization(guards = "isNil(nil)")
    public MissingValue decode(DynamicObject nil) {
        return MissingValue.INSTANCE;
    }

    @Specialization
    public Object read(VirtualFrame frame, long address,
                       @Cached("create()") TaintNode taintNode) {
        final Pointer pointer = new Pointer(address);

        try {
            checkAssociated((Pointer[]) frame.getObject(FormatFrameDescriptor.SOURCE_ASSOCIATED_SLOT), pointer);
        } catch (FrameSlotTypeException e) {
            CompilerDirectives.transferToInterpreter();
            throw new JavaException(e);
        }

        final byte[] bytes = pointer.readZeroTerminatedByteArray(getContext(), 0, limit);
        final DynamicObject string = makeStringNode.executeMake(bytes, USASCIIEncoding.INSTANCE, CodeRange.CR_7BIT);
        taintNode.executeTaint(string);
        return string;
    }

    @TruffleBoundary
    private void checkAssociated(Pointer[] associated, Pointer reading) {
        if (associated != null) {
            for (Pointer pointer : associated) {
                if (pointer.equals(reading)) {
                    return;
                }
            }
        }

        throw new RaiseException(getContext(), getContext().getCoreExceptions().argumentError("no associated pointer", this));
    }

}
