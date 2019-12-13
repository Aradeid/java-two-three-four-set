## Description
This is a Java implementation for [2-3-4 binary trees](https://en.wikipedia.org/wiki/2%E2%80%933%E2%80%934_tree) also known as red-black trees.
Initially created as part of a university assignment, I now use it to showacase that I indeed do have some knowledge of Java programming.

## Disclaimer
The tests presented in this project have not been written by my, but instead were provided as part of the assignment.

## Documentation
The class' *TwoThreeFourTreeSet* attributes are used as follows:
- *values* - array of T, contains all values in the current node;
- *valCount* - number of values in array; used to increase efficiency;
- *children* - array of references to tree elements;
- *childCount* - number of child-branches;
- *parent* - reference to parent, if such exists;
- *comp* - comparator function for type T;

The datatype can be initialized with no parameters, resulting in *naturalOrder()* being used, or with a given comparator

*returnAsArray()* delivers an array of the tree sorted "In-Order".

All algorythm explanations can be found in comments.