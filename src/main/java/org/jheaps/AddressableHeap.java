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

import java.util.Comparator;

/**
 * A heap whose elements can be addressed.
 *
 * An insert operation returns a {@link AddressableHeap.Handle} which can later
 * be used in order to manipulate the element, such as decreasing its key, or
 * deleting it.
 *
 * @param <K>
 *            the type of keys maintained by this heap
 *
 * @author Dimitrios Michail
 */
public interface AddressableHeap<K> {

	/**
	 * A heap element handle. Allows someone to address an element already in
	 * the heap and perform additional operations.
	 *
	 * @param <K>
	 *            the type of keys maintained by this heap
	 */
	interface Handle<K> {

		/**
		 * Return the key of an element.
		 *
		 * @return the key of an element
		 */
		K getKey();

		/**
		 * Decrease the key of the element.
		 *
		 * @param newKey
		 *            the new key
		 * @throws IllegalArgumentException
		 *             if the new key is larger than the old key according to
		 *             the comparator used when constructing the heap or the
		 *             natural ordering of the elements if no comparator was
		 *             used
		 */
		void decreaseKey(K newKey);

		/**
		 * Delete the element from the heap that it belongs.
		 *
		 * @throws IllegalArgumentException
		 *             in case this function is called twice on the same element
		 */
		void delete();

	}

	/**
	 * Returns the comparator used to order the keys in this AddressableHeap, or
	 * {@code null} if this heap uses the {@linkplain Comparable natural
	 * ordering} of its keys.
	 *
	 * @return the comparator used to order the keys in this heap, or
	 *         {@code null} if this addressable heap uses the natural ordering
	 *         of its keys
	 */
	Comparator<? super K> comparator();

	/**
	 * Insert a new element into the heap.
	 *
	 * @param key
	 *            the element's key
	 * @return a handle for the newly added element
	 */
	AddressableHeap.Handle<K> insert(K key);

	/**
	 * Find an element with the minimum key.
	 *
	 * @return a handle to an element with minimum key
	 */
	AddressableHeap.Handle<K> findMin();

	/**
	 * Delete an element with the minimum key. If multiple such elements exists,
	 * only one of them will be deleted.
	 */
	void deleteMin();

	/**
	 * Returns {@code true} if this heap is empty.
	 *
	 * @return {@code true} if this heap is empty, {@code false} otherwise
	 */
	boolean isEmpty();

	/**
	 * Returns the number of elements in this heap.
	 *
	 * @return the number of elements in this heap
	 */
	long size();

	/**
	 * Clear all the elements of this heap.
	 */
	void clear();

}