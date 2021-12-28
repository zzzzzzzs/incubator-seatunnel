/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package io.github.interestinglab.waterdrop.config.impl;

import io.github.interestinglab.waterdrop.config.ConfigException;

// value is allowed to be null
final class ResolveResult<V extends AbstractConfigValue> {
    public final ResolveContext context;
    public final V value;

    private ResolveResult(ResolveContext context, V value) {
        this.context = context;
        this.value = value;
    }

    static <V extends AbstractConfigValue> ResolveResult<V> make(ResolveContext context, V value) {
        return new ResolveResult<V>(context, value);
    }

    // better option? we don't have variance
    @SuppressWarnings("unchecked")
    ResolveResult<AbstractConfigObject> asObjectResult() {
        if (!(value instanceof AbstractConfigObject))
            throw new ConfigException.BugOrBroken("Expecting a resolve result to be an object, but it was " + value);
        Object o = this;
        return (ResolveResult<AbstractConfigObject>) o;
    }

    // better option? we don't have variance
    @SuppressWarnings("unchecked")
    ResolveResult<AbstractConfigValue> asValueResult() {
        Object o = this;
        return (ResolveResult<AbstractConfigValue>) o;
    }

    ResolveResult<V> popTrace() {
        return make(context.popTrace(), value);
    }

    @Override
    public String toString() {
        return "ResolveResult(" + value + ")";
    }
}