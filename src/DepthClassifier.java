
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
	
public class DepthClassifier {

		final static String TOP = "(TOP";
		HashMap<Integer, List<String>> hashmapSentences =  new HashMap<Integer, List<String>>();
		int maxDepth = 0;
		int sentenceCount = 0;
		/**
		 * @param args
		 */
		
		public int getDepth(String s){
			int count = 0;
			int localMax = 0;
			for(int i = 0; i < s.length(); i++){
				if(s.charAt(i) == '(')
					count++;
				else if(s.charAt(i) == ')')
					count--;
				if(count > localMax)
					localMax = count;
			}
			return localMax;
		}
		
		
		public void calcLastSentence(String sentence){
			int depth = getDepth(sentence);
			if(depth > maxDepth)
				maxDepth = depth;
			
			if(depth != 0){
				if(hashmapSentences.containsKey(depth)){
					hashmapSentences.get(depth).add(sentence);
				}else{
					ArrayList<String> stringlist = new ArrayList<String>();
					stringlist.add(sentence);
					hashmapSentences.put(depth, stringlist);
				}
			}
		}
		
		public void output(){
			int quarterNum = sentenceCount / 4;
			System.out.println(Integer.toString(maxDepth));
			System.out.println(Integer.toString(sentenceCount));
			File[] files = new File[4];
			for(int i = 0; i < 4; i++){
				files[i] = new File("./depth-group-" + Integer.toString(i) + ".txt");
			}
			try {
				int fileNum = 0;
				BufferedWriter bwriter = new BufferedWriter(new FileWriter(files[fileNum]));
				int count = 0;
				for(int i = 0 ; i < maxDepth; i ++){
					if(hashmapSentences.containsKey(i)){
						int numOfSen =  hashmapSentences.get(i).size();
						for(int k = 0; k < numOfSen; k++ ){
							bwriter.write(hashmapSentences.get(i).get(k) + "\n");
							count ++;
							if(count > quarterNum){
								fileNum ++;
								count = 0;
								bwriter = new BufferedWriter(new FileWriter(files[fileNum]));
								System.out.println(Integer.toString(i));
							}
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		public static void main(String[] args) {
			// TODO Auto-generated method stub
			Scanner scanner = new Scanner(System.in);
			String filepath = scanner.next();
			DepthClassifier classifier = new DepthClassifier();
			
			FileReader fileReader;
			try {
				fileReader = new FileReader(filepath);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				String line = "";
				String sentence = "";
				while((line = bufferedReader.readLine()) != null){
					if (line.startsWith(TOP)){
						classifier.calcLastSentence(sentence);
						classifier.sentenceCount ++;
						sentence = line;
					}else{
						sentence = sentence + "\n" + line;
					}
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			classifier.output();
			
			
		}

}
