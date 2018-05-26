package col106.a3;

import java.util.List;
import java.util.Vector;
//import java.util.ArrayList;

public class BTree<Key extends Comparable<Key>,Value> implements DuplicateBTree<Key,Value> {

    int t;
    node root;
    int pairs = 0;
    int height = -1;
	
	class node  {

	    /*Data Attributes*/
	    public node parent;
	    public Vector<Value> values = new Vector<Value>();
	    public Vector<Key> keys = new Vector<Key>();
	    public Vector<node> children = new Vector<node>();
	    public boolean isLeaf;

	    /*constructors*/
	    public node(node paren, Vector<Value> newValues, Vector<Key> newKeys, Vector<node> newChildren, boolean leaf){
	    	this.parent = paren;
	    	this.values = newValues;
	    	this.keys = newKeys;
	    	this.children = newChildren;
	    	this.isLeaf = leaf;
	    }

	    public String traverse(){
	    	int i=0;
	    	int flag = 0;
	    	String s = "[";
	    	for(i=0; i<this.keys.size();i++){
	    		if(flag == 1){
	    			s = s + ", ";
	    		}
	    		if(this.isLeaf==false){
	    			s = s + this.children.get(i).traverse() + ", ";
	    		}
	    		s = s + this.keys.get(i) + "=" + this.values.get(i);
	    		flag = 1;
	    	}

	    	if(this.isLeaf==false){
	    		s = s + ", " + this.children.get(i).traverse();
	    	}

	    	s = s + "]";
	    	return s;
	    }

	}


    public BTree(int b) throws bNotEvenException {  /* Initializes an empty b-tree. Assume b is even. */
        
        if(b%2!=0){
            throw new bNotEvenException();
        }
        else{
        t = b/2;
        root = new node(null, new Vector<Value>(), new Vector<Key>(), new Vector<node>(), true);
        }

    }

    @Override
    public boolean isEmpty() {
        if(pairs==0){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public int size() {
        return pairs;
    }

    @Override
    public int height() {
        return height;
    }

    class nodeAndKey{
    	node n;
    	int key;
    	public nodeAndKey(node o,int k){
    		n = o;
    		key = k;
    	}
    }

    private void find(node n, Key key, int h, List<Value> list){


    	if(key.compareTo(n.keys.get(0))<=0){
    		if(n.isLeaf==false) {
    			find(n.children.get(0), key, h-1, list);
    		}
    		else {
    
    		}
    	}

    	if(key.compareTo(n.keys.get(n.keys.size()-1))>=0){
    		if(n.isLeaf==false) {
    			find(n.children.get(n.children.size()-1), key, h-1, list);
    		}
    	}

    	int i=0;
    	for(i=0; i<n.keys.size(); i++){
    		if(n.keys.get(i).equals(key)){
    			list.add(n.values.get(i));

       //          if(n.isLeaf==false){
    		 //  	   find(n.children.get(i), key, h-1, list);

    			// if(i==n.keys.size()-1){
    			// 	find(n.children.get(i+1), key, h-1, list);
    			// }
       //          }

    		}
//            if(key.compareTo(n.keys.get(i))<0) {
//            	break;
//            }
    	}



    	if(n.isLeaf==false){
    		for(i=0; i<n.keys.size()-1; i++){
    			if(n.keys.get(i).compareTo(key)<=0 && n.keys.get(i+1).compareTo(key)>=0){
    				find(n.children.get(i+1), key, h-1, list);
    			}
    		}
    	}

    }

    @Override
    public List<Value> search(Key key) throws IllegalKeyException {

//        if(height==-1){
//            throw new IllegalKeyException();
//        }
        List<Value> list = new Vector<Value>();

        find(root, key, height, list);
       // if(list==null)
        	//throw new IllegalKeyException();

        return list;
        
    }

    private node split(node n, Key key){

    			Vector<Key> v1 =new Vector<Key>();
    			Vector<Key> v2 =new Vector<Key>();

    			Vector<Value> val1 =new Vector<Value>();
    			Vector<Value> val2 =new Vector<Value>();

    			Vector<node> c1 = new Vector<node>();
       			Vector<node> c2 = new Vector<node>();

    			for(int i=0; i<t-1; i++){
    				v1.add(n.keys.get(i));
    				val1.add(n.values.get(i));
    			}
    			for(int i=t; i<2*t-1; i++){
					v2.add(n.keys.get(i));
					val2.add(n.values.get(i));
				}

				for(int i=0; i<n.children.size()/2; i++){
					c1.add(n.children.get(i));
				}
				for(int i=n.children.size()/2; i<n.children.size(); i++){ 
					c2.add(n.children.get(i));
				}

				node child1 = new node(null, val1, v1, c1, n.isLeaf);
				node child2 = new node(null, val2, v2, c2, n.isLeaf);
                for(int i=0; i< n.children.size()/2; i++){
                    child1.children.get(i).parent = child1;
                    child2.children.get(i).parent = child2;
                }

                Key currKey;
                //node curr;
				if(n.equals(root)){
                    currKey = root.keys.get(t-1);
					Vector<Key> z = new Vector<Key>();
					z.add(root.keys.get(t-1));
					Vector<Value> zv = new Vector<Value>();
					zv.add(root.values.get(t-1));

					Vector<node> ch = new Vector<node>();
					ch.add(child1);
					ch.add(child2);

					root = new node(null, zv, z, ch, false);
					height= height +1;
					child1.parent = root;
					child2.parent = root;

				}
				else{
                    node y = n.parent;
                    int k = y.children.indexOf(n);
					if(k<y.keys.size()){
						y.keys.add(k,n.keys.get(t-1));
                        y.values.add(k,n.values.get(t-1));
					}
					else{
						y.keys.add(n.keys.get(t-1));
                        y.values.add(n.values.get(t-1));
					}

					y.children.set(k, child1);
                    child1.parent = y;
                    child2.parent = y;
//                    y.children.get(k).parent = y;

					if(k+1<y.children.size()){
						y.children.add(k+1, child2);
					}
					else{
						y.children.add(child2);
					}

                    currKey = y.keys.get(k);
				}

    				if(key.compareTo(currKey)<=0){
    					return child1;
    				}
    				else{
    					return child2;	
    				}
    }

    @Override
    public void insert(Key key, Value val) {
    	pairs++;
    	node x = root;
    	node y = null;

    	if(x.isLeaf==false && root.keys.size()==2*t-1) {
    		x = split(x, key);
    		root = x.parent;
    	}
    	
    	while(x.isLeaf==false){
    		int i=0;
    		if(key.compareTo(x.keys.get(0))<=0){
    			y = x.children.get(0);
				//find(n.children.get(0), key, h-1);
			}

			else if(key.compareTo(x.keys.get(x.keys.size()-1))>=0){
				y = x.children.get(x.children.size()-1);
			//find(n.children.get(n.children.size()-1), key, h-1);
			}
			else if(x.keys.size()>1){
				for(i=0; i<x.children.size()-1; i++){
					if(x.keys.get(i).compareTo(key)>0){   // && x.keys.get(i+1).compareTo(key)>=0
 					y = x.children.get(i);  //init get(i+1)
					break;
					//find(n.children.get(i+1), key, h-1);
					}
				}
			}

			    if(y.keys.size()!=2*t-1){
    				x = y;
    			}
    			else{
    				//node z = y.parent;
    				x = split(y, key);

//    				if(key.compareTo(z.keys.get(i))<0){
//    					x = z.children.get(i);
//    				}
//    				else{
//    					x = z.children.get(i+1);	
//    				}
    			}
    		
    	}
    	
    	if(x.keys.size()==2*t-1){
    		x = split(x, key);
    	}

    	int j=0;
    	for(j=0;j<x.keys.size();j++){
    		if(key.compareTo(x.keys.get(j))<0){
    			break;
    		}
    	}

    	if(x==root && x.keys.size()==0){
    		height = height + 1;
    	}

    	if(j<x.keys.size()){
    		x.keys.add(j, key);
    		x.values.add(j, val);
    	}
    	else{
    		x.keys.add(key);
    		x.values.add(val);
    	}
    }

    private boolean isPresent(node n, Key key){

        boolean present = false;
        if(n.keys.size()!=0) {
        if(key.compareTo(n.keys.get(0))<0){
        	if(n.isLeaf==false) {
            present = isPresent(n.children.get(0), key);
        	}
        }

        if(key.compareTo(n.keys.get(n.keys.size()-1))>0){
        	if(n.isLeaf==false) {
            present = isPresent(n.children.get(n.children.size()-1), key);
        	}
        }

        int i=0;
        for(i=0; i<n.keys.size(); i++){
            if(n.keys.get(i).equals(key)){
                present = true;
                return present;
            }
        }


        if(n.isLeaf==false){
        	if(n.keys.size()>1) {
        		for(i=0; i<n.keys.size(); i++){
        			if(n.keys.get(i).compareTo(key)<0 && n.keys.get(i+1).compareTo(key)>0){
        				if(n.isLeaf==false) {
        					present = isPresent(n.children.get(i+1), key);
        				}
                }
            }
        	}
        }
        }
        return present;

    }

    private void del(node x, Key key){

    	// nodeAndKey l = findOfDelete(root, key);

    	// node x = l.n;
    	// int i = l.key;

        int i = -1;

        boolean keyPresent = false;

        for(i=0; i<x.keys.size(); i++){
            if(x.keys.get(i).compareTo(key)==0){
                keyPresent = true;
                break;
            }
        }

        if(keyPresent==true){

        	if(x.isLeaf==true){
        		if(x.keys.size()>=t) {
	        		x.keys.remove(i);
	        		x.values.remove(i);
	        		pairs--;
	                return;
        		}
        		else {
        			if(x!=root){
	        			
	        			node y = x;
	        			x = y.parent;
	        			node z = null;
	        			boolean isNext = false;
	  			      	int j = x.children.indexOf(y);

	            		if(j!=x.children.size()-1) {
	            			if(x.children.get(j+1).keys.size()>=t){
	            				z = x.children.get(j+1);
	            				isNext = true;
	            			}
	            		}
	            		if(j!=0) {
	            			if(x.children.get(j-1).keys.size()>=t){
	            				z = x.children.get(j-1);
	            				isNext = false;
	            			}
	            		}
	            		if(z!=null){
		            		if(isNext==true){
		            			y.keys.remove(i);
		            			y.values.remove(i);
		                        y.keys.add(x.keys.get(j)); 
		                        y.values.add(x.values.get(j));
		                        x.keys.set(j, z.keys.get(0));
		            			x.values.set(j, z.values.get(0));
		            			z.keys.remove(0);
		            			z.values.remove(0);
		            			return;
		            		}
		            		else {
		            			y.keys.remove(i);
		            			y.values.remove(i);
		                        y.keys.add(0, x.keys.get(j-1)); 
		                        y.values.add(0, x.values.get(j-1));
		                        x.keys.set(j-1, z.keys.get(z.keys.size()-1));
		            			x.values.set(j-1, z.values.get(z.values.size()-1));
		            			z.keys.remove(z.keys.size()-1);
		            			z.values.remove(z.values.size()-1);
		            			return;            			
		            		}
	            		}

	            		else {
	  	            			if(j!=x.children.size()-1) {
		            			z = x.children.get(j);
	            				y.keys.set(0, x.keys.get(j));
		                        y.values.set(0, x.values.get(j));
		
		            			for(int l=0; l< z.keys.size(); l++){
		            			 y.keys.add(z.keys.get(l));
		                         y.values.add(z.values.get(l));
		                        }
		            			
		                        x.keys.remove(j);
		                        x.values.remove(j);
		
		                        if(x.equals(root)&&x.keys.size()==0) {
		                        	root = y;
		                        	height = height - 1;
		                        }
		                        return;
		            		}
	            			else {
		            			z = x.children.get(j-1);
	            				y.keys.set(0, x.keys.get(j-1));
		                        y.values.set(0, x.values.get(j-1));
		                        
		            			for(int l=0; l< y.keys.size(); l++){
			            			 z.keys.add(y.keys.get(l));
			                         z.values.add(y.values.get(l));
			                    }
		                        
		                        x.keys.remove(j-1);
		                        x.values.remove(j-1);
		
		                        if(x.equals(root)&&x.keys.size()==0) {
		                        	root = y;
		                        	height = height - 1;
		                        }
		                        return;
	            			}
	            		}
	        		}
        			else {
    	        		x.keys.remove(i);
    	        		x.values.remove(i);
    	        		pairs--;
    	        		if(x.keys.size()==0){
    	        			height--;
    	        		}
    	                return;
        			}
        		}

        	}

        	else{
        		node y = x.children.get(i);
        		node z = null;
        		if(i!=x.children.size()-1) {
        			z = x.children.get(i+1);
        		}

        		if(y.keys.size() >= t){
        			Key k = x.keys.get(i);
        			Value v = x.values.get(i);
        			x.keys.set(i, y.keys.get(y.keys.size()-1));
        			x.values.set(i, y.values.get(y.values.size()-1));
                    y.keys.set(y.keys.size()-1, k); 
                    y.values.set(y.values.size()-1, v);
        			del(y, key);
        		}
        		
        		else if(y.keys.size() < t && z.keys.size() >= t){
        			Key k = x.keys.get(i);
        			Value v = x.values.get(i);
					x.keys.set(i, z.keys.get(0));
					x.values.set(i, z.values.get(0));
					z.keys.set(0, k); 
                    z.values.set(0, v);
					del(z, key);
        		}
        		else if(y.keys.size()==t-1 && z.keys.size()==t-1){
        			y.keys.add(x.keys.get(i));
                    y.values.add(x.values.get(i));
                    int j =0;
        			for(j = 0; j< z.keys.size(); j++){
        			 y.keys.add(z.keys.get(j));
                     y.values.add(z.values.get(j));
                     if(y.isLeaf==false) {
                     y.children.add(z.children.get(j));
                     y.children.get(y.children.size()-1).parent = y;
                     }
                    }
        			
        			if(y.isLeaf==false) {
                    y.children.add(z.children.get(j));
                    y.children.get(y.children.size()-1).parent = y;
                    x.children.remove(i+1);
        			}
                    x.keys.remove(i);
                    x.values.remove(i);

                    if(x.equals(root)&&x.keys.size()==0) {
                    	root = y;
                    	y.parent = null;
                    	height = height - 1;
                    }
                    del(y, key);
        		}
        	}

        }

	    else{
                i=-1;

                boolean between = false;
                int j=0;
                
                if(key.compareTo(x.keys.get(0))<=0){
                    i = 0;
                }

                if(key.compareTo(x.keys.get(x.keys.size()-1))>=0){
                    i =x.children.size() -1;
                }

                else if(x.keys.size()>1) {
                	for(j=0; j<x.keys.size(); j++){
                    	if(x.keys.get(j).compareTo(key)<=0 && x.keys.get(j+1).compareTo(key)>=0){
                    		between = true;
                    		i = j+1;
                        	//break;
                    	}
                	}
                }

                if(i!=-1){
                    // i last child nhi hai aur first bhi nhi
                	if(i!=0 && i!=x.children.size() - 1) {
                        if(x.children.get(i).keys.size()==t-1){
                            if(x.children.get(i+1).keys.size()>=t){
                                    x.children.get(i).keys.add(x.keys.get(i));
                                    x.children.get(i).values.add(x.values.get(i));

                                    x.keys.set(i, x.children.get(i+1).keys.get(0));
                                    x.values.set(i, x.children.get(i+1).values.get(0));

                                    if(x.children.get(i).isLeaf==false){
                                    	x.children.get(i).children.add(x.children.get(i+1).children.get(0));
                                    	x.children.get(i).children.get(x.children.get(i).children.size()-1).parent = x.children.get(i);
                                    	x.children.get(i+1).children.remove(0);
                                	}

                                    x.children.get(i+1).keys.remove(0);
                                    x.children.get(i+1).values.remove(0);

                                    del(x.children.get(i), key);
                                    
                            }
                            else if(x.children.get(i-1).keys.size()>=t){
                                    x.children.get(i).keys.add(0, x.keys.get(i-1));
                                    x.children.get(i).values.add(0, x.values.get(i-1));

                                    x.keys.set(i-1, x.children.get(i-1).keys.get(x.children.get(i-1).keys.size()-1));
                                    x.values.set(i-1, x.children.get(i-1).values.get(x.children.get(i-1).values.size()-1));

                                    if(x.children.get(i).isLeaf==false){
	                                    x.children.get(i).children.add(0, x.children.get(i-1).children.get(x.children.get(i-1).children.size()-1));
	                                    x.children.get(i).children.get(0).parent = x.children.get(i);
                                    }
                                    if(x.children.get(i-1).isLeaf==false){
	                                    x.children.get(i-1).children.remove(x.children.get(i-1).children.size()-1);
	                                    x.children.get(i-1).keys.remove(x.children.get(i-1).keys.size()-1);
	                                    x.children.get(i-1).values.remove(x.children.get(i-1).values.size()-1);
                                    }
                                    del(x.children.get(i), key);
                            }
                            else if(x.children.get(i-1).keys.size()==t-1 && x.children.get(i+1).keys.size()==t-1){
                                // int j = 0;
                                //    if(i<x.children.size()-1){
                                        node y = x.children.get(i);
                                        node z = x.children.get(i+1);

                                        y.keys.add(x.keys.get(i));
                                        y.values.add(x.values.get(i));

                                         int l = 0;
                                        for(l=0; l<z.keys.size(); l++){
                                            y.keys.add(z.keys.get(l));
                                            y.values.add(z.values.get(l));

                                            if(y.isLeaf==false){
                                            	y.children.add(z.children.get(l));
                                            	y.children.get(y.children.size()-1).parent = y;
                                        	}
                                        }

                                        if(y.isLeaf==false){
                                        	y.children.add(z.children.get(l));
                                        	y.children.get(y.children.size()-1).parent = y;
                                        	y.children.remove(i+1);
                                    	}
                                        x.keys.remove(i);
                                        x.values.remove(i);

                                 		if(x.equals(root)&&x.keys.size()==0) {
		                        			root = y;
		                        			x = y;
		                        			height = height - 1;
		                        		}
                                del(y, key);

                              //      }

                            }
                        }
                    
                        else {
                        	del(x.children.get(i), key);
                        }
                    }
                	else if(i == x.children.size() - 1) {
                        if(x.children.get(i).keys.size()==t-1){
                        	 if(x.children.get(i-1).keys.size()>=t){
                                 x.children.get(i).keys.add(0, x.keys.get(i-1));
                                 x.children.get(i).values.add(0, x.values.get(i-1));

                                 x.keys.set(i-1, x.children.get(i-1).keys.get(x.children.get(i-1).keys.size()-1));
                                 x.values.set(i-1, x.children.get(i-1).values.get(x.children.get(i-1).values.size()-1));
                                 
                                 if(x.children.get(i).isLeaf==false){
                                	x.children.get(i).children.add(0, x.children.get(i-1).children.get(x.children.get(i-1).children.size()-1));
                                 	x.children.get(i).children.get(0).parent = x.children.get(i);
                                 }

                                 x.children.get(i-1).keys.remove(x.children.get(i-1).keys.size()-1);
                                 x.children.get(i-1).values.remove(x.children.get(i-1).values.size()-1);
                                 if(x.children.get(i-1).isLeaf==false){
                                	 x.children.get(i-1).children.remove(x.children.get(i-1).children.size()-1);
                                 }
                                    del(x.children.get(i), key);
                        	 }
                        	 else {
                                 node y = x.children.get(i-1);
                                 node z = x.children.get(i);

                                 y.keys.add(x.keys.get(i-1));
                                 y.values.add(x.values.get(i-1));

                                 int l = 0;
                                 for(l=0; l<z.keys.size(); l++){
                                     y.keys.add(z.keys.get(l));
                                     y.values.add(z.values.get(l));

                                     if(y.isLeaf==false) {
                                    	y.children.add(z.children.get(l));
                                     	y.children.get(y.children.size()-1).parent = y;
                                     }
                                 }
                                 
                                 if(y.isLeaf==false&&z.isLeaf==false) {
                                 y.children.add(z.children.get(l));
                                 y.children.get(y.children.size()-1).parent = y;
                                 y.children.remove(i);
                                 }
                                 x.keys.remove(i-1);
                                 x.values.remove(i-1);

                         		if(x.equals(root)&&x.keys.size()==0) {
                        			root = y;
                        			x = y;
                        			height = height - 1;
                        		}
                                del(y, key);
	                    	 }
                            }
                        else {
                        	del(x.children.get(i), key);
                        }
                	}
                	
                	else if(i == 0) {
                        if(x.children.get(i).keys.size()==t-1){
                            if(x.children.get(i+1).keys.size()>=t){
                                x.children.get(i).keys.add(x.keys.get(i));
                                x.children.get(i).values.add(x.values.get(i));

                                x.keys.set(i, x.children.get(i+1).keys.get(0));
                                x.values.set(i, x.children.get(i+1).values.get(0));

                                if(x.children.get(i).isLeaf==false) {
                                	x.children.get(i).children.add(x.children.get(i+1).children.get(0));
                                	x.children.get(i).children.get(x.children.get(i).children.size()-1).parent = x.children.get(i);
                                }

                                if(x.children.get(i).isLeaf==false) {
                                    x.children.get(i+1).children.remove(0);
                                }
                                x.children.get(i+1).keys.remove(0);
                                x.children.get(i+1).values.remove(0);
                                    del(x.children.get(i), key);
                            }
                            else {
                                node y = x.children.get(i);
                                node z = x.children.get(i+1);

                                y.keys.add(x.keys.get(i));
                                y.values.add(x.values.get(i));

                                 int l = 0;
                                for(l=0; l<z.keys.size(); l++){
                                    y.keys.add(z.keys.get(l));
                                    y.values.add(z.values.get(l));
                                    
                                    if(y.isLeaf==false) {
                                    y.children.add(z.children.get(l));
                                    y.children.get(y.children.size()-1).parent = y;
                                    }
                                }

                                if(y.isLeaf==false) {
                                y.children.add(z.children.get(l));
                                y.children.get(y.children.size()-1).parent = y;
                                }

                                x.keys.remove(i);
                                x.values.remove(i);
                                x.children.remove(i+1);
                                
                                if(x.equals(root)&&x.keys.size()==0) {
                                	y.parent = null;
                                	root = y;
                                	height = height - 1;
                                }
                                
                                del(y, key);
                            }
                        }
                        else {
                        	del(x.children.get(i), key);
                        }
                	}
                }
            }
    }

    @Override
    public void delete(Key key) throws IllegalKeyException {
        if(isPresent(root, key)==false){
            throw new IllegalKeyException();
        }

        while(isPresent(root, key)==true){
            	del(root,key);
        }

    }

    public String toString(){
    	return root.traverse();
    }
}
