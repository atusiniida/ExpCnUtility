

import java.util.*;

public class SegmentContainer implements Iterable <Segment> {

	private Map <Integer, List<Integer>> chr2index; 
	private List <Segment> segmentList;
	private double[] probeCountCumFreq;
	//hg18
	//static int[] chrLength = {247249719, 242951149, 199501827, 191273063, 180857866, 170899992, 158821424, 146274826, 140273252, 135374737, 134452384, 132349534, 114142980, 106368585, 100338915, 88827254, 78774742, 76117153, 63811651, 62435964, 46944323, 49691432, 154913754, 57772954};
	//hg19
	static int[] chrLength = {249250621, 243199373, 198022430, 191154276, 180915260, 171115067, 159138663, 146364022, 141213431, 135534747, 135006516, 133851895, 115169878, 107349540, 102531392, 90354753, 81195210, 78077248, 59128983, 63025520, 48129895, 51304566, 155270560, 59373566};
	public static int chrLength(int i){
		return chrLength[i-1];
	}
	
	public void sort(){
		java.util.Collections.sort(segmentList, new SegmentComp());
		chr2index = new LinkedHashMap<Integer, List<Integer>>();
		int i = 0;
		for(Segment s: segmentList){
			if(!chr2index.containsKey(s.chr())){
				chr2index.put(s.chr(), new ArrayList<Integer>());
			}
			chr2index.get(s.chr()).add(i);
			i++;
		}		
	}
		
	public class SegmentComp implements Comparator<Segment> {
        public int compare(Segment s1, Segment s2) {
        	
        		if(s1.chr() == s2.chr()){
        			if(s1.start() < s2.start()){
        				return -1;
        			}
        			if(s1.start() > s2.start()){
        				return 1;
        			}
        				
        		}else{
        			if(s1.chr() < s2.chr()){
        				return -1;
        			}
        			if(s1.chr() > s2.chr()){
        				return 1;
        			}
        		}
				return 0;
        }
		
	}
	
	public void remove(int i){
		this.segmentList.remove(i);
		for(Integer c: chr2index.keySet()){
			List <Integer> tmp = chr2index.get(c);
			List <Integer> tmp2 = new ArrayList<Integer>();
			chr2index.put(c, tmp2);
			for(Integer I: tmp){
				if(I<i){
					tmp2.add(I);
				}else if(I > i){
					tmp2.add(I-1);
				}
			}
		}	
	}
	
	public double mode(){
		List <Double> tmp = new ArrayList<Double>();
		for(Segment s: this){
			for(int i = 0; i < s.probeCount(); i++){
				tmp.add(s.value());
			}
		}
		return MyFunc.mode(tmp);
	}
	
	public void shiftMode(double d){
		double m = mode();
		for(Segment s: this){
			s.value(s.value()-m+d);
		}
	}
	
	public void binarize(double d){
		for(Segment s: this){
			if(s.value() > d){
				s.value(1);
			}else{
				s.value(0);
			}
		}
		mergeSegments();
	}
	
	
	public void binarizeLess(double d){
		for(Segment s: this){
			if(s.value() < d){
				s.value(1);
			}else{
				s.value(0);
			}
		}
		mergeSegments();
	}
	
	public void ternarize(double d, double  d2){
		for(Segment s: this){
			if(s.value() > d){
				s.value(1);
			}else if(s.value() < d2){
				s.value(-1);
			}else{
				s.value(0);
			}
		}
		mergeSegments();
	}
	
	
	public void bind(SegmentContainer SC){
		int i = size();
		this.segmentList.addAll(SC.segmentList);
		for(Segment s: SC.segmentList){
			if(!chr2index.containsKey(s.chr())){
				chr2index.put(s.chr(), new ArrayList<Integer>());
			}
			chr2index.get(s.chr()).add(i);
			i++;
		}		
	}
	
	public SegmentContainer(){
		chr2index = new LinkedHashMap<Integer, List<Integer>>();
		segmentList = new ArrayList <Segment>();
	}
	
	public void clear(){
		chr2index.clear();
		segmentList.clear();
	}
	
	public SegmentContainer(SegmentContainer SC){
		chr2index = new LinkedHashMap<Integer, List<Integer>>();
		for(Integer i: SC.chr2index.keySet()){
			chr2index.put(i, new ArrayList<Integer>(SC.chr2index.get(i)));
		}
		segmentList = new ArrayList <Segment>(SC.segmentList);
	}
	
	public int size(){
		return segmentList.size();
	}
	
	public List <Double> values(){
		List <Double> tmp = new ArrayList<Double>();
		for(Segment s: segmentList){
			tmp.add(s.value());
		}
		return tmp;
	}
	
	
	public void add(Segment S){
		int chr = S.chr();
		if(!chr2index.containsKey(chr)){
			chr2index.put(chr, new ArrayList<Integer>());
		}
		chr2index.get(chr).add(size());	
		segmentList.add(new Segment(S));
	}
	
	public void prapareForRandomValueGeneration(){
		List <Double> tmp = new ArrayList<Double>();
		double sum = 0; 
		for(Segment s : segmentList){
			sum += s.probeCount();
			tmp.add(sum);
		}
		probeCountCumFreq = new double[size()];
		int i = 0;
		for(Double d: tmp){
			probeCountCumFreq[i++] = d/sum;
		}
	}
	
	public double generateRandomValue(){
		double r = Math.random();
		int index = Arrays.binarySearch(probeCountCumFreq, r);
		index = (index < 0) ? (Math.abs(index) - 1) : index;
		return segmentList.get(index).value();
		
	}
	
	public List<Segment> get(){
		return segmentList;	
	}
	
	public Segment get(int i){
		return segmentList.get(i);	
	}
	
	public boolean contains(int chr, int pos){
		if(chr2index.containsKey(chr)){
			List <Integer> tmp = chr2index.get(chr);
			for(Integer i: tmp){
				if(pos >= get(i).start() & pos <= get(i).end()){
					return true;
				}
			}
			return false;
		}else{
			return false ;
		}
	}
	public boolean contains(int chr){
		return chr2index.containsKey(chr);
	}
	
	public Segment get(int chr, int pos){
		List <Integer> tmp = chr2index.get(chr);
		for(Integer i: tmp){
			if(pos >= get(i).start() & pos <= get(i).end()){
				return get(i);
			}
		}
		return null;
	}
	
	public List<Segment> getByChr(int chr){
		List <Integer> tmp = chr2index.get(chr);
		List <Segment> S = new ArrayList<Segment>();
		for(Integer i: tmp){
			S.add(get(i));
		}
		return S;
	}
	
	
	
	public List <Integer> chrList(){
	 return new ArrayList<Integer>(chr2index.keySet());
	}
	
	public boolean checkSegmentConsistency(){
		int c0 = -1;
		int e0 = -1;
		for(SegmentIterator i = iterator();i.hasNext();){
			Segment S = i.next();
			int c = S.chr();
			int s = S.start();
			int e = S.end();
			if(s > e){
				return false;
			}
			if(c == c0){
				if(s < e0){
					return false;
				}
			}else if(c < c0){
				return false;
			}
			c0 = c;
			e0 = e;
		}
		return true;
	}
	
	public void cleanSegmentContainer (){
		sort();;
		Segment S0 = null;
		for(SegmentIterator i = iterator();i.hasNext();){
			Segment S = i.next();
			int c = S.chr();
			int s = S.start();
			int e = S.end();
			if(s > e){
				S.start(e);
				S.end(s);
				s = S.start();
				e = S.end();
			}
			if(S0!=null){
				if(c == S0.chr()){
					if(s < S0.end()){
						i.remove();
						continue;
					}
				}
			}
			S0 = S;
		}
	}
	
	public void mergeSegments(){
		Segment S0 = null;
		for(SegmentIterator i = iterator();i.hasNext();){
			Segment S = i.next();
			if(S0!=null){
				if(S.chr() == S0.chr()){
					if(S0.end()+1 == S.start() & S0.value() == S.value()){
						S0.probeCount(S0.probeCount() + S.probeCount());
						S0.end(S.end());
						i.remove();
						continue;
					}
				}
			}
			S0 = S;
		}
	}
	
	
	
	
	public long totalLength(){
		long L = 0; 
		for(Segment S:segmentList){
			int l = S.end() -S.start();
			L += l;
		}
		return L;
	}
	
	
	public int minPosAtChr(int i){
		List <Integer> tmp = chr2index.get(i);
		return get(tmp.get(0)).start();
	}
	
	public int maxPosAtChr(int i){
		List <Integer> tmp = chr2index.get(i);
		return get(tmp.get(tmp.size()-1)).end();
	}

	
	
	public long getTotalSegmentLength(){
		long l = 0;
		for(Integer c: chrList()){
			int M = maxPosAtChr(c);
			int m = minPosAtChr(c);
			l += (M - m + 1);
		}
		return l;
	}
	
	
	public class SegmentIterator implements Iterator<Segment>{
		int index;
		
		public SegmentIterator(){
			index = -1;
		}

		public boolean hasPrevious() {
			if(index == 0){
				return false;
			}else{
				return true;
			}
		}
		
		public boolean hasNext() {
			if(index >= size()-1){
				return false;
			}else{
				return true;
			}
		}
		public Segment next() {
			index++;
			return SegmentContainer.this.get(index);
		}
		public void remove() {
			SegmentContainer.this.remove(index);
			index--;
		}
		public Segment get() {
			return SegmentContainer.this.get(index);
		}
		public Segment getNext() {
			return SegmentContainer.this.get(index++);
		}
		public Segment getPrevious() {
			return SegmentContainer.this.get(index--);
		}
	
	}
	
	
	public SegmentIterator iterator(){
		return new SegmentIterator();
	}
	
	public String toString(){
		List <String> tmp = new ArrayList<String>();
		if(size()>0){
		for(int i=0;i<size();i++){
			tmp.add(get(i).toString());
		}
		return MyFunc.join("\n", tmp) + "\n";
		}else{
			return "";
		}
	}
	
	
	public void fillGaps(){	
		sort();
		SegmentContainer SC = new SegmentContainer(this);
		clear();
		for(Integer i: SC.chr2index.keySet()){
			List<Integer> tmp = SC.chr2index.get(i);
			
			SC.get(tmp.get(0)).start(1);
			add(SC.get(tmp.get(0)));
			if(get(size()-1).end() >=  chrLength(i)){
				get(size()-1).end(chrLength(i));
				continue;
			}
			
			int preEnd = get(size()-1).end();
			for(int j = 1; j < tmp.size(); j++){
				Segment S = SC.get(tmp.get(j));
				int start = S.start();
				int end  = S.end();
				if(start  >= chrLength(i)){
					break;
				}
				int m = (int) Math.floor((preEnd+start)/2.0);
				get(size()-1).end(m);
				S.start(m+1);
				add(S);
				if(end >  chrLength(i)){
						S.end(chrLength(i));
						break;
				}
				preEnd = get(size()-1).end();
			}
			get(size()-1).end(chrLength(i));
		}
		mergeSegments();
	}
	
	
	public void fillGaps(double defaultValue){
		sort();
		SegmentContainer SC = new SegmentContainer(this);
		clear();
		for(int i=1; i<=24;i++){
			if(!SC.chr2index.containsKey(i)){
				Segment S = new Segment(i, 1,  chrLength(i), 0, defaultValue);	
				add(S);
				continue;
			}
			List<Integer> tmp = SC.chr2index.get(i);
		    
			int preEnd = 0;
			for(int j = 0; j < tmp.size(); j++){
				int start = SC.get(tmp.get(j)).start();
				if(start  < chrLength(i)){
					if(preEnd + 1 != start){
						Segment S = new Segment(i, preEnd+1, start-1, 0, defaultValue);	
						add(S);
					}
					add(SC.get(tmp.get(j)));
					if(get(size()-1).end() > chrLength(i)){
						get(size()-1).end(chrLength(i));
					}
					preEnd = SC.get(tmp.get(j)).end();
				}
			}
			
			if(preEnd < chrLength(i)){	
				Segment S = new Segment(i, preEnd+1, chrLength(i), 0, defaultValue);	
				add(S);
			}
		}
		sort();
		mergeSegments();
	}
	
	
	public void removeXY(){
		SegmentContainer tmp = new SegmentContainer(this);
		clear();
		for(Segment s: tmp){
			if(s.chr() != 23 & s.chr() != 24){
				add(s);
			}
		}
	}
	
	public void removeY(){
		SegmentContainer tmp = new SegmentContainer(this);
		clear();
		for(Segment s: tmp){
			if(s.chr() != 24){
				add(s);
			}
		}
	}
	
	public void filter(int chr, int start, int end){
		SegmentContainer SC = new SegmentContainer(this);
		clear();
		for(Segment S: SC){
			if(S.chr()!=chr){
				continue;
			}
			if((S.end()-start)*(end - S.end()) < 0){
				continue;
			}
			add(new Segment(S.chr(), S.start()>start?S.start():start,S.end()<end?S.end():end, S.probeCount(), S.value()));
		}
	}
	
	public  void filter(int chr){
		SegmentContainer SC = new SegmentContainer(this);
		clear();
		for(Segment S: SC){
			if(S.chr()!=chr){
				continue;
			}
			add(new Segment(S.chr(),S.start(),S.end(),S.probeCount(),S.value()));
		}
	}
	
	public ProbeInfo generatePsuedoProbeInfo(int probeCount){
		long L = totalLength();
		long interval = L/probeCount; 
		ProbeInfo PI = new ProbeInfo();
		if(interval < 1){
			interval =  1;
		}
		for(Integer c: chrList()){
			int M =	maxPosAtChr(c);
			int m = minPosAtChr(c);
			for(int p = m; p <= M; p+= interval){
				PI.put("chr" + c + "_" + p, c, p);
			}
		}
		return PI;
	}
	
	public void changePosition2NearestProbe(ProbeInfo PI){
		PI.sort();
		SegmentContainer SC = new SegmentContainer(this);
		SC.fillGaps();
		clear();
		Segment S = null;
		for(String p: PI.getProbeSet()){
			int chr =PI.chr(p);
			if(!SC.chr2index.keySet().contains(chr)){
				continue;
			}
			int pos = PI.pos(p);
			double value = SC.get(chr,pos).value();
			if(S==null){
				S = new Segment(chr, pos, pos, 1, value);	
				continue;
			}
			if(chr != S.chr()){
				if(S.probeCount()!=1){
					add(S);
				}else{
					get(size()-1).end(S.end());
					get(size()-1).probeCount(get(size()-1).probeCount()+1);
				}
				S = new Segment(chr, pos, pos, 1, value);
				continue;
			}
			if(value != S.value()){
				if(S.probeCount()!=1){
					add(S);
					S = new Segment(chr, pos, pos, 1, value);	
				}else{
					S = new Segment(chr, S.start(), pos, 2, value);
				}
				continue;
			}
			S.end(pos);
			S.probeCount(S.probeCount()+1);
		}
		if(S.probeCount()!=1){
			add(S);
		}else{
			get(size()-1).end(S.end());
			get(size()-1).probeCount(get(size()-1).probeCount()+1);
		}
	}

	public double max(){
		double d = -Double.MAX_VALUE;
		for(Segment S:segmentList){
			if(S.value()>d){
				d = S.value();
			}		
		}
		return d;
	}

	public double min(){
		double d = Double.MAX_VALUE;
		for(Segment S:segmentList){
			if(S.value()<d){
				d = S.value();
			}		
		}
		return d;
	}
	
	public double mean(){
		double d = 0;
		long  L = totalLength(); 
		for(Segment S:segmentList){
			int l = S.end() -S.start();
			d += S.value()*l;
		}
		return d/L;
	}
	
	public double var(){
		double m = mean();
		double d = 0;
		long  L = totalLength(); 
		for(Segment S:segmentList){
			int l = S.end() -S.start();
			d += Math.pow(S.value()-m,2)*l;
		}
		return d/L;
	}
	
	public void removeShortSegmentsByProbeCount(int minProbeCount){
		SegmentIterator I = new SegmentIterator();
		for(Segment S = I.next(); I.hasNext();S = I.next()){
			if(S.probeCount() < minProbeCount){
				I.remove();
			}
		}
	}
	
	public void removeShortSegmentsByLength(int length){
		SegmentIterator I = new SegmentIterator();
		for(Segment S = I.next(); I.hasNext();S = I.next()){
			if(S.end() - S.start() +1  < length){
				I.remove();
			}
		}
	}
	
	public double getMeanValue(int chr, int start, int end){
		List <Segment> tmp = getByChr(chr);
		List <Integer> L = new ArrayList<Integer>();
		List <Double> V = new ArrayList<Double>();
		for(Segment S: tmp){
			int s = S.start();
			int e = S.end();
			if(start >= s & start <= e & end > e){
				V.add(S.value());
				L.add(e-start+1);
			}else if(start >=s & end <= e){
				V.add(S.value());
				L.add(end-start+1);
			}else if(start < s & end >= s & end <= e){
				V.add(S.value());
				L.add(end-s+1);
			}else if(start < s & end > e){
				V.add(S.value());
				L.add(e-s+1);
			}
		}
		
		double v = 0;
		double l = 0;
		for(int i = 0; i < L.size(); i++){
			v += L.get(i)*V.get(i);
			l += L.get(i);
		}
		v /= l;		
		return v;
	}
	
	public List<Double> sampleValues(int n){
		List <Double> tmp = new ArrayList<Double>();
		double t =  (double)getTotalSegmentLength();
		for(SegmentIterator i = iterator(); i.hasNext();){
			Segment s = i.next();
			double r = (s.end() -s.start())/t;
			double v = s.value();
			int m = Math.round((float)(n* r));
			for(int j=0; j<m; j++){
				tmp.add(v);
			}
		}
		return tmp;
	}
	
}

