package edu.smith.cs.csc212.lists;

import me.jjfoley.adt.ArrayWrapper;
import me.jjfoley.adt.ListADT;
import me.jjfoley.adt.errors.RanOutOfSpaceError;

/**
 * FixedSizeList is a List with a maximum size.
 * @author jfoley
 *
 * @param <T>
 */
public class FixedSizeList<T> extends ListADT<T> {
	/**
	 * This is the array of fixed size.
	 */
	private ArrayWrapper<T> array;
	/**
	 * This keeps track of what we have used and what is left.
	 */
	private int fill;

	/**
	 * Construct a new FixedSizeList with a given maximum size.
	 * @param maximumSize - the size of the array to use.
	 */
	public FixedSizeList(int maximumSize) {
		this.array = new ArrayWrapper<>(maximumSize);
		this.fill = 0;
	}

	@Override
	public boolean isEmpty() {
		return this.fill == 0;
	}

	@Override
	public int size() {
		return this.fill;
	}

	@Override
	public void setIndex(int index, T value) {
		checkNotEmpty();
		this.checkExclusiveIndex(index);
		this.array.setIndex(index, value);
	}

	@Override
	public T getIndex(int index) {
		checkNotEmpty();
		this.checkExclusiveIndex(index);
		return this.array.getIndex(index);
	}

	@Override
	public T getFront() {
		checkNotEmpty();
		this.checkExclusiveIndex(0);
		return array.getIndex(0);
	}

	@Override
	public T getBack() {
		checkNotEmpty();
		this.checkExclusiveIndex(fill-1);
		return this.array.getIndex(fill-1);
	}

	@Override
	public void addIndex(int index, T value) {
		if (!isFull()) {
			// slide to the right
			for (int i = fill++; i > index; i--) {
				array.setIndex(i, array.getIndex(i-1));
			}
			
			// fill in new element
			array.setIndex(index, value);
		} else {
			throw new RanOutOfSpaceError();
		}
	}

	@Override
	public void addFront(T value) {
		addIndex(0, value);
	}

	@Override
	public void addBack(T value) {
		if (fill < array.size()) {
			array.setIndex(fill++, value); // why not fill? over one too many?
		} else {
			throw new RanOutOfSpaceError();
		}
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
	public T removeBack() {
		checkNotEmpty();
		return removeIndex(fill - 1);
	}

	@Override
	public T removeFront() {
		checkNotEmpty();
		return removeIndex(0);
	}

	/**
	 * Is this data structure full? Used in challenge: {@linkplain ChunkyArrayList}.
	 * 
	 * @return if true this FixedSizeList is full.
	 */
	public boolean isFull() {
		return this.fill == this.array.size();
	}
}
