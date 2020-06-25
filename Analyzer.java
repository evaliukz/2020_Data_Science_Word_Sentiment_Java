
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Analyzer {

	/*
	 * Implement this method in Part 1
	 */

	
	public static List<Sentence> readFile(String filename) {

		List<Sentence> result = new LinkedList<Sentence>(); // this is the returned list
		
		// if the file name is null, return null list
    	File file = new File(filename);
		if (filename == null || filename == ""||file.exists()==false||file.canRead()==false) {
			return result;
		}
			
		Charset charset = Charset.forName("US-ASCII");
		
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename),charset)) {
		   
			String strLine = null;
		    while ((strLine = reader.readLine()) != null) {
	

				// number follow by one single space, then follow by text
				if (strLine.length() >= 4 && (strLine.substring(0, 3).equalsIgnoreCase("-1 ") || strLine.substring(0, 3).equalsIgnoreCase("-2 "))
						 && strLine.charAt(3) != ' ') {

					Sentence addnew = new Sentence(Integer.parseInt(strLine.substring(0, 2).trim()),
							strLine.substring(3).trim());

					result.add(addnew);
				}

				else if (strLine.length() >= 3 && (strLine.substring(0, 2).equalsIgnoreCase("0 ")
						|| strLine.substring(0, 2).equalsIgnoreCase("1 ")
						|| strLine.substring(0, 2).equalsIgnoreCase("2 ")) 
						&& strLine.charAt(2) != ' ') {

					Sentence addnew = new Sentence(Integer.parseInt(strLine.substring(0, 1).trim()),
							strLine.substring(2).trim());

					result.add(addnew);
				}
			}

			// fstream.close(); // Close the input stream, need to cancel this to create a
			// pointer

		// if file empty or cannot read, return empty
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// print to test
		for (int i = 0; i < result.size(); i++) {
			System.out.println(result.get(i).text + "score is " + result.get(i).score);
		}

		return result;
	}

	/*
	 * Implement this method in Part 2
	 */

	public static Set<Word> allWords(List<Sentence> sentences) {

//		result for this method 
		Set<Word> wordset = new HashSet<Word>();

		// if input List of Sentences is null or is empty, the allWords method should
		// return an empty set
		if (sentences == null || sentences.isEmpty()) {
			
			System.out.println("hey hey null");
			
			return wordset;

		}

		// use a Hashmap to store the strings and counts and totals
		Map<String, Integer> wordcount = new HashMap<String, Integer>(); // to store word and count
		Map<String, Integer> wordtotal = new HashMap<String, Integer>(); // to store word and count

		// print to test
		System.out.println("sentence size is " + sentences.size());

		// remove all null sentences
		for (int i = 0; i < sentences.size(); i++) {

			if (sentences.get(i) == null) {
				sentences.remove(i);
			}
		}

		// process all non-null sentences

		for (int i = 0; i < sentences.size(); i++) {

			if (sentences.get(i) != null) {

				String[] stext = sentences.get(i).text.split("\\s+"); // regex, split with spaces, interesting to see
																		// "\\W+"
																		// split with non word
				// normalize word
				for (int x = 0; x < stext.length; x++) {

					if ((stext[x].charAt(0) >= 'A' && stext[x].charAt(0) <= 'Z')
							|| (stext[x].charAt(0) >= 'a' && stext[x].charAt(0) <= 'z')) {

						stext[x] = stext[x].toLowerCase().toString().trim();

						// get the count for each word
						if (wordcount.containsKey(stext[x])) {
							wordcount.put(stext[x], wordcount.get(stext[x]) + 1);
						} else {
							wordcount.put(stext[x], 1);
						}

						// get the total for each word
						if (wordtotal.containsKey(stext[x])) {
							wordtotal.put(stext[x], wordtotal.get(stext[x]) + sentences.get(i).score);
						} else {
							wordtotal.put(stext[x], sentences.get(i).score);
						}

					}
				}
			}
		}

		for (String wordkey : wordcount.keySet()) { // for each key in word map

			Word toadd = new Word(wordkey); // add string to an WORD
			toadd.count = wordcount.get(wordkey); // get integer to count
			toadd.total = wordtotal.get(wordkey);

			wordset.add(toadd);
		}
		// print to test
		//for (Word w : wordset) {
		//	System.out.println("text is " + w.text);
		//	System.out.println("count is " + w.count);
		//	System.out.println("total is " + w.total);
		//}

		// print to test
		System.out.println("Count map size is  " + wordcount.size());
		System.out.println("Total map size is  " + wordtotal.size());

		return wordset;
	}

	/*
	 * Implement this method in Part 3
	 */
	public static Map<String, Double> calculateScores(Set<Word> words) {

		// a hash map to store the result
		Map<String, Double> scoremap = new HashMap<String, Double>(); // to store word and count

		// the input Set of Words is null or is empty, the calculateScores method should
		// return an empty Map
		if (words == null || words.isEmpty()) {
			System.out.println("this is a null list");
			return scoremap;
		}

		// remove all null words in the set
		for (Word w : words) {
			if (w == null) {
				words.remove(w);
			}
		}

		// go through the non-null map and calculate the score
		for (Word w : words) {
			scoremap.put(w.text, (double) w.calculateScore());
			// System.out.println(" ********");
			// System.out.println("the map size is " + scoremap.size());
			// System.out.println("the word is " + w.text);
			// System.out.println("the count is " + w.count);
			// System.out.println("the total is " + w.total);
			// System.out.println("the wordscore before is " + w.calculateScore());
			// System.out.println("the score is " + scoremap.get(w.text));
		}
		return scoremap;
	}

	/*
	 * Implement this method in Part 4
	 */
	public static double calculateSentenceScore(Map<String, Double> wordScores, String sentence) {

		double score = 0.00;
		int count =0;

		if (wordScores == null || wordScores.isEmpty() || sentence == null || sentence.isEmpty()) {
			
			return 0.00;
		}

		else if (wordScores.size()>=1){

			System.out.println("sentence is " + sentence);

			String[] stext = sentence.split("\\s+"); // regex, split with spaces, interesting to see "\\W+"
			System.out.println("length is " + stext.length);

			// normalize word
			for (int x = 0; x < stext.length; x++) {

				System.out.println("raw " + stext[x]);

				if ((stext[x].charAt(0) >= 'A' && stext[x].charAt(0) <= 'Z')
						|| (stext[x].charAt(0) >= 'a' && stext[x].charAt(0) <= 'z')) {

					stext[x] = stext[x].toLowerCase().toString().trim();
                    count++;
					System.out.println(stext[x] + "this word score is" + wordScores.get(stext[x]));

					if (wordScores.containsKey(stext[x])) {
						score = score + (double)wordScores.get(stext[x]);
					}
					else {
						score = score +0.00;
					}

					System.out.println(" score is " + score);
					System.out.println(" count is " + score);
				}
			}
		}

		return score/count;
	}

	/*
	 * You do not need to modify this code but can use it for testing your program!
	 */
	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("Please specify the name of the input file");
			System.exit(0);
		}
		String filename = args[0];
		System.out.print("Please enter a sentence: ");
		Scanner in = new Scanner(System.in);
		String sentence = in.nextLine();
		in.close();
		List<Sentence> sentences = Analyzer.readFile(filename);
		Set<Word> words = Analyzer.allWords(sentences);
		Map<String, Double> wordScores = Analyzer.calculateScores(words);
		double score = Analyzer.calculateSentenceScore(wordScores, sentence);
		System.out.println("The sentiment score is " + score);

	}
}
