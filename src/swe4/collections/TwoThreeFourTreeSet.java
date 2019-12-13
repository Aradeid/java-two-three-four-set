package swe4.collections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class TwoThreeFourTreeSet<T extends Comparable <T>> implements SortedTreeSet<T> {
	protected T[] values = (T[])new Object[3]; // stores all values of current node
	protected int valCount = 0;
	protected TwoThreeFourTreeSet<T>[] children = new TwoThreeFourTreeSet[4]; // stores all references to children
	protected int childCount = 0;
	
	protected TwoThreeFourTreeSet<T> parent = null;
	protected Comparator<T> comp;
	
	public TwoThreeFourTreeSet(Comparator<T> c) {
		comp = c;
	}
	
	public TwoThreeFourTreeSet() {
		this(Comparator.<T>naturalOrder());
	}
	
	// sends elem down until a children-less node is found; saves elem at found node; expand parent if countVal reaches 3
	@Override
	public boolean add(T elem) {
		if (this.contains(elem)) { // no duplicates
			return false;
		} else if (valCount < 3) { // if has space for more
			if (childCount == 0) { // add elem as direct descendant
				for (int i = 0; i < valCount; ++i) {
					if (comp.compare(elem,values[i]) < 0) {
						for (int j = 2; j > i; --j) { // shifts all other elements by 1
							values[j] = values[j - 1];
						}
					}
					values[i] = elem;
					++valCount;
					return true;
				}
			} else { // add elem as indirect descendant
				for (int i = 0; i < childCount; ++i) { // searching with which child elem belongs
					if (comp.compare(elem,values[i]) < 0) {
						return children[i].add(elem);
					}
				}
				return children[childCount].add(elem); // elem is greater than all other children and should be put rightmost
			}
		} else { // node is 4-tree and should be reduced
			TwoThreeFourTreeSet<T> tempLeft = new TwoThreeFourTreeSet<T>(); // generating new left and right children of reduced node
			TwoThreeFourTreeSet<T> tempRight = new TwoThreeFourTreeSet<T>();
			
			tempLeft.values[0] = values[0];
			tempLeft.valCount = 1;
			tempLeft.children[0] = this.children[0];
			tempLeft.children[1] = this.children[1];
			tempLeft.childCount = 2;
			
			tempRight.values[0] = values[2];
			tempRight.valCount = 1;
			tempRight.children[0] = this.children[2];
			tempRight.children[1] = this.children[3];
			tempRight.childCount = 2;
			
			if (parent == null) { // if no parent, create new node with middle value
				TwoThreeFourTreeSet<T> tempParent = new TwoThreeFourTreeSet<T>();
				tempParent.values[0] = values[1];
					
				tempParent.children[0] = tempLeft;
				tempParent.children[1] = tempRight;
				tempParent.childCount = 2;
				this.parent = tempParent;
			} else { // else parent exists, and current node should be added
				if (comp.compare(parent.values[0],values[1]) < 0) { // if node belongs leftmost
					if (parent.valCount > 1) {
						// shift right element by 1
						parent.values[2] = parent.values[1];
						parent.children[3] = parent.children[2];
					}
					// shift left element by 1
					parent.values[1] = parent.values[0];
					parent.children[2] = parent.children[1];
					
					// push left;
					parent.values[0] = values[1];
					parent.children[0] = tempLeft;
					parent.children[1] = tempRight;
				} else if (parent.valCount < 2 || comp.compare(parent.values[1],values[1]) < 0) { // if node belongs in center
					if (parent.valCount > 1) { // should we shift?
						parent.values[2] = parent.values[1];
						parent.children[3] = parent.children[2];
					}
					// push center
					parent.values[1] = values[1];
					parent.children[1] = tempLeft;
					parent.children[2] = tempRight;
				} else { // if node belongs rightmost
					// push right
					parent.values[2] = values[1];
					parent.children[2] = tempLeft;
					parent.children[3] = tempRight;
				}
				
			}
			TwoThreeFourTreeSet<T> newTree = this;
			newTree = newTree.parent; // changing this indirectly
			++newTree.valCount;
			return newTree.add(elem);
		}
		return false;
	}

	// algorithm is identical to contains();
	// creates a single-directional route from root to elem; if bottom is reached, no elem exists
	@Override
	public T get(T elem) {
		for (int i = 0; i < valCount; ++i) {
			if (values[i].equals(elem)) {
				return values[i];
			} else if (comp.compare(values[i],elem) < 0) { // finds the set of children that can possibly contain elem
				return children[i].get(elem);
			}
		}
		if (childCount > 1) { // check final set of children
			return children[valCount - 1].get(elem);
		}
		return null;
	}

	// algorithm is identical to get()
	@Override
	public boolean contains(T elem) {
		for (int i = 0; i < valCount; ++i) {
			if (values[i].equals(elem)) { // if current value is same
				return true;
			} else if (comp.compare(values[i],elem) < 0) { // finds the set of children that can possibly contain elem
				return children[i].contains(elem);
			}
		}
		if (childCount > 1) { // check final set of children
			return children[valCount - 1].contains(elem);
		}
		return false;
	}

	// returns valCount and passes on the function to its children
	@Override
	public int size() {
		int s = valCount; // saves value of current node
		for (int i = 0; i < childCount; ++i) { // then checks all children
			s += children[i].size();
		}
		return s;
	}

	@Override
	public T first() {
		if (childCount > 0) { // goes left until no more children
			return children[0].first();
		} else {
			return values[0];
		}
	}

	@Override
	public T last() {
		if (childCount > 1) { // goes right until no more children
			return children[childCount - 1].last();
		} else {
			return values[valCount - 1];
		}
	}

	@Override
	public Comparator<T> comparator() {
		if (comp == Comparator.<T>naturalOrder()) {
			return null;
		}
		return comp;
	}

	@Override
	public Iterator<T> iterator() {
		return new TwoFreeFourTreeIterator();
	}

	// checks each possible route to find the longest one;
	// partially redundant in 2-3-4 Tree as all routes have identical length
	// the algorithm from first() could be used with faster time and identical result
	@Override
	public int height() {
		if (this.valCount == 0) { // if empty leaf
			return 0;
		}
		int max = 1; 
		for (int i = 0; i < childCount; ++i) { // finds longest route among children
			max = Math.max(max, children[i].height() + 1);
		}
		return max;
	}
	
	// generates array of tree, traversing it in-order
	public ArrayList<T> returnAsArray(ArrayList<T> arr) {
		if (arr == null) {
			arr = new ArrayList<T>();
		}
		if (this == null) {
			return arr;
		}
		if (this.valCount == 0) {
			return arr;
		}
		for (int i = 0; i < childCount; ++i) {
			this.children[i].returnAsArray(arr);
			if (i < childCount - 1) {
				arr.add(values[i]);
			}
		}
		return arr;
	}
	
	private class TwoFreeFourTreeIterator implements Iterator<T> {
		ArrayList<T> arr = returnAsArray(null); // generates tree as array, then calls each index
		int index = -1;

		@Override
		public boolean hasNext() {
			return (index + 1 < arr.size());
		}

		@Override
		public T next() {
			return arr.get(++index);
		}
		
	}
}
