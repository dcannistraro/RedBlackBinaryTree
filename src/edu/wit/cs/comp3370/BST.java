package edu.wit.cs.comp3370;

public class BST extends LocationHolder {
	
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
		
		//set children to nil
		d.right = nil;
		d.left = nil;
		
		//if the tree is empty insert at root and make parent nil
		if (root == null) {
			root = d;
			d.parent = nil;
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
		}else if(d.equals(root)){ //root counts as 0
			return 0 + max(_height(d.left), _height(d.right));
		}else{ //add 1 and recurse down both children
			return 1 + max(_height(d.left), _height(d.right));
		}
	}
	
	//returns the larger integer of the two
	public int max(int a, int b){
		return a > b ? a : b;
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

}
