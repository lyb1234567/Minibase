## Task 3: Optimization

**Projection**

In Projection operator,  to remove the duplicate from the projected tuples,  we usually use a tuple to store the reported tuple. However, it will take up a lot of space. To optimize it, I will first hash the corresponding tuple and get its hash code, then instead of storing the tuple itself, the hashset will store the generated hash value, which is just an integer and hence can save a lot of space. 



**Selection**

In queryplanner, when the planner try to join the selection operator, we will check if there is a implicit constant in the selection operator, such as T(x,1), if so we will remove the constant column by using projection operator. For example,if the getNextTuple() method get a Tuple [3,1] in this case, the planner will project out the 3 for the following join operator and hence save the space.