package edu.smith.cs.csc212.lists;

import me.jjfoley.adt.ArrayWrapper;
import me.jjfoley.adt.ListADT;

/**
 * A GrowableList is also known as an ArrayList. It starts at a particular size
 * and grows as needed, replacing its inner array with a larger one when more
 * space is necessary.
 * 
 * @author jfoley
 *
 * @param <T> - the type of item stored in this list.
 */
public class GrowableList<T> extends ListADT<T> {
	/**
	 * How big should the initial list be?
	 * This is not private for use in tests.
	 */
	static final int START_SIZE = 10;
	/**
	 * This is the current array held by the GrowableList. It may be replaced.
	 */
	private ArrayWrapper<T> array;
	/**
	 * This is the number of elements in the array that are used.
	 */
	private int fill;

	/**
	 * Construct a new, empty, GrowableList.
	 */
	public GrowableList() {
		this.array = new ArrayWrapper<>(START_SIZE);
		this.fill = 0;
	}

	@Override
	public T removeFront() {
		this.checkNotEmpty();
		return removeIndex(0);
	}

	@Override
	public T removeBack() {
		this.checkNotEmpty();
		return removeIndex(fill - 1);
	}

	@Override
	public T removeIndex(int index) {
		checkNotEmpty();
		this.checkExclusiveIndex(index);
		// so we can return removed element
		T removed = array.getIndex(index);
		
		// slide to the left everything from index on
		for (int i = index; i < size()-1; i++) {
			array.setIndex(i, array.getIndex(i+1));
		}
		
		// last element is null
		array.setIndex(size()-1, null);
		
		// fill is smaller by 1
		fill--;
		
		return removed;
	}

	@Override
	public void addFront(T item) {
		addIndex(0, item);
	}

	@Override
	public void addBack(T item) {
		if (fill >= array.size()) {
			this.resizeArray();
		}
		array.setIndex(fill++, item);
	}

	/**
	 * This private method is called when we need to make room in our GrowableList.
	 */
	private void resizeArray() {
		// New array twice as large
		ArrayWrapper<T> larger = new ArrayWrapper<>(this.array.size() * 2);
		
		// Copy over elements of old array
		for (int i = 0; i < this.array.size(); i++) {
			larger.setIndex(i, this.array.getIndex(i));
		}
		
		// Replace old array with new larger one
		this.array = larger;
	}

	@Override
	public void addIndex(int index, T item) {
		// resize array if full already
		if (fill >= array.size()) {
			this.resizeArray();
		}
		
		// make sure this is an index that's in the list already
		// UNLESS adding to back
		if (index != size()) {
			checkExclusiveIndex(index);

		}

		// slide to the right
		for (int i = fill++; i > index; i--) {
			array.setIndex(i, array.getIndex(i-1));
		}
		
		// fill in new element
		array.setIndex(index, item);
	}

	@Override
	public T getFront() {
		checkNotEmpty();
		return this.getIndex(0);
	}

	@Override
	public T getBack() {
		checkNotEmpty();
		return this.getIndex(this.fill - 1);
	}

	@Override
	public T getIndex(int index) {
		checkNotEmpty();
		checkExclusiveIndex(index);
		return this.array.getIndex(index);
	}

	@Override
	public int size() {
		return this.fill;
	}

	@Override
	public boolean isEmpty() {
		return this.fill == 0;
	}

	@Override
	public void setIndex(int index, T value) {
		checkNotEmpty();
		checkExclusiveIndex(index);
		this.array.setIndex(index, value);
	}

}
