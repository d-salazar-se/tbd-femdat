package tbd.grupo1.femdat.controllers;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

class KeyWord {
	private static final String RESOURCE_PATH = "classpath:bagOfWords.txt";
	
	public static List<String> findAll(ResourceLoader resourceLoader) {
		List<String> bag = new ArrayList<String>();
		try {
			Resource resource = resourceLoader.getResource(RESOURCE_PATH);
			Scanner sc = new Scanner(resource.getFile());
			String word;
			while (sc.hasNextLine()) {
				word = sc.nextLine();
				if (word.charAt(0) != '@' && word.charAt(0) != '#') {
					bag.add(word);
				}
			}
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bag;
	}

	public static List<String> getTwitterUsers(ResourceLoader resourceLoader) {
		List<String> twitterUsers = new ArrayList<String>();
		try {
			Resource resource = resourceLoader.getResource(RESOURCE_PATH);
			Scanner sc = new Scanner(resource.getFile());
			String word;
			while (sc.hasNextLine()) {
				word = sc.nextLine();
				if (word.charAt(0) == '@') {
					twitterUsers.add(word);
				}
			}
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return twitterUsers;
	}

	public static List<String> getHashtags(ResourceLoader resourceLoader) {
		List<String> hashtags = new ArrayList<String>();
		try {
			Resource resource = resourceLoader.getResource(RESOURCE_PATH);
			Scanner sc = new Scanner(resource.getFile());
			String word;
			while (sc.hasNextLine()) {
				word = sc.nextLine();
				if (word.charAt(0) == '#') {
					hashtags.add(word);
				}
			}
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hashtags;
	}

	public static boolean save(ResourceLoader resourceLoader, String word) {
		try{
			Resource resource = resourceLoader.getResource(RESOURCE_PATH);
			BufferedWriter out = new BufferedWriter(new FileWriter(resource.getFile(), true));
	        out.write(word+"\n");
	        out.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static boolean delete(ResourceLoader resourceLoader, String word) {
		boolean outcome = false;
		try {
			Resource resourceIn = resourceLoader.getResource(RESOURCE_PATH);
			Scanner sc = new Scanner(resourceIn.getFile());
			
			File resourceOut = new File(resourceIn.getFile().getParent(), "bagOfWordsOut.tmp.txt");
			BufferedWriter out = new BufferedWriter(new FileWriter(resourceOut, true));

			String currentWord;
			while (sc.hasNextLine()) {
				currentWord = sc.nextLine();
				if ( ! currentWord.equals(word)) {
	        		out.write(currentWord+"\n");
				}
			}

			sc.close();
	        out.close();

			outcome = resourceOut.renameTo(resourceIn.getFile());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return outcome;
	}

}