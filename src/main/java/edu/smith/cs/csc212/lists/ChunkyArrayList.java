package edu.smith.cs.csc212.lists;

import me.jjfoley.adt.ListADT;
import me.jjfoley.adt.errors.BadIndexError;

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
		checkNotEmpty();
		return removeIndex(0);
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
		 
		int start = 0;
		int i = 0;
		boolean found = false;
		for (i = 0; i < this.chunks.size(); i++) {
			// get chunk
			FixedSizeList<T> chunk = this.chunks.getIndex(i);
		
			// calculate bounds of chunk
			int end = start + chunk.size();
			
			// check wither index in this chunk
			if (start <= index && index < end) {
				// if so, stop looking over chunks
				found = true;
				break;
			}
			
			// update bounds of next chunk.
			start = end;
		}
		
		// Bad index
		if (!found) {
			throw new BadIndexError(index);
		}
		
		// remove element
		FixedSizeList<T> chunk = this.chunks.getIndex(i);
		T removed = chunk.removeIndex(index - start);
		
		// remove chunk if empty
		if (chunk.isEmpty()) {
			chunks.removeIndex(i);
		}
		
		return removed;

	}

	@Override
	public void addFront(T item) {
		addIndex(0, item);
	
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
		// fix empty list to have one chunk
		if (this.isEmpty()) {
			chunks.addBack(makeChunk());
		}
		
		int chunkIndex = 0;
		int start = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			// calculate bounds of this chunk.
			int end = start + chunk.size();
			
			// Check whether the index should be in this chunk:
			if (start <= index && index <= end) {
				if (chunk.isFull()) {
					// check there's space in next chunk
					// (if no next chunk or if next chunk is full, add chunk)
					if (chunkIndex + 1 == this.chunks.size() || this.chunks.getIndex(chunkIndex+1).isFull()) {
						this.chunks.addIndex(chunkIndex+1, makeChunk());
					}

					if (index - start == 3) {
						// actually belongs at start of next chunk
						this.chunks.getIndex(chunkIndex+1).addFront(item);
					} else {
						// roll end of chunk to next chunk
						this.chunks.getIndex(chunkIndex+1).addFront(this.chunks.getIndex(chunkIndex).removeBack());
												
						// add this item in newly vacated space
						this.chunks.getIndex(chunkIndex).addIndex(index - start, item);
					}
					
				} else {
					// put right in this chunk, there's space.
					this.chunks.getIndex(chunkIndex).addIndex(index - start, item);
				}	
				// upon adding, return.
				return;
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
		checkNotEmpty();

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