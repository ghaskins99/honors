/*
 * (C) Copyright 2014-2016, by Dimitrios Michail.
 *
 * Java Heaps Library
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
package org.jheaps;

import java.io.Serializable;
import java.util.Comparator;

/**
 * A d-ary heap with a maximum number of elements.
 * 
 * An implicit d-ary heap implementation of the {@link Heap} interface. The heap
 * is sorted according to the {@linkplain Comparable natural ordering} of its
 * keys, or by a {@link Comparator} provided at heap creation time, depending on
 * which constructor is used.
 *
 * <p>
 * Implicit implementations of a heap use an array in order to store the
 * elements. This implementation automatically maintains the size of the array
 * much like a {@link java.util.Vector} does, providing amortized O(log(n)) time
 * cost for the {@code insert} and {@code deleteMin} operations. Operation
 * {@code findMin}, is a worst-case O(1) operation.
 *
 * <p>
 * Note that the ordering maintained by a d-ary heap, like any heap, and whether
 * or not an explicit comparator is provided, must be <em>consistent with
 * {@code equals}</em> if this heap is to correctly implement the {@code Heap}
 * interface. (See {@code Comparable} or {@code Comparator} for a precise
 * definition of <em>consistent with equals</em>.) This is so because the
 * {@code Heap} interface is defined in terms of the {@code equals} operation,
 * but a d-ary heap performs all key comparisons using its {@code compareTo} (or
 * {@code compare}) method, so two keys that are deemed equal by this method
 * are, from the standpoint of the d-ary heap, equal. The behavior of a heap
 * <em>is</em> well-defined even if its ordering is inconsistent with
 * {@code equals}; it just fails to obey the general contract of the
 * {@code Heap} interface.
 *
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong> If
 * multiple threads access a heap concurrently, and at least one of the threads
 * modifies the heap structurally, it <em>must</em> be synchronized externally.
 * (A structural modification is any operation that adds or deletes one or more
 * elements or changing the key of some element.) This is typically accomplished
 * by synchronizing on some object that naturally encapsulates the heap.
 *
 * @param <K>
 *            the type of keys maintained by this heap
 *
 * @author Dimitrios Michail
 * @see Heap
 * @see Comparable
 * @see Comparator
 * @see Serializable
 */
public class FixedSizeDaryHeap<K> extends AbstractDaryImplicitHeap<K> implements Serializable {

	private final static long serialVersionUID = 1;

	/**
	 * Constructs a new, empty heap, with a provided capacity using the natural
	 * ordering of its keys.
	 *
	 * <p>
	 * All keys inserted into the heap must implement the {@link Comparable}
	 * interface. Furthermore, all such keys must be <em>mutually
	 * comparable</em>: {@code k1.compareTo(k2)} must not throw a
	 * {@code ClassCastException} for any keys {@code k1} and {@code k2} in the
	 * heap. If the user attempts to put a key into the heap that violates this
	 * constraint (for example, the user attempts to put a string key into a
	 * heap whose keys are integers), the {@code insert(Object key)} call will
	 * throw a {@code ClassCastException}.
	 *
	 * @param d
	 *            the children of each node in the d-ary heap
	 * @param capacity
	 *            the heap capacity
	 */
	public FixedSizeDaryHeap(int d, int capacity) {
		super(d, null, capacity);
	}

	/**
	 * Constructs a new, empty heap, with a provided capacity ordered according
	 * to the given comparator.
	 *
	 * <p>
	 * All keys inserted into the heap must be <em>mutually comparable</em> by
	 * the given comparator: {@code comparator.compare(k1,
	 * k2)} must not throw a {@code ClassCastException} for any keys {@code k1}
	 * and {@code k2} in the heap. If the user attempts to put a key into the
	 * heap that violates this constraint, the {@code insert(Object key)} call
	 * will throw a {@code ClassCastException}.
	 *
	 * @param d
	 *            the children of each node in the d-ary heap
	 * @param comparator
	 *            the comparator that will be used to order this heap. If
	 *            {@code null}, the {@linkplain Comparable natural ordering} of
	 *            the keys will be used.
	 * @param capacity
	 *            the heap capacity
	 */
	public FixedSizeDaryHeap(int d, Comparator<? super K> comparator, int capacity) {
		super(d, comparator, capacity);
	}

	/**
	 * Create a heap from an array of elements. The elements of the array are
	 * not destroyed. The method has linear time complexity.
	 *
	 * @param <K>
	 *            the type of keys maintained by the heap
	 * @param d
	 *            the number of children of the d-ary heap
	 * @param array
	 *            an array of elements
	 * @return a binary heap
	 */
	@LinearTime
	public static <K> FixedSizeDaryHeap<K> heapify(int d, K[] array) {
		assert array != null && array.length > 0 && d >= 2;

		FixedSizeDaryHeap<K> h = new FixedSizeDaryHeap<K>(d, array.length);

		System.arraycopy(array, 0, h.array, 1, array.length);
		h.size = array.length;

		for (int i = array.length / d; i > 0; i--) {
			h.fixdown(i);
		}

		return h;
	}

	/**
	 * Create a heap from an array of elements. The elements of the array are
	 * not destroyed. The method has linear time complexity.
	 *
	 * @param <K>
	 *            the type of keys maintained by the heap
	 * @param d
	 *            the number of children of the d-ary heap
	 * @param array
	 *            an array of elements
	 * @param comparator
	 *            the comparator to use
	 * @return a binary heap
	 */
	@LinearTime
	public static <K> FixedSizeDaryHeap<K> heapify(int d, K[] array, Comparator<? super K> comparator) {
		assert array != null && array.length > 0 && d >= 2;

		FixedSizeDaryHeap<K> h = new FixedSizeDaryHeap<K>(d, comparator, array.length);

		System.arraycopy(array, 0, h.array, 1, array.length);
		h.size = array.length;

		for (int i = array.length / d; i > 0; i--) {
			h.fixdownWithComparator(i);
		}

		return h;
	}

	@Override
	protected void ensureCapacity(int capacity) {
		checkCapacity(capacity);
		if (capacity > array.length) {
			throw new IllegalStateException("Data structure has no extra space");
		}
	}

}