package swe4.collections;

import java.util.Comparator;
import java.util.Iterator;

public interface SortedSet<T> extends Iterable<T> {
	 boolean add(T elem); // adds element to set; returns false if element already exists
	 T get(T elem); // returns reference to given element in set; null if not found
	 boolean contains(T elem); // returns true if element iss resent in set
	 int size(); // returns current number of elements in set
	 T first(); // returns smallest element in set
	 T last(); // returns largest element in set
	 Comparator<T> comparator(); // 
	 Iterator<T> iterator(); // returns iterator that visits all elements ascending
}
