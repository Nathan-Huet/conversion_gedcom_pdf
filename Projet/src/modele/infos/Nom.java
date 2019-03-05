package modele.infos;

import java.util.LinkedList;

public class Nom extends Information {
	public final static String KEY = "Noms";

	public Nom() {
		listeInformation = new LinkedList<String>();
	}
}
