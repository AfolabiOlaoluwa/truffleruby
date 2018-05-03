/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 1.0
 * GNU General Public License version 2
 * GNU Lesser General Public License version 2.1
 */
package org.truffleruby.stdlib.readline;

import com.oracle.truffle.api.object.DynamicObject;
import org.truffleruby.RubyContext;
import org.truffleruby.core.adapters.InputStreamAdapter;
import org.truffleruby.core.adapters.OutputStreamAdapter;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * A simple file descriptor -> IO stream class.
 *
 * STDIO FDs will map to the polyglot STDIO streams. For all other IO, a valid Ruby IO object
 * must be provided.
 */
public class IoStream {

    private final RubyContext context;
    private final int fd;
    private final DynamicObject io;
    private InputStream in;
    private OutputStream out;

    public IoStream(RubyContext context, int fd, DynamicObject io) {
        this.context = context;
        this.fd = fd;
        this.io = io;
    }

    public int getFd() {
        return fd;
    }

    public DynamicObject getIo() {
        return io;
    }

    public InputStream getIn() {
        if (in == null) {
            if (fd == 0) {
                in = context.getEnv().in();
            } else {
                in = new InputStreamAdapter(context, io);
            }
        }

        return in;
    }

    public OutputStream getOut() {
        if (out == null) {
            switch (fd) {
                case 1:
                    out = context.getEnv().out();
                    break;

                case 2:
                    out = context.getEnv().err();
                    break;

                default:
                    out = new OutputStreamAdapter(context, io, context.getEncodingManager().getDefaultExternalEncoding());
            }
        }

        return out;
    }

}
