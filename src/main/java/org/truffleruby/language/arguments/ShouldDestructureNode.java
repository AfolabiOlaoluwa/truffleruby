/*
 * Copyright (c) 2013, 2019 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 2.0, or
 * GNU General Public License version 2, or
 * GNU Lesser General Public License version 2.1.
 */
package org.truffleruby.language.arguments;

import org.truffleruby.language.RubyContextSourceNode;
import org.truffleruby.language.RubyGuards;
import org.truffleruby.language.dispatch.DispatchNode;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.BranchProfile;

import static org.truffleruby.language.dispatch.DispatchConfiguration.PRIVATE_DOES_RESPOND;

public class ShouldDestructureNode extends RubyContextSourceNode {

    @Child private DispatchNode respondToToAry;

    private final BranchProfile checkIsArrayProfile = BranchProfile.create();

    @Override
    public Object execute(VirtualFrame frame) {
        if (RubyArguments.getArgumentsCount(frame) != 1) {
            return false;
        }

        checkIsArrayProfile.enter();

        final Object firstArgument = RubyArguments.getArgument(frame, 0);

        if (RubyGuards.isRubyArray(firstArgument)) {
            return true;
        }

        if (respondToToAry == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            respondToToAry = insert(DispatchNode.create(PRIVATE_DOES_RESPOND));
        }

        // TODO(cseaton): check this is actually a static "find if there is such method" and not a
        // dynamic call to respond_to?
        return respondToToAry.doesRespondTo(frame, "to_ary", firstArgument);
    }

}
