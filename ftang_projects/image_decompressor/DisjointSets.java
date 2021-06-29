
// Task 1. DisjointSets class (10%)

// hint: you can use the DisjointSets from your textbook

import java.util.ArrayList;


// disjoint sets class, using union by size and path compression.
public class DisjointSets<T>
{
    //Constructor
    public DisjointSets( ArrayList<T> data  )
    {
      //your code here
      sets = new ArrayList<Set<T>>();
      s = new int [data.size()];
      for (int i = 0; i < s.length; i++) {
        s[i] = -1;
        Set<T> set = new Set<T>();
        set.add(data.get(i));
        sets.add(set);
      }
      
    }
    
    
    private void assertIsRoot(int root){
        assertIsItem(root);
        if (s[root] >= 0){
            throw new IllegalArgumentException();
        }
    }
    
    private void assertIsItem(int x){
        if (x<0 || x>s.length){
            throw new IllegalArgumentException();
        }
    }
    
    //Must have O(1) time complexity
    public int union( int root1, int root2 )
    {
     assertIsRoot(root1);
     assertIsRoot(root2);
     if (root1 == root2){
         throw new IllegalArgumentException();
      
     }
     if (s[root2] < s[root1]){
         s[root1] = root2; //root 2 is new root
         sets.get(root2).addAll(sets.get(root1));
         sets.set(root1, null);
         return root2;
     }
     else{
         if (s[root1] == s[root2]){
             s[root1]--;
         }
         s[root2] = root1; //root1 is new root
         sets.get(root1).addAll(sets.get(root2));
         sets.set(root2, null);
         return root1;
     }

    }
    
    //Must implement path compression
    public int find( int x )
    {
        if (s[x] < 0) {
          return x;                         // x is the root of the tree; return it
        } else {
          // Find out who the root is; compress path by making the root x's parent.
          s[x] = find(s[x]);
          return s[x];                                       // Return the root
        }
    }
    
    

    //Get all the data in the same set
    //Must have O(1) time complexity
    public Set<T> get( int root )
    {
      return sets.get(root);
    }

    //return the number of disjoint sets remaining
    // must be O(1) time
    public int getNumSets()
    {
            int size = 0;
            for (int i = 0; i<sets.size(); i++){
                if (sets.get(i) != null){
                    size++;
                }
            }
            return size;
    }


    
    //Data
    private int [ ] s;
    
    //This is the only data that is different from your textbook
    //An array of sets, each set stores unique values in the set
    //such as a set of pixels
    
    private ArrayList<Set<T>> sets;

}
