package com.example.scraperremus;

import com.example.scraperremus.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ScraperMilltekApplication {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Working Directory: " + System.getProperty("user.dir"));
		Scanner scanner = new Scanner(System.in);
		System.out.println("Wklej link do kategorii (lub wpisz 'exit', aby zakończyć):");

		String input = "";
		while (!(input = scanner.nextLine()).equalsIgnoreCase("exit")) {
			if (isCategoryPage(input)) {
				ProductScrapperMilltek.scrapeAndSaveCategory(input);
			} else {
				System.out.println("Podano niepoprawny URL kategorii.");
			}
		}

		System.out.println("Dziękuję, koniec.");
	}

	/**
	 * Zakładamy, że URL kategorii zawiera "/collections/".
	 */
	private static boolean isCategoryPage(String url) {
		return url.contains("/collections/");
	}
}
