import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

//import org.apache.commons.math.MathException;
//import org.apache.commons.math.distribution.*;


public class MyFunc{
	public static double mean(List<Double> v){	
		 double m = 0;	
		  for(int i = 0, n = v.size(); i < n; i++){
		    m += v.get(i);
		  }
		  return m/v.size();
	}
	public static double sd(List<Double> v){
		double sd = 0;
		double m = mean(v);
		for(int i = 0, n = v.size(); i < n; i++){
		   sd += Math.pow((v.get(i) - m), 2);
		}
		sd /= v.size();
		return Math.pow(sd, 0.5);
	}
	public static double var(List<Double> v){
		double var = 0;
		double m = mean(v);
		for(int i = 0, n = v.size(); i < n; i++){
		   var += Math.pow((v.get(i) - m), 2);
		}
		var /= v.size();
		return var;
	}
	public static double abs(List<Double> v){
		double tmp = 0;
		for(int i = 0, n = v.size(); i < n; i++){
		    tmp += v.get(i) * v.get(i);
		  }
		  return Math.sqrt(tmp);
	}
	public static double innerProduct( List<Double> v,  List<Double> u){
		 double tmp = 0;
		 for(int i = 0, n = v.size(); i < n; i++){
		    tmp += v.get(i) * u.get(i);
		 }
		 return tmp;
	}
	public static double pearsonCorrelation( List<Double> v, List<Double> u){
		  double c = 0;
		  double vm = mean(v);
		  double um = mean(u);
		  for(int i = 0, n = v.size(); i < n; i++){
		    c +=  (v.get(i)-vm) * (u.get(i)-um);
		  }
		  c /= v.size()*sd(v)*sd(u);
		  return c;
    }
	public static double covariance( List<Double> v, List<Double> u){
		  double c = 0;
		  double vm = mean(v);
		  double um = mean(u);
		  for(int i = 0, n = v.size(); i < n; i++){
		    c +=  (v.get(i)-vm) * (u.get(i)-um);
		  }
		  return c;
  }
	public static double euclideanDist(List <Double> v, List <Double> u){
		  double d = 0;
		  for(int i = 0, n = v.size(); i < n; i++){
		    d += Math.pow(v.get(i)-u.get(i),2);
		  }
		  return Math.sqrt(d);
	}
	
	public static double pearsonCorrelationForNormarizedList(List <Double> v, List <Double> u){
		  double c = 0;
		  for(int i = 0, n = v.size(); i < n; i++){
		    c +=  v.get(i)*u.get(i);
		  }
		  c /= v.size();
		  return c;
	}

	public static double max(List <Double> v){
		  double  m = v.get(0);
		  for(int i = 0, n = v.size(); i < n; i++){
		    if(v.get(i) > m){
		      m = v.get(i); 
		    }
		  }
		  return m;
    }
	public static double min(List <Double> v){
		  double  m = v.get(0);
		  for(int i = 0, n = v.size(); i < n; i++){
		    if(v.get(i) <  m){
		      m = v.get(i); 
		    }
		  }
		  return m;
	}
	public static double median(List <Double> v){
		  List <Double> u = new ArrayList<Double>(v);  
		  Collections.sort(u);
		  int n = u.size();
		  if(n%2 == 1){
		    return u.get((n-1)/2);
		  }else{
		    return (u.get(n/2) + u.get((n/2)-1))/2;
		  }
	}
	public static double mode(List <Double> v){
		double M = max(v);
		double m = min(v);
		double binNum = 1000;
		double delta = (M-m)/binNum;
		List <Double> u = new ArrayList<Double>(v);
		Collections.sort(u);
	
		int count = 0;
		int countMax = 0;
		double  d = m+delta;
		double  dMax = d;
		for(Double e: u){
			if(e <= d){
				count++;
			}else{
				if(count > countMax){
					dMax = d;
					countMax = count;
				}
				while(e > d){
					d += delta;
				}
				count = 1;
			}
		}	
		dMax = dMax-(0.5*delta);	
		return dMax;
		
	}
	
	
	
	
	public static double percentile(List <Double> v, double d){
		  List <Double> u = new ArrayList<Double>(v);  
		  Collections.sort(u);  
		  int n = u.size();
		  if(d > 1){
		    d /= 100;
		  }
		  int i = (int)Math.floor(n*d)-1;
		  if(i < 0){
			  i = 0;
		  }
		  return u.get(i);
	}
	public static double upperQuartile(List <Double> v){
		return percentile(v,0.75);
	}
	public static double lowerQuartile(List <Double> v){
		return percentile(v,0.25);
	}
	public static double sum(List <Double> v){
		double s = 0;
		for(int i = 0, n = v.size(); i < n; i++){
		    s += v.get(i);
		}
		return s;
	}
	public static List <Double> normalize( List <Double> v){
    	List <Double> tmp = new ArrayList<Double>(v);
    	double m = mean(v);
    	double s = sd(v);
    	for(int i = 0, n = v.size(); i < n; i++){
    		tmp.set(i,(tmp.get(i) - m)/s); 
    	}	
    	return tmp;
    }
	public static double tStatistic( List <Double> v, List <Double> u ){
    	  double V  = Math.sqrt(var(v)/v.size() + var(u)/u.size());
    	  return (mean(v) - mean(u))/V;
    }
	

	public static double ANOVAStatistics(List <List <Double>> X){
		double SStotal = 0, SSbetween = 0;
		List <Double> all = new ArrayList<Double>();
		for(List <Double> x: X){
			all.addAll(x);
		}
		double meanAll = MyFunc.mean(all);
		for(List <Double> x: X){
			SSbetween += x.size()*Math.pow(MyFunc.mean(x)-meanAll,2);
			for(Double d: x){
				SStotal += Math.pow(d-meanAll,2);
			}
		}
		double SSwithin = SStotal -SSbetween;
		
		double MSbetween = SSbetween/(X.size()-1);
		double MSwithin = SSwithin/(all.size() - X.size());
		return  MSbetween/MSwithin;		
	}
	
	
	
	
	public static List<String>readStringList(String infile) throws IOException{
		   BufferedReader inputStream = new BufferedReader(new FileReader(infile));
			List<String> lines = new ArrayList<String>();
			String line;
			while((line = inputStream.readLine()) != null){
				if(line.charAt(0) == '#'){
					continue;				
				}
				lines.add(line);
			}
			inputStream.close();
			return lines;
	   }
	
	
	public static List<String>readStringList2(String infile) throws IOException{
		   BufferedReader inputStream = new BufferedReader(new FileReader(infile));
			List<String> list = new ArrayList<String>();
			String line;
			while((line = inputStream.readLine()) != null){
				if(line.charAt(0) == '#'){
					continue;				
				}
				List <String> tmp = split("\t",line);
				list.add(tmp.get(0));
			}
			inputStream.close();
			return list;
	   }
	
	public static List<Double>readDoubleList(String infile) throws IOException{
		   BufferedReader inputStream = new BufferedReader(new FileReader(infile));
			List<Double> lines = new ArrayList<Double>();
			String line;
			while((line = inputStream.readLine()) != null){
				if(line.charAt(0) == '#'){
					continue;				
				}
				lines.add(Double.valueOf(line));
			}
			inputStream.close();
			return lines;
	   }
	
	public static void printList(List<?> L, String outfile) throws IOException{
		PrintWriter os = new PrintWriter(new FileWriter(outfile));
		for(int i = 0, n = L.size(); i < n; i++){
			os.println(L.get(i).toString());
		}
		os.flush();
		os.close();
	}
	public static void printList(List<?> O) throws IOException{
		PrintWriter os = new PrintWriter(System.out);
		for(int i = 0, n = O.size(); i < n; i++){
			os.println(O.get(i).toString());
		}
		os.flush();
		os.close();
	}
	public static void printMap( Map<?,?> m, String outfile) throws IOException{
		PrintWriter os = new PrintWriter(new FileWriter(outfile));
		for(Map.Entry<?, ?> e: m.entrySet()){
			os.println(e.getKey().toString() + "\t" + e.getValue().toString());
		}
		os.flush();
		os.close();
	}
	public static void printMap( Map<?,?> m) throws IOException{
		PrintWriter os = new PrintWriter(System.out);
		for(Map.Entry<?, ?> e: m.entrySet()){
			os.println(e.getKey().toString() + "\t" + e.getValue().toString());
		}
		os.flush();
		os.close();
	}
	public static Map<String,String> readStringStringMap(String infile) throws IOException, DataFormatException{
	   BufferedReader inputStream = new BufferedReader(new FileReader(infile));
		List<String> str = new ArrayList<String>();
		Map<String,String> map = new HashMap<String, String>();
		String line;	
		while((line = inputStream.readLine()) != null){
			if(line.charAt(0) == '#'){
				continue;				
			}
			str = Arrays.asList(line.split("\t"));
			if(str.size() < 2){
				throw new DataFormatException("readStringStringMap: file format is wrong!");
			}
			map.put(str.get(0), str.get(1));
		}
		inputStream.close();
		return map;
   }
   
	public static Map<String,Double> readStringDoubleMap(String infile) throws IOException, DataFormatException{
	   BufferedReader inputStream = new BufferedReader(new FileReader(infile));
		List<String> str = new ArrayList<String>();
		Map<String,Double> map = new HashMap<String, Double>();
		String line;	
		while((line = inputStream.readLine()) != null){
			if(line.charAt(0) == '#'){
				continue;				
			}
			str = Arrays.asList(line.split("\t"));
			if(str.size() < 2){
				throw new DataFormatException("readStringDoubleMap: file format is wrong!");
			}
			map.put(str.get(0), Double.valueOf(str.get(1)));
		}
		inputStream.close();
		return map;	
   }

	public static String join(String d, List <?> s){
		   StringBuffer S = new StringBuffer(s.get(0).toString());
		   for(int i = 1, n = s.size(); i < n; i++){
			   S.append(d);
			   S.append(s.get(i).toString());   
		   }
		   return S.toString();
   }
   
   public static List<String> split(String  d, String  s){
	   List<String> S = new ArrayList<String>(Arrays.asList(s.split(d)));
	   return S;
   }
   
   public static List<String> uniq(List<String> O){
	   Set<String> tmp = new LinkedHashSet<String>(O);
	   return new ArrayList<String>(tmp);
   }
   public static List<String> isect(List<String> A, List<String> B){
	   Set<String> tmp = new LinkedHashSet<String>(A);
	   Set<String> tmp2 = new LinkedHashSet<String>(B);
	   tmp.retainAll(tmp2);
	   return new ArrayList<String>(tmp);
   }
   public static List<String> union(List<String> A, List<String> B){
	   Set<String> tmp = new LinkedHashSet<String>(A);
	   Set<String> tmp2 = new LinkedHashSet<String>(B);
	   tmp.addAll(tmp2);
	   return new ArrayList<String>(tmp);
   }
   public static List<String> diff(List<String> A, List<String> B){
	   Set<String> tmp = new LinkedHashSet<String>(A);
	   Set<String> tmp2 = new LinkedHashSet<String>(B);
	   tmp.removeAll(tmp2);
	   return new ArrayList<String>(tmp);
   	}

   @SuppressWarnings("unchecked")
   public static List sample(List  S, int n){
	   List  tmp = new ArrayList (S);
	   Collections.shuffle(tmp);
	   return tmp.subList(0, n);
   	}
   	
   public static List sampleForSmallN(List  S, int n){
	   List tmp = new ArrayList ();
	   Random rnd = new Random();
	   Set<Integer> seen = new HashSet<Integer>();
	   for(int i = 0; i < n; i++){
		   int j = rnd.nextInt(S.size());
		   if(!seen.contains(j)){
			   tmp.add(S.get(j));
			   seen.add(j);
		   }
	   };
  	   return tmp;
   }	
   	
   @SuppressWarnings("unchecked")
   public static List sampleWithReplacement(List  S, int n){
	   List tmp = new ArrayList ();
	   Random rnd = new Random();
	   for(int i = 0; i < n; i++){
		   tmp.add(S.get(rnd.nextInt(S.size())));
	   	}
	   return tmp;
   	}
   
   /*  Set <?> A , B, Background;
    *   calculatePvalueForSetOverlap( Background.size(), A.size(), B.size(), (isect(A,B)).size);
    */
  /* public static double  calculatePvalueForSetOverlap(int t, int a, int b, int u) throws ArithmeticException{  
	   if(!(t >  0 && a > 0 && b > 0 && u > 0  && t >= a && t >= b && a >= u && b >= u)){
	     throw new  ArithmeticException("calculatePvalueForSetOverlap: input error!");
	   	}	
	   int s;
	   int l;
	   if(a>b){
	     l = a;
	     s = b;
	   }else{
	     l = b;
	     s = a;
	   }
	   HypergeometricDistributionImpl H = new HypergeometricDistributionImpl(t, l, s);
	   return  H.upperCumulativeProbability(u);
	 }*
   //for 2x2 diveded table  | a  b |
   //                       | c  d |
   /*public static double calculateFisheExactPvalue0(int a, int b, int c, int d){
	   int t = a+b+c+d;
	   int s;
	   int l;
	   if(b>c){
		   l = a+b;
		   s = b+c;
		}else{
			l	= b+c;
			s = a+b;
		 }
	   HypergeometricDistributionImpl H = new HypergeometricDistributionImpl(t, l, s);
	   return  H.upperCumulativeProbability(a);
   }*/
   
   //two sided
   /*public static double calculateFisheExactPvalue(int c1v1, int c1v2, int c2v1, int c2v2) {
       int populationSize = c1v1 + c1v2 + c2v1 + c2v2;
       int numberOfSuccess;
       int sampleSize;
       int observed;
       int c1 = c1v1 + c1v2;
       int c2 = c2v1 + c2v2;

       if (c1 < c2) {
           numberOfSuccess = c1;
           if (c1v1 > c1v2) {
               observed = c1v2;
               sampleSize = c1v2 + c2v2;
           } else {
               observed = c1v1;
               sampleSize = c1v1 + c2v1;
           }
       	} else {
       		numberOfSuccess = c2;
       		if (c2v1 > c2v2) {
               observed = c2v2;
               sampleSize = c1v2 + c2v2;
       		} else {
       			observed = c2v1;
               sampleSize = c1v1 + c2v1;
       		}
       	}
  
       HypergeometricDistributionImpl h = new HypergeometricDistributionImpl(
    		   populationSize, numberOfSuccess, sampleSize);

       double pvalue = 0.0; 
 
       double conditonValue = h.probability(observed);
       for (int i = 0; i <= numberOfSuccess; i++) {
    	   double value = h.probability(i);
    	   if (conditonValue >= value) {
    		   pvalue += value;
           	}
           }
       return pvalue;
   	}*/
   	
   
   public  static List<String>  sortKeysByAscendingOrderOfValues(Map <String,Double> map){
		SortedMap<Double,List<String>> new_map = new TreeMap<Double,List<String>>();
		for(Map.Entry<String,Double> e : map.entrySet()){
			if(new_map.containsKey(e.getValue())){
				(new_map.get(e.getValue())).add(e.getKey());
			}else{
				List<String> tmp = new ArrayList<String>();
				tmp.add(e.getKey());
				new_map.put(e.getValue(), tmp);
			}
		}
		List <String> tmp = new ArrayList<String>();
		for(List<String> s: new_map.values()){
			tmp.addAll(s);
		}
		return tmp;
	}
   
   public static  List<String>  sortKeysByDescendingOrderOfValues(Map <String,Double> map){
	   List <String> tmp = sortKeysByAscendingOrderOfValues(map);
 	   Collections.reverse(tmp);
 	   return tmp;
   }
      
   public static List < List <String> >  getAllCombination( List <String> v, int n){
     List < List<String> > V = new ArrayList<List<String>>();
     if(v.size() <= n){
       return V;
     }
     Set < List<String> > seen = new HashSet<List<String>>();
     int i;
     for(i = 0; i < v.size(); i++){
    	 List <String> tmp = new ArrayList<String>();
    	 tmp.add(v.get(i));
       if(!seen.contains(tmp)){
         V.add(tmp);
         seen.add(tmp);
       }
     }
     
     int j, k;
     for(i = 1; i < n; i++){
       List < List<String> > TMP = new ArrayList<List<String>>();
       for(j = 0; j < V.size(); j++){
         for(k = 0; k < v.size(); k++){
        	 List <String> tmp = new ArrayList<String>(V.get(j));
        	 tmp.add(v.get(k));
        	 Collections.sort(tmp);
        	 if(seen.contains(tmp)){
        		 continue;
        	 }	
        	 if(!V.get(j).contains(v.get(k))){
        		 TMP.add(tmp);
        		 seen.add(tmp);	
        	 }	
         }
       	}
       V = TMP;
     }
     return V;
   }
   
   public static Map <String, List<String> > readGeneSetFromGmtFile(String gmtfile) throws IOException, DataFormatException{
	   BufferedReader inputStream = new BufferedReader(new FileReader(gmtfile));
		List<String> str = new ArrayList<String>();
		Map <String, List<String>> GeneSetMap = new LinkedHashMap<String, List<String>>();
		String line;	
		while((line = inputStream.readLine()) != null){
			if(line.charAt(0) == '#'){
				continue;				
			}
			str = Arrays.asList(line.split("\t"));
			if(str.size() < 3 ){
				continue;	
			}
			String id = str.get(0);
			str = MyFunc.uniq(str.subList(2, str.size()));
			GeneSetMap.put(id, new ArrayList<String>(str));	  
		}
		inputStream.close();	
		if(GeneSetMap.isEmpty()){
			throw new DataFormatException("readGeneSetFromGmtFile: file format is wrong!");
		}
		return GeneSetMap;
   }
   
   public static Map <String, Double> calculateQvalue(Map <String, Double> Pvalue) {
	   List <String> keys = sortKeysByAscendingOrderOfValues(Pvalue);
	   int n = Pvalue.size();
	   int i ;
	   Map <String, Double> Qvalue = new HashMap<String, Double>();
	   for(i = 0; i < keys.size(); i++){
		   double Q = (n * Pvalue.get(keys.get(i))) / (i+1) ; 
		   if(Q>1){
			   Q=1;
		   }
		   Qvalue.put(keys.get(i), Q);
   		}	
	   return Qvalue;
   }
	
   public static Map <String, Double> calculateStoreyQvalue(Map <String, Double> Pvalue){
	   List <String> keys = sortKeysByAscendingOrderOfValues(Pvalue);
	   int m = Pvalue.size();
	   Map <String, Double> Qvalue = new HashMap<String, Double>();
	   List <Double> p = new ArrayList<Double>();
	   for(int i = 0; i < m; i++ ){
		   p.add(Pvalue.get(keys.get(i)));  
	   }
	   
	   double maxLamda = 0.95;
	   double minLamda = 0.6;
	   double d = 0.05;
	   List <Double> lambda = new ArrayList<Double>();
	   for(double l = minLamda; l < maxLamda + 0.000000001; l += d){
		  lambda.add(l);
	   }
	  List <Double> pi0 = new ArrayList<Double>();
	   for(int i = 0; i < lambda.size(); i++){
		   int k = 0;
		   for(int j = 0; j <  p.size(); j++){
			   if(p.get(j) > lambda.get(i)){
				   k++;
			   }
		   }
		   pi0.add(k /( m * (1-lambda.get(i))));
	   }
	   // estimate pi0 using bootstrap method
	   double minpi0 = min(pi0);
	   List <Double> mse = new ArrayList<Double>();
	   List <Double> pi0_boot = new ArrayList<Double>();
	   for(int I = 0; I < 100; I++){
		   List <Double> p_boot = sampleWithReplacement(p, m);
		   for(int i = 0; i < lambda.size(); i++){
			   int k = 0;
			   for(int j = 0; j <  p_boot.size(); j++){
				   if(p_boot.get(j) > lambda.get(i)){
					   k++;
				   }
			   }
			   pi0_boot.add(k /( m * (1-lambda.get(i))));
		   }
		   for(int i = 0; i < pi0_boot.size(); i++){
			   if(mse.size() != pi0_boot.size()){
				   mse.add( Math.pow(pi0_boot.get(i)-minpi0, 2));
			   }else{
				   mse.set(i, mse.get(i) + Math.pow(pi0_boot.get(i)-minpi0, 2));
			   }   
		   }
	   }
	   double PI0 = pi0.get(0);
	  double minmse = mse.get(0); 
	  for(int i = 1; i < pi0.size(); i++){
		   if(mse.get(i) < minmse){
			   minmse = mse.get(i);
			   PI0 = pi0.get(i);
		   }
	   }
	   List <Double> q = new ArrayList<Double>(m);
	   q.add( PI0 * p.get(m-1)); 
	   for(int i = m-1; i > 0; i--){
		   q.add(Math.min(PI0 * m * p.get(i-1), q.get(q.size()-1)));
	   }
	   
	   Collections.reverse(q);
	   for(int i = 0; i < m; i++){
		   Qvalue.put(keys.get(i), q.get(i));
	   }
	   return Qvalue;
   }
   
  /* public static Map <String, Double> convertZscore2PvalueUsingNomalDistribution(Map <String, Double> Zscore){
	   Map <String, Double> Pvalue = new HashMap<String, Double>();
	   NormalDistributionImpl ND = new NormalDistributionImpl();
 	   for(Map.Entry<String, Double> e: Zscore.entrySet()){
 		   try{
 			   Pvalue.put(e.getKey(), 1-ND.cumulativeProbability(e.getValue()) );   
 		   }
 		   catch(Exception ex){
 			  throw  new ArithmeticException();
 		   }
 	   }
 	   return Pvalue;
   }*/
   
   
   
   public static double shapiroTest(List<Double> v){
	   String tmpFile = "tmp" + Math.round(Math.random()*100000000); 
	   try {
	   PrintWriter os;
	   os = new PrintWriter(new FileWriter(tmpFile + ".dist"));
	   for(int i = 0, n = v.size(); i < n; i++){
		   os.println(v.get(i).toString());
	   }
	   os.flush();
	   os.close();
	   os = new PrintWriter(new FileWriter(tmpFile + ".R"));
	   os.println("a<-scan(\"" +  tmpFile + ".dist" + "\")");
	   os.println("write(shapiro.test(a)$p, file=\"" + tmpFile + ".p" + "\")");
	   os.flush();
	   os.close();
	   runRscript(tmpFile + ".R");
	   BufferedReader is = new BufferedReader(new FileReader( tmpFile + ".p"));
	   double p = Double.valueOf(is.readLine());

	   File f = new File(tmpFile + ".dist");
	   if(!f.delete()){
		   f.deleteOnExit();
	   }
	   f = new File(tmpFile + ".R");
	   if(!f.delete()){
		   f.deleteOnExit();
	   }
	   f = new File(tmpFile + ".p");
	   if(!f.delete()){
		   f.deleteOnExit();
	   }
	   
	   	return p;
	   } catch (Exception e) {
		   
		   File f = new File(tmpFile + ".dist");
		   if(!f.delete()){
			   f.deleteOnExit();
		   }
		   f = new File(tmpFile + ".R");
		   if(!f.delete()){
			   f.deleteOnExit();
		   }
		   f = new File(tmpFile + ".p");
		   if(!f.delete()){
			   f.deleteOnExit();
		   }
		   throw  new ArithmeticException();
	   }
   }
   
  
   public static class Density{
	   private double bandWidth;
	   private int N; 
	   private List <Double> X;
	   public Density(List <Double> X){
		   this.X = new ArrayList<Double>(X);
		   N = X.size();
		   /*Silverman's"rules of thumb" implemented in R "density" function */
		   bandWidth = Math.min(sd(X)/(Math.pow(N,1/5.0)*1.34), (percentile(X, 0.75) - percentile(X, 0.25))/2)    ;	
	   }	
	   public void setBandWidth(double d){
	     bandWidth = d;
	   }
	   public double  kernelFunction(double x){
		   return Math.exp(-0.5 * Math.pow(x, 2)) / Math.sqrt( 2*Math.PI ); 
	   }
	   public double estimate(double x){
		 List <Double> tmp = new ArrayList<Double>();
		 int i;
		 for(i =0; i< N; i++){
			  tmp.add(kernelFunction((x-X.get(i))/bandWidth));
		 }
		 return sum(tmp)/(N*bandWidth);  
	   }
	   public List <Double> estimate(List <Double> X){
		   List <Double> Y = new ArrayList<Double>();
		   int i;
		   for(i =0; i < X.size(); i++){
			   Y.add(estimate(X.get(i)));
		   }
		   return Y;
	   }	   
   }
   
   public static void runRscript(String scriptfile) throws Exception{
	   String os = System.getProperty("os.name").toUpperCase();
	   ProcessBuilder pb;
	  if(Pattern.matches("WIN.*",os)){
		 String[] tmp = {"\"C:\\Program Files (x86)\\R\\R-2.8.1\\bin\\R\"", "--slave", "--vanilla", "-f", scriptfile};
		 pb = new ProcessBuilder(tmp);  
	   }else{
		   String[] tmp = {"R",  "--vanilla", "--slave", "-f", scriptfile};
		   pb = new ProcessBuilder(tmp);
	   }
	   pb.start().waitFor();  
   }
   
   public static void removeFiles(Collection <String> files) throws Exception{
	  for(String f: files){
		File file = new File(f);
		file.delete();
   		}
   }
   
   public static void removeFile(String file) throws Exception{
	   File f = new File(file);
		f.delete();
   }
   
   public static Map<String,Double> StirngStringMap2StringDoubleMap(Map <String, String> map){
	   Map <String, Double> map2 = new HashMap<String, Double>();
	   for(Map.Entry<String, String> e: map.entrySet()){
		   if(e.getValue().equals("")){
			   	map2.put(e.getKey(),0.0);
	   	}else{
	   			map2.put(e.getKey(), Double.valueOf(e.getValue()));
	   		}
	   }
	   return map2;
   }
   public static Map<String,Integer> StirngStringMap2StringIntegerMap(Map <String, String> map){
	   Map <String, Integer> map2 = new HashMap<String, Integer>();
	   for(Map.Entry<String, String> e: map.entrySet()){
		   if(e.getValue().equals("")){
			   	map2.put(e.getKey(),0);
	   	}else{
	   		map2.put(e.getKey(), Integer.valueOf(e.getValue()));
	   		}
	   	}
	   return map2;
   }
   public static Map<String,Boolean> StirngStringMap2StringBooleanMap(Map <String, String> map){
	   Map <String, Boolean> map2 = new HashMap<String, Boolean>();
	   for(Map.Entry<String, String> e: map.entrySet()){
		   if(Integer.valueOf(e.getValue()).equals(1) || Boolean.valueOf(e.getValue()).equals(true)){
			   map2.put(e.getKey(), true);
		   }else{
			   map2.put(e.getKey(), false);
		   }
	   }
	   return map2;
   }
   public static Map<String, Double> getSubMapWithValueAboveCutoff( Map<String, Double> map, double cutoff){
	   Map <String, Double> map2 = new HashMap<String, Double>();
	   for(Map.Entry<String, Double> e: map.entrySet()){
		   if(e.getValue() > cutoff){
			   map2.put(e.getKey(), e.getValue());
		   }
	   }
	   return map2;
   }
   public static Map<String, Double> getSubMapWithValueBelowCutoff( Map<String, Double> map, double cutoff){
	   Map <String, Double> map2 = new HashMap<String, Double>();
	   for(Map.Entry<String, Double> e: map.entrySet()){
		   if(e.getValue() < cutoff){
			   map2.put(e.getKey(), e.getValue());
		   }
	   }
	   return map2;
   }
   
   
  static private  final String Digits     = "(\\p{Digit}+)";
  static private final String HexDigits  = "(\\p{XDigit}+)";
           // an exponent is 'e' or 'E' followed by an optionally 
           // signed decimal integer.
  static private final String Exp        = "[eE][+-]?"+Digits;
  static private final String fpRegex    =
               ("[\\x00-\\x20]*"+  // Optional leading "whitespace"
                "[+-]?(" + // Optional sign character
                "NaN|" +           // "NaN" string
                "Infinity|" +      // "Infinity" string

                // A decimal floating-point string representing a finite positive
                // number without a leading sign has at most five basic pieces:
                // Digits .Digits ExponentPart FloatTypeSuffix
                // 
                // Since this method allows integer-only strings as input
                // in addition to strings of floating-point literals, the
                // two sub-patterns below are simplifications of the grammar
                // productions from the Java Language Specification, 2nd 
                // edition, section 3.10.2.

                // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
                "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+

                // . Digits ExponentPart_opt FloatTypeSuffix_opt
                "(\\.("+Digits+")("+Exp+")?)|"+

   // Hexadecimal strings
          "((" +
   // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
   "(0[xX]" + HexDigits + "(\\.)?)|" +

   // 0[xX] HexDigits_opt .HexDigits BinaryExponent FloatTypeSuffix_opt
   "(0[xX]" + HexDigits + "?(\\.)"+ HexDigits + ")" +

   ")[pP][+-]?"+ Digits + "))" +
                "[fFdD]?))" +
                "[\\x00-\\x20]*");// Optional trailing "whitespace"
   
   public static boolean canBeDouble(String s){  
	   return Pattern.matches(fpRegex, s);               
   }
   
   public static String getPreffix(String fileName) {
	    int point = fileName.lastIndexOf(".");
	    if (point != -1) {
	        return fileName.substring(0, point);
	    } 
	    return fileName;
	}
   public static boolean canBeDouble(Collection <String> S){
	   for(String s: S){
		   if(!canBeDouble(s)){
			  return false;
		   }
	   }
	   return true;
   }
   public static List <Double> toDouble(List <String> S){
	   List <Double> tmp = new ArrayList<Double>();
	   for(String s: S){
		   tmp.add(Double.valueOf(s));   
	   }
	   return tmp;
   }
   public static List <String> toString(List S){
	   List <String> tmp = new ArrayList<String>();
	   for(Object s: S){
		   tmp.add(s.toString());   
	   }
	   return tmp;
   }
   
   public static List removeNull(List S){
	   List tmp = new ArrayList ();
	   for(Object s: S){
		   if(s != null){
			   tmp.add(s);
		   }
	   }
	   return tmp;
   }
  
   public static List removeEmptyString(List <String> S){
	   List <String> tmp = new ArrayList<String> ();
	   for(String s: S){
		   if(!s.equals("")){
			   tmp.add(s);
		   }
	   }
	   return tmp;
   }
   
   public static String toString(double doubleValue, int maxDigitNumber){
	   int sign = doubleValue < 0?-1:1;
	   doubleValue = sign * doubleValue;
	   int p =  (int)Math.floor(Math.log10(doubleValue));
	   if(p < -10000000){
		   return "0";
	   }
	   int i =  p - maxDigitNumber + 1;
	   double d = Math.round( doubleValue / Math.pow( 10, i )) *  Math.pow( 10, i );
	   DecimalFormat df = new DecimalFormat();
	   d = sign * d;
	   if(Math.abs(p) > 5){
		   df.setMaximumFractionDigits(maxDigitNumber-1);
		   return  df.format(d/Math.pow(10,p)) + "E" + p;
	   }else{
		   if(p > maxDigitNumber){ 
			   df.setMaximumFractionDigits(0);	 
		   }else{
			   df.setMaximumFractionDigits(maxDigitNumber - p);	 
		   }
		   return  df.format(d); 
	   }
   }
   
   

   
   public static String getMemoryInfo(){
	   DecimalFormat f1 = new DecimalFormat("#,###KB");
	    DecimalFormat f2 = new DecimalFormat("##.#");
	    long free = Runtime.getRuntime().freeMemory() / 1024;
	    long total = Runtime.getRuntime().totalMemory() / 1024;
	    long max = Runtime.getRuntime().maxMemory() / 1024;
	    long used = total - free;
	    double ratio = (used * 100 / (double)total);
	    String info = 
	    "Mem:\t" + f1.format(total) + " total,\t" +
	     f1.format(used) + " (" + f2.format(ratio) + "%) used,\t" +
	    f1.format(max) + " max";
	    return info;
	   
   }
   
   public static double shannonEntropy(List <Double> v){
	   double e = 0;
	   for(Double d: v){
		   e  += (d==0)?0:(d*Math.log(d));
	   }
	   return e/ Math.log(v.size());   
   }
   
   public static List<String> toList(String[] s){
	   List<String> L = new ArrayList<String>();
	   for(int i=0; i < s.length; i++){
		   L.add(s[i]);
	   }
	   return L;
   	}
   public static List<Double> toList(double[] s){
	   List<Double> L = new ArrayList<Double>();
	   for(int i=0; i < s.length; i++){
		   L.add(s[i]);
	   }
	   return L;
   }
   
   public static double getEfficientRound( double value, int effectiveDigit ) {
	   int sign = (int)(value/Math.abs(value)); 
	   value = Math.abs(value);
	   int valueDigit = (int)Math.rint( Math.log10(value ) );
	   int roundDigit = valueDigit - effectiveDigit + 1;
	   double v = Math.floor( value / Math.pow( 10, roundDigit ) + 0.5 );
	   return sign*v* Math.pow( 10, roundDigit );
   }
   
   public static String getEfficientRoundString( double value, int effectiveDigit ) {
	   double sign = value/Math.abs(value); 
	   value = Math.abs(value);
	   int valueDigit = (int)Math.floor( Math.log10(value ) );
	   int roundDigit = valueDigit - effectiveDigit + 1;
	   
	   if(value < Math.pow( 10, valueDigit-effectiveDigit-5 )){
		   String tmp = "0";
		   if(effectiveDigit>1){
			   tmp = tmp + ".";
			   for(int i=1; i<effectiveDigit; i++){
				   tmp = tmp + "0";
			   }
	   		}
	   		return tmp;
	   }
	   
	   double v = Math.floor( value / Math.pow( 10, roundDigit ) + 0.5 );
	   v =  v * Math.pow( 10, roundDigit );
	   int chrNum = -valueDigit+effectiveDigit +1;
	   if(Double.toString(v).length() >  -valueDigit+effectiveDigit + 5){
		   v += Math.pow( 10, valueDigit-effectiveDigit-5 );
		  
		   //System.err.println(value + " "  + v +" "  + (Double.toString(v).substring(0, chrNum)) + " " + chrNum);
	  
		   
		   return   ((sign == -1)?"-":"") + ((Double.toString(v).substring(0, chrNum)));
	   	   
	   }else{   
		 String tmp = Double.toString(v);
		 if(tmp.length() < chrNum){
			 while(tmp.length() < chrNum){
				 tmp += "0";
			 }
		 }
		   //System.err.println(value + " "  + v +" "  + tmp);
		   return ((sign == -1)?"-":"") + tmp;
	   }
   }
   
   public static List <Double> asList(double[] d){
	   List <Double> tmp = new ArrayList<Double>();
	   for(int i = 0; i < d.length; i++){
		   tmp.add(d[i]);
	   }
	   return tmp;
   }
	
   public static double calculatePvalueUsingExtremeDistribusion(double value, List <Double> nullDist){
		   
		   String tmpFile = "tmp" + Math.round(Math.random()*100000000); 
		   try {
			   PrintWriter os;
			   os = new PrintWriter(new FileWriter(tmpFile + ".nulldist"));
			   for(int i = 0, n = nullDist.size(); i < n; i++){
				   os.println(nullDist.get(i).toString());
			   }
			   os.flush();
			   os.close();
			   os = new PrintWriter(new FileWriter(tmpFile + ".R"));
			   os.println(" data<-read.table(\"" + tmpFile + ".nulldist" + "\")[,1]");
			   os.println("library(\"ismev\")");
			   os.println("fit<-gev.fit(data)");
			   os.println("if(fit$conv==0){");
			   os.println("write(fit$mle, \""+ tmpFile + ".mle" + "\", ncolumns=1)");
			   os.println("}");
			   os.flush();
			   os.close();
			   MyFunc.runRscript(tmpFile + ".R");
			   BufferedReader is = new BufferedReader(new FileReader( tmpFile + ".mle"));
			   double m = Double.valueOf(is.readLine());
			   double s = Double.valueOf(is.readLine());	
			   double e = Double.valueOf(is.readLine());	 
			   is.close();
			
			   File f = new File(tmpFile + ".nulldist");
			   if(!f.delete()){
				   f.deleteOnExit();
			   }
			   f = new File(tmpFile + ".R");
			   if(!f.delete()){
				   f.deleteOnExit();
			   }
			   f = new File(tmpFile + ".mle");
			   if(!f.delete()){
				   f.deleteOnExit();
			   }
			   double x = 1+e*(value-m)/s; 
			   if(x > 0){
				   return 1 - Math.exp( - Math.pow(x,-1/e));
			   }else{
				   return 0;
			   }
		   } catch (Exception e) {
			   //e.printStackTrace();
			   File f = new File(tmpFile + ".nulldist");
			   if(!f.delete()){
				   f.deleteOnExit();
			   }
			   f = new File(tmpFile + ".R");
			   if(!f.delete()){
				   f.deleteOnExit();
			   }
			   f = new File(tmpFile + ".mle");
			   if(!f.delete()){
				   f.deleteOnExit();
			   }
			   throw  new ArithmeticException();
		   }
	   }
	
   
}
   
 
   
