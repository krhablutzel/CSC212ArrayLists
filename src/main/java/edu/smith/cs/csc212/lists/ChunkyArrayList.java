package edu.smith.cs.csc212.lists;

import me.jjfoley.adt.ListADT;
import me.jjfoley.adt.errors.BadIndexError;
import me.jjfoley.adt.errors.EmptyListError;
import me.jjfoley.adt.errors.TODOErr;

/**
 * This is a data structure that has an array inside each node of an ArrayList.
 * Therefore, we only make new nodes when they are full. Some remove operations
 * may be easier if you allow "chunks" to be partially filled.
 * 
 * @author jfoley
 * @param <T> - the type of item stored in the list.
 */
public class ChunkyArrayList<T> extends ListADT<T> {
	/**
	 * How big is each chunk?
	 */
	private int chunkSize;
	/**
	 * Where do the chunks go?
	 */
	private GrowableList<FixedSizeList<T>> chunks;

	/**
	 * Create a ChunkedArrayList with a specific chunk-size.
	 * @param chunkSize - how many items to store per node in this list.
	 */
	public ChunkyArrayList(int chunkSize) {
		this.chunkSize = chunkSize;
		chunks = new GrowableList<>();
	}
	
	private FixedSizeList<T> makeChunk() {
		return new FixedSizeList<>(chunkSize);
	}
	
	@Override
	public T removeFront() {
		throw new TODOErr();
	}

	@Override
	public T removeBack() {
		checkNotEmpty();
		
		// remove end of last chunk
		T removed = chunks.getBack().removeBack();
		
		// remove last chunk if it's empty
		if (chunks.getBack().isEmpty()) {
			chunks.removeBack();
		}
		
		return removed;
	}

	@Override
	public T removeIndex(int index) {
		checkNotEmpty();
		
		// CAN'T USE same strategy as getIndex for finding where index is 
		// BECAUSE current modification exception
		
		throw new TODOErr();
	}

	@Override
	public void addFront(T item) {
		throw new TODOErr();
	}

	@Override
	public void addBack(T item) {
		// fix empty list to have one chunk
		if (this.isEmpty()) {
			chunks.addBack(makeChunk());
		}
		
		// if last chunk is full, add another
		if (chunks.getBack().isFull()) {
			chunks.addBack(makeChunk()); // TODO I suppose this could be its own method because I use it twice
		}
		
		// add to end of last chunk
		chunks.getBack().addBack(item);
	}

	@Override
	public void addIndex(int index, T item) {
		// THIS IS THE HARDEST METHOD IN CHUNKY-ARRAY-LIST.
		// DO IT LAST.
		
		int chunkIndex = 0;
		int start = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			// calculate bounds of this chunk.
			int end = start + chunk.size();
			
			// Check whether the index should be in this chunk:
			if (start <= index && index <= end) {
				if (chunk.isFull()) {
					// check can roll to next
					// or need a new chunk
					throw new TODOErr();
				} else {
					// put right in this chunk, there's space.
					throw new TODOErr();
				}	
				// upon adding, return.
				// return;
			}
			
			// update bounds of next chunk.
			start = end;
			chunkIndex++;
		}
		throw new BadIndexError(index);
	}
	
	@Override
	public T getFront() {
		return this.chunks.getFront().getFront();
	}

	@Override
	public T getBack() {
		return this.chunks.getBack().getBack();
	}


	@Override
	public T getIndex(int index) {
		if (this.isEmpty()) {
			throw new EmptyListError();
		}
		int start = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			// calculate bounds of this chunk.
			int end = start + chunk.size();
			
			// Check whether the index should be in this chunk:
			if (start <= index && index < end) {
				return chunk.getIndex(index - start);
			}
			
			// update bounds of next chunk.
			start = end;
		}
		throw new BadIndexError(index);
	}
	
	@Override
	public void setIndex(int index, T value) {
		checkNotEmpty();
		// same strategy as getIndex for finding where index is 
		int start = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			// calculate chunk bounds
			int end = start + chunk.size();
			
			// index in this chunk?
			if (start <= index && index < end) {
				chunk.setIndex(index - start, value);
				return;
			}
			
			// update bounds for next chunk
			start = end;
		}
		throw new BadIndexError(index);
	}

	@Override
	public int size() {
		int total = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			total += chunk.size();
		}
		return total;
	}

	@Override
	public boolean isEmpty() {
		return this.chunks.isEmpty();
	}
}