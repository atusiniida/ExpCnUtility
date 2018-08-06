import java.util.*;
import java.util.zip.DataFormatException;
import java.io.*;


public class MyMat implements Serializable{
	private static final long serialVersionUID = -6849257508515306759L;
	protected double M[][];
	protected Map<String, Integer> colname2index;
	protected Map<String, Integer> rowname2index;
	protected int ncol;
	protected int nrow;
	protected List <String> colname;
	protected List <String> rowname;
	public MyMat(){	}
	public MyMat(int i, int j){
		nrow = i;
		ncol = j;
		colname = new ArrayList<String>();
		rowname = new ArrayList<String>();
		colname2index = new HashMap<String, Integer>();
		rowname2index = new HashMap<String, Integer>();
		M = new double[i][j];
		for(i=0;i<nrow;i++){
			for(j=0;j<ncol;j++){
		      M[i][j] = 0;
		    }	
		}
		for(i=1;i<=ncol;i++){
			colname.add("c"+i);
		}
		for(j=1;j<=nrow;j++){
			rowname.add("r"+j);
		}
		for(i=0;i<ncol;i++){
			colname2index.put(colname.get(i),i);
		 }
		for(i=0;i<nrow;i++){
			rowname2index.put(rowname.get(i),i);
	    }
	}
	
	public MyMat(List <String> row, List <String> col){
		nrow = row.size();
		ncol = col.size();
		M = new double[nrow][ncol];
		for(int i=0;i<nrow;i++){
			for(int j=0;j<ncol;j++){
		      M[i][j] = 0;
		    }	
		}
		setColNames(col);
		setRowNames(row);		
	}

	public MyMat(List <String> row, List <String> col, double[][] M){
		nrow = row.size();
		ncol = col.size();
		this.M = new double[nrow][ncol];
		for(int i=0;i<nrow;i++){
			for(int j=0;j<ncol;j++){
		      this.M[i][j] = M[i][j];
		    }	
		}
		setColNames(col);
		setRowNames(row);		
	}
	
	public MyMat(String infile) throws IOException, DataFormatException{
		colname = new ArrayList<String>();
		rowname = new ArrayList<String>();
		colname2index = new HashMap<String, Integer>();
		rowname2index = new HashMap<String, Integer>();
		BufferedReader inputStream = new BufferedReader(new FileReader(infile));
		List<String> lines = new ArrayList<String>();
		String line;
		nrow = -1;
		while((line = inputStream.readLine()) != null){
			if(line.charAt(0) == '#'){
				continue;				
			}
			lines.add(line);
			nrow++;
		}
		
		int i;	
		
		int l = 0;
		line = lines.get(0);
		List<String> str = Arrays.asList((line.split("\t")));
		ncol = str.size()-1;
		for(i = 1; i < str.size(); i++){
			if(colname.contains(str.get(i))){
				throw new DataFormatException("MyMat: file format is wrong (colnames are not unique)! \n");
			}
			colname.add(str.get(i));	
		}
			
		M = new double[nrow][ncol];
		for(l=1;l<=nrow;l++){
			line = lines.get(l);
			str = Arrays.asList(line.split("\t"));
			if(str.size() != ncol+1){
				throw new DataFormatException("MyMat: file format is wrong (column size is inconsistent) !\n");
			}
			if(rowname.contains(str.get(0))){
				throw new DataFormatException("MyMat: file format is wrong (rownames are not unique)! \n");
			}
			rowname.add(str.get(0));
			for(i=1; i<str.size(); i++){
				try{
					M[l-1][i-1] = Double.parseDouble(str.get(i));
				}catch(NumberFormatException e){
					throw new DataFormatException("MyMat: file format is wrong (an element is not Double)! \n");
					//continue;
					//M[l-1][i-1] = Double.NaN;
				}
			 }
		}
	   for(i=0;i<ncol;i++){
		 colname2index.put(colname.get(i),i);
	   }
	  for(i=0;i<nrow;i++){
		 rowname2index.put(rowname.get(i),i);
	  }
	  inputStream.close();
	}
	public static MyMat  readMyMatFromText(String infile) throws IOException, DataFormatException{
		return new MyMat(infile);
	}
	
	
	public MyMat(MyMat m){
		nrow = m.nrow;
		ncol = m.ncol;
		colname2index = new HashMap<String, Integer>(m.colname2index);
		rowname2index = new HashMap<String, Integer>(m.rowname2index);
		colname = new ArrayList<String>(m.colname);
		rowname = new ArrayList<String>(m.rowname);
		M = new double[nrow][ncol];
		int i,j;
		for(i=0;i<nrow;i++){
			for(j=0;j<ncol;j++){
				M[i][j] = m.M[i][j];
			}	
		}
	}
	public MyMat(String colname, Map<String, Double> rowname2value){
		nrow = rowname2value.size();
		ncol = 1;
		this.colname = new ArrayList<String>();
		rowname = new ArrayList<String>();
		colname2index = new HashMap<String, Integer>();
		rowname2index = new HashMap<String, Integer>();
		M = new double[nrow][ncol];
		for(int i=0;i<nrow;i++){
			for(int j=0;j<ncol;j++){
		      M[i][j] = 0;
		    }	
		}
		List <String> tmp = new ArrayList<String>();
		tmp.add(colname);
		setColNames(tmp);
		setRowNames(new ArrayList<String>(rowname2value.keySet()));		
		for(Map.Entry<String, Double> e: rowname2value.entrySet()){
			set(e.getKey(), colname, e.getValue());	
		}	
	}
	
	
	public boolean containsColName(String colname){
		return colname2index.containsKey(colname);
	}
	public boolean containsRowName(String rowname){
		return rowname2index.containsKey(rowname);
	}
	public int colIndexOf(String colname){
		return colname2index.get(colname);
	}
	public int rowIndexOf(String rowname){
		return rowname2index.get(rowname);
	}
	public double get(int i, int j){
		if(i >= nrow || j >= ncol){
			throw new IndexOutOfBoundsException();
		}
		return M[i][j];
	}
	public double get(String i, String j){
		return M[rowname2index.get(i)][colname2index.get(j)];
	}
	public void set(int i, int j, double d){
		M[i][j] = d;
	}
	public void set(String i, String j, double d){
		M[rowname2index.get(i)][colname2index.get(j)] = d;	
	}
	public void setRowNames(List <String> v){
		rowname = new ArrayList<String>(v);
		if(rowname2index == null){
			rowname2index = new HashMap<String, Integer>();
		}else{
			rowname2index.clear();
		}
		int i;
		for(i=0;i<nrow;i++){
			rowname2index.put(rowname.get(i),i);
		}
	}
	public void setColNames(List <String> v){
		colname = new ArrayList<String>(v);
		if(colname2index == null){
			colname2index = new HashMap<String, Integer>();
		}else{
			colname2index.clear();
		}
		int i;
		for(i=0;i<ncol;i++){
			colname2index.put(colname.get(i),i);
		}
	}
	public List<String> getRowNames(){
		return rowname;
	}
	public List<String> getColNames(){
		return colname;
	}
	public int rowSize(){
		return nrow;
	}
	public int colSize(){
		return ncol;
	}
	public void transpose(){
		int i,j;
		double[][] tmp = new double[ncol][nrow];
		for(i=0;i<nrow;i++){
			for(j=0;j<ncol;j++){
				tmp[j][i] = M[i][j];
		    }
		}
		M = tmp;
		i = nrow;
		nrow = ncol;
		ncol = i;
		List <String> tmp2 = new ArrayList<String>(rowname);
		setRowNames(colname);
		setColNames(tmp2);
	}
	@SuppressWarnings("unchecked")
	public void reorderRows(List row){
		List<Integer> v = new ArrayList<Integer>();
		int i,j;
		Set<Object>seen = new HashSet<Object>();
		for(i=0;i<row.size();i++){
			if(row.get(i) instanceof Integer){
				if((Integer)row.get(i) < nrow && !seen.contains(row.get(i)))
				v.add((Integer) row.get(i));
				seen.add(row.get(i));
				continue;	
			}
			if(row.get(i) instanceof String){
				if(rowname2index.containsKey(row.get(i)) && !seen.contains(row.get(i)))
				v.add(rowname2index.get(row.get(i)));	
				seen.add(row.get(i));
				continue;	
			}
			throw new IllegalArgumentException("Need Integer or String");	
		}
		List <String> new_rowname= new ArrayList<String>();
		double[][] new_M = new double[v.size()][ncol];
		nrow = v.size();
		for(i=0;i<nrow;i++){
		  new_rowname.add(rowname.get(v.get(i)));
		  for(j = 0; j< ncol; j++){
			  new_M[i][j] = M[v.get(i)][j];
		   }	
		}
		setRowNames(new_rowname);
		M = new_M;	
	}
	@SuppressWarnings("unchecked")
	public void reorderCols(List col){
		List<Integer> v = new ArrayList<Integer>();
		int i,j;
		Set<Object>seen = new HashSet<Object>();
		for(i=0;i<col.size();i++){
			if(col.get(i) instanceof Integer){
				if((Integer)col.get(i) < ncol && !seen.contains(col.get(i)))
				v.add((Integer) col.get(i));
				seen.add(col.get(i));
				continue;	
			}
			if(col.get(i) instanceof String){
				if(colname2index.containsKey(col.get(i)) && !seen.contains(col.get(i)))
				v.add(colname2index.get(col.get(i)));
				seen.add(col.get(i));
				continue;	
			}
			throw new IllegalArgumentException("Need Integer or String");	
		}
		List <String> new_colname= new ArrayList<String>();
		double[][] new_M = new double[nrow][v.size()];
		ncol = v.size();
		for(j=0;j<ncol;j++){
		  new_colname.add(colname.get(v.get(j)));
		  for(i = 0; i< nrow; i++){
			  new_M[i][j] = M[i][v.get(j)];
		   }	
		}
		setColNames(new_colname);
		M = new_M;	
	}
	public MyMat getSubMatrix(List row, List col){
		List<Integer> v = new ArrayList<Integer>();
		int i,j;
		Set<Object>seen = new HashSet<Object>();
		
		for(i=0;i<row.size();i++){
			if(row.get(i) instanceof Integer){
				if((Integer)row.get(i) < nrow && !seen.contains(row.get(i)))
				v.add((Integer) row.get(i));	
				seen.add(row.get(i));
				continue;	
			}
			if(row.get(i) instanceof String){
				if(rowname2index.containsKey(row.get(i)) && !seen.contains(row.get(i)))
				v.add(rowname2index.get(row.get(i)));
				seen.add(row.get(i));
				continue;	
			}
			throw new IllegalArgumentException("Need Integer or String");	
		}
		List <String> new_rowname= new ArrayList<String>();
		int new_nrow = v.size();
		for(i=0;i<new_nrow;i++){
		  new_rowname.add(rowname.get(v.get(i)));
		}
		List<Integer> u = new ArrayList<Integer>();
		seen.clear();
		for(j=0;j<col.size();j++){
			if(col.get(j) instanceof Integer){
				if((Integer)col.get(j) < ncol && !seen.contains(col.get(j)))
				u.add((Integer) col.get(j));
				seen.add(col.get(j));
				continue;	
			}
			if(col.get(j) instanceof String){
				if(colname2index.containsKey(col.get(j)) && !seen.contains(col.get(j)))
				u.add(colname2index.get(col.get(j)));	
				seen.add(col.get(j));
				continue;	
			}
			throw new IllegalArgumentException("Need Integer or String");	
		}
		List <String> new_colname= new ArrayList<String>();
		int new_ncol = u.size();
		for(j=0;j<new_ncol;j++){
		  new_colname.add(colname.get(u.get(j)));
		}
		MyMat newMat = new MyMat(new_rowname, new_colname);
		for(i = 0; i < new_nrow; i++){
			for(j = 0; j < new_ncol; j++){
				 newMat.M[i][j] = M[v.get(i)][u.get(j)];
				
			}
		}
		return newMat;
	}
	@SuppressWarnings("unchecked")
	public MyMat getSubMatByRow(List row){
		List<Integer> v = new ArrayList<Integer>();
		int i;
		Set<Object>seen = new HashSet<Object>();
		for(i=0;i<row.size();i++){
			if(row.get(i) instanceof Integer){
				if((Integer)row.get(i) < nrow && !seen.contains(row.get(i)))
				v.add((Integer) row.get(i));
				seen.add(row.get(i));
				continue;	
			}
			if(row.get(i) instanceof String){
				if(rowname2index.containsKey(row.get(i)) && !seen.contains(row.get(i)))
				v.add(rowname2index.get(row.get(i)));
				seen.add(row.get(i));
				continue;	
			}
			throw new IllegalArgumentException("Need Integer or String");	
		}
		List <String> new_rowname= new ArrayList<String>();
		for(i=0;i<v.size();i++){
		  new_rowname.add(rowname.get(v.get(i)));
		}
		
		MyMat new_M = new MyMat(new_rowname, colname);
		for(String s: new_rowname){
			for(String t: colname){
				new_M.set(s, t, get(s,t));		
			}
		}
		return new_M;	
	}
	
	
	@SuppressWarnings("unchecked")
	public MyMat getSubMatByCol(List col){
		List<Integer> v = new ArrayList<Integer>();
		int i,j;
		Set<Object>seen = new HashSet<Object>();
		for(i=0;i<col.size();i++){
			if(col.get(i) instanceof Integer){
				if((Integer)col.get(i) < ncol && !seen.contains(col.get(i)))
				v.add((Integer) col.get(i));
				seen.add(col.get(i));
				continue;	
			}
			if(col.get(i) instanceof String){
				if(colname2index.containsKey(col.get(i)) && !seen.contains(col.get(i)))
				v.add(colname2index.get(col.get(i)));
				seen.add(col.get(i));
				continue;	
			}
			throw new IllegalArgumentException("Need Integer or String");	
		}
		List <String> new_colname= new ArrayList<String>();
		for(j=0;j<v.size();j++){
		  new_colname.add(colname.get(v.get(j)));
		}
		MyMat new_M = new MyMat(rowname, new_colname);
		for(String s: rowname){
			for(String t: new_colname){
				new_M.set(s, t, get(s,t));
			}
			
		}
		return new_M;		
	}
	
	public List<Double> getRow(int i){
		List<Double> tmp = new ArrayList<Double>();
		int j;
		for(j = 0; j < ncol; j++){
			tmp.add(M[i][j]);
		}
		return tmp;
	}
	public List<Double> getRow(String i){
		List<Double> tmp = new ArrayList<Double>();
		int j;
		for(j = 0; j < ncol; j++){
			tmp.add(M[rowname2index.get(i)][j]);
		}
		return tmp;
	}
	public Map<String,Double> getRowMap(int i){
		Map<String, Double> tmp = new HashMap<String, Double>();
		int j;
		for(j = 0; j < ncol; j++){
			tmp.put(colname.get(j), M[i][j]);
		}
		return tmp;
	}
	public Map<String,Double> getRowMap(String i){
		Map<String, Double> tmp = new HashMap<String, Double>();
		int j;
		for(j = 0; j < ncol; j++){
			tmp.put(colname.get(j),M[rowname2index.get(i)][j]);
		}
		return tmp;
	}
	public List<Double> getCol(int j){
		List<Double> tmp = new ArrayList<Double>();
		int i;
		for(i = 0; i < nrow; i++){
			tmp.add(M[i][j]);
		}
		return tmp;
	}
	public List<Double> getCol(String j){
		List<Double> tmp = new ArrayList<Double>();
		int i;
		for(i = 0; i < nrow; i++){
			tmp.add(M[i][colname2index.get(j)]);
		}
		return tmp;
	}
	public Map<String,Double> getColMap(int j){
		Map<String, Double> tmp = new HashMap<String, Double>();
		int i;
		for(i = 0; i < nrow; i++){
			tmp.put(rowname.get(i), M[i][j]);
		}
		return tmp;
	}
	public Map<String,Double> getColMap(String j){
		Map<String, Double> tmp = new HashMap<String, Double>();
		int i;
		for(i = 0; i < nrow;i++){
			tmp.put(rowname.get(i),M[i][colname2index.get(j)]);
		}
		return tmp;
	}
	public void filterRowByVariance(double d){
		 SortedMap < Double, List<Integer>> sm = new TreeMap<Double, List<Integer>>();
		  int i;
		  for(i=0;i<nrow;i++){
			  double s = MyFunc.sd(getRow(i)); 
			  if(sm.containsKey(s)){
					 (sm.get(s)).add(i);
				 }else{
					 List<Integer> tmp = new ArrayList<Integer>();
					 tmp.add(i);
					 sm.put(s, tmp);
				 }
		  }
		  
		  List< Double > tmp = new ArrayList<Double>(sm.keySet());
		  Collections.reverse(tmp);
		 List<Integer> tmp2 = new ArrayList<Integer>(); 
		  if(d < 1){
		    d = Math.round(nrow*d);
		  } 
		  if(d >= nrow){
			  return;
		  }
		 for(i=0;;i++){
			 List<Integer> tmp3 = sm.get(tmp.get(i));
			 if(tmp3.size()+tmp2.size() <= d){
			  tmp2.addAll(sm.get(tmp.get(i)));
			 }else{
				break;
			 }
		 }
		 reorderRows(tmp2);
	}
	public MyMat bindRow(MyMat m){
		List <String> newRowname = new ArrayList<String>(rowname);
		newRowname.addAll(m.rowname);
		newRowname = MyFunc.uniq(newRowname);
		List <String> newColname = MyFunc.isect(colname, m.colname);
		MyMat tmp = new MyMat(newRowname, newColname);
		for(String t: tmp.colname){
			for(String s: tmp.rowname){
				if(containsRowName(s)){	
					tmp.set(s, t, get(s, t));
				}else{
					tmp.set(s, t, m.get(s, t));
				}
			}
		}	
		return tmp;
	}
	public MyMat addRow(String rowname, Map<String, Double> value){
		List <String> newRowname = new ArrayList<String>(this.rowname);
		newRowname.add(rowname);
		MyMat tmp = new MyMat(newRowname, colname);
		for(String t: colname){
			for(String s: this.rowname){
				tmp.set(s, t, get(s, t));
			}
			if(value.containsKey(t)){
				tmp.set(rowname, t, value.get(t));
			}
		}
		return tmp;
	}
	public MyMat bindCol(MyMat m){
		List <String> newColname = new ArrayList<String>(colname);
		newColname.addAll(m.colname);
		newColname = MyFunc.uniq(newColname);
		List <String> newRowname = MyFunc.isect(rowname, m.rowname);
		MyMat tmp = new MyMat(newRowname, newColname);
		for(String s: tmp.rowname){
			for(String t: tmp.colname){
				if(containsColName(t)){
				tmp.set(s, t, get(s, t));
			}else{
				tmp.set(s, t, m.get(s, t));
				}
			}
		}
		return tmp;
	}
	public MyMat addCol(String colname, Map<String, Double> value){
		List <String> newColname = new ArrayList<String>(this.colname);
		newColname.add(colname);
		MyMat tmp = new MyMat(rowname, newColname);
		for(String s: rowname){
			for(String t: this.colname){
				tmp.set(s, t, get(s, t));
			}
			if(value.containsKey(s)){
				tmp.set(s, colname, value.get(s));
			}
		}
		return tmp;
	}
	public MyMat bind(MyMat m){
		if(!MyFunc.isect(getRowNames(), m.getRowNames()).isEmpty()){
			return bindCol(m);
		}
		if(!MyFunc.isect(getRowNames(),m.getColNames()).isEmpty()){
			MyMat tmp = new MyMat(m);
			tmp.transpose();
			return bindCol(tmp);
		}
		if(!MyFunc.isect(getColNames(), m.getRowNames()).isEmpty()){
			MyMat tmp = new MyMat(m);
			tmp.transpose();
			return bindRow(tmp);
		}
		if(!MyFunc.isect(getColNames(),m.getColNames()).isEmpty()){
			return bindRow(m);
		}
		return this;		
	}
	
	public MyMat add(String name, Map<String, Double> value){
		if(!MyFunc.isect(getRowNames(),new ArrayList<String>(value.keySet())).isEmpty()){
			return addCol(name, value);
		}
		if(!MyFunc.isect(getColNames(), new ArrayList<String>(value.keySet())).isEmpty()){
			return addRow(name, value);
		}
		return this;
	}
	
	
	
	public MyMat multiply(MyMat m){
		if(ncol != m.nrow){
			throw new RuntimeException("colsize of the first MyMat must be equal to rowSize of the second MyMat");
		}
		MyMat tmp = new MyMat(nrow, m.ncol);
		  int i,j,k;
		  for(i = 0; i < nrow; i++){
		    for(j = 0; j < m.ncol; j++){
		      double tmp2 = 0;
		      for(k = 0; k < ncol; k++){
		        tmp2 += M[i][k] * m.get(k,j);
		      }
		      tmp.M[i][j] = tmp2;
		    }
		  }
		 tmp.setRowNames(rowname);
		 tmp.setColNames(m.colname);
		 return tmp;
	}
	public void normalizeRows(){
		int i,j;
		for(i=0;i<nrow;i++){
		  List <Double> tmp = getRow(i);
		  double tmp2 = MyFunc.mean(tmp);
		  double tmp3 = MyFunc.sd(tmp);
		  for(j=0;j<ncol;j++){
		     M[i][j] = (M[i][j] - tmp2)/tmp3;
		  }
		}		
	}
	public void normalizeCols(){
		int i,j;
		for(j=0;j<ncol;j++){
		  List <Double> tmp = getCol(j);
		  double tmp2 = MyFunc.mean(tmp);
		  double tmp3 = MyFunc.sd(tmp);
		  for(i=0;i<nrow;i++){
		     M[i][j] = (M[i][j] - tmp2)/tmp3;
		  }
		}		
	}
	
	public void normalizeAll(){
		int i,j;
		 double tmp = MyFunc.mean(asList());
		  double tmp2 = MyFunc.sd(asList());
		for(j=0;j<ncol;j++){
		  for(i=0;i<nrow;i++){
		     M[i][j] = (M[i][j] - tmp)/tmp2;
		  }
		}		
	}
	
	
	
	public void shuffleRows(){
	    List<String> new_rowname = new ArrayList<String>(rowname);
	    List<String> old_rowname = new ArrayList<String>(colname);
	    Collections.shuffle(new_rowname);
	    reorderRows(new_rowname);
	    setRowNames(old_rowname);
	}
	public void shuffleCols(){
	    List<String> new_colname = new ArrayList<String>(colname);
	    List<String> old_colname = new ArrayList<String>(colname);
	    Collections.shuffle(new_colname);
	    reorderCols(new_colname);	
	    setColNames(old_colname);
	}
	
	
	public void sortColsByValue(String rowname){
		Map <String,Double> tmp = getRowMap(rowname);
		List <String> tmp2 = MyFunc.sortKeysByAscendingOrderOfValues(tmp);
		reorderCols(tmp2);
	}
	public void sortRowsByValue(String colname){
		Map <String,Double> tmp = getRowMap(colname);
		List <String> tmp2 = MyFunc.sortKeysByAscendingOrderOfValues(tmp);
		reorderRows(tmp2);
	}
	
	public void sortRowsByRowNames(){
	    List<String> new_rowname = new ArrayList<String>(rowname);
	    Collections.sort(new_rowname);
	    reorderRows(new_rowname); 		
	}
	public void sortRowsByVariance(){
		 SortedMap < Double, List<Integer>> sm = new TreeMap<Double, List<Integer>>();
		  int i;
		  for(i=0;i<nrow;i++){
			 double s = MyFunc.sd(getRow(i)); 
			 if(sm.containsKey(s)){
				 (sm.get(s)).add(i);
			 }else{
				 List<Integer> tmp = new ArrayList<Integer>();
				 tmp.add(i);
				 sm.put(s, tmp);
			 }
		  }
		  List< Double > tmp = new ArrayList<Double>(sm.keySet());
		  Collections.reverse(tmp);
		 List<Integer> tmp2 = new ArrayList<Integer>(); 
		 for(i=0;i < tmp.size() ;i++){
		    tmp2.addAll(sm.get(tmp.get(i)));
		  }
		 reorderRows(tmp2);
		
	}
	public void print(String outfile) throws IOException {
		PrintWriter os = new PrintWriter(new FileWriter(outfile));
		os.println("\t"+  MyFunc.join("\t", getColNames())); 
		int i,j;
		for(i=0; i<nrow; i++){
			List<Double> tmp = getRow(i);
			List<String> tmp2 = new ArrayList<String>();
			for(j=0;j<ncol;j++){
				tmp2.add( (tmp.get(j)).toString() );
			}	
			os.println(rowname.get(i) + "\t"+  MyFunc.join("\t",tmp2));
		}
		os.flush();
		os.close();
	}
	public void print() throws IOException {
		PrintWriter os = new PrintWriter(System.out);
		os.println("\t"+  MyFunc.join("\t", getColNames())); 
		int i,j;
		for(i=0; i<nrow; i++){
			List<Double> tmp = getRow(i);
			List<String> tmp2 = new ArrayList<String>();
			for(j=0;j<ncol;j++){
				tmp2.add( (tmp.get(j)).toString() );
			}	
			os.println(rowname.get(i) + "\t"+  MyFunc.join("\t",tmp2));
		}
		os.flush();
		os.close();
	}
	public String toString(){
		StringBuffer S = new StringBuffer("\t"+  MyFunc.join("\t", getColNames()) + "\n");
		for(int i=0; i<nrow; i++){	
			S.append(getRowNames().get(i) + "\t"+  MyFunc.join("\t",MyFunc.toString(getRow(i))) + "\n");
		}
		return S.toString();
	}
	
	public boolean equals(MyMat m){
		if(colname != m.colname || rowname != m.rowname){
			return false;	
		}
		int i,j;
		for(i=0;i<nrow;i++){
			for(j=0;j<ncol;j++){
				if(M[i][j] == m.M[i][j]){
				  return false;
				}
			}
		}
		return true;
	}
	public List<Double> asList(){
		List <Double> v = new ArrayList<Double>();
		int i,j;
		for(i=0;i<nrow;i++){
			for(j=0;j<ncol;j++){
				v.add(M[i][j]);
			}
		}	
		return v;
	}
	
	
	public Map <String, Double> asMap(){
		Map <String, Double> m = new HashMap<String, Double>();
		for(String s: rowname){
			for(String t: colname){
				m.put(s + "\t" + t, get(s,t));	
			}
		}
		return m;		
	}
	
	
	public List<Double> getRowMeans(List <String> rows){
		 List <Double> v = new ArrayList<Double>();
		  int i,j;
		  for(j=0; j<ncol; j++){
		    double tmp = 0;
		    for(i=0; i<rows.size(); i++){
		      tmp += get(rowname2index.get(rows.get(i)),j);
		    }
		    v.add(tmp/rows.size()) ;
		  }
		  return v;
	}
	public List<Double> getRowMeans(){
		 List <Double> v = new ArrayList<Double>();
		  int i,j;
		  for(j=0; j<ncol; j++){
		    double tmp = 0;
		    for(i=0; i<nrow; i++){
		      tmp += get(i,j);
		    }
		    v.add(tmp/nrow) ;
		  }
		  return v;
	}
	public List<Double> getColMeans(List <String> cols){
		 List <Double> v = new ArrayList<Double>();
		  int i,j;
		  for(i=0; i<nrow; i++){
		    double tmp = 0;
		    for(j=0; j<cols.size(); j++){
		      tmp += get(rowname2index.get(cols.get(i)),j);
		    }
		    v.add(tmp/cols.size()) ;
		  }
		  return v;
	}
	public List<Double> getColMeans(){
		 List <Double> v = new ArrayList<Double>();
		  int i,j;
		  for(i=0; i<nrow; i++){
		    double tmp = 0;
		    for(j=0; j<ncol; j++){
		      tmp += get(i,j);
		    }
		    v.add(tmp/ncol) ;
		  }
		  return v;
	}
	
	
	
	/*public MyMatViewer getViewer(){
		return new MyMatViewer(this);
	}*/
	

	/*public DenseMatrix getMTJDenseMatrix(){
		return new DenseMatrix(M);
	}*/
	
	
	public MyMat getCovMatForRow(){
		MyMat M = new MyMat(this);
		List <Double>  ColMean =  M.getColMeans();
		for(int i = 0; i <  M.rowSize(); i++){
			for(int j = 0; j <  M.colSize(); j++){
				M.set(i, j, M.get(i, j)-ColMean.get(i));
			}	
		}
		MyMat Mt = new MyMat(M);
		Mt.transpose();
		M = M.multiply(Mt);
		for(int i = 0; i <  M.rowSize(); i++){
			for(int j = 0; j <  M.colSize(); j++){
				M.set(i, j, M.get(i, j)/ncol);
			}
		}
		return M;
		
	}
		
	public MyMat getCovMatForCol(){
		MyMat M = new MyMat(this);
		List <Double>  RowMean = M.getRowMeans();
		for(int i = 0; i <  M.rowSize(); i++){
			for(int j = 0; j <  M.colSize(); j++){
				M.set(i, j, M.get(i, j)-RowMean.get(j));
			}	
		}
		MyMat Mt = new MyMat(M);
		Mt.transpose();
		M =  Mt.multiply(M);
		for(int i = 0; i <  M.rowSize(); i++){
			for(int j = 0; j <  M.colSize(); j++){
				M.set(i, j, M.get(i, j)/nrow);
			}
		}
		return M;
	}
	
	/*public static Dist getDistBetweenRows(MyMat m, char type){
		 Dist dist = new Dist(m.getRowNames());
		 int i,j;
		 switch(type){
			case 'c':
				MyMat copy_m = new MyMat(m);
				copy_m.normalizeRows();
				for(i = 0; i < dist.size(); i++){
					for(j = 0; j < i; j++){
						dist.set(i, j, MyFunc.pearsonCorrelationForNormarizedList(copy_m.getRow(i),copy_m.getRow(j)));
				    }
				}
				dist.setDiagonalElement(1);
				break;
			case 'C':
				for(i = 0; i < dist.size(); i++){
					for(j = 0; j < i; j++){
						dist.set(i, j,  MyFunc.pearsonCorrelationForNormarizedList(m.getRow(i),m.getRow(j)));
				    }
				}
				dist.setDiagonalElement(1);
				break;
			case 'e':
				for(i = 0; i < dist.size(); i++){
					for(j = 0; j < i; j++){
						dist.set(i, j, MyFunc.euclideanDist(m.getRow(i),m.getRow(j)));
				    }
				}
				break;
			default:
				throw new IllegalArgumentException("type must be 'c','C', or 'e'");
		}
		return dist;
	}
	
	public static Dist getDistBetweenCols(MyMat m, char type){
		 Dist dist = new Dist(m.getColNames());
		 int i,j;
		 switch(type){
			case 'c':
				MyMat copy_m = new MyMat(m);
				copy_m.normalizeCols();
				for(i = 0; i < dist.size(); i++){
					for(j = 0; j < i; j++){
						dist.set(i, j,  MyFunc.pearsonCorrelationForNormarizedList(copy_m.getCol(i),copy_m.getCol(j)));
				    }
				}
				dist.setDiagonalElement(1);
				break;
			case 'C':
				for(i = 0; i < dist.size(); i++){
					for(j = 0; j < i; j++){
						dist.set(i, j, MyFunc.pearsonCorrelationForNormarizedList(m.getCol(i),m.getCol(j)));
				    }
				}
				dist.setDiagonalElement(1);
				break;
			case 'e':
				for(i = 0; i < dist.size(); i++){
					for(j = 0; j < i; j++){
						dist.set(i, j, MyFunc.euclideanDist(m.getCol(i),m.getCol(j)));
				    }
				}
				break;
			default:
				throw new IllegalArgumentException("type must be 'c','C', or 'e'");
		}
		return dist;
	}*/
	
	/*public double getInformationContent(MyMat m) throws NotConvergedException{
		 DenseMatrix M = m.getMTJDenseMatrix();
		 SVD svd = SVD.factorize(M);
		 double[] sv =  svd.getS();
		 double ic = 1-MatrixInformationEnrichmentAnalysis.shannonEntropy(sv);
		 return  ic;
	}*/
	

	public void printDimension(){
		System.out.println("ncol:\t" + ncol);
		System.out.println("nrow:\t" + nrow);
	}
	
	
	public void printStatistics(){
		List <Double> l  = asList();
 		System.out.println("mean:\t" + MyFunc.mean(l));
		System.out.println("sd:\t" + MyFunc.sd(l));
		System.out.println("max:\t" + MyFunc.max(l));
		System.out.println("min:\t" + MyFunc.min(l));
	}
	
	
	
	public void binarize(double d){
		for(int i= 0; i < nrow; i++){
			for(int j= 0; j < ncol; j++){
				if(get(i,j)>d){
					set(i,j,1);
				}else{
					set(i,j,0);
				}
			}
		}	
	}
	public void binarizeLess(double d){
		for(int i= 0; i < nrow; i++){
			for(int j= 0; j < ncol; j++){
				if(get(i,j)<d){
					set(i,j,1);
				}else{
					set(i,j,0);
				}
			}
		}	
	}
	public void ternarize(double d, double  d2){
		for(int i= 0; i < nrow; i++){
			for(int j= 0; j < ncol; j++){
				if(get(i,j)>d){
					set(i,j,1);
				}else if(get(i,j)<d2){
					set(i,j,-1);
				}else{
					set(i,j,0);
				}
			}
		}			
	}
	
	public double percentile(double d){
		List <Double> tmp = asList();
		return  MyFunc.percentile(tmp, d);
	}
	
}