/*
 * Copyright (c) 2015, 2016 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 1.0
 * GNU General Public License version 2
 * GNU Lesser General Public License version 2.1
 */
package org.truffleruby.core.format.exceptions;

public class CantConvertException extends FormatException {

    private static final long serialVersionUID = -1748812990145250644L;

    public CantConvertException(String message) {
        super(message);
    }

}