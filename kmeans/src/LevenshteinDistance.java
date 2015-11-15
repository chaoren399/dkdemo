/**
 * 编辑距离,计算文本的相似度
 * @author zzy
 *
 */
public class LevenshteinDistance {
	public static void main (String[] args){
		
		String str1 = "东海路与燕儿岛路路口 山东路海泊桥 山东路与抚顺路路口 辽阳西路与劲松四路路口 重庆中路与振华路路口";
		String str2 = "青岛东收费站 夏庄主站收费站 S217朱诸路-张应大朱戈 胶宁高架路三百惠桥上";
		
//		String str2 = "S217朱诸路-张应大朱戈";//S217朱诸路-张应大朱戈
//		String str1 = "青兰高速（双埠-管家楼）K23+800桩号增大方向";
//		String str2 = "青岛路";
//		String str1 = "山东";
		System.out.println("ld= "+ levenshteinDistance(str1, str2));
//		System.out.println("sim="+sim(str1,str2) );
	}
	private static int min(int one,int two,int three){
		int min = one;
		if (two < min){
			min = two;
		}
		if (three <min){
			min = three;
		}
		return min;
	}
	public static int levenshteinDistance(String str1,String str2){
		
		int d[][]; // 矩阵
		int n = str1.length();
		int m = str2.length();
		int i ; // for str1
		int j; // for str2
		char ch1;
		char ch2;
		int temp; // 记录相同字符,在某个矩阵位置值得增量,不是0就是1;
		if(n == 0){
			return m;
		}
		if (m == 0){
			return n;
		}
		d = new int [n+1][m+1];
		for ( i = 0; i < n ; i++) { // 初始化第一列
			d[i][0] = i; 
		}
		for(j = 0; j<= m;j++){ // 初始化第一行
			d[0][j] = j;
		}
		 for (i =1; i<= n;i++){
			 ch1 = str1.charAt(i-1);
			 for(j=1;j <= m;j++){
				 ch2 = str2.charAt(j-1);
				 if(ch1 == ch2){
					 temp = 0;
				 }else{
					 temp =1;
				 }
				 d[i][j] = min(d[i-1][j]+1,d[i][j-1]+1,d[i-1][j-1]+temp);
			 }
		 }
		 return d[n][m];
		
		
	}
	public static double sim (Route initR,Route r){

		if (initR.routename == null || r.routename == null){
			return 0;
		}
		String str1 = initR.routename;
		String str2 = r.routename;
		double ld = levenshteinDistance(str1, str2);
		double strMax = Math.max(str1.length(), str2.length());
		double sim =1- ld/strMax;
//		if(ld < strMax){
//			sim = 1-ld/Math.min(str1.length(), str2.length());
//		}
		return sim;
	}
	
}
