/*
 * (C) Copyright 2014-2016, by Dimitrios Michail
 *
 * JHeaps Library
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jheaps.minmax;

import java.util.Comparator;

import org.jheaps.AddressableHeap;
import org.jheaps.tree.AbstractAddressableHeapTest;

public class ReflectedPairingHeapAddressableHeapTest extends AbstractAddressableHeapTest {

    @Override
    protected AddressableHeap<Integer, Void> createHeap() {
        return new ReflectedPairingHeap<Integer, Void>(null);
    }

    @Override
    protected AddressableHeap<Integer, Void> createHeap(Comparator<Integer> comparator) {
        return new ReflectedPairingHeap<Integer, Void>(comparator);
    }

    @Override
    protected AddressableHeap<Integer, String> createHeapWithStringValues() {
        return new ReflectedPairingHeap<Integer, String>(null);
    }
}
