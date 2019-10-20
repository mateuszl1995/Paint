package tpal;

import java.util.HashMap;
import java.util.Map;

public class Translator {
	private static Translator translator;

	public static Translator getInstance() {
		if (translator == null) {
			translator = new Translator();
		}
		return translator;
	}
	
	public String translate(String word) {
		return translations.get(word) == null ? word : translations.get(word);
	}
	public String getDateFormat() {
		return (translations == ENtranslations) ? "yyyy-MM-dd hh:mm" : "dd.MM.yyyy hh:mm";
	}
	
	public void setLanguage(String lang) {
		if (lang.equals("pl")) {
			translations = PLtranslations;
		} else if (lang.equals("en")) {
			translations = ENtranslations;
		} else {
			translations = null; // TODO: Locale
		}
	}
	
	private Map<String, String> translations;
	private Map<String, String> PLtranslations = new HashMap<String, String>();
	private Map<String, String> ENtranslations = new HashMap<String, String>();
	
	private Translator() {
		setLanguage("en");
		PLtranslations.put("File", "Plik");
		PLtranslations.put("New", "Nowy");
		PLtranslations.put("Open", "Otwórz");
		PLtranslations.put("Save", "Zapisz");
		PLtranslations.put("Save as...", "Zapisz jako...");
		PLtranslations.put("Edit", "Edycja");
		PLtranslations.put("Undo", "Cofnij");
		PLtranslations.put("Redo", "Powtórz");
		PLtranslations.put("Size...", "Rozmiar...");
		PLtranslations.put("Image", "Obraz");
		PLtranslations.put("Language", "Język");
		PLtranslations.put("Polish", "Polski");
		PLtranslations.put("English", "Angielski");
		PLtranslations.put("Colors", "Kolory");
		PLtranslations.put("Size", "Rozmiar");
		PLtranslations.put("Tools", "Narzędzia");
		PLtranslations.put("Options", "Opcje");
		PLtranslations.put("Fill", "Wypełnienie");
		PLtranslations.put("Dashed", "Przerywana linia");
		PLtranslations.put("Set size of picture", "Ustaw rozmiar obrazka");
		PLtranslations.put("Width", "Szerokość");
		PLtranslations.put("Height", "Wysokość");
		PLtranslations.put("Cancel", "Anuluj");
	}

	public String getLanguage() {
		if (translations == PLtranslations) return "pl";
		else return "en";
	}
	
}
