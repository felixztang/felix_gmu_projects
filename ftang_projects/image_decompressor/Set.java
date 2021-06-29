
//
// Task 1. Set<T> class (5%)
//
// A set of a data structure that stores only unique data
// This class is used in DisjointSets<T> to store actual data in the same sets
//

//You cannot import additonal items
import java.util.AbstractCollection;
import java.util.Iterator;
//You cannot import additonal items

//
//Hint: if you think really hard, you will realize this class Set<T> is in fact just a list
//      because DisjointSets<T> ensures that all values stored in Set<T> must be unique,
//      but should it be array list or linked list??
//




public class Set<T> extends AbstractCollection<T>
{
	
	//Must take O(1) time
        Node head;
        int size;
        static class Node<T>{
            T data;
            Node next;

            Node(T data){
                this.data = data;
                this.next = null;
            }
        }
        
        public Set(){
            this.head = null;
            size = 0;
        }
        
	public boolean add(T item)
	{
            Node node = this.head;
            if (node == null){
                this.head = new Node(item);
                size++;
                return true;
            }
            else{
                while(node.next != null){
                    node = node.next;
                }
                node.next = new Node(item);
                size++;
                return true;
            }
	}

	//Must take O(1) time
	public boolean addAll(Set<T> other)
	{
		Iterator<T> it = other.iterator();
                while(it.hasNext()){
                    add(it.next());
                }
                return true;
	}

	//Must take O(1) time
	public void clear()
	{
            head =  null;
	}

	//Must take O(1) time
	public int size()
	{
            return size;
	}
        
        public T getHead(){
            return (T)head.data;
        }
        
	public Iterator<T> iterator()
	{
		return new Iterator<T>()
		{
                        private Node<T> currentNode = null;
			public T next()
			{
                            if (currentNode == null){
                                currentNode = head;
                                return currentNode.data;
                            }
                            currentNode = currentNode.next;
                            return currentNode.data;
			}

			public boolean hasNext()
			{
                            if (currentNode == null){
                                return head != null;
                            }
                            else{
                                return currentNode.next != null;
                            }
			}
		};
	}
}

//bd4f0c8da4c44278947c5d1a79d71d1f
