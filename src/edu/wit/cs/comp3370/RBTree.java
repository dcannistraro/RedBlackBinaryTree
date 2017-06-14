
package edu.wit.cs.comp3370;

/* Implements methods to use a red-black tree 
 * 
 * Wentworth Institute of Technology
 * COMP 3370
 * Lab Assignment 5
 * 
 */

public class RBTree extends LocationHolder {
	
	/* sets a disk location's color to red.
	 * 
	 * Use this method on fix-insert instead of directly
	 * coloring nodes red to avoid setting nil as red.
	 */
	private void setRed(DiskLocation z) {
		if (z != nil)
			z.color = RB.RED;
	}
	
	@Override
	public DiskLocation find(DiskLocation d) {
		if (root == null){ //empty tree so return nil
			return nil;
		}else{
			return _find(d, root);
		}
	}
	
	//recursive find for the tree
	public DiskLocation _find(DiskLocation d, DiskLocation curr){
		if (curr.equals(nil)){ //location doesn't exist
			return nil;
		}else if (d.equals(curr)){ //found the location
			return curr;
		}else if (d.isGreaterThan(curr)){ //if what we're searching for is larger, check next node. else, check previous node
			return _find(d, next(curr));
		}else{
			return _find(d, prev(curr));
		}
	}

	@Override
	public DiskLocation next(DiskLocation d) {
		DiskLocation next = d.right;
		if (!next.equals(nil)){ //if there is a right child find smallest child of that node, otherwise look upwards
			
			while(!next.left.equals(nil)){
				next = next.left;
			}
			
			return next;
		}
		else{
			return upNext(d);
		}
	}
	
	//traverse upward to find next node
	public DiskLocation upNext(DiskLocation d) {
		DiskLocation p = d.parent;
		if (p.equals(nil) || d == p.left){//if the child is to the left or parent is nil, return, otherwise recurse
			return p;
		}else{
			return upNext(p);
		}
	}

	@Override
	public DiskLocation prev(DiskLocation d) {
		DiskLocation prev = d.left;
		if (!prev.equals(nil)){ //if there is a left child find largest child of that node, otherwise look upwards
			
			while(!prev.right.equals(nil)){
				prev = prev.right;
			}
			
			return prev;
		}
		else{
			return upPrev(d);
		}
	}
	
	//travels upward to find previous node
	public DiskLocation upPrev(DiskLocation d) {
		DiskLocation p = d.parent;
		if (p.equals(nil) || d == p.right){ //if the child is to the right or parent is nil, return, otherwise recurse
			return p;
		}else{
			return upPrev(p);
		}
	}

	@Override
	public void insert(DiskLocation d) {
		
		//set children to nil and color them black
		d.right = nil;
		d.right.color = RB.BLACK;
		d.left = nil;
		d.left.color = RB.BLACK;
		
		//if the tree is empty insert at root and make parent nil
		if (root == null) {
			root = d;
			root.color = RB.BLACK;
			d.parent = nil;
			d.parent.color = RB.BLACK; //nil is black
			return;
		}
		
		//set new Location parent
		d.parent = findParent(d, root, nil);
		
		//set correct child of the parent to d to establish both links
		if (d.isGreaterThan(d.parent)){
			d.parent.right = d;
		}else{
			d.parent.left = d;
		}
		setRed(d); //set newly inserted node to red
		fixInsert(d); //insert rebalance
	}
	
	public void fixInsert(DiskLocation d){
		while (d.parent.color == RB.RED){ //while the parent is red
			
			if (d.parent == d.parent.parent.left){ //if the parent is a left child
				DiskLocation n = d.parent.parent.right; //store uncle
				if (n.color == RB.RED){ //if uncle is red fix colors
					d.parent.color = RB.BLACK;
					n.color = RB.BLACK;
					n.parent.color = RB.RED;
					d = d.parent.parent; //set d to grandparent
				}else{ //uncle is black
					if (d == d.parent.right){ //if d is a right child rotate
						d = d.parent;
						rotateLeft(d);
					}
					//update colors and rotate grandparent right
					d.parent.color = RB.BLACK;
					d.parent.parent.color = RB.RED;
					rotateRight(d.parent.parent);
				}
			}else{ //parent is a right child
				DiskLocation n = d.parent.parent.left; //store uncle
				if (n.color == RB.RED){ //if uncle is red fix colors
					d.parent.color = RB.BLACK;
					n.color = RB.BLACK;
					n.parent.color = RB.RED;
					d = d.parent.parent; //set d to grandparent
				}else{ //uncle is black
					if (d == d.parent.left){ //if d is a left child rotate
						d = d.parent;
						rotateRight(d);
					} 
					//update colors and rotate grandparent left
					d.parent.color = RB.BLACK;
					d.parent.parent.color = RB.RED;
					rotateLeft(d.parent.parent);
				}
			}
			
			root.color = RB.BLACK; //set root color to black
		}
	}

	@Override
	public int height() {
		if (root == null){ //empty tree
			return 0;
		}else{
			return _height(root);
		}
	}
	
	//recursive height function for the tree
	public int _height(DiskLocation d){
		if (d.equals(nil)){ //end of branch
			return 0;
		}else if(d.equals(root)){ //root has a value of 0
			return 0 + max(_height(d.left), _height(d.right));
		}else{ //add 1 and recurse down both children
			return 1 + max(_height(d.left), _height(d.right));
		}
	}
	
	//returns the larger integer of the two
	public int max(int a, int b){
		return a > b ? a : b;
	}
	
	//traverse the tree to find a parent for d
	public DiskLocation findParent(DiskLocation d, DiskLocation curr, DiskLocation p){
		if (curr.equals(nil)){ //open child spot
			return p;
		}else if (curr.isGreaterThan(d)){ //if current parent is greater than the value to be inserted traverse left
			return findParent(d, curr.left, curr);
		}else{ //otherwise traverse right
			return findParent(d, curr.right, curr);
		}
	}
	
	public void rotateLeft(DiskLocation d){
		//store original parent
		DiskLocation op = d.parent;
		
		//perform rotation
		d.parent = d.right;
		d.right = d.parent.left;
		if (d.right != nil){ //if right child isn't nil set parent
			d.right.parent = d;
		}
		d.parent.left = d;
		d.parent.parent = op;
		if (d != root){ //if d was not root set the original child relationship to the one rotated in
			if (d == op.left){
				op.left = d.parent;
			}else{
				op.right = d.parent;
			}
		}else{ //set the newly rotated node to the root
			root = d.parent;
		}
	}
	
	public void rotateRight(DiskLocation d){
		//store original parent
		DiskLocation op = d.parent;
				
		//perform rotation
		d.parent = d.left;
		d.left = d.parent.right;
		if (d.left != nil){ //if left child isn't nil set parent
			d.left.parent = d;
		}
		d.parent.right = d;
		d.parent.parent = op;
		if (d != root){ //if d was not root set the original child relationship to the one rotated in
			if (d == op.left){
				op.left = d.parent;
			}else{
				op.right = d.parent;
			}
		}else{ //set the newly rotated node to the root
			root = d.parent;
		}
	}
	
	//old rotateRight method
	public void _rotateRight(DiskLocation d){
		DiskLocation y = d.left;
				
		d.left = y.right;
		if (y.right != nil){ //if left child isn't nil set parent
			y.right.parent = d;
		}
		y.parent = d.parent;
		if (d.parent == nil){
			root = y;
		}else if(d == d.parent.left){
			d.parent.left = y;
		}else{
			d.parent.right = y;
		}
		y.right = d;
		d.parent = y;
	}

}
