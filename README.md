## Task 2: Extract Join Condition

To extract join condition from the query body, we first need to get the list of relationAtoms from the query body. Then, we can get a list of scan Operators, which in my code I use a **getScanOperatorList()** method, which will be used in the constructor of **Queryplanner** for initialization. Then to filter out those join conditions, we will use a **filterOutComparisonMultipleRelationalAtoms()** method to achieve it, where the method will filter out those comparison atoms, which has two variable terms such as : x>z, by iterating through comparison atom list and check if current comparison atom is a single selection comparison Atom such as : x>1, which can be achieved by using a **isSingleSelectionComparisonAtom()** method. The logic of the **isSingleSelectionComparisonAtom()** is summarized as follows:

1. It will first check if there is only one variable term in the comparison atom, if so, it means the current comparison atom is proved to be single comparison atom directly.
2. If there are two variable terms in the current comparison atom such as **x>z**, we need to make sure that, there are no scanOperators, whose relationAtom contains exactly one of the terms of the comparison term, which in this case is either **x or z** , since if we have a scanOperator ,whose relationAtom is liek R(x,y,3), it means, the comparison Atom **x>z**, will be used as a join condition, which in this case will return a false in the **isSingleSelectionComparisonAtom()** method.

So, when we iterating through a list of comparison atoms, for those comparison atoms that do not satisfy the condition of the  **isSingleSelectionComparisonAtom()** method, it will be regarded as a join condition.

## Task 3: Optimization

**Projection**

In Projection operator,  to remove the duplicate from the projected tuples,  we usually use a tuple to store the reported tuple. However, it will take up a lot of space. To optimize it, I rewrite the **toString()** method of Tuple, then instead of storing the tuple itself, the hash set will store string version of each tuple which is just an integer and hence can save a lot of space. 



**Query Planner**
In the class of query planner, it follows the principle of constructing selection Operator first and then perform the Join Operation, which can hence reduce the number of tuples before projection operation