/*
 * Copyright (c) 2015, 2017 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 1.0, or
 * GNU General Public License version 2, or
 * GNU Lesser General Public License version 2.1.
 */
package org.truffleruby.core.support;

import java.lang.ref.WeakReference;

import org.truffleruby.builtins.CoreClass;
import org.truffleruby.builtins.Primitive;
import org.truffleruby.builtins.PrimitiveArrayArgumentsNode;
import org.truffleruby.language.objects.ReadObjectFieldNode;
import org.truffleruby.language.objects.ReadObjectFieldNodeGen;
import org.truffleruby.language.objects.WriteObjectFieldNode;
import org.truffleruby.language.objects.WriteObjectFieldNodeGen;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.object.HiddenKey;

@CoreClass("Truffle::WeakRefOperations")
public abstract class WeakRefNodes {

    private static final WeakReference<Object> EMPTY_WEAK_REF = new WeakReference<>(null);
    private static final HiddenKey fieldName = new HiddenKey("weak_ref");

    @Primitive(name = "weakref_set_object")
    public static abstract class WeakRefSetObjectPrimitiveNode extends PrimitiveArrayArgumentsNode {

        @Child WriteObjectFieldNode fieldNode = WriteObjectFieldNodeGen.create(fieldName);

        @Specialization
        public Object weakRefSetObject(DynamicObject weakRef, Object object) {
            fieldNode.write(weakRef, new WeakReference<>(object));
            return object;
        }
    }

    @Primitive(name = "weakref_object")
    public static abstract class WeakRefObjectPrimitiveNode extends PrimitiveArrayArgumentsNode {

        @Child ReadObjectFieldNode fieldNode = ReadObjectFieldNodeGen.create(fieldName, EMPTY_WEAK_REF);

        @Specialization
        public Object weakRefObject(DynamicObject weakRef) {
            @SuppressWarnings("unchecked")
            final Object object = ((WeakReference<Object>) fieldNode.execute(weakRef)).get();
            if (object == null) {
                return nil();
            } else {
                return object;
            }
        }
    }

}
