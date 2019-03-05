package modele.infos;

import java.util.LinkedList;

public class Profession extends Information {
	public final static String KEY = "professions";

	public Profession() {
		listeInformation = new LinkedList<String>();
	}
}
