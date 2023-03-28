## Task 3: Optimization

**Projection**

In Projection operator,  to remove the duplicate from the projected tuples,  we usually use a tuple to store the reported tuple. However, it will take up a lot of space. To optimize it, I will first hash the corresponding tuple and get its hash code, then instead of storing the tuple itself, the hashset will store the generated hash value, which is just an integer and hence can save a lot of space. 